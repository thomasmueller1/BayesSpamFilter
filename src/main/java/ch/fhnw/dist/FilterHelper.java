package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Anlernen des Filters
 */

class FilterHelper {
    private HashMap<String, WordModel> resultsNumMails;
    private FileHelper fh = new FileHelper();

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

    public HashMap<String, Integer> determineSpamProbability(HashMap<String, WordModel> wordList, String fileName) throws IOException {
        String zipMAILS = fileName + ".zip";
        String tempDir = CONFIG.DEFAULT_TEMP_DIR + "/" + fileName;
        ArrayList<String> wordsInMail;

        File fileMAILS = new File(tempDir);
        fh.unzip(zipMAILS, tempDir);
        ArrayList<File> files = fh.getAllFilesFromDirectory(fileMAILS);

        int countHAM = 0;
        int countSPAM = 0;

        for (File file : files) {
            wordsInMail = fh.readFileContentToList(file, false);
            double result = calcBayesAlgo(wordsInMail, wordList);
            if(result > CONFIG.SPAM_THRESHOLD) countSPAM++;
            else countHAM++;
        }

        HashMap<String,Integer> result = new HashMap<>();
        result.put("ham", countHAM);
        result.put("spam", countSPAM);

        return result;
    }


    private static double calcBayesAlgo(ArrayList<String> wordsInMail, HashMap<String, WordModel> wordList) {
        double hamProbability  = 1.0d;
        double spamProbability = 1.0d;

        // Liste mit den 10 signifikantesten Wörtern (Das heisst, die Wörter die am stärksten Spam oder Ham signalisieren)
        HashMap<String, Double> wordSignificances = new HashMap<>();

        for (String word: wordsInMail) {
            if(wordList.containsKey(word)) {
                WordModel wordModel = wordList.get(word);

                double significance = 0;
                if(wordModel.getHamProbability() > wordModel.getSpamProbability()) {
                    significance = wordModel.getHamProbability() / wordModel.getSpamProbability();
                } else {
                    significance = wordModel.getSpamProbability() / wordModel.getHamProbability();
                }

                if(wordSignificances.size() < 9) {
                    wordSignificances.put(word, significance);
                    continue;
                }

                Optional<Map.Entry<String,Double>> minValue = wordSignificances.entrySet().stream().min(Comparator.comparingDouble(Map.Entry::getValue));
                if(!minValue.isPresent())
                    continue;

                Map.Entry<String,Double> minEntry = minValue.get();
                if(minEntry.getValue() < significance) {
                    wordSignificances.remove(minEntry.getKey());
                    wordSignificances.put(word, significance);
                }
            }
        }

        for (String word : wordSignificances.keySet()) {
            hamProbability *= wordList.get(word).getHamProbability();
            spamProbability *= wordList.get(word).getSpamProbability();
        }

        double probOfSpam = spamProbability / (spamProbability + hamProbability); // 1 = 100% Spam

        return probOfSpam;
    }
}



