package chess;

public class Pawn extends Piece {
    Type type = Type.pawn;
    int range = 2; // updated after first move

    public Pawn(Player player, int row, int col) {
        super(player, row, col);

    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
        range = 1;
    }
}