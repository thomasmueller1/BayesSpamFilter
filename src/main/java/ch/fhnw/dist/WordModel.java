package ch.fhnw.dist;

public class WordModel
{
    private String Word;

    private int SpamAmount;

    private double SpamProbability;

    private int HamAmount;

    private double HamProbability;

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

    public double getHamProbability() {
        return HamProbability;
    }

    public void setHamProbability(double hamProbability) {
        HamProbability = hamProbability;
    }
}
