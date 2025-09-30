package chess;

public class Queen extends Piece {
    
    public Queen(Player player, int row, int col) {
        super(player, row, col);
        type = Type.queen;
        range = 7;
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.horizontal);
        moveTypes.add(MoveType.diagonal);
    }

    public boolean canMove(int row, int col) {
        return true;
    }
}