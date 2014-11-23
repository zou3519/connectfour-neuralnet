
package connect;

import java.util.ArrayList;
import java.util.List;
import neuralnet.NeuralNet;
import genetic.Genome;

/**
 * Represents an AI that performs a move by calculating 
 * outputs from a NeuralNet.
 */
public class NeuralNetAI extends AI {

    //my neural network 
    private NeuralNet network;

    //the constructor
    public NeuralNetAI(Genome g) {
        setNetwork(g);     
    }
    //construct with the string representation of a neuralnet
    public NeuralNetAI(String network) {
        this.network = new NeuralNet(network); 
    }
    
    @Override
    public int generateMove(int player, Board board) {
        // use board.pieceAt(row, col) to get piece. 
        // pieces are 0 (blank), 1 (X), 2 (O)
        // the inputs go across the board and down
        // and they're grouped in threes by (0, 1, 2) 

        ArrayList<Double> inputs = new ArrayList<Double>();
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                int x = board.pieceAt(row, col);     
                if (x == 0) { 
                    inputs.add(1.);
                    inputs.add(0.);
                    inputs.add(0.);
                } else if (x == 1) {
                    inputs.add(0.);
                    inputs.add(1.);
                    inputs.add(0.);
                } else { //x == 2
                    inputs.add(0.);
                    inputs.add(0.);
                    inputs.add(1.); 
                }

            }     
        }

        //there should only be 7 outputs
        ArrayList<Double> outputs = network.calculateOutputs(inputs);

        List<Integer> possibleMoves = board.getPossibleMoves();

        int result = -1;
        double maxOutput = Double.NEGATIVE_INFINITY;

        //pick the largest output in the possible moves
        for (int i : possibleMoves) {
            double out = outputs.get(i);
            if(out > maxOutput) {
                result = i;
                maxOutput = out;    
            } else if (out == maxOutput){
                //pick the one that's closer to the center
                if (Math.abs(result - 3) > Math.abs(i-3)){
                    result = i; 
                    maxOutput = out;
                }
            }
        }

        //System.out.println("outputs: " + outputs);
        //System.out.println("max output: " + maxOutput);

        return result;
    }    

    public void setNetwork(Genome g) {
        network = new NeuralNet(g);    
    }
}
