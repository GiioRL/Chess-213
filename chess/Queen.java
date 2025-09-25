package chess;

public class Queen extends Piece {
    Type type = Type.queen;
    int range = 7;

    public Queen(Player player, int row, int col) {
        super(player, row, col);

    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}