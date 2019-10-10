package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Anlernen des Filters
 */

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

        fh.unzip(zipHAM, tempDirHAM);
        fh.unzip(zipSPAM, tempDirSPAM);

        final int countHAMmails = Objects.requireNonNull(fileHAM.listFiles()).length;
        final int countSPAMmails = Objects.requireNonNull(fileSPAM.listFiles()).length;

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

        resultsNumMails.forEach((s, wordModel) -> {
            double hamProbability = CONFIG.PROBABILITY_ON_ZEROMAILS;
            if(wordModel.getHamAmount() > 0)
                hamProbability = (double)wordModel.getHamAmount() / countHAMmails;

            double spamProbability = CONFIG.PROBABILITY_ON_ZEROMAILS;
            if(wordModel.getSpamAmount() > 0)
                spamProbability = (double)wordModel.getSpamAmount() / countSPAMmails;

            wordModel.setHamProbability(hamProbability);
            wordModel.setSpamProbability(spamProbability);
        });

        return resultsNumMails;
    }
}



