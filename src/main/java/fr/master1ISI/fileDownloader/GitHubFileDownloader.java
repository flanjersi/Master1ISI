package fr.master1ISI.fileDownloader;

import fr.master1ISI.AppJavaFX;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitHubFileDownloader implements FileDownloader {

    private String ownersName;
    private String repoName;
    private String pathFileGithub;

    private String pathFileDest;

    private JSONObject jsonTreeParentFile = null;

    private final String token = "74e8fddbc0046c81f9c4e3796eaadbef07fec2a7";


    public GitHubFileDownloader(String ownersName, String repoName, String pathFileGithub, String pathFileDest) {
        this.ownersName = ownersName;
        this.repoName = repoName;
        this.pathFileGithub = pathFileGithub;
        this.pathFileDest = pathFileDest;
    }

    private String makeUrlApiGithub(){
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.github.com/repos/");
        sb.append(ownersName);
        sb.append("/");
        sb.append(repoName);
        sb.append("/contents/");
        sb.append(pathFileGithub);

        return sb.toString();
    }

    private String getParentFile(){
        Pattern pattern = Pattern.compile("(.*)\\/(.*)");
        Matcher matcher = pattern.matcher(pathFileGithub);

        if(!matcher.matches()){
            return null;
        }

        return matcher.group(1);
    }

    private String getFile(){
        Pattern pattern = Pattern.compile("(.*)\\/(.*)");
        Matcher matcher = pattern.matcher(pathFileGithub);

        if(!matcher.matches()){
            return pathFileGithub;
        }

        return matcher.group(2);
    }

    private String getUrlBlob(){
        JSONArray treesJson = jsonTreeParentFile.getJSONArray("tree");


        String nameFile = getFile();
        for(int i = 0 ; i < treesJson.length() ; i++){
            JSONObject node = treesJson.getJSONObject(i);

            if (!node.getString("type").equalsIgnoreCase("blob")) continue;
            if(!node.getString("path").equalsIgnoreCase(nameFile)) continue;

            return node.getString("url");
        }

        return null;
    }

    private String makeURLApiTreesGitHub(){
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.github.com/repos/");
        sb.append(ownersName);
        sb.append("/");
        sb.append(repoName);
        sb.append("/git/trees/master");

        String parentFile = getParentFile();

        if(parentFile != null){
            sb.append(":" + parentFile);
        }

        return sb.toString();
    }

    private HttpGet buildHTTPRequestGET(String url){
        HttpGet getRequest = new HttpGet(url);
        getRequest.addHeader("accept", "application/vnd.github.v3+json");
        getRequest.setHeader("Authorization", "token " + token);

        return getRequest;
    }

    private void getTreeParentFileJson() {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            String url = makeURLApiTreesGitHub();
            AppJavaFX.logger.info("Envoie de l'url : " + url + " en cours");

            HttpGet getRequest = buildHTTPRequestGET(url);

            HttpResponse response = httpClient.execute(getRequest);

            AppJavaFX.logger.info("Code réponse reçu de l'url " + url + " : " + response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed (" + url + "): HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;

            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            httpClient.getConnectionManager().shutdown();


            jsonTreeParentFile = new JSONObject(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private boolean fileSizeIsGreaterThan1Mb(){
        JSONArray treesJson = jsonTreeParentFile.getJSONArray("tree");


        String nameFile = getFile();
        for(int i = 0 ; i < treesJson.length() ; i++){
            JSONObject node = treesJson.getJSONObject(i);

            if (!node.getString("type").equalsIgnoreCase("blob")) continue;
            if(!node.getString("path").equalsIgnoreCase(nameFile)) continue;

            int size = node.getInt("size");

            return size > 1_000_000;
        }

        return false;
    }

    private JSONObject getJSonResponseApi(){
        try {
            String url = makeUrlApiGithub();
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet getRequest = buildHTTPRequestGET(url);

            AppJavaFX.logger.info("Envoie de l'url : " + url + " en cours");

            HttpResponse response = httpClient.execute(getRequest);

            AppJavaFX.logger.info("Code réponse reçu de l'url " + url + " : " + response.getStatusLine().getStatusCode());

            if(response.getStatusLine().getStatusCode() == 403){
                String test = makeURLApiTreesGitHub();
                System.out.println(test);
                return null;
            }
            else if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed (" + url + "): HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;

            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            httpClient.getConnectionManager().shutdown();

            return new JSONObject(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private void downloadFileFromBase64(){
        String urlBlob = getUrlBlob();

        try {
            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

            HttpGet getRequest = buildHTTPRequestGET(urlBlob);

            AppJavaFX.logger.info("Envoie de l'url : " + urlBlob + " en cours");

            HttpResponse response = defaultHttpClient.execute(getRequest);

            AppJavaFX.logger.info("Code réponse reçu de l'url " + urlBlob + " : " + response.getStatusLine().getStatusCode());

           if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed (" + urlBlob + "): HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(
                    new InputStreamReader((response.getEntity().getContent())));

            String output;

            StringBuilder sb = new StringBuilder();
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            defaultHttpClient.getConnectionManager().shutdown();

            JSONObject jsonObject = new JSONObject(sb.toString());
            String contentBase64 = jsonObject.getString("content");

            byte[] decoded = Base64.getMimeDecoder().decode(contentBase64);

            File file = new File(pathFileDest);

            if(file.getParentFile() != null){
                file.getParentFile().mkdirs();
            }

            file.createNewFile();


            FileUtils.writeByteArrayToFile(file, decoded);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void downloadFile(String url){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet getRequest = buildHTTPRequestGET(url);

            AppJavaFX.logger.info("Envoie de l'url : " + url + " en cours");

            HttpResponse response = httpClient.execute(getRequest);

            AppJavaFX.logger.info("Code réponse reçu de l'url " + url + " : " + response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed (" + url + "): HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;


            File file = new File(pathFileDest);

            if(file.getParentFile() != null){
                file.getParentFile().mkdirs();
            }

            file.createNewFile();

            PrintWriter writer = new PrintWriter(file, "UTF-8");

            while ((output = br.readLine()) != null) {
                writer.println(output);
            }

            writer.close();
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean download() {
        getTreeParentFileJson();

        if(!fileSizeIsGreaterThan1Mb()){
            JSONObject jsonObject = getJSonResponseApi();
            downloadFile(jsonObject.getString("download_url"));
            return true;
        }
        else {
            downloadFileFromBase64();
            return true;
        }

    }


}
