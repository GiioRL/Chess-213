package chess;

public class King extends Piece {

    public King(Player player, int row, int col) {
        super(player, row, col);
        type = Type.king;
        range = 1;
    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}