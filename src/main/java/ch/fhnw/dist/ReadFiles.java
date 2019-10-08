package ch.fhnw.dist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReadFiles {
    HashMap<String, WordModel> wordModels = new HashMap<>();
    boolean isSpam;

    public ReadFiles() {

    }

    public void listAllFiles(File folder){
        File[] fileNames = folder.listFiles();
        for(File file : fileNames){
            // if directory call the same method again
            if(file.isDirectory()){
                listAllFiles(file);
            } else{
                try {
                    readContent(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        wordModels.forEach((s, wordModel) -> {
            double wordModelsSize = (double)wordModels.size()/100;
            wordModel.setHamProbability(wordModel.getHamAmount()/wordModelsSize);
            wordModel.setSpamProbability(wordModel.getSpamAmount()/wordModelsSize);
        });

        wordModels.forEach((s, wordModel) -> {
            if(wordModel.getHamProbability() > 1) System.out.println(s + " ::::: " + wordModel.getHamProbability());
        });
    }

    public void readContent(File file) throws IOException{
        //System.out.println("read file " + file.getCanonicalPath() );
        try(BufferedReader br  = new BufferedReader(new FileReader(file))){
            String strLine;
            Set<String> wordList = new HashSet(); //auto distinct

            isSpam = file.getName().contains("spam");

            // Read lines from the file, returns null when end of stream is reached
            while((strLine = br.readLine()) != null){
                String[] wordSplitted = strLine.split(" ");
                wordList.addAll(Arrays.stream(wordSplitted).map(s -> s.toUpperCase().trim()).filter(s -> !s.isEmpty()).collect(Collectors.toList()));
            }

            for (String word : wordList){
                //System.out.println(word);
                if(!wordModels.containsKey(word)) wordModels.put(word, new WordModel(isSpam));
                else wordModels.get(word).incAmount(isSpam);
            }
        }
    }
}



