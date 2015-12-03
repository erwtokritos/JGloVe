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
public class OrderedIntegerPair {
    
    private int word1;
    private int word2;

    public OrderedIntegerPair(int word1, int word2) {
                                
        this.word1 = word1;
        this.word2 = word2;
    }
        
    
    public int getWord1() {
        return word1;
    }


    public int getWord2() {
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
        final OrderedIntegerPair other = (OrderedIntegerPair) obj;
        
        
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
