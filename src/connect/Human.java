package connect;

import java.util.Scanner;

/**
 * Represents a Human player.
 *
 */
public class Human extends AI {
    public int generateMove(int player, Board board) {
        System.out.print("Make a move: ");
        Scanner scanner = new Scanner(System.in);
        int move = scanner.nextInt();
        return move;
    }
}
