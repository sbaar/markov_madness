package com.example.myapplication8;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Created by SBaar on 12/22/13.
 */
public class Markov {
    Map<String, Map<ArrayList<String>, Integer>> mMap = new HashMap<String, Map<ArrayList<String>, Integer>>(); //<word, <[words], frequency>>
    Map<String, Integer> totals = new HashMap<String, Integer>();
    ArrayList<String> starts = new ArrayList<String>();
    Set<String> terminals = new HashSet<String>();
    Random random = new Random();
    int totalWords = 0;
    public void add(String first, List<String> second){//add association to map
        Map newHM = new HashMap<ArrayList<String>, Integer>();
        //TODO lowercase punctuation
        if (!mMap.containsKey(first)){
            newHM.put(second, 1);
            mMap.put(first, newHM);
        }
        else {
            newHM = mMap.get(first);
            int freq = 1;
            if (newHM.containsKey(second)) freq = (Integer)newHM.get(second);
            newHM.put(second, freq);
        }
    }
    public String tokenize(String s){
        StringBuilder sb = new StringBuilder(s);
    return "ERRRR";
    }
    public void addSentence(String sentence, int gramSize){
        sentence = sentence.replace('.', ' ');
        sentence = sentence.replace('-', ' ');
        sentence = sentence.replace(',', ' ');
        sentence = sentence.trim();
        //sentence = tokenize(sentence);//TODO better tokenizer
        String [] words = sentence.split(" +");
        if (words.length < 2) return;
        starts.add(words[0]);
        terminals.add(words[words.length-1]);

        String first;
        List<String>  second = new ArrayList<String>();
        for (int i = 0; i < words.length; i++){
        first = words[i];
            for (int j = 1; j < gramSize + 1; j++)
                if (i + j < words.length) second.add(words[i + j]);
        add(first, second);
        second = new ArrayList<String>();
        }
    }
    public List<QuoteModel> generate(int number, int minsize, int maxsize)
    {
        List<QuoteModel> sentences = new ArrayList<QuoteModel>();
        while(sentences.size() < number){
            ArrayList<String> sentence = new ArrayList<String>(1);
            String word = starts.get(random.nextInt(starts.size()));
            sentence.add(word);
            while (sentence.size() < maxsize){
                List<String> words = choose(mMap.get(word));
                sentence.addAll(words);
                if (words.isEmpty()) break;
                word = sentence.get(sentence.size() - 1);
                if (terminals.contains(sentence.get(sentence.size() - 1)) && sentence.size() >= minsize)
                    break;

            }
            StringBuilder builder = new StringBuilder();
            for(String s : sentence) {
                builder.append(s);
                builder.append(" ");
            }
            Log.d("sentence", builder.toString());
            sentences.add(new QuoteModel(builder.toString().trim()));
        }
            return sentences;


        }

public List<String> choose(Map<ArrayList<String>, Integer> map){
    int sum = 0;
    int sum2 = 0;
    for (Integer val : map.values())
        sum += val;
    int r =random.nextInt(sum);
    for (Map.Entry<ArrayList<String>, Integer> entry : map.entrySet())
    {
        sum2 += entry.getValue();
        if (r <= sum2)
            return entry.getKey();
    }
    return new ArrayList<String>();

}
}
