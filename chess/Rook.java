package chess;

public class Rook extends Piece {
    
    boolean hasMoved;

    public Rook(Player player, int row, int col) {
        super(player, row, col);
        type = Type.rook;
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.horizontal);
        hasMoved = false;
    }

    public int move(int newRow, int newCol, ReturnPlay rp)
    {
        if (super.move(newRow, newCol, rp) == 1)
        {
            hasMoved = true;
            return 1;
        }
        return -1;
    }
}