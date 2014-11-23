package neuralnet;

import java.util.ArrayList;
import genetic.Genome;
import io.IO;

/**
 * Represents a Neural Network.
 *
 * Structure taken loosely from 
 * http://www.ai-junkie.com/ann/evolved/nnt1.html
 *
 */

public class NeuralNet implements AbstractNeuralNet {

    // variables
    private int numInputs;
    private int numOutputs;
    private int numHiddenLayers;
    private ArrayList<Integer> neuronsPerHiddenLayer;

    //storage for each layer of neurons including the output layer
    private ArrayList<NeuralLayer> layers;

    //standard constructor
    public NeuralNet(int inputs, int outputs, int hiddenLayers,
                     ArrayList<Integer> neuronsPerLayer){
        //set variables
        numInputs = inputs;
        numOutputs = outputs;
        numHiddenLayers = hiddenLayers;
        neuronsPerHiddenLayer = neuronsPerLayer;

        createNet();

    } //end constructor

    //constructor with string
    public NeuralNet(String init) {
        initFromString(init);
    }

    //constructor with genome
    public NeuralNet(Genome genome) {
        initFromGenome(genome);    
    }

    //helper function for constructor
    private void createNet(){

        if (neuronsPerHiddenLayer.size() == numHiddenLayers){
            layers = new ArrayList<NeuralLayer>();

            for(int n = 0; n < numHiddenLayers; n++){

                if(n == 0){
                    layers.add(
                        new NeuralLayer(neuronsPerHiddenLayer.get(0), 
                            numInputs));
                } else {
                    layers.add(
                        new NeuralLayer(
                            neuronsPerHiddenLayer.get(n), 
                            neuronsPerHiddenLayer.get(n-1)));

                }//end inner if else

            }//end for loop

            layers.add(new NeuralLayer(
                numOutputs, 
                neuronsPerHiddenLayer.get(numHiddenLayers - 1)));
        } else {
            layers = new ArrayList<NeuralLayer>();
        }


    }//end helper function

    //getters
    public ArrayList<Double> getWeights(int layerNum, int neuronNum){
        if (layers.size() >= layerNum){
            return new ArrayList<Double>();
        } else {
            NeuralLayer temp = layers.get(layerNum);

            if (temp.getNumNeurons() >= neuronNum){
                return new ArrayList<Double>();

            } else {
                return temp.getNeurons().get(neuronNum).getWeights();

            }//end inner if-else

        }//end if-else

    } //end getWeight
    public int getNumOutputs(){
        return numOutputs;
    }
    public int getNumInputs(){
        return numInputs;
    }
    public int getNumHiddenLayers(){
        return numHiddenLayers;
    }
    public int getNeuronsPerHiddenLayer(int layerNum){
        return neuronsPerHiddenLayer.get(layerNum);
    }
    public ArrayList<Integer> getNeuronsPerHiddenLayer(){
        return neuronsPerHiddenLayer;
    }
    public int getNumberOfWeights(){
        int counter = 0;

        for(NeuralLayer temp: layers){
            for(Neuron temp2: temp.getNeurons()){
                counter += temp2.getNumInputs();
            }
        }

        return counter;
    }

    //setter
    public void putWeights(ArrayList<Double> newWeights, 
                           int layerNum, int neuronNum){
        if (layers.size() < layerNum){
            NeuralLayer temp = layers.get(layerNum);

            if (temp.getNumNeurons() < neuronNum){
                Neuron temp2 = temp.getNeurons().get(neuronNum);

                if (temp2.getNumInputs() == newWeights.size()){
                    temp2.setWeights(newWeights);
                }

            }//end inner if-else

        }//end if-else

    }

    public ArrayList<Double> calculateOutputs(ArrayList<Double> inputs){
        ArrayList<Double> outputs = inputs;

        //checks to see if there are the correct number of inputs
        if (inputs.size() != numInputs){
            System.err.println(
                "Incorrect number of inputs to calculateOutput!");
            return new ArrayList<Double>();
        }

        for (NeuralLayer layer: layers){
            ArrayList<Double> tempValues = new ArrayList<Double>();
            for (Neuron neuron: layer.getNeurons()){
                tempValues.add(formula(outputs, neuron.getWeights()));

            }

            outputs = tempValues;

        }

        return outputs;

    }

    public double formula(
        ArrayList<Double> inputs, ArrayList<Double> weights)
    {
        double returnDouble = 0;

        if (inputs.size() + 1 == weights.size()){
            int numElements = inputs.size();
            for (int n = 0; n < numElements; n++){
                returnDouble += (weights.get(n) * inputs.get(n));

            }

            returnDouble += (-1 * weights.get(numElements));

            return sigmoid(returnDouble, 1.0);
        } else {
            return 0;
        }

    }

    public double sigmoid(double activation, double response){
        return (1 / (1 + (Math.pow(Math.E, (- activation/ response)))));

    }

    public String toString(){
        String returnString = "";

        for(NeuralLayer layer: layers){
            for(Neuron neuron: layer.getNeurons()){
             
            	
            	
            }
           
        }

        return returnString;
    }

    /**
     * =============================
     *
     *      Writing methods
     *
     * =============================
     *
     */
    
