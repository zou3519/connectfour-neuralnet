package connect;

//import files from the project
import neuralnet.*;
import genetic.*;
import io.*;

//import java libraries
import java.util.ArrayList;
import java.io.File;
import java.util.Scanner;

/**
 * The main class of this project.
 *
 */
public class Main {

    //the main function
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.println(
            "Welcome to Aaron and Richard's CS51 Final Project");
        System.out.println("What would you like to do?");
        System.out.println("a. Run a genetic algorithm");
        System.out.println("b. Play a game against our minmax AI");
        System.out.println("c. Play a game against a saved neural network AI");
        System.out.println("d. Run a game with a minmax AI and a " +
            "saved neural network AI");
        System.out.println("z. Exit");

        boolean loop = false;
        do {
            System.out.print("Your response: ");
            String choice = input.next();


            loop = false;
            if (choice.equals("a")) {
                setupGenAlg(input);         
            } 
            else if (choice.equals("b")) {
                setupHumanVAlphaBeta(input); 
            }
            else if (choice.equals("c")) {
                setupHumanVNeuralNet(input); 
            }
            else if (choice.equals("d")) {
                setupAlphaBetaVNeuralNet(input);
            }    
            else if (choice.equals("z")) {
                System.out.println("Goodbye!"); 
            }
            else {
                System.out.println(
                    "Pick a letter. This is case sensitive.");
                loop = true;
            }
        } while (loop);

