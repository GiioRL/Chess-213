package chess;

public class Knight extends Piece {
    
    public Knight(Player player, int row, int col) {
        super(player, row, col);
        type = Type.knight;
        range = 3; //special case
    }

    public boolean canMove(int row, int col) {
        return false; //implement
    }

    public void move(int row, int col) {
        // implement
    }
}