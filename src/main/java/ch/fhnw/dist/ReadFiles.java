package ch.fhnw.dist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadFiles {
    public ReadFiles() {
        /* silence is golden */

    }
    public void listAllFiles(File folder){
        System.out.println("In listAllfiles(File) method");
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
    }

    public void readContent(File file) throws IOException{
        System.out.println("read file " + file.getCanonicalPath() );
        try(BufferedReader br  = new BufferedReader(new FileReader(file))){
            String strLine;
            // Read lines from the file, returns null when end of stream is reached
            while((strLine = br.readLine()) != null){
                String[] currencies = strLine.split(" ");
                for (String word : currencies){
                    word = word.toUpperCase().trim();
                    if(!word.isEmpty()) System.out.println(word);
                }

            }
        }
    }
}



