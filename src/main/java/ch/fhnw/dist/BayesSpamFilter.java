package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Hauptklasse des Bayes Spamfilters
 */

public class BayesSpamFilter {

    public static void main(String[] args) throws IOException {
        FilterEducation trainer = new FilterEducation();
        HashMap<String, WordModel> wordList = trainer.educateFilter();

        FileHelper fh = new FileHelper();

        String zipMAILS = CONFIG.HAM_MAILS_TO_TEST_ZIP + ".zip";
        String tempDir = CONFIG.DEFAULT_TEMP_DIR + "/" + CONFIG.HAM_MAILS_TO_TEST_ZIP;
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

                System.out.println(result);
            }

            System.out.println("Threshold: " + CONFIG.SPAM_THRESHOLD + "  |  HAM: " + countHAM + "  |  SPAM: " + countSPAM);

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