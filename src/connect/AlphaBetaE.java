package connect;

import java.util.ArrayList;
import java.util.List;
/**
 * Represents an AI that uses an alpha-beta pruning algorithm with
 * min-max.
 * 
 * The difficulty for this AI can be set.
 */
public class AlphaBetaE extends AI {

    private int player_me;

    private int counter;

    private final int[] SEARCH_DEPTH_3 =   {6,7,8,9,12,19,7};
    private final int[] SEARCH_DEPTH_2 = {5,6,7,9,11,18,7};
    private final int[] SEARCH_DEPTH_1 = {4,5,6,8,10,17,7};

    private int[] searchDepth;

    public AlphaBetaE() {
        searchDepth = SEARCH_DEPTH_3;     
    }

    //initialize an alpha beta algorithm with a different
    //difficulty.  1 is the easiest, 3 is the hardest.
    public AlphaBetaE(int searchNum) {
        switch(searchNum) {
            case 3: searchDepth = SEARCH_DEPTH_3;break;     
            case 2: searchDepth = SEARCH_DEPTH_2;break;     
            case 1: searchDepth = SEARCH_DEPTH_1;break;     
            default: System.err.println("incorrect parameter" +
                " passed to AlphaBetaE constructor.");
        } 
    }


    @Override
    public int generateMove(int player, Board board) {
        player_me = player;
        return alphabetaHelper(player, board); 

        /*
        int result = alphabetaHelper(player, board);
        
        if(result == -1) { //there's something wrong here...
            List<Integer> moves = board.getPossibleMoves();
            if (moves.size() != 0) {
                System.err.println("I think the machine resigned...");
                result = moves.get((int)Math.random()*(moves.size()));        
            } else {
                System.err.println("Tie not processed!");     
            }
        }

        return result;
        */
    }

    public int alphabetaHelper(int player, Board board) {
        List<Integer> moves = board.getPossibleMoves();
        List<Double> values = new ArrayList();

        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        int bestMove = -1;
        double bestValue = (player_me == player) ? alpha : beta;
        counter = 0;
        int depth = 6;
        //actual depth is depth +1
        switch(moves.size()) {
            case 7: depth = searchDepth[0]; break;
            case 6: depth = searchDepth[1]; break;
            case 5: depth = searchDepth[2]; break;
            case 4: depth = searchDepth[3]; break;
            case 3: depth = searchDepth[4];break;
            case 2: depth = searchDepth[5];break;
            default: depth = searchDepth[6];
        }

//        switch(moves.size()) {
//            case 7: depth = 5; break;
//            case 6: depth = 6; break;
//            case 5: depth = 7; break;
//            case 4: depth = 8; break;
//            case 3: depth = 11;break;
//            case 2: depth = 19;break;
//            default: depth = 7;
//        }
        
        for(int m : moves) {
            board.makeMove(player, m);
            double score = alphabeta(
                        otherPlayer(player), board, alpha, beta, depth);
            board.undoMove(player, m);
            
            //now do the weighting
            score += (double)(weights[board.indices[m]][m])/100;
            
            values.add(score);

            //you want to maximize your score
            if(player_me == player) {
                if (score > bestValue) {
                    bestValue = score;
                    bestMove = m;
                }
            } else { //minimize score
                if (score < bestValue) {
                    bestValue = score;
                    bestMove = m;
                }
            }
        }

        //System.out.println("Evaluated " + counter + " nodes");

        //     for (int i = 0; i < moves.size(); i++) {
        //    System.out.print("(" + moves.get(i) + "," + values.get(i)+") ");
        //}
        //System.out.println("");
        return bestMove;
    }

