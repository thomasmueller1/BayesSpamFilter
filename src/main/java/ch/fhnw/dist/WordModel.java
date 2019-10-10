package ch.fhnw.dist;

/**
 * Model für ein Wort. Anzahl Vorkommen und die Wahrscheinlichkeit in Ham und Spam werden gespeichert.
 */

class WordModel
{
    private int    SpamAmount = 0;
    private double SpamProbability;
    private int    HamAmount = 0;
    private double HamProbability;

    WordModel(boolean isSpam) {
        if(isSpam) SpamAmount = 1;
        else HamAmount = 1;
    }

    void incAmount(boolean isSpam) {
        if(isSpam) incSpamAmount();
        else incHamAmount();
    }

    int getSpamAmount() {
        return SpamAmount;
    }

    void setSpamAmount(int spamAmount) {
        SpamAmount = spamAmount;
    }

    void incSpamAmount() {
        SpamAmount++;
    }

    double getSpamProbability() {
        return SpamProbability;
    }

    void setSpamProbability(double spamProbability) {
        SpamProbability = spamProbability;
    }

    int getHamAmount() {
        return HamAmount;
    }

    void setHamAmount(int hamAmount) {
        HamAmount = hamAmount;
    }

    void incHamAmount() {
        HamAmount++;
    }

    double getHamProbability() {
        return HamProbability;
    }

    void setHamProbability(double hamProbability) {
        HamProbability = hamProbability;
    }
}
