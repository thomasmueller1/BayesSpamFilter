package ch.fhnw.dist;

import java.io.IOException;
import java.util.*;

/**
 * Hauptklasse des Bayes Spamfilters
 */

public class BayesSpamFilter {
    public static void main(String[] args) throws IOException {
        FilterHelper filter = new FilterHelper();
        HashMap<String, WordModel> wordList  = filter.educateFilter(); // Filter trainieren
        HashMap<String, Integer> resultsHam  = filter.determineSpamProbability(wordList, CONFIG.HAM_MAILS_TO_TEST_ZIP); // Spamwahrscheinlichkeit in Ham Testmails
        HashMap<String, Integer> resultsSpam = filter.determineSpamProbability(wordList, CONFIG.SPAM_MAILS_TO_TEST_ZIP); // Spamwahrscheinlichkeit in Spam Testmails

        /* Allgemeine Infos ausgeben */
        System.out.println("Schwellwert: " + CONFIG.SPAM_THRESHOLD);
        System.out.println("Alpha Wert:  " + CONFIG.PROBABILITY_ON_ZEROMAILS);
        System.out.println("Anz. Wörter: " + CONFIG.NUM_SIGNIFICANT_WORDS + " (Signifikanteste Wörter für Spam Erkennung)\n");

        /* Resultate ausgeben */
        System.out.println("HAM Testmails Erkennungsraten:");
        showResults(resultsHam);
        System.out.println("\nSPAM Testmails Erkennungsraten:");
        showResults(resultsSpam);
    }


    /**
     * Erkennungsraten anzeigen
     */
    private static void showResults(HashMap<String, Integer> results) {
        int ham = results.get("ham"), spam = results.get("spam");
        double total = results.get("ham") + results.get("spam");

        System.out.printf("Ham:  %s%% (%d)%n", Math.round(ham/total*100*100.0)/100.0, ham);
        System.out.printf("Spam: %s%% (%d)%n", Math.round(spam/total*100*100.0)/100.0, spam);
    }


}