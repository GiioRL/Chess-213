package chess;

public class Bishop extends Piece {
    Type type = Type.bishop;
    int range = 7;

    public Bishop(Player player, int row, int col) {
        super(player, row, col);

    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}