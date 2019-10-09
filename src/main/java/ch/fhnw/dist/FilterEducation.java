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
            if (resultsNumMails.containsKey(s)) resultsNumMails.get(s).setSpamAmount(wordModel.getSpamAmount());
            else resultsNumMails.put(s, wordModel);
        });

        int finalCountHAMmails = countHAMmails;
        int finalCountSPAMmails = countSPAMmails;
        resultsNumMails.forEach((s, wordModel) -> {
            wordModel.setHamProbability((double) wordModel.getHamAmount() / finalCountHAMmails);
            wordModel.setSpamProbability((double) wordModel.getSpamAmount() / finalCountSPAMmails);
            if (wordModel.getHamProbability() == 0.0) wordModel.setHamProbability(CONFIG.PROBABILITY_ON_ZEROMAILS);
            if (wordModel.getSpamProbability() == 0.0) wordModel.setSpamProbability(CONFIG.PROBABILITY_ON_ZEROMAILS);
        });

        return resultsNumMails;
    }
}



