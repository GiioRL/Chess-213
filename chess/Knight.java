package chess;

public class Knight extends Piece {
    
    public Knight(Player player, int row, int col) {
        super(player, row, col);
        type = Type.knight;
        moveTypes.add(MoveType.knight);
    }
}