package chess;

public class King extends Piece {
    Type type = Type.king;
    int range = 1;

    public King(Player player, int row, int col) {
        super(player, row, col);

    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}