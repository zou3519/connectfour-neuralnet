package connect;

/**
 * The class representing a connect four game.
 *
 * Plays two AI's against each other.
 *
 */
public class Game {
    private AI[] players;

    private Board board;

    //debug
    public static boolean showBoard = false;

    //turn will oscillate between 1 and 2.
    private int turn = 1;
    private int true_turn = 1;

    // 0 = game is not over
    // 1 = player 1 has won
    // 2 = player 2 has won
    // 3 = there is a tie.
    private int isGameOver;

    public Game(AI one, AI two) {
        players = new AI[2];
        players[0] = one;
        players[1] = two;
        isGameOver = 0;

        board = new Board();

        if (showBoard)
            System.out.println(board);

        while(isGameOver==0) {
            if (showBoard) {
                System.out.println("\n*****Turn: " +true_turn + "*****");
                System.out.println(board.getPossibleMoves());
                System.out.println("");
            }

            int move = players[turn-1].generateMove(turn, board);

            if (showBoard) {
                System.out.println("Move: " + move);
            }

            board.makeMove(turn, move);

            if (showBoard) {
                System.out.println(board);
            }

            //System.out.println("Rows: " + board.getRowsAsStr());
            //System.out.println("Rows: " + board.rows);
            //System.out.println("Cols: "+ board.getColsAsStr());
            //System.out.println("Cols: "+ board.cols);
            //System.out.println("Diags: "+board.getDiagsAsStr());
            //System.out.println("Diags: "+board.diagonals);

            this.isGameOver = board.isGameOver();

            true_turn++;
            turn++;
            if (turn == 3) {
                turn = 1;
            }
        }

        if (showBoard) {
            if (isGameOver == 3) 
                System.out.println("Draw!"); 
            else  
                System.out.println("Player " + isGameOver
                    + (isGameOver == 1 ? " (X)" : " (O)") + " wins!");
        }

    }
    public int getTotalTurns() {
        return true_turn;
    }
    // returns 1 if the first player won, 2 if the second
    // returns 3 if it's a draw.
    public int getWinner() {
        return isGameOver;     
    }
}
