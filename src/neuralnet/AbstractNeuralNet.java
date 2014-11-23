package neuralnet;

import java.util.ArrayList;

/**
 * The interface for NeuralNet.
 *
 */
public interface AbstractNeuralNet {

    // get the weights from the neural network
    public ArrayList<Double> getWeights(int layerNum, int neuronNum);
  
    public int getNumInputs();
    public int getNumberOfWeights();
    public int getNumOutputs();
    public int getNumHiddenLayers();
    public ArrayList<Integer> getNeuronsPerHiddenLayer();

    //replace the weights with new ones
    public void putWeights(
        ArrayList<Double> newWeights, int layerNum, int neuronNum);

    //calculates the outputs from inputs
    public ArrayList<Double> calculateOutputs(ArrayList<Double> inputs);

    // sigmoid response curve
    public double sigmoid(double activation, double response);
}

