package ch.fhnw.dist;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class BayesSpamFilter {
    final static public String DEFAULT_TEMP_DIR = "C:/Temp"; // without final slash
    final static public ClassLoader INTERNAL_FILE_PATH = BayesSpamFilter.class.getClassLoader();

    public static void main(String[] args) {
        FileHelper fh = new FileHelper();
        String zipHAM = INTERNAL_FILE_PATH.getResource("ham-anlern.zip").getPath();
        String zipSPAM = INTERNAL_FILE_PATH.getResource("spam-anlern.zip").getPath();
        String zipFileTest = INTERNAL_FILE_PATH.getResource("ham-test.zip").getPath();


        String tempDirHAM = DEFAULT_TEMP_DIR+"/ham-anlern";
        String tempDirSPAM = DEFAULT_TEMP_DIR+"/spam-anlern";
        ArrayList<String> resultsHAM = new ArrayList<>();
        ArrayList<String> resultsSPAM = new ArrayList<>();

        File fileHAM = new File(tempDirHAM);
        File fileSPAM = new File(tempDirSPAM);

        int countHAMmails = fileHAM.listFiles().length;
        int countSPAMmails = fileSPAM.listFiles().length;

        try {
            fh.unzip(zipHAM, tempDirHAM);
            fh.unzip(zipSPAM, tempDirSPAM);
            for (File file : fh.getAllFilesFromDirectory(fileHAM)) {
                resultsHAM.addAll(fh.readFileContentToList(file, true));
            }
            for (File file : fh.getAllFilesFromDirectory(fileSPAM)) {
                resultsSPAM.addAll(fh.readFileContentToList(file, true));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //System.out.println(zipSPAM);

        HashMap<String, WordModel> resultsHAMNumMails = fh.createHashMapFromWords(resultsHAM, false);
        HashMap<String, WordModel> resultsSPAMNumMails = fh.createHashMapFromWords(resultsSPAM, true);

        HashMap<String, WordModel> resultsNumMails = resultsHAMNumMails;
        resultsSPAMNumMails.forEach((s, wordModel) -> {
            if(resultsNumMails.containsKey(s)) resultsNumMails.get(s).incSpamAmount();
            else resultsNumMails.put(s, wordModel);
        });

        /*resultsHAMNumMails.forEach((s, wordModel) -> System.out.println(s + " : " + wordModel.getHamAmount()));
        System.out.println("-------------");
        resultsSPAMNumMails.forEach((s, wordModel) -> System.out.println(s + " : " + wordModel.getHamAmount()));*/

        resultsNumMails.forEach((s, wordModel) -> {
            wordModel.setHamProbability((double)wordModel.getHamAmount()/countHAMmails*100);
            wordModel.setSpamProbability((double)wordModel.getSpamAmount()/countSPAMmails*100);
        });

        /*resultsHAMNumMails.forEach((s, wordModel) -> {
            System.out.println(s + ": " + wordModel.getHamAmount() + " | " + wordModel.getSpamAmount());
        });*/

        System.out.println("----------");

        /*resultsSPAMNumMails.forEach((s, wordModel) -> {
            System.out.println(s + ": " + wordModel.getHamAmount() + " | " + wordModel.getSpamAmount());
        });*/

        resultsNumMails.forEach((s, wordModel) -> {
            System.out.println(s + ": " + wordModel.getHamAmount() + "("+wordModel.getHamProbability()+") | " + wordModel.getSpamAmount()+" ("+wordModel.getSpamProbability());
        });

        /*try{
            fh.unzip(zipFileTest, DEFAULT_TEMP_DIR+"/test");
            list = fh.createHashMapFromWords(results, false);
            ArrayList<File> mails =  fh.getAllFilesFromDirectory(new File(DEFAULT_TEMP_DIR+"/test"));
            ArrayList<String> firstMailWords = fh.readFileContentToList(mails.get(0),false);
            for (String mailWord : firstMailWords){
                System.out.println(mailWord + " "+ calcPajassAlgo(firstMailWords,list));
            }
        }catch (Exception e){

        }*/


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


    public static void setProbabilities(HashMap<String, WordModel> words, int numMails) {

    }
}