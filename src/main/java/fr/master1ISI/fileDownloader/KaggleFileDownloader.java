package fr.master1ISI.fileDownloader;

import fr.master1ISI.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class KaggleFileDownloader implements FileDownloader{

    private String nameDataSet;
    private String nameFileData;

    private String pathDirectoryDest;

    public KaggleFileDownloader(String nameDataSet, String nameFileData, String pathDirectoryDest){
        this.nameDataSet = nameDataSet;
        this.pathDirectoryDest = pathDirectoryDest;
        this.nameFileData = nameFileData;
    }


    private void executeCommandKaggle(){
        try {
            Runtime runtime = Runtime.getRuntime();
            String cmd = "kaggle datasets download -p " + pathDirectoryDest +  " -f " + nameFileData + " -d " + nameDataSet;

            App.logger.info("Téléchargement du dataset : " + nameDataSet + "/" + nameFileData + " sur kaggle");

            Process process = runtime.exec(cmd);

            process.waitFor();

            App.logger.info("Fin du téléchargement du dataset : " + nameDataSet + "/" + nameFileData + " sur kaggle");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean unzip(){
        try {
            char lastChar = pathDirectoryDest.charAt(pathDirectoryDest.length() - 1);

            if(lastChar != '/'){
                pathDirectoryDest += "/";
            }

            String fileZip = pathDirectoryDest + nameFileData + ".zip";

            if(!new File(fileZip).exists()){
                return false;
            }

            byte[] buffer = new byte[1024];

            App.logger.info("Récupération du fichier \"" + nameFileData + "\" dans l'archive \"" + fileZip + "\"");

            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();

            File newFile = new File(pathDirectoryDest + zipEntry.getName());

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;

            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            zis.closeEntry();
            zis.close();
            App.logger.info("Récupération terminé du fichier \"" + nameFileData + "\" dans l'archive \"" + fileZip + "\"");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public boolean download() {
        executeCommandKaggle();

        unzip();
        return true;
    }
}
