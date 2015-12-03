/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package glove.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Thanos
 */
public class WordPair {
    
    private String word1;
    private String word2;

    public WordPair(String word1, String word2) {
                        
        List<String> words = new ArrayList<>();
        words.add(word1);
        words.add(word2);
        Collections.sort(words);
        
        this.word1 = words.get(0);
        this.word2 = words.get(1);
    }
        
    
    public String getWord1() {
        return word1;
    }


    public String getWord2() {
        return word2;
    }


    @Override
    public int hashCode() {
        int hash = 3;
       
        hash = 47 * hash + Objects.hashCode(this.word1);
        hash = 47 * hash + Objects.hashCode(this.word2);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WordPair other = (WordPair) obj;
        
        
        if (!Objects.equals(this.word1, other.word1)) {
            return false;
        }
        if (!Objects.equals(this.word2, other.word2)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "WordPair{" + "word1=" + word1 + ", word2=" + word2 + '}';
    }           
}
