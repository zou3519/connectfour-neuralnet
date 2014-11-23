package neuralnet;

import java.util.ArrayList;

/**
 * Represents a layer of neurons.
 *
 */
public class NeuralLayer {

    // instance variables
    private int numNeurons;
    private ArrayList<Neuron> neurons;
    private int numInputs;

    //constructor
    public NeuralLayer(int newNumNeurons, int inputs){
        neurons = new ArrayList<Neuron>();

        numNeurons = newNumNeurons;
        numInputs = inputs;

        for (int n = 0; n < numNeurons; n++){
  
            neurons.add(new Neuron(inputs));

        }//end for loop

    }//end constructor

    //getters
    public int getNumNeurons(){
        return numNeurons;
    } 

    public int getNumInputs() {
        return numInputs;
    }

    public ArrayList<Neuron> getNeurons(){
        return neurons;
    }

} //end class
