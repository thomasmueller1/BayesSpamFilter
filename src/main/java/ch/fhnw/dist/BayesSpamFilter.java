package ch.fhnw.dist;

public class BayesSpamFilter {

    public static void main(String[] args) {
        FilterEducation trainer = new FilterEducation();
        trainer.educateFilter();

        trainer.getWords().forEach((s, wordModel) -> {
            if (wordModel.getHamAmount() == 0 || wordModel.getSpamAmount() == 0) {
                System.out.println(s + ": " + wordModel.getHamAmount() + "(" + wordModel.getHamProbability() + ") | " + wordModel.getSpamAmount() + " (" + wordModel.getSpamProbability()+")");
            }
        });
    }

}