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