package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BayesSpamFilter {
    public static void main(String[] args) {
        File folderHam = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\ham-anlern");
        File folderSpam = new File(System.getProperty("user.dir")+"\\src\\main\\resources\\spam-anlern");
        ReadFiles listFiles = new ReadFiles();
        listFiles.listAllFiles(folderHam, folderSpam);



        /*FileHelper fh = new FileHelper();
        String zipFile = BayesSpamFilter.class.getClassLoader().getResource("ham-anlern.zip").getPath();
        String tempDir = "C:/Temp/unzipFolder";
        ArrayList<String> results = new ArrayList<>();
        File f = new File(tempDir);
        try {
            fh.unzip(zipFile, tempDir);
            for (File file : fh.getAllFilesFromDirectory(f)) {
                results.addAll(fh.readFileContentToList(file,false));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        results.forEach(System.out::println);*/
    }
}