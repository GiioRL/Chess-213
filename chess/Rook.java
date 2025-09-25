package chess;

public class Rook extends Piece {
    Type type = Type.rook;
    int range = 7;

    public Rook(Player player, int row, int col) {
        super(player, row, col);

    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}