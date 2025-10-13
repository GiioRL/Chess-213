package chess;

public class Bishop extends Piece {
    
    public Bishop(Chess.Player player, int row, int col) {
        super(player, row, col);
        type = Type.bishop;
        moveTypes.add(MoveType.diagonal);
    }
}