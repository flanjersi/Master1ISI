package fr.master1ISI;

import fr.master1ISI.fileDownloader.FileDownloader;
import fr.master1ISI.fileDownloader.GitHubFileDownloader;
import fr.master1ISI.fileDownloader.KaggleFileDownloader;

public class AppTest {



    public static void main(String[] args) {
        FileDownloader downloader = new GitHubFileDownloader("fivethirtyeight", "data", "police-killings/police_killings.csv", "police_killings.csv");
        downloader.download();
        downloader = new KaggleFileDownloader("START-UMD/gtd", "globalterrorismdb_0617dist.csv", "dataset");
        downloader.download();
    }
}
