package fr.master1ISI.fileDownloader;

import fr.master1ISI.App;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.*;

public class GitHubFileDownloader implements FileDownloader {

    private String ownersName;
    private String repoName;
    private String pathFileGithub;

    private String pathFileDest;



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

    private JSONObject getJSonResponseApi(){
        try {
            String url = makeUrlApiGithub();
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);

            getRequest.addHeader("accept", "application/json");

            App.logger.info("Envoie de l'url : " + url + " en cours");

            HttpResponse response = httpClient.execute(getRequest);

            App.logger.info("Code réponse reçu de l'url " + url + " : " + response.getStatusLine().getStatusCode());

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

            return new JSONObject(sb.toString());

        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }


    public boolean fileHasNewVersion(String sha){


        return false;
    }


    private void downloadFile(String url){
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet getRequest = new HttpGet(url);

            App.logger.info("Envoie de l'url : " + url + " en cours");

            HttpResponse response = httpClient.execute(getRequest);

            App.logger.info("Code réponse reçu de l'url " + url + " : " + response.getStatusLine().getStatusCode());

            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("Failed (" + url + "): HTTP error code : "
                        + response.getStatusLine().getStatusCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

            String output;


            File file = new File(pathFileDest);
            file.getParentFile().mkdirs();
            file.createNewFile();

            PrintWriter writer = new PrintWriter(file, "UTF-8");

            while ((output = br.readLine()) != null) {
                writer.print(writer);
            }

            writer.close();
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean download() {
        JSONObject jsonObject = getJSonResponseApi();

        downloadFile(jsonObject.getString("download_url"));


        return false;
    }
}
