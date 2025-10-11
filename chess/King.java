package chess;

import java.lang.Math;
import java.util.ArrayList;

public class King extends Piece {

    public King(Player player, int row, int col) {
        super(player, row, col);
        type = Type.king;
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.horizontal);
        moveTypes.add(MoveType.diagonal);
    }

    public boolean canMove(int newRow, int newCol, MoveType movetype) {
        if (cannibalCheck(newRow, newCol)) { // need to check if move is within range
            if (moveTypes.contains(movetype)) {
                // Check if King would be in check by moving to (newRow, newCol)
                Piece dummy = new Queen(player, newRow, newCol);
                ArrayList<Piece> pieces = dummy.sees();
                for (Piece piece : pieces)
                    if (piece.seesSquare(newRow, newCol))
                        return false;
                // Need to also account for knight attacking (newRow, newCol)
                dummy = new Knight(player, newRow, newCol);
                pieces = dummy.sees();
                for (Piece piece : pieces)
                {
                    System.out.println(piece);
                    if (piece.seesSquare(newRow, newCol))
                        return false;
                }
                return (Math.abs(row-newRow) <= 1) && (Math.abs(col-newCol) <= 1);
            }
        }
        return false;
    }

    public ArrayList<Piece> sees() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i != 0) && (j != 0)) {
                    seePiece(row+i, row+j, pieces);
                }
            }
        }
        return pieces;
    }
}