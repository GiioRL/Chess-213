package chess;

public class Bishop extends Piece {
    
    public Bishop(Player player, int row, int col) {
        super(player, row, col);
        type = Type.bishop;
        range = 7;
        moveTypes.add(MoveType.diagonal);
    }
}