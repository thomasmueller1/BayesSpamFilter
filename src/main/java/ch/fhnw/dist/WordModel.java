package ch.fhnw.dist;

class WordModel
{
    private String Word;
    private int SpamAmount = 0;
    private double SpamProbability;
    private int HamAmount = 0;
    private double HamProbability;

    WordModel(boolean spam) {
        if(spam) SpamAmount = 1;
        else HamAmount = 1;
    }

    String getWord() {
        return Word;
    }

    void setWord(String word) {
        Word = word;
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

    void incAmount(boolean isSpam) {
        if(isSpam) incSpamAmount();
        else incHamAmount();
    }

    double getHamProbability() {
        return HamProbability;
    }

    void setHamProbability(double hamProbability) {
        HamProbability = hamProbability;
    }
}
