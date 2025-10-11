package chess;

import java.util.ArrayList;

public class Pawn extends Piece {
    int range;
    
    public Pawn(Player player, int row, int col) {
        super(player, row, col);
        type = Type.pawn;
        range = 2; // updated after first move
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.diagonal);
    }

    public boolean canMove(int newRow, int newCol, MoveType movetype) {
        if (cannibalCheck(newRow, newCol)) {
            if (selfCheck(newRow, newCol)) {
                if (movetype == MoveType.vertical) {
                    if (!Board.hasPiece[newRow][newCol]) {
                        if (player == Player.white) {
                            // System.out.println("player is white");
                            if (!Board.hasPiece[row-1][newCol]) {
                                // System.out.println("clear up ahead");
                                return ((row > newRow) && (row - newRow) <= range);
                            }
                        } else {
                            // System.out.println("im black");
                            if (!Board.hasPiece[row+1][newCol]) {
                                return ((newRow > row) && (newRow - row) <= range);
                            }
                        }
                    } else {
                        return false;
                    }
                } else if (movetype == MoveType.diagonal) {
                    if (Board.hasPiece[newRow][newCol]) {
                        if (player == Player.white) {
                            return ((row > newRow) && (row - newRow) <= 1);
                        } else {
                            return ((newRow > row) && (newRow - row) <= 1);
                        }
                    // } else { // can condense all return falses
                        // return false;
                    }
                // } else {
                    // return false;
                }
            }
        // } else {
            // return false;
        }
        return false;
    }

    public boolean seesSquare(int newRow, int newCol) {
        if (player == Player.white) {
            return ((row - newRow == 1) && (Math.abs(col - newCol) == 1));
        } else {
            return ((newRow - row == 1) && (Math.abs(newCol - col) == 1));
        }
    }

    public ArrayList<Piece> sees() {
        int newRow = row; // placeholder
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        if (player == Player.white) {
            newRow = row - 1;
        } else {
            newRow = row + 1;
        }
        seePiece(newRow, col-1, pieces);
        seePiece(newRow, col+1, pieces);
        return pieces;
    }

    public int move(int newRow, int newCol, ReturnPlay rp) {
        int num = super.move(newRow, newCol, rp);
        if (num == 1) {
            range = 1;
        }
        return num;
    }
}