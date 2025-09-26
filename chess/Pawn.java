package chess;

public class Pawn extends Piece {
    
    public Pawn(Player player, int row, int col) {
        super(player, row, col);
        type = Type.pawn;
        range = 2; // updated after first move
    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
        range = 1;
    }
}