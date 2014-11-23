package neuralnet;

import java.util.ArrayList;

/**
 * Represents a neuron.
 *
 */
public class Neuron {

    // instance variables
    private int numInputs;
    private ArrayList<Double> weights;

    // constructor
    public Neuron(int inputs){
        numInputs = inputs;
        weights = new ArrayList<Double>();

        for (int n = 0; n < inputs + 1; n++){

            weights.add(Math.random() * 2 - 1);

        } //end for loop

    }//end constructor

    //getters
    public int getNumInputs(){
        return numInputs;
    }

    public ArrayList<Double> getWeights(){
        return weights;
    }

    //setter (numInputs should not be able to change)
    public void setWeights(ArrayList<Double> newWeights){
        weights = newWeights;
    }

}//end class

