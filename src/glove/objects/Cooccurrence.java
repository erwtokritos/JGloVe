/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package glove.objects;

/**
 *
 * @author Thanos
 */
public class Cooccurrence implements Comparable<Cooccurrence>{
    
    private int i_main;
    private int i_context;
    private double Xij;

    public Cooccurrence(int i_main, int i_context, double Xij) {
        this.i_main = i_main;
        this.i_context = i_context;
        this.Xij = Xij;
    }

    public int getI_main() {
        return i_main;
    }

    public void setI_main(int i_main) {
        this.i_main = i_main;
    }

    public int getI_context() {
        return i_context;
    }

    public void setI_context(int i_context) {
        this.i_context = i_context;
    }

    public double getXij() {
        return Xij;
    }

    public void setXij(double Xij) {
        this.Xij = Xij;
    }        

    @Override
    public int compareTo(Cooccurrence other) {
            
        final int BEFORE = -1;        
        final int EQUAL = 0;                           
        final int AFTER = 1;
        
        if(this.i_main < other.i_main) {
            return BEFORE;
        }else if(this.i_main > other.i_main){
            return AFTER;
        }else {                                
            if(this.i_context < other.i_context) {            
                return BEFORE;
            }else if(this.i_context > other.i_context){           
                return AFTER;       
            }else {
                return EQUAL;
            }
        }
        
    }
    
}
