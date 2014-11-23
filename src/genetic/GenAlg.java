package genetic;

import java.util.ArrayList;
import java.util.Collections;

import neuralnet.NeuralNet;

/**
 * The implementation of a genetic algorithm.
 *
 * Method calculateFitness is abstract because it depends on
 * what exactly the GenAlg is being used for.
 *
 */
public abstract class GenAlg implements AbstractGenAlg {
    //the population of genomes
    private ArrayList<Genome> population;

    //size of that population
    private int popSize;

    //not sure what this is
    private int weightsPerChromosome;
    //the best fitness in the pop
    private double bestFitness;
    //the worst fitness in the pop
    private double worstFitness;
    //the total fitness
    private double totalFitness;
    //the most fit genome
    private Genome fittestGenome;

    //the generation number
    public int generation = 0;

    //online tutorials say 0.05 to 0.3 is fine.
    double mutationRate = 0.3;
    //online tutorials say 0.7 is good
    double crossoverRate = 0.7;

    //the most a weight can be changed
    //the new weight should be +/- maxMutation
    double maxMutation = 0.1;

    int keepNBest;

    public GenAlg(int popSize, int keepNBest, double mutationRate, 
        double crossoverRate,double maxMutation) 
    {
        this.popSize = popSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;    
        this.maxMutation = maxMutation;
        this.population = new ArrayList<Genome>();
        this.keepNBest = keepNBest;
    }

    // returns 2 baby genomes
    // crossover crosses over individual neurons with probability
    // crossoverRate
    // the idea is that individual neurons are responsible for some of
    // the "thought" process.
    public Genome[] crossover(Genome dad, Genome mom)
    {
        Genome[] result = new Genome[2];
        result[0] = dad.clone();
        result[1] = mom.clone();

        ArrayList<ArrayList<ArrayList<Double>>> daddy = 
            result[0].getWeights();

        ArrayList<ArrayList<ArrayList<Double>>> mommy = 
            result[1].getWeights();

        for (int layer = 0; layer < daddy.size(); layer++) {
            for (int neuron = 0; neuron < daddy.get(layer).size(); neuron++) {
                if (Math.random() < crossoverRate) {
                    crossover(daddy.get(layer).get(neuron),
                        mommy.get(layer).get(neuron));
                }         
            }    
        }
        return result;
    }

    //crosses over two lists of doubles destructively
    public void crossover(
        ArrayList<Double> dad, ArrayList<Double> mom)
    {
        //determine a crossover point
        int length = dad.size();

        // cp runs from 1 to length-1, inclusive, because a
        // crossover must occur
        int cp = (int) (Math.random()*(length - 1) + 1);     

        ArrayList<Double> d2 = new ArrayList<Double>(dad.subList(cp,length));
        dad = new ArrayList<Double>(dad.subList(0,cp));

        ArrayList<Double> m2 = new ArrayList<Double>(mom.subList(cp,length));
        mom = new ArrayList<Double>(mom.subList(0,cp));
        
        //create the offspring
        dad.addAll(m2);
        mom.addAll(d2);
    }

    public double mutateWeight(double weight) {
        return weight +
            (2*Math.random()*maxMutation-maxMutation);
    }
    
    // returns a mutated version of the genome
    // every weight has a mutationRate chance of mutation.
    public Genome mutate(Genome gene)
    {
        Genome result = gene.clone();
        for (ArrayList<ArrayList<Double>> lst1 : result.getWeights()) {
            for (ArrayList<Double> weights : lst1) {
                for (int i = 0; i < weights.size(); i++) {
                    //mutate
                    if (Math.random() < mutationRate) {
                        weights.set(i,mutateWeight(weights.get(i)));  
                    } 
                }
            }
        }
        return result;
    }

    // takes the N best genomes from the population
    // assumes the population already has its fitness calculated
    // and that it is already sorted in order
    public ArrayList<Genome> getNBest(int n) {
        return new ArrayList<Genome>(population.subList(0,n));
    } 


    // advance time forward one generation
    public void nextGeneration() {
        //keep the elite in the population
        ArrayList<Genome> newPop = getNBest(keepNBest); 
        
        // for (Genome g : newPop){
        //    System.out.print(g.getFitness()+ ", ");
        //}
        //System.out.print("\n");

        while (newPop.size() < popSize) {
            // next, take the children of random parents
            // with a bias towards the best parents
            Genome mom = sample();
            Genome dad = sample();     
            
            //have children
            Genome[] children = crossover(mom,dad);
            Genome b1 = mutate(children[0]);
            Genome b2 = mutate(children[1]);

            //add them in
            newPop.add(b1);
            if (popSize != newPop.size()) {
                newPop.add(b2);
            }
        }

        population = newPop;

        // calculates the fitness of the individuals in the population
        calculateFitness();
        updateFitness();
    }

    // returns a genome based on a sampling algorithm
    public Genome sample() { return rouletteWheelSample(); }

    // roulette wheel sample
    public Genome rouletteWheelSample() {
        double roulette = (Math.random() * totalFitness);
        double total = 0;
        for (Genome g : population) {
            total += g.getFitness();

            if (total > roulette)
                return g; 
        }

        // should never be called
        System.err.println ("roulette wheel sampling returned nothing");
        return null;
    }

    // calculate the fitness of the individuals in the population
    // this is written here because the fitness may be
    // calculated as how the programs do relative to each other.
    public abstract void calculateFitness();
        
    // getters
    public double bestFitness() {
        return bestFitness; 
    }
    public Genome bestGenome() {
        return fittestGenome;    
    }
    public double averageFitness() {
        return totalFitness/popSize;
    } 

    //updates all the variables involved with fitness
    // best, worst, total, fittestGenome
    public void updateFitness() {
        //reset the variables
        totalFitness = 0;
        bestFitness = Double.NEGATIVE_INFINITY;
        worstFitness = Double.POSITIVE_INFINITY;
        fittestGenome = null;

        //now, find the best
        for (Genome g : population) {
            double fitness = g.getFitness();

            totalFitness += fitness;

            if (fitness > bestFitness) {
                bestFitness = fitness;   
                fittestGenome = g;
            } 
            else if (fitness < worstFitness) {
                worstFitness = fitness;     
            } 
        } 
        //now sort them
        Collections.reverse(population);
        Collections.sort(population);
        /*for (Genome g: population){
            System.out.println(g.toString());
        }*/
    }
    public ArrayList<Genome> getPopulation() {
        return population;     
    }
    
}
