package chess;

public class Knight extends Piece {
    Type type = Type.knight;
    int range = 3; //special case

    public Knight(Player player, int row, int col) {
        super(player, row, col);

    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}