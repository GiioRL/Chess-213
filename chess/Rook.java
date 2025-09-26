package chess;

public class Rook extends Piece {

    public Rook(Player player, int row, int col) {
        super(player, row, col);
        type = Type.rook;
        range = 7;
    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}