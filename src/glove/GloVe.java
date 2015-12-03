/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glove;

import glove.objects.Cooccurrence;
import glove.objects.OrderedIntegerPair;
import glove.objects.Vocabulary;
import glove.utils.BigFile;
import glove.utils.Methods;
import glove.utils.Options;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jblas.DoubleMatrix;
import org.jblas.MatrixFunctions;

/**
 *
 * @author Thanos
 */
public class GloVe {

    private static java.util.logging.Logger _logger = Logger.getLogger(GloVe.class.getName());

    public static Vocabulary build_vocabulary(String file, Options options) {

        Vocabulary vocab = new Vocabulary();

        BigFile stream;
        try {
            stream = new BigFile(file);
            Iterator<String> iterator = stream.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] tokens = line.split(" ");

                for (String token : tokens) {
                    vocab.addOrUpdate(token);
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(GloVe.class.getName()).log(Level.SEVERE, null, ex);
        }

        if(options.debug) {
            _logger.info("Building vocabulary complete.. There are " + vocab.getSize() + " terms");
        }
        return vocab;
    }

    public static List<Cooccurrence> build_cooccurrence(Vocabulary vocab, String corpusFile, Options options) {
       
        HashMap<OrderedIntegerPair, Double> cooccurrence_map = new HashMap<OrderedIntegerPair, Double>(vocab.getSize() * 2);

        try {
            BigFile stream = new BigFile(corpusFile);
            Iterator<String> iterator = stream.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                String[] tokens = line.split(" ");

                List<Integer> token_ids = vocab.recoverIds(tokens);

                //iterate over each element
                for (int center_i = 0; center_i < token_ids.size(); center_i++) {

                    int center_id = token_ids.get(center_i);

                    int start = Math.max(0, center_i - options.window_size);
                    int end = center_i;

                    //check 
                    if (end < start) {
                        continue;
                    }

                    List<Integer> context_ids = Methods.getSubset(token_ids, start, end);
                    int context_len = context_ids.size();

                    for (int left_i = 0; left_i < context_len; left_i++) {

                        Integer left_id = context_ids.get(left_i);

                        int distance = context_len - left_i;
                        double increment = 1.0 / distance;
                        OrderedIntegerPair pair1 = new OrderedIntegerPair(center_id, left_id);
                        Double get = cooccurrence_map.get(pair1);
                        if (get == null) {
                            get = 0.0;
                        }

                        get += increment;
                        cooccurrence_map.put(pair1, get);

                        OrderedIntegerPair pair2 = new OrderedIntegerPair(left_id, center_id);
                        get = cooccurrence_map.get(pair2);
                        if (get == null) {
                            get = 0.0;
                        }

                        get += increment;
                        cooccurrence_map.put(pair2, get);

                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(GloVe.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Cooccurrence> coocurrences = new ArrayList<>();
        for (Entry<OrderedIntegerPair, Double> entry : cooccurrence_map.entrySet()) {

            Cooccurrence c = new Cooccurrence(entry.getKey().getWord1(),
                    entry.getKey().getWord2(),
                    entry.getValue());

            coocurrences.add(c);
        }

        return coocurrences;
    }

    public static DoubleMatrix train(Vocabulary vocab, List<Cooccurrence> cooccurrences, Options options) {

        int vocab_size = vocab.getSize();
        DoubleMatrix W = (DoubleMatrix.rand(options.vector_size, vocab_size * 2).sub(0.5)).mmul(1.0 / (options.vector_size + 1));
        DoubleMatrix biases = (DoubleMatrix.rand(1, vocab_size * 2).sub(0.5)).mmul(1.0 / (options.vector_size + 1));
        DoubleMatrix gradient_squared = DoubleMatrix.ones(options.vector_size, vocab_size * 2);
        DoubleMatrix gradient_squared_biases = DoubleMatrix.ones(1, vocab_size * 2);

        for (int iteration = 1; iteration <= options.iterations; iteration++) {

            double global_cost = 0.0;
            
            Collections.shuffle(cooccurrences);
            for (Cooccurrence c : cooccurrences) {

                int i_main = c.getI_main();
                int i_context = c.getI_context();
                double coocurrence = c.getXij();

                double weight;
                if (coocurrence < options.x_max) {
                    weight = Math.pow(coocurrence / options.x_max, options.alpha);
                } else {
                    weight = 1.0;
                }

                DoubleMatrix v_main = W.getColumn(i_main);
                DoubleMatrix v_context = W.getColumn(vocab_size + i_context);
                DoubleMatrix gradsq_W_main = gradient_squared.getColumn(i_main);
                DoubleMatrix gradsq_W_context = gradient_squared.getColumn(i_context + vocab_size);
                double gradsq_b_main = gradient_squared_biases.get(0, i_main);
                double gradsq_b_context = gradient_squared_biases.get(0, i_context + vocab_size);

                double b_main = biases.get(0, i_main);
                double b_context = biases.get(0, vocab_size + i_context);
                double cost_inner = v_main.dot(v_context) + b_main + b_context - Math.log(coocurrence);

                double cost = weight * Math.pow(cost_inner, 2);
                global_cost += 0.5 * cost;

                // Compute gradients for word vector terms.
                DoubleMatrix grad_main = v_context.mmul(cost_inner);
                DoubleMatrix grad_context = v_main.mmul(cost_inner);

                // Compute gradients for bias terms                       
                double grad_bias_main = cost_inner;
                double grad_bias_context = cost_inner;

                // Perform adaptive updates
                v_main = v_main.sub(grad_main.divi(MatrixFunctions.sqrt(gradsq_W_main)).mmul(options.learning_rate));
                W.putColumn(i_main, v_main);

                v_context = v_context.sub(grad_context.divi(MatrixFunctions.sqrt(gradsq_W_context)).mmul(options.learning_rate));
                W.putColumn(vocab_size + i_context, v_context);

                b_main -= (options.learning_rate * grad_bias_main / Math.sqrt(gradsq_b_main));
                biases.put(0, i_main, b_main);

                b_context -= (options.learning_rate * grad_bias_context / Math.sqrt(gradsq_b_context));
                biases.put(0, vocab_size + i_context, b_context);

                // Update squared gradient sums
                gradsq_W_main = gradsq_W_main.add(MatrixFunctions.pow(grad_main, 2));
                gradient_squared.putColumn(i_main, gradsq_W_main);

                gradsq_W_context = gradsq_W_context.add(MatrixFunctions.pow(grad_context, 2));
                gradient_squared.putColumn(i_context + vocab_size, gradsq_W_context);

                gradsq_b_main += Math.pow(grad_bias_main, 2);
                gradient_squared_biases.put(0, i_main, gradsq_b_main);

                gradsq_b_context += Math.pow(grad_bias_context, 2);
                gradient_squared_biases.put(0, i_context + vocab_size, gradsq_b_context);

            }
            
            if(options.debug)
                System.out.println("Iteration #" + iteration + " , cost = " + global_cost);
        }

        int[] c1 = Methods.getSequenceArr(0, vocab_size);
        int[] c2 = Methods.getSequenceArr(vocab_size, 2 * vocab_size);
        
        return Methods.average(W.getColumns(c1), W.getColumns(c2));

    }

}
