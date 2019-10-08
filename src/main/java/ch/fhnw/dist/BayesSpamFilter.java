package ch.fhnw.dist;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BayesSpamFilter {
    public static void main(String[] args) {
        File folderHam = new File("C:\\Users\\braini\\Dropbox\\FHNW\\Semester5\\dist\\#FHNW data copy\\Übungen\\Programmieraufgabe1\\ham-anlern");
        //File folderSpam = new File("C:\\Users\\braini\\Dropbox\\FHNW\\Semester5\\dist\\#FHNW data copy\\Übungen\\Programmieraufgabe1\\spam-anlern");
        //ReadFiles listFiles = new ReadFiles();
        //listFiles.listAllFiles(folderHam);
        //listFiles.listAllFiles(folderSpam);


        FileHelper fh = new FileHelper();
        String zipFile = BayesSpamFilter.class.getClassLoader().getResource("ham-anlern.zip").getPath();
        String zipFileTest = BayesSpamFilter.class.getClassLoader().getResource("ham-test.zip").getPath();

        String tempDir = "C:/Temp/unzipFolder";
        ArrayList<String> results = new ArrayList<>();
        HashMap<String, WordModel> list = new HashMap<>();
        File f = new File(tempDir);
        try {
            fh.unzip(zipFile, tempDir);
            for (File file : fh.getAllFilesFromDirectory(f)) {
                results.addAll(fh.readFileContentToList(file, false));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try{
            fh.unzip(zipFileTest, "C:/Temp/test");
            list = fh.createHashMapFromWords(results, false);
            ArrayList<File> mails =  fh.getAllFilesFromDirectory(new File("C:/Temp/test"));
            ArrayList<String> firstMailWords = fh.readFileContentToList(mails.get(0),false);
            for (String mailWord : firstMailWords){
                System.out.println(mailWord + " "+ calcPajassAlgo(firstMailWords,list));
            }
        }catch (Exception e){

        }


    }


    public static double calcPajassAlgo(ArrayList<String> wordsFormMail, HashMap<String, WordModel> validationList) {
        double spamProduct = 0;
        double hamProduct = 0;

        for (String word : wordsFormMail) {
            if (validationList.containsKey(word)) {
                spamProduct *= validationList.get(word).getSpamProbability();
                hamProduct *= validationList.get(word).getHamProbability();
            }
        }
        return spamProduct / (spamProduct + hamProduct);
    }
}