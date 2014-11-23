package genetic;

import java.util.ArrayList;

/**
 * The interface for our genetic algorithm.
 *
 */
public interface AbstractGenAlg { 
    // advances to the next generation
    public void nextGeneration();  

    // get chromosomes
    public ArrayList<Genome> getPopulation();

    // get the average fitness
    public double averageFitness();

    // get the best fitness
    public double bestFitness();

    // crosses over the parents to create two offspring
    public Genome[] crossover(Genome dad, Genome mom);

    // mutates one parent to create an offspring
    public Genome mutate(Genome gene);

    // calculates the fitness of the population
    public void calculateFitness();
    
    //get the best genome
    public Genome bestGenome();
}
