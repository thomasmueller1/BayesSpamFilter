package ch.fhnw.dist;

/**
 * Einige wichtige Konstanten
 */

class CONFIG {
    final static String DEFAULT_TEMP_DIR         = "C:/Temp";   // OHNE abschliessenden Slash
    final static double PROBABILITY_ON_ZEROMAILS = 0.00001;     // Wenn ein Wort NUR in Ham oder Spam vorkommt (alpha)
    final static double SPAM_THRESHOLD           = 0.8;         // Grenze wann ein Mail als Spam eingestuft wird
    final static String HAM_MAILS_TO_TEST_ZIP    = "ham-test";  // Dateiname für das ZIP von den Ham Testmails
    final static String SPAM_MAILS_TO_TEST_ZIP   = "spam-test"; // Dateiname für das ZIP von den Spam Testmails
    final static int    NUM_SIGNIFICANT_WORDS    = 10;          // Anzahl signifikanteste Wörter für Spam-Erkennung
}