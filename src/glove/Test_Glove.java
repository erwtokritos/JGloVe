/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package glove;

import glove.objects.Cooccurrence;
import glove.objects.Vocabulary;
import glove.utils.Methods;
import glove.utils.Options;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jblas.DoubleMatrix;
 
/**
 *
 * @author Thanos
 */
public class Test_Glove {
     
    public static void main(String[] args) {
        
        String file = "test.txt";
                
        Options options = new Options(); 
        options.debug = true;
        
        Vocabulary vocab = GloVe.build_vocabulary(file, options);
        
        options.window_size = 3;
        List<Cooccurrence> c =  GloVe.build_cooccurrence(vocab, file, options);
        
        options.iterations = 10;
        options.vector_size = 10;
        options.debug = true;
        DoubleMatrix W = GloVe.train(vocab, c, options);  

        List<String> similars = Methods.most_similar(W, vocab, "graph", 15);
        for(String similar : similars) {
            System.out.println("@" + similar);
        }
        
    }
    
}
