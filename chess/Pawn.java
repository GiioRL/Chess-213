package chess;

public class Pawn extends Piece {
    
    public Pawn(Player player, int row, int col) {
        super(player, row, col);
        type = Type.pawn;
        range = 2; // updated after first move
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.diagonal);
    }

    public boolean canMove(int row, int col) {
        return true;
    }
}