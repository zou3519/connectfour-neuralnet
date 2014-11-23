package connect;

import java.util.List;
import java.util.ArrayList;

/**
 * Represents a connect four board.
 *
 */
public class Board {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    //board[row][col]
    private int[][] board;
    
    //gives the first empty row in each column.
    //very useful.
    protected int[] indices;

    //rows, columns, and diagonals as strings
    protected List<String> rows;
    protected List<String> cols;
    protected List<String> diagonals;

    public int[][][] position_to_down_left_diagonal;
    public int[][][] position_to_down_right_diagonal;

    public Board() {
        //board[row][col]
        board = new int[ROWS][COLS];
        indices = new int[7];

        rows = new ArrayList();
        cols = new ArrayList();
        diagonals = new ArrayList();
        position_to_down_left_diagonal = new int[ROWS][COLS][2];
        position_to_down_right_diagonal = new int[ROWS][COLS][2];
        refreshRCD();
    }

    public Board(Board other) {
        board = new int[ROWS][COLS];
        indices = new int[7];

        rows = new ArrayList();
        cols = new ArrayList();
        diagonals = new ArrayList();

        for (int row = 0; row < ROWS; row++) {
            System.arraycopy(other.board[row], 0, board[row], 0, COLS);
        }
        System.arraycopy(other.indices, 0, indices, 0, 7);

        position_to_down_left_diagonal = new int[ROWS][COLS][2];
        position_to_down_right_diagonal = new int[ROWS][COLS][2];

        refreshRCD();
    }

    public Board invert() {
        Board other = new Board();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (board[row][col] == 1) {
                    other.makeMove(2, col);
                }
                else if (board[row][col] == 2) {
                    other.makeMove(1, col);
                }
            }
        }
        return other;
    }

    public int[][] getBoardArray() {
        return board;
    }
    public int pieceAt(int row, int col) {
        return board[row][col];
    }

    @Override
    public String toString() {
        String result = new String();
        result += "===============\n";
        for(int row = ROWS-1; row >= 0; row--) {
            result += "|";
            for(int col = 0; col < COLS; col++) {
                switch(board[row][col]) {
                    case 1: result += "X"; break;
                    case 2: result += "O"; break;
                    default:result += "-"; break;
                }
                if (col != COLS-1) {
                    result += " ";
                }
            }
            result += "|\n";
        }
        result += "|0 1 2 3 4 5 6|\n===============";
        return result;
    }

    public void makeMove(int player_piece, int move_position) {
        //get the empty row value in indices
        int row = indices[move_position];
        if (row != ROWS) {
            board[row][move_position] = player_piece;
            indices[move_position]++;

            //now, correct the row/col/diagonal storage
            refreshRCD2(player_piece, row, move_position);
            return;
        }
        //if the program has not returned, we have an issue
        throw new RuntimeException("Illegal makeMove(" + player_piece 
                + "," + move_position + ")");
    }
    private void refreshRCD2(int piece, int row, int col) {
        char newPiece = (char)(piece+48);

        String r = rows.get(row);
        char[] rt = r.toCharArray();
        rt[col] = newPiece;
        rows.set(row, new String(rt));

        String c = cols.get(col);
        char[] ct = c.toCharArray();
        ct[row] = newPiece;
        cols.set(col, new String(ct));

        int[] d1 = position_to_down_left_diagonal[row][col];
        int[] d2 = position_to_down_right_diagonal[row][col];

        if (d1[0]==0&&d1[1]==0 && !(row == 3 && col == 0)) {

        } else {
            String dd1 = diagonals.get(d1[0]);
            char[] dd1t = dd1.toCharArray();
            dd1t[d1[1]] = newPiece;
            diagonals.set(d1[0], new String(dd1t));
        }

        if (d2[0] == 0 && d2[1] == 0) {

        } else {
            String dd2 = diagonals.get(d2[0]);
            char[] dd2t = dd2.toCharArray();
            dd2t[d2[1]] = newPiece;
            diagonals.set(d2[0], new String(dd2t));
        }
    }
    public void undoMove(int player_piece, int move_position) {
        //System.out.println("before undo\n" + this);
        int row = indices[move_position];
        if (row == 0 || row > ROWS) {
            throw new RuntimeException("Illegal undoMove("
                    +player_piece + "," + move_position + ")");
        }
        indices[move_position]--;
        board[row-1][move_position] = 0;
        refreshRCD2(0, row-1, move_position);
        //System.out.println("after undo\n" + this);
    }

    //return: 0 for no win, 1 for player 1 win, 2 for player 2 win
    // 3 for draw
    public int isGameOver() {
        //refreshRCD();
        for (String s : rows) {
            if (s.contains("1111")) {
                return 1;
            }
            else if(s.contains("2222")) {
                return 2;
            }
        }
        for (String s : cols) {
            if (s.contains("1111")) {
                return 1;
            }
            else if(s.contains("2222")) {
                return 2;
            }
        }
        for (String s : diagonals) {
            if (s.contains("1111")) {
                return 1;
            }
            else if(s.contains("2222")) {
                return 2;
            }
        }

        if(getPossibleMoves().size() == 0) {
            return 3;     
        }
        return 0;
    }

    public List<Integer> getPossibleMoves() {
        List<Integer> result = new ArrayList();
        for(int col = 0; col < COLS; col++) {
            //check if any of the columns are not full
            if (indices[col] != ROWS) {
                result.add(col);
            }
        }
        return result;
    }

