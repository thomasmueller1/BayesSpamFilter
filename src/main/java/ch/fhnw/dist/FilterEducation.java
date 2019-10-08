package ch.fhnw.dist;

import java.io.File;
import java.util.*;

class FilterEducation {
    final static private String DEFAULT_TEMP_DIR = "C:/Temp"; // without final slash
    final static private double PROBABILITY_ON_ZEROMAILS = 0.001;
    final static private ClassLoader INTERNAL_FILE_PATH = BayesSpamFilter.class.getClassLoader();
    private HashMap<String, WordModel> resultsNumMails;

    void educateFilter() {
        FileHelper fh = new FileHelper();

        String zipHAM = Objects.requireNonNull(INTERNAL_FILE_PATH.getResource("ham-anlern.zip")).getPath();
        String zipSPAM = Objects.requireNonNull(INTERNAL_FILE_PATH.getResource("spam-anlern.zip")).getPath();
        String zipFileTest = Objects.requireNonNull(INTERNAL_FILE_PATH.getResource("ham-test.zip")).getPath();

        String tempDirHAM = DEFAULT_TEMP_DIR + "/ham-anlern";
        String tempDirSPAM = DEFAULT_TEMP_DIR + "/spam-anlern";
        ArrayList<String> resultsHAM = new ArrayList<>();
        ArrayList<String> resultsSPAM = new ArrayList<>();

        File fileHAM = new File(tempDirHAM);
        File fileSPAM = new File(tempDirSPAM);

        int countHAMmails = 1;
        int countSPAMmails = 1;

        try {

            fh.unzip(zipHAM, tempDirHAM);
            fh.unzip(zipSPAM, tempDirSPAM);

            countHAMmails = Objects.requireNonNull(fileHAM.listFiles()).length;
            countSPAMmails = Objects.requireNonNull(fileSPAM.listFiles()).length;

            for (File file : fh.getAllFilesFromDirectory(fileHAM)) {
                resultsHAM.addAll(fh.readFileContentToList(file, true));
            }
            for (File file : fh.getAllFilesFromDirectory(fileSPAM)) {
                resultsSPAM.addAll(fh.readFileContentToList(file, true));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        HashMap<String, WordModel> resultsHAMNumMails = fh.createHashMapFromWords(resultsHAM, false);
        HashMap<String, WordModel> resultsSPAMNumMails = fh.createHashMapFromWords(resultsSPAM, true);

        resultsNumMails = resultsHAMNumMails;
        resultsSPAMNumMails.forEach((s, wordModel) -> {
            if (resultsNumMails.containsKey(s)) resultsNumMails.get(s).incSpamAmount();
            else resultsNumMails.put(s, wordModel);
        });

        /*resultsHAMNumMails.forEach((s, wordModel) -> System.out.println(s + " : " + wordModel.getHamAmount()));
        System.out.println("-------------");
        resultsSPAMNumMails.forEach((s, wordModel) -> System.out.println(s + " : " + wordModel.getHamAmount()));*/

        int finalCountHAMmails = countHAMmails;
        int finalCountSPAMmails = countSPAMmails;
        resultsNumMails.forEach((s, wordModel) -> {
            wordModel.setHamProbability((double) wordModel.getHamAmount() / finalCountHAMmails * 100);
            wordModel.setSpamProbability((double) wordModel.getSpamAmount() / finalCountSPAMmails * 100);
            if (wordModel.getSpamProbability() == 0.0) wordModel.setSpamProbability(PROBABILITY_ON_ZEROMAILS);
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

    HashMap<String, WordModel> getWords() {
        return resultsNumMails;
    }

    private static double calcPajassAlgo(ArrayList<String> wordsFormMail, HashMap<String, WordModel> validationList) {
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



