package fr.master1ISI.fileDownloader;

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

            Process process = runtime.exec(cmd);

            process.waitFor();

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

            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();

            String fileName = zipEntry.getName();

            File newFile = new File(pathDirectoryDest + nameFileData);
            FileOutputStream fos = new FileOutputStream(newFile);

            int len;

            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            zis.closeEntry();
            zis.close();

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