    //player is the player that is about to go.
    public double alphabeta(
            int player, Board node,double alpha, double beta, int depth)
    {
        //game is over when isGameOver() == 1 or 2
        if (depth == 0 || node.isGameOver() != 0) {
            counter++;
            return evaluate(player, node, depth);
        }

        //player max
        if (player == player_me) {
            double score = alpha;
            for(int move : node.getPossibleMoves()) {
                node.makeMove(player, move);
                score = alphabeta(
                        otherPlayer(player), node, alpha, beta, depth-1);
                node.undoMove(player, move);
                //find the max score
                if (score > alpha)
                    alpha = score; //you have found a better move
                if (alpha >= beta)
                    return alpha; //cutoff
            }
            return alpha;
        }
        //if  (player != player_me)
        else {
            double score = beta;
            for(int move : node.getPossibleMoves()) {
                node.makeMove(player, move);
                score = alphabeta(
                        otherPlayer(player), node, alpha, beta, depth-1);
                node.undoMove(player, move);
                //find the min score
                if (score < beta)
                    beta = score; //opponent has found a better move
                if (alpha >= beta)
                    return beta; //cutoff
            }
            return beta;
        }
    }

    public static final int[][] weights =
        { {0, 1, 2, 3, 2, 1, 0}, {1, 2, 3, 4, 3, 2, 1}, {2, 3, 4, 5, 4, 3, 2},
        {3, 4, 5, 6, 5, 4, 3}, {2, 3, 4, 5, 4, 3, 2}, {1, 2, 3, 4, 3, 2, 1}};

    public double evaluate(int player, Board board, int depth) {
        
//        //invert the board if necessary
//        Board board;
//        if (player_me == 1) {
//            board = b;
//            //board.refreshRCD();
//        } else {
//            board =  b.invert();
//        }

        //board.refreshRCD();
        List<String> mega = new ArrayList();
        mega.addAll(board.rows);
        mega.addAll(board.cols);
        mega.addAll(board.diagonals);

        double result = 0;

        if (player_me == 1) {
            megasearch:
            for (String s : mega) {
                if (s.contains("1111")) {
                    result = 1000;
                    break megasearch;
                }
                else if(s.contains("2222")) {
                    result = -1000;
                    break megasearch;
                }
                if (s.contains("01110")) {
                    result += 150;
                }
                else if(s.contains("0111") || s.contains("1110")) {
                    result += 100;
                }
                else if(s.contains("1011") || s.contains ("1101")) {
                    result += 90;
                }
                else if (s.contains("1100") || s.contains("0011")) {
                    result += 10;
                }
                else if (s.contains("0110")) {
                    result += 10;
                }
                if (s.contains("2111") || s.contains("1112")) {
                    result -= 50;
                }
                else if (s.contains("1211") || s.contains("1121")) {
                    result -= 45;
                }
                if(s.contains("02220")) {
                    result -= 152;
                }
                else if(s.contains("2220") || s.contains("2220")) {
                    result -= 102;
                }
                else if(s.contains("2022") || s.contains ("2202")) {
                    result -= 92;
                }
                else if (s.contains("2200") || s.contains("0022")) {
                    result -= 10;
                }
                else if (s.contains("0220")) {
                    result -= 10;
                }
            }
        } else {
            megasearch:
            for (String s : mega) {
                if (s.contains("2222")) {
                    result = 1000;
                    break megasearch;
                }
                else if(s.contains("1111")) {
                    result = -1000;
                    break megasearch;
                }
                if (s.contains("02220")) {
                    result += 150;
                }
                else if(s.contains("0222") || s.contains("2220")) {
                    result += 100;
                }
                else if(s.contains("2022") || s.contains ("2202")) {
                    result += 90;
                }
                else if (s.contains("220") || s.contains("022")) {
                    result += 10;
                }
                if (s.contains("1222") || s.contains("2111")) {
                    result -= 50;
                }
                else if (s.contains("2122") || s.contains("2212")) {
                    result -= 45;
                }
                if (s.contains("01110")) {
                    result -= 152;
                }
                else if(s.contains("1110") || s.contains("1110")) {
                    result -= 102;
                }
                else if(s.contains("1011") || s.contains ("1101")) {
                    result -= 92;
                }
                else if (s.contains("110") || s.contains("011")) {
                    result -= 10;
                }
            }

        }
        if (result > 0) {
            result += (double)depth/10;
        } else {
            result -= (double) depth/10;
        }
        //result += Math.random()/1000;
        return result;
    }
    public int otherPlayer(int player) {
        return (player == 1) ? 2 : 1;
    }
}
