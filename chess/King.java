package chess;

import java.lang.Math;

public class King extends Piece {

    public King(Player player, int row, int col) {
        super(player, row, col);
        type = Type.king;
        range = 1;
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.horizontal);
        moveTypes.add(MoveType.diagonal);
    }

    public boolean canMove(int newRow, int newCol) {
        if (cannibalCheck(newRow, newCol)) { // need to check if move is within range
            return (Math.abs(row-newRow) <= range) && (Math.abs(col-newCol) <= range);
        }
        return false;
        // return (Math.abs(row-newRow) <= range) && (Math.abs(col-newCol) <= range);
    }
}