package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BayesSpamFilter {

    public static void main(String[] args) throws IOException {
        FilterEducation trainer = new FilterEducation();
        HashMap<String, WordModel> wordList = trainer.educateFilter();
        //Map<String, WordModel> wordListFiltered = wordList.entrySet().stream().filter(w -> w.getValue().getHamProbability() > .8 || w.getValue().getSpamProbability() > .8).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

        FileHelper fh = new FileHelper();

        /*wordList.forEach((s, wordModel) -> {
            System.out.println(s + ": " + wordModel.getHamAmount() + " (" + wordModel.getHamProbability() + "%)  |  " + wordModel.getSpamAmount() + " (" + wordModel.getSpamProbability()+"%)");
        });*/

        String zipMAILS = CONFIG.MAILS_TO_TEST_ZIP + ".zip";
        String tempDir = CONFIG.DEFAULT_TEMP_DIR + "/" + CONFIG.MAILS_TO_TEST_ZIP;
        ArrayList<String> wordsInMail;

        File fileMAILS = new File(tempDir);
        //fh.unzip(zipMAILS, tempDir);
        ArrayList<File> files = fh.getAllFilesFromDirectory(fileMAILS);

        int countHAM = 0;
        int countSPAM = 0;
        double threshold = 0.6;

            for (File file : files) {
                wordsInMail = fh.readFileContentToList(file, false);
                //System.out.println(file.getName() + " (" + wordsInMail.size() + " words)");

                if(calcPajassAlgo(wordsInMail, wordList) > threshold) countSPAM++;
                else countHAM++;
            }

            System.out.println("Threshold: " + threshold + "  |  HAM: " + countHAM + "  |  SPAM: " + countSPAM);

    }

    private static double calcPajassAlgo(ArrayList<String> wordsInMail, HashMap<String, WordModel> wordList) {
        /* TODO: Thomas prüfen-> Richtig so??? */

        double hamProbability  = 1.0d;
        double spamProbability = 1.0d;

        for (String word: wordsInMail) {
            if(wordList.containsKey(word)) {
                hamProbability *= wordList.get(word).getHamProbability();
                spamProbability *= wordList.get(word).getSpamProbability();

                //System.out.println("multiply " + wordList.get(word).getSpamProbability());
            }
        }


        double probOfSpam = spamProbability / (spamProbability + hamProbability); // 1 = 100% Spam

        //probOfSpam = isNaN(probOfSpam) ? 1.0 : probOfSpam;

        /*System.out.println("hamProbability: " + hamProbability);
        System.out.println("spamProbability: " + spamProbability);*/
        //System.out.println("probOfSpam: " + probOfSpam);
        //System.out.println("-----------------------");
        return probOfSpam;
    }

}