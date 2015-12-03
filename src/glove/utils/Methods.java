/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package glove.utils;

import glove.objects.IntegerDoublePair;
import glove.objects.Vocabulary;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jblas.DoubleMatrix;

/**
 *
 * @author Thanos
 */
public class Methods {
    
    public static List<Integer> getSequence(int start, int end) {
        
        List<Integer> list = new ArrayList<>();
        for(int i = start; i <= end; i++) {
            list.add(i);
        }        
        return list;
    }
    
        
    public static int[] getSequenceArr(int start, int end) {
        
        int len = end - start;
        int[] ind = new int[len];   
        int index = 0;
        
        for(int i = start; i < end; i++) {
            ind[index] = i;
            index++;
        }        
        return ind;
    }
       
    public static List<Integer> getSubset(List<Integer> list, int start, int end) {
        
        if(start < 0 || end > (list.size() - 1)) {
            return null;
        } 
        
        List<Integer> response = new ArrayList<>();
        for(int i = start; i < end; i++) {
            response.add(list.get(i));
        }        
        return response;
    }
    
    public static DoubleMatrix average(DoubleMatrix a, DoubleMatrix b) {

        if ((a.getRows() != b.getRows()) || (a.getColumns() != b.getColumns())) {
            System.err.println("Dimensions do not match.. (" + a.getRows() + "x" + a.getColumns() + ")"
                    + "( " + b.getRows() + "x" + b.getColumns() + ")");
            return null;
        }

        DoubleMatrix c = DoubleMatrix.zeros(a.getRows(), a.getColumns());
        for (int row = 0; row < c.getRows(); row++) {
            for (int col = 0; col < c.getColumns(); col++) {                                                   
                c.put(row, col, (a.get(row, col) + b.get(row, col))/2 );   
            }
        }

        return c;

    }
    
    public static DoubleMatrix merge (DoubleMatrix a, DoubleMatrix b) {
        
        if(a.getRows() != b.getRows()) {
            System.err.println("Dimensions do not match.. (" + a.getRows() + "x" + a.getColumns() + ")"
            + "( " + b.getRows() + "x" + b.getColumns() + ")");
            return null;
        }
        
        int totalCols = a.getColumns() + b.getColumns();
        DoubleMatrix c = DoubleMatrix.zeros(a.getRows(), totalCols);
        for(int row = 0; row < c.getRows(); row++) {
            for(int col = 0; col < c.getColumns(); col++ ) {
                
                System.out.println("( " + row + " , " + col + ")");
                if(col < a.getColumns()) {
                    c.put(row, col, a.get(row, col));
                }else {
                    c.put(row, col, b.get(row, col - a.getColumns()));
                }
                
            }
        }
        
        return c;
    }
    
    
    public static List<String> most_similar(DoubleMatrix W, Vocabulary vocab, String word, int n) {                
        
        List<String> response = new ArrayList<>();
        int word_id = vocab.getWordId(word);
        DoubleMatrix wordVector = W.getColumn(word_id);
        List<IntegerDoublePair> list = new ArrayList<>();
        
        for(int i = 0; i < W.getColumns(); i++ ) {            
            if(i != word_id) {
                
                DoubleMatrix curr = W.getColumn(i);
                double result = wordVector.dot(curr);
                IntegerDoublePair pair = new IntegerDoublePair(i, result);
                list.add(pair);
            }
        }
        Collections.sort(list);
        int min = Math.min(list.size(), n);
        
        for(int i = 0; i < min; i++) {
            IntegerDoublePair p = list.get(i);
            response.add(vocab.getWord(p.getId()));
        }
        
        return response;
    }
    
    
    
}
