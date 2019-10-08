package ch.fhnw.dist;

import java.io.File;

public class BayesSpamFilter {
    public static void main(String[] args) {
        File folderHam = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\ham-anlern");
        File folderSpam = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\spam-anlern");
        ReadFiles listFiles = new ReadFiles();
        listFiles.listAllFiles(folderHam, folderSpam);
    }
}
