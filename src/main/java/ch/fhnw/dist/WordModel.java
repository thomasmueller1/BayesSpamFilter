package ch.fhnw.dist;

public class WordModel
{
    private String Word;
    private int SpamAmount = 0;
    private double SpamProbability;
    private int HamAmount = 0;
    private double HamProbability;

    public WordModel(boolean spam) {
        if(spam) SpamAmount = 1;
        else HamAmount = 1;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        Word = word;
    }

    public int getSpamAmount() {
        return SpamAmount;
    }

    public void setSpamAmount(int spamAmount) {
        SpamAmount = spamAmount;
    }

    public void incSpamAmount() {
        SpamAmount++;
    }

    public double getSpamProbability() {
        return SpamProbability;
    }

    public void setSpamProbability(double spamProbability) {
        SpamProbability = spamProbability;
    }

    public int getHamAmount() {
        return HamAmount;
    }

    public void setHamAmount(int hamAmount) {
        HamAmount = hamAmount;
    }

    public void incHamAmount() {
        HamAmount++;
    }

    public void incAmount(boolean isSpam) {
        if(isSpam) incSpamAmount();
        else incHamAmount();
    }

    public double getHamProbability() {
        return HamProbability;
    }

    public void setHamProbability(double hamProbability) {
        HamProbability = hamProbability;
    }
}
