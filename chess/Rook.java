package chess;

public class Rook extends Piece {

    public Rook(Player player, int row, int col) {
        super(player, row, col);
        type = Type.rook;
        range = 7;
        moveTypes.add(MoveType.vertical);
    }

    public boolean canMove(int row, int col) {
        return cannibalCheck(row, col);
    }
}