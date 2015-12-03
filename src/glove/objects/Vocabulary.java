/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package glove.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Logger;

/**
 *
 * @author Thanos
 */
public class Vocabulary {
    
    private Logger _logger = Logger.getLogger(Vocabulary.class.getName());
    
    private HashMap<String, Integer> word2id;
    private HashMap<Integer, String> id2word;
    private HashMap<String, Integer> wordFreqs;
    
    private int current_size = 0;

    public Vocabulary() {
        word2id = new HashMap<>(5000);
        id2word = new HashMap<>(5000);
        wordFreqs = new HashMap<>(5000);
    }
    
       
    public Vocabulary(int initialCapacity) {
        word2id = new HashMap<>(initialCapacity);
        id2word = new HashMap<>(initialCapacity);
        wordFreqs = new HashMap<>(initialCapacity);
    } 

    public int getSize() {
        return current_size;
    }
            
    
    public Integer getWordId(String word) {
        return word2id.get(word);
    }
    
    public String getWord(Integer id) {
        return id2word.get(id);
    }
    
    public void addOrUpdate(String word) {
           
        Integer get = word2id.get(word);                
        if(get == null) { //if word does not exist

            int assigned_id = current_size;
            current_size++;
            word2id.put(word, assigned_id);
            id2word.put(assigned_id, word);
            wordFreqs.put(word, 1);
            
        }else {
           
            Integer freq = wordFreqs.get(word);
            freq++;
            wordFreqs.put(word, freq);
        }
    }
    
    public List<Word> iterate() {
        
        List<Word> list = new ArrayList<>();
        
        for(Entry<String, Integer> entry : word2id.entrySet()) {
            
            Word word = new Word();
            word.setText(entry.getKey());
            word.setId(entry.getValue());
            word.setFrequency(wordFreqs.get(entry.getKey()));
            list.add(word);
        }
        
        return list;
    }
    
    
    public List<Integer> recoverIds(String[] words) {
        List<Integer> list = new ArrayList();
        for(String word : words) {
            Integer get = word2id.get(word);
            if(get != null) {
                list.add(get);
            }
        }
        
        return list;        
    }
    
    public void print() {
        
           
        List<Word> words = new ArrayList();
        String txt = "\n";
        for(Entry<String, Integer> entry : word2id.entrySet()) {
            
            Word word = new Word();
            word.setText(entry.getKey());
            word.setId(entry.getValue());
            word.setFrequency(wordFreqs.get(entry.getKey()));
            words.add(word);                                    
        }
        
        Collections.sort(words);
        for(Word word : words) {
                
            txt += word.toString() + "\n";                                
        }
        _logger.info(txt);
    }
    
}
