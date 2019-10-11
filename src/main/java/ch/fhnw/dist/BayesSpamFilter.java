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

        /* Banner ausgeben */
        showBanner();

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

    private static  void showBanner() {
        System.out.println(" ____      __     ________  _____    _____                                  ______ _ _ _            ");
        System.out.println("|  _ \\   /\\\\ \\   / /  ____|/ ____|  / ____|                                |  ____(_) | |           ");
        System.out.println("| |_) | /  \\\\ \\_/ /| |__  | (___   | (___  _ __   __ _ _ __ ___    ______  | |__   _| | |_ ___ _ __ ");
        System.out.println("|  _ < / /\\ \\\\   / |  __|  \\___ \\   \\___ \\| '_ \\ / _` | '_ ` _ \\  |______| |  __| | | | __/ _ \\ '__|");
        System.out.println("| |_) / ____ \\| |  | |____ ____) |  ____) | |_) | (_| | | | | | |          | |    | | | ||  __/ |   ");
        System.out.println("|____/_/    \\_\\_|  |______|_____/  |_____/| .__/ \\__,_|_| |_| |_|          |_|    |_|_|\\__\\___|_|   ");
        System.out.println("                                          | |                                                       ");
        System.out.println("                                          |_|         by Thierry Hundt, Thomas Müller, Markus Winter\n");
    }


}