package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

class FilterEducation {
    private HashMap<String, WordModel> resultsNumMails;

    HashMap<String, WordModel> educateFilter() throws IOException {
        FileHelper fh = new FileHelper();

        String zipHAM = "ham-anlern.zip";
        String zipSPAM = "spam-anlern.zip";

        String tempDirHAM = CONFIG.DEFAULT_TEMP_DIR + "/ham-anlern";
        String tempDirSPAM = CONFIG.DEFAULT_TEMP_DIR + "/spam-anlern";
        ArrayList<String> resultsHAM = new ArrayList<>();
        ArrayList<String> resultsSPAM = new ArrayList<>();

        File fileHAM = new File(tempDirHAM);
        File fileSPAM = new File(tempDirSPAM);

        int countHAMmails;
        int countSPAMmails;

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


        HashMap<String, WordModel> resultsHAMNumMails = fh.createHashMapFromWords(resultsHAM, false);
        HashMap<String, WordModel> resultsSPAMNumMails = fh.createHashMapFromWords(resultsSPAM, true);

        resultsNumMails = resultsHAMNumMails;
        resultsSPAMNumMails.forEach((s, wordModel) -> {
            if (resultsNumMails.containsKey(s)) resultsNumMails.get(s).incSpamAmount();
            else resultsNumMails.put(s, wordModel);
        });

        int finalCountHAMmails = countHAMmails;
        int finalCountSPAMmails = countSPAMmails;
        resultsNumMails.forEach((s, wordModel) -> {
            wordModel.setHamProbability((double) wordModel.getHamAmount() / finalCountHAMmails * 100);
            wordModel.setSpamProbability((double) wordModel.getSpamAmount() / finalCountSPAMmails * 100);
            if (wordModel.getSpamProbability() == 0.0) wordModel.setSpamProbability(CONFIG.PROBABILITY_ON_ZEROMAILS);
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



