package genetic;

import java.util.ArrayList;
import connect.*;
import neuralnet.*;

/**
 * A genetic algorithm for connect four.
 *
 */
public class ConnectGenAlg extends GenAlg {

    private boolean roundRobin;
    
    public ConnectGenAlg(int popSize, int keepNBest,
                         double mutationRate, double crossoverRate, 
                         double maxMutation, NeuralNet net,
                         boolean newRoundRobin)
    {
        super(popSize, keepNBest,mutationRate,crossoverRate,maxMutation);
        
        ArrayList<Genome> pop = getPopulation();

        int inputs = net.getNumInputs();
        int outputs = net.getNumOutputs();
        int hiddenLayers = net.getNumHiddenLayers();
        ArrayList<Integer> sizes = net.getNeuronsPerHiddenLayer();
        roundRobin = newRoundRobin;

        while (pop.size() < popSize){
            NeuralNet temp = new NeuralNet(inputs,outputs,hiddenLayers,sizes);
            pop.add(new Genome(temp.toGenomeFormat()));
        }

    }

    //calculate the fitness of the population
    public void calculateFitness() {
        if (roundRobin){
            rrFitness();
        } else {
            ArrayList<Genome> population = getPopulation();
            // int counter = 0;
            for (Genome g : population) {
                if (g.getCalculatedFitness()){
                    // do nothing
                } else {
                    double temp =calculateFitness(g);
                    double temp2 = g.getFitness();
                     
                    g.setFitness(temp);
                    //counter++;
                    //   System.out.println(
                    //        "Finished " + counter + " of them. It was "
                    //                   + g.getFitness());
                    //System.out.println((temp-temp2)+"," + temp + "," + temp2);
                    
                }
            }
            //this just makes stuff look nice
            System.out.println("");
        }


    }
    
    public void rrFitness() {
        ArrayList<Genome> population = getPopulation();
        int length = population.size();
        ArrayList<Double> fitnesses = new ArrayList<Double>();
        // int counter = 0;
        for (int i = 0; i < length; i++) {
            Genome g = population.get(i);
            double temp = calculateFitness(g);
            
            g.setFitness(temp);
            fitnesses.add(temp);
            //counter++;
            //   System.out.println("Finished " + counter + " of them. It was "
            //                   + g.getFitness());
            
            
            
        }
        
        // int counter = 0;
        
            
        for (int i = 0; i < length; i++){
            for (int j = 0; j < length; j++){
                
                if (i != j){
                    // makes them play a game versus all the other ai and then adds
                    // the results to the arraylist
                    Genome g1 = population.get(i);
                    Genome g2 = population.get(j);
                    Game temp = new Game(new NeuralNetAI(g1),
                                        new NeuralNetAI(g2));
                    int score = temp.getTotalTurns();
                    
                    if(temp.getWinner() == 1){
                        fitnesses.set(i, fitnesses.get(i) + .5);
                        
                    } else if (temp.getWinner() == 2){
                        fitnesses.set(j, fitnesses.get(j) + .25);
                    } else {
                        fitnesses.set(i, fitnesses.get(i) + .25);
                        fitnesses.set(j, fitnesses.get(i) + .25);
                    }
                    
                        
                }//end if statement
                    
            }//end inner for loop
                
        }//end outer for loop
        
        for (int i = 0; i < length; i++){
            population.get(i).setFitness(fitnesses.get(i));
            
        }//end recording fitnesses
    }

    /*
     * Let's define the fitness as (the amount of turns) before
     * this AI gets beaten.
     *
     * If the NeuralNetAI wins the game, the fitness becomes 50 
     *
     */ 
    public double calculateFitness(Genome g) {
       
        Game game1 = new Game(new NeuralNetAI(g), new AlphaBetaE(1));
        Game game2 = new Game(new AlphaBetaE(1), new NeuralNetAI(g));
        
        double score1 = game1.getTotalTurns();
            
        if (game1.getWinner() == 1) {
                score1 = 200;
        } else if (game1.getWinner() == 3) {
                score1 = 46;
        }

        double score2 = game2.getTotalTurns();

        if (game2.getWinner() == 2) {
                score2 = 200;
        } else if (game2.getWinner() == 3) {
                score2 = 46;
        }
            
        return ( (score1 + score2)/2);
        
        
    } 
    
    
    
}
