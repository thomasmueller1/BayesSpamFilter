package ch.fhnw.dist;

import java.io.File;

public class BayesSpamFilter {
    public static void main(String[] args) {
        File folderHam = new File("C:\\Users\\braini\\Dropbox\\FHNW\\Semester5\\dist\\#FHNW data copy\\Übungen\\Programmieraufgabe1\\ham-anlern");
        //File folderSpam = new File("C:\\Users\\braini\\Dropbox\\FHNW\\Semester5\\dist\\#FHNW data copy\\Übungen\\Programmieraufgabe1\\spam-anlern");
        ReadFiles listFiles = new ReadFiles();
        listFiles.listAllFiles(folderHam);
        //listFiles.listAllFiles(folderSpam);
    }
}