        System.out.println("");
    }

    /*
     * =======================================
     *
     *      Setup and request input methods
     *
     * =======================================
     */

    //gets a the difficulty, a number between 1 and 3;
    public static int requestDifficulty(Scanner input) {
        while (true) {
            System.out.println("What difficulty? 1 is the easiest, "
                + "3 is the hardest");    
            System.out.print("Difficulty: ");
            String diff = input.next();
            
            if (diff.equals("1") || diff.equals("2") || diff.equals("3"))
            {
                int difficulty = Integer.parseInt(diff);
                System.out.println("");
                return difficulty;
            }
        }  
    }
    //returns true if human is going first, false if not
    public static boolean requestFirstPlayer(Scanner input) {
        while(true) {
            System.out.println("Do you want to go first? (yes/no)");
            System.out.print("yes or no (case sensitive): ");
            String in = input.next();
            if (in.equals("yes")) {
                System.out.println("");
                return true;    
            }
            else if (in.equals("no")) {
                System.out.println("");
                return false; 
            }
        }
    }
    public static String requestNeuralNetData(Scanner input) {
        while (true) {
            System.out.println("Please tell us the file of the "
                + "saved neural net data");
            System.out.print("file: ");     
            String in = input.next();
            
            File f = new File(in);
            //check if the file exists
            if (!f.exists() || f.isDirectory()) {
                System.out.println("I'm sorry, the file you picked "
                + "does not exist.");
            } else {
                return IO.readFile(in);     
            }
        }      
    }

    //setup the genetic algorithm
    public static void setupGenAlg(Scanner input) {
        String dir = "";
        while (dir.length() < 4){
            System.out.println("Please tell us the name of the directory " +
                "you want to save the results."); 
            System.out.println("It should have at least 4 characters.");
            System.out.print("Directory: ");
            dir = input.next();
        }

        System.out.println("\nStarting the genetic algorithm");

        runGenAlg(dir);
    }

    //setup a game
    public static void setupHumanVAlphaBeta(Scanner input) {
        int difficulty = requestDifficulty(input);
        boolean goesFirst = requestFirstPlayer(input);

        System.out.println("\nStarting the game");

        Game.showBoard = true;
        if (goesFirst) {
            new Game(new Human(), new AlphaBetaE(difficulty));    
        } else {
            new Game(new AlphaBetaE(difficulty), new Human());    
        }
    } 

    
    //sets up another game
    public static void setupHumanVNeuralNet(Scanner input) {
        String nnInput = requestNeuralNetData(input); 
        boolean goesFirst = requestFirstPlayer(input); 
        System.out.println("\nStarting the game");

        Game.showBoard = true;
        if (goesFirst) {
            new Game(new Human(), new NeuralNetAI(nnInput));    
        } else {
            new Game(new NeuralNetAI(nnInput), new Human());    
        }
    }

    //sets up another game
    public static void setupAlphaBetaVNeuralNet(Scanner input) {
        String nnInput = requestNeuralNetData(input);
        int difficulty = requestDifficulty(input);
        boolean nnGoesFirst = false;

        boolean loop = true;
        while(loop) {
            System.out.println(
                "Do you want the NeuralNet AI to go first? (yes/no)");
            System.out.print("yes or no (case sensitive): ");
            String in = input.next();
            if (in.equals("yes")) {
                System.out.println("");
                nnGoesFirst = true;    
                loop = false;
            }
            else if (in.equals("no")) {
                System.out.println("");
                nnGoesFirst = false;
                loop = false;
            }
        }

        System.out.println("\nStarting the game");

        Game.showBoard = true;
        if (nnGoesFirst) {
            new Game(new NeuralNetAI(nnInput),new AlphaBetaE(difficulty));    
        } else {
            new Game(new AlphaBetaE(difficulty), new NeuralNetAI(nnInput));    
        }
    }

    //run the genetic algorithm
    public static void runGenAlg(String dir) {
        //create the writer (write to file) thread
        WriterRunnable writer = new WriterRunnable();
        (new Thread(writer)).start();

        //add a shutdown hook that will terminate writer
        Runtime.getRuntime().addShutdownHook(new TerminatorThread(writer));

        //create the directory
        File directory = new File(dir);
        directory.mkdir();

        //initialize the genalg
        ArrayList<Integer> sizes = new ArrayList<Integer>();
        sizes.add(126);
        sizes.add(126); 
        NeuralNet temp = new NeuralNet(126,7,2,sizes);

        //surpress the output of the board
        Game.showBoard = false;
        
        
        // LOOK HERE. The boolean in this constructor determines fitness method.
        ConnectGenAlg alg = new ConnectGenAlg(20, 10,.2,.7,.2,temp, false);
        
        alg.calculateFitness();
        alg.updateFitness();
        System.out.println("Generation 1");
        System.out.println("Best: " + alg.bestFitness());
        System.out.println("Average: " + alg.averageFitness());
       
        /*Genome g = alg.bestGenome();
        
        Game game1 = new Game(new NeuralNetAI(g), new AlphaBetaE(1));
        Game game2 = new Game(new AlphaBetaE(1), new NeuralNetAI(g));
        
        int score1 = game1.getTotalTurns();
        
        System.out.println("Best: " + score1);
         */

        for (int i = 0; i <99999; i++){

            alg.nextGeneration();
            System.out.println("Generation " + (i+2));
            System.out.println("Best: " + alg.bestFitness());
            System.out.println("Average: " + alg.averageFitness());
            
            /*g = alg.bestGenome();
            
            game1 = new Game(new NeuralNetAI(g), new AlphaBetaE(1));
            game2 = new Game(new AlphaBetaE(1), new NeuralNetAI(g));
            
            score1 = game1.getTotalTurns();
            
            System.out.println("Best: " + score1);
             */
            //now, write the best genome to file
            System.out.println("Writing best genome to file");
            String filename = dir + "/best_genome_gen" + (i+2);

            writer.addRequest(filename, alg.bestGenome());
        }
    }


    /* ===============  END SETUP ===============  */
    
    public static void test2(){
        
        //initialize the genalg
        ArrayList<Integer> sizes = new ArrayList<Integer>();
        sizes.add(50); sizes.add(50); 
        NeuralNet temp = new NeuralNet(126,7,2,sizes);
        Genome g = new Genome(temp.toGenomeFormat());
        
        //surpress the output of the board
        Game.showBoard = false;
        
        
        // LOOK HERE. The boolean in this constructor determines fitness method.
        ConnectGenAlg alg = new ConnectGenAlg(20, 10, .1, .5,.2,temp, false);
        
        for (int i = 0; i < 20; i++){
            System.out.println("" + alg.calculateFitness(g));
        }
        
    }
    
    
    
    public static void test(){
    
        System.out.println("hello, world!");

        int numInputs = 126;
		
        ArrayList<Integer> sizes;
		
        sizes = new ArrayList<Integer>();
		
        sizes.add(3);
	    sizes.add(1);
	
        NeuralNet myNet = new NeuralNet(numInputs, 2, 2, sizes);
		
		
        ArrayList<Double> inputs = new ArrayList<Double>();
		
        for (int n = 0; n < numInputs; n++){
            inputs.add(1. / (n + 1));
        }
		
		
        ArrayList<Double> outputs = myNet.calculateOutputs(inputs);
	
		
		
        for (Double value: outputs){
            System.out.println("output 1: " + value);
        }

        myNet.saveAsFile("network.txt");
        

        NeuralNet netTwo = NeuralNet.initFromFile("network.txt");
        outputs = netTwo.calculateOutputs(inputs);
		
        for (Double value: outputs){
            System.out.println("output 2: " + value);
        }


        Genome network = netTwo.toGenome();

        NeuralNet netThree = new NeuralNet(network);

        outputs = netThree.calculateOutputs(inputs);

        System.out.println(""+ outputs.size());
		
        System.out.println("inputs: "+netThree.getNumInputs());
        System.out.println("outputs: "+netThree.getNumOutputs());
        System.out.println("hidden layers: "+netThree.getNumHiddenLayers());
        
        ArrayList<Integer> lengths = netThree.getNeuronsPerHiddenLayer();

        for (Integer length: lengths){
            System.out.println("length: " + length);
        }

        for (Double value: outputs){
            System.out.println("hey");
            System.out.println("output 3: " + value);
        }

    }

}
