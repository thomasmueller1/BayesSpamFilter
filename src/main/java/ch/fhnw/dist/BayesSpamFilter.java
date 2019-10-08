package ch.fhnw.dist;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class BayesSpamFilter {

    public static void main(String[] args) throws IOException {
        FilterEducation trainer = new FilterEducation();
        HashMap<String, WordModel> wordList = trainer.educateFilter();
        FileHelper fh = new FileHelper();

        /*wordList.forEach((s, wordModel) ->
            System.out.println(s + ": " + wordModel.getHamAmount() + " (" + wordModel.getHamProbability() + "%)  |  " + wordModel.getSpamAmount() + " (" + wordModel.getSpamProbability()+"%)")
        );*/

        String zipMAILS = CONFIG.MAILS_TO_TEST_ZIP + ".zip";
        String tempDir = CONFIG.DEFAULT_TEMP_DIR + "/" + CONFIG.MAILS_TO_TEST_ZIP;
        ArrayList<String> wordsInMail;

        File fileMAILS = new File(tempDir);

            fh.unzip(zipMAILS, tempDir);
            ArrayList<File> files = fh.getAllFilesFromDirectory(fileMAILS);

            for (File file : files) {
                wordsInMail = fh.readFileContentToList(file, false);
                //System.out.println(file.getName() + " (" + wordsInMail.size() + " words)");

                /* TODO: Thomas -> Bestimmen ob Spam oder nicht bzw. mit welcher Wahrscheinlichkeit */

            }


    }

}