    String weightSep = ",";
    String neuronSep = ";";
    String neuralLayerSep = "'";

    private String cutLastChar(String str) {
        return str.substring(0, str.length() -1);
    }
    public String toStringNoLine(){
        String returnString = "";

        for(NeuralLayer layer: layers){
            for(Neuron neuron: layer.getNeurons()){
                for(double weight: neuron.getWeights()){
                    returnString += weight + weightSep;
                }
                returnString = cutLastChar(returnString);
                returnString += neuronSep;
            }
            returnString = cutLastChar(returnString);
            returnString += neuralLayerSep;
        }
        returnString = cutLastChar(returnString);

        return returnString;
    }

    public void saveAsFile(String name) {
        IO.writeFile(name, toStringNoLine());
    }

    //returns a neural net based on a file name
    public static NeuralNet initFromFile(String name) {
        String data = IO.readFile(name);
        return new NeuralNet(data);
    }

    //initialzes the neural network for a string.
    //helper function for the second constructor
    private void initFromString(String str) {
        layers = new ArrayList<NeuralLayer>();
        neuronsPerHiddenLayer = new ArrayList<Integer>();
        
        String[] stringLayers = str.split(neuralLayerSep);

        //for each of the neuron layers
        for (int i = 0; i < stringLayers.length; i++) {
            String stringLayer = stringLayers[i];
            NeuralLayer nLayer = initLayerFromString(stringLayer);
            layers.add(nLayer);

            if (i == stringLayers.length - 1) {
                numOutputs = nLayer.getNumNeurons();
            } else {
                neuronsPerHiddenLayer.add(nLayer.getNumNeurons());
            }

            //initialize numInputs
            if (i == 0) {
                numInputs = nLayer.getNumInputs();
            } 
        }
        

        //breakdown of the layers:
        //the last layer is the output layer.
        //all other layers are hidden layers.
        //the number of inputs to the first layer
        //is the number of inputs to the entire network.

        //initialize
        numHiddenLayers = layers.size() - 1; 
    }

    //returns a neuronLayer
    private NeuralLayer initLayerFromString(String str) {
        String[] strNeurons = str.split(neuronSep);
        String[] weightsOfNeuron = strNeurons[0].split(weightSep);

        int numberOfWeights = weightsOfNeuron.length -1;
        int numberOfNeurons = strNeurons.length;

        NeuralLayer nLayer = new NeuralLayer(numberOfNeurons, numberOfWeights);
        ArrayList<Neuron> neurons = nLayer.getNeurons();

        for (int i = 0; i < numberOfNeurons; i++) {
            ArrayList<Double> newWeights = 
                getWeightsFromString(strNeurons[i]);
            neurons.get(i).setWeights(newWeights);
        }

        return nLayer;
    }
    private ArrayList<Double> getWeightsFromString(String str) {
        String[] strWeights = str.split(weightSep);
        int length = strWeights.length - 1;
        
        ArrayList<Double> weights = new ArrayList<Double>();
        for (int i = 0; i < length+1; i++) {
            weights.add(Double.parseDouble(strWeights[i]));
        }
        
        return weights;
    }

    /**
     * =================================================
     *
     *          Genome methods
     *
     * =================================================
     *
     */

    public void initFromGenome(Genome g) {
        ArrayList<ArrayList<ArrayList<Double>>> megaList = g.getWeights();

        layers = new ArrayList<NeuralLayer>();
        neuronsPerHiddenLayer = new ArrayList<Integer>();

        for (int i = 0; i < megaList.size(); i++) {
            NeuralLayer nLayer = initLayerFromList(megaList.get(i));
            layers.add(nLayer);

            if (i == megaList.size() -1){
                numOutputs = nLayer.getNumNeurons();
            } else {
                neuronsPerHiddenLayer.add(nLayer.getNumNeurons());
          
            }

            if (i == 0) {
                numInputs = nLayer.getNumInputs();
            }
         
        }
        numHiddenLayers = layers.size() - 1;
    }
    public NeuralLayer initLayerFromList(
        ArrayList<ArrayList<Double>> neuronWeights) 
    { 
        int numberOfNeurons = neuronWeights.size();
        int numberOfWeights = neuronWeights.get(0).size() - 1;

        NeuralLayer nLayer = 
            new NeuralLayer(numberOfNeurons, numberOfWeights);
        ArrayList<Neuron> neurons = nLayer.getNeurons();

        for (int i = 0; i < numberOfNeurons; i++) {
            neurons.get(i).setWeights( neuronWeights.get(i) );     
        }

        return nLayer;
    }

    public Genome toGenome() 
    {
        return new Genome(toGenomeFormat());    
    }
    //very slow, please don't call often.
    public ArrayList<ArrayList<ArrayList<Double>>> 
        toGenomeFormat() 
    {
        //not as scary as it looks!  please don't kill me.
        ArrayList<ArrayList<ArrayList<Double>>> result =
            new ArrayList<ArrayList<ArrayList<Double>>>();

        for(NeuralLayer layer: layers) {
            ArrayList<ArrayList<Double>> nList = new
                ArrayList<ArrayList<Double>>();

            for(Neuron neuron: layer.getNeurons()){
                nList.add(neuron.getWeights());
            }

            result.add(nList);
        }

        return result;
    }
  


} //
