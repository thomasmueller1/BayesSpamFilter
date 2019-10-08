package ch.fhnw.dist;

import java.io.File;

public class BayesSpamFilter {
    public static void main(String[] args) {
        System.out.println("test");

        File folder = new File("C:\\Users\\braini\\Dropbox\\FHNW\\Semester5\\dist\\#FHNW data copy\\Übungen\\Programmieraufgabe1\\ham-anlern");
        ReadFiles listFiles = new ReadFiles();
        listFiles.listAllFiles(folder);
    }
}
