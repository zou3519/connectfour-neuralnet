package genetic;

import java.util.ArrayList;
import neuralnet.*;

/**
 * Genome is a collection of weights that represent some neural network.
 *
 */
public class Genome implements Comparable {
    //vector weights
    //     the layer/the neuron/the neuron's weights
    public ArrayList<ArrayList<ArrayList<Double>>> vecWeights;
    public double fitness;
    public boolean calculatedFitness = false;

    public double compare_error = 0.0001;

    //use this to keep track of which genome this is
    public static int GENOME_ID = 0;
    public int genomeID;

    public Genome(ArrayList<ArrayList<ArrayList<Double>>> weights) {
        //create the id
        genomeID = GENOME_ID;
        GENOME_ID++;

        //create the weights
        vecWeights = weights;    
    }

    //create a copy of this genome.
    public Genome clone() {
        ArrayList<ArrayList<ArrayList<Double>>> clone = new
        ArrayList<ArrayList<ArrayList<Double>>>();
        ArrayList<ArrayList<ArrayList<Double>>> weights = getWeights();
        
        int lenA = weights.size();
        for (int a = 0; a < lenA; a++) {
            ArrayList<ArrayList<Double>> cloneA = new ArrayList<ArrayList<Double>>();

            int lenB = weights.get(a).size();
            for (int b = 0; b < lenB; b++) {
                ArrayList<Double> cloneB = new ArrayList<Double>();

                int lenC = weights.get(a).get(b).size();
                for (int c = 0; c < lenC ; c++){
                    cloneB.add(weights.get(a).get(b).get(c));    
                }   
                cloneA.add(cloneB);
            }     
            clone.add(cloneA);
        }

        return new Genome(clone); 
    }

    //getter
    public boolean getCalculatedFitness(){
        return calculatedFitness;
    }

    // lower fitness is better 
    public int compareTo(Object oth) {
        Genome other = (Genome) oth;
        return (int) (-1.*(fitness - other.fitness)/compare_error);
    }

    public ArrayList<Double> getWeights(int layer, int neuron) {
        return vecWeights.get(layer).get(neuron);
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getWeights() {
        return vecWeights;
    }

    public void setFitness(double fitness) {
      
        this.fitness = fitness;
        calculatedFitness = true;

    }
    
    public double getFitness() {
        /*if (calculatedFitness == false)
            System.err.println("No fitness!");*/
        return fitness;     
    }

    @Override
    public String toString() {
        String fitness = "" + this.fitness;
        if (calculatedFitness == false) 
            fitness = "uncal";

        return "[[Genome: " + genomeID + " ; Fitness: " + fitness + "]]";   
    }
}