//    public List<Move> getPossibleMoves(int player) {
//        List<Move> result = new ArrayList();
//        for(int col = 0; col < COLS; col++) {
//            //check if any of the columns are empty
//            boolean isColumnFull = true;
//            searchcol:
//            for(int row = 0; row < ROWS; row++) {
//                if (board[row][col] == 0) {
//                    isColumnFull = false;
//                    break searchcol;
//                }
//            }
//            if (!isColumnFull) {
//                result.add(new Move(player, col));
//            }
//        }
//        return result;
//    }

    /*
     * all of the rows, columns, and diagonals will be
     * encoded in strings.
     * this will then allow for the easy searching of
     * substrings using the string api
     *
     */
    //rows, columns, and diagonals
//    protected List<String> getRCDasStr() {
//        List<String> result = new ArrayList();
//        result.addAll(getRowsAsStr());
//        result.addAll(getColsAsStr());
//        result.addAll(getDiagsAsStr());
//        return result;
//    }
    protected void refreshRCD() {
        rows = getRowsAsStr();
        cols = getColsAsStr();
        diagonals = getDiagsAsStr();
    }
    protected List<String> getRowsAsStr() {
        List<String> result = new ArrayList();
        for(int row = 0; row < ROWS; row++) {
            String s = "";
            for (int col = 0; col < COLS; col++) {
                s += board[row][col];
            }
            result.add(s);
        }
        return result;
    }

    protected List<String> getColsAsStr() {
        List<String> result = new ArrayList();
        for (int col = 0; col < COLS; col++) {
            String s = "";
            for(int row = 0; row < ROWS; row++) {
                s += board[row][col];
            }
            result.add(s);
        }
        return result;
    }
    protected List<String> getDiagsAsStr()  {
        List<String> result = new ArrayList();

        //down-right arrays first. there are 6 of them.
        int row = 3;
        int col = 0;
        for (int i = 0; i < 6; i++) {
            String s = "";
            int row_c = row;
            int col_c = col;
            int count = 0;
            while (row_c >= 0 && col_c < COLS) {
                position_to_down_right_diagonal[row_c][col_c][0]
                        = i;
                position_to_down_right_diagonal[row_c][col_c][1]
                        = count;
                s += board[row_c][col_c];
                row_c--;
                col_c++;
                count++;
            }
            //move row, col up
            if (row != ROWS - 1) {
                row++;
            } else {
                //move row, col to the right
                col++;
            }
            result.add(s);
        }

        //down-left arrays. there are 6 of them.
        row = 3;
        col = COLS-1;
        for (int i = 0; i < 6; i++) {
            String s = "";
            int row_c = row;
            int col_c = col;
            int count = 0;
            while (row_c >= 0 && col_c >= 0) {
                position_to_down_left_diagonal[row_c][col_c][0]
                        = i+6;
                position_to_down_left_diagonal[row_c][col_c][1]
                        = count;
                s += board[row_c][col_c];
                count++;
                row_c--;
                col_c--;
            }
            //move row, col up
            if (row != ROWS - 1) {
                row++;
            } else {
                //move row, col to the leftright
                col--;
            }
            result.add(s);
        }

        return result;
    }

//    //changes the position (row, column) to a corresponding
//    //down-left diagonal position.
//    public static final int[][][] left_diag_lookup =
//        { { {9, 5}, {8, 5}, {7, 4}, {6, 3}, {-1, 0}, {-1,0}, {-1,0} },
//          { {10, 4},{9, 4}, {8, 4}, {7, 3}, {6, 2}, {-1, 0}, {-1, 0}},
//          { {11,3}, {10,3}, {9, 3}, {8, 3}, {7, 2}, {6, 1}, {-1, 0}},
//          { {-1,0}, {11,2}, {10,2}, {9, 2}, {8, 2}, },
//          {},
//          {}};
}
