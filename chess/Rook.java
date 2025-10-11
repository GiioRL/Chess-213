package chess;

public class Rook extends Piece {

    public Rook(Player player, int row, int col) {
        super(player, row, col);
        type = Type.rook;
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.horizontal);
    }
}