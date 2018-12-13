package sample.dictionary;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Translator {
    private static String path= System.getProperty("user.dir")+ "/dict.txt";
    static Map words;

    Translator(){}


    private static void getDictionary() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path));
            words= new HashMap<String, String>();
            for (String line : lines) {
                words.put(line.substring(0,line.indexOf('-')-1),line.substring(line.indexOf('-')+2));
                //System.out.println(line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String searchWord(String word){
        if (words.containsKey(word)){
            return  (String)words.get(word);
        }
        else {
            return "Для данного слова отсутствует перевод";
        }
    }


    public static String doTranslate(String word){
        getDictionary();
        return searchWord(word.toLowerCase());
    }
}