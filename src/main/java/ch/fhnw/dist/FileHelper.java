package ch.fhnw.dist;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

class FileHelper {
    private static final int BUFFER_SIZE = 4096;
    private static final ClassLoader INTERNAL_FILE_PATH = BayesSpamFilter.class.getClassLoader();

    ArrayList<String> readFileContentToList(File file, Boolean SelectDistinct) throws IOException {
        ArrayList<String> lst = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String strLine;
            while ((strLine = br.readLine()) != null) {
                String[] wordSplitted = strLine.split(" ");
                Arrays.stream(wordSplitted).filter(s -> !s.isEmpty()).forEach(s -> lst.add(formatWord(s)));
            }
        }
        if (SelectDistinct) {
            return (ArrayList<String>) lst.stream().distinct().collect(Collectors.toList());
        }
        return lst;
    }

    private String formatWord(String word) {
        return word.toUpperCase().trim();
    }

    ArrayList<File> getAllFilesFromDirectory(File folder) {
        ArrayList<File> files = new ArrayList<>();
        File[] fileNames = folder.listFiles();
        if (fileNames == null) return files;
        for (File file : fileNames) {
            // if directory call the same method again
            if (file.isDirectory()) {
                files.addAll(getAllFilesFromDirectory(file));
            } else {
                files.add(file);
            }
        }
        return files;
    }

    HashMap<String, WordModel> createHashMapFromWords(ArrayList<String> results, Boolean isSpam) {
        HashMap<String, WordModel> wordModels = new HashMap<>();

        for(String word : results) {
            if(wordModels.containsKey(word)) {
                wordModels.get(word).incAmount(isSpam);
            } else{
                wordModels.put(word, new WordModel(isSpam));
            }
        }
        return wordModels;
    }

    void unzip(String zipFilePath, String destDirectory) throws IOException {
        zipFilePath = Objects.requireNonNull(INTERNAL_FILE_PATH.getResource(zipFilePath)).getPath();

        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }

    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }
}
