package chess;

import java.lang.Math;
import java.util.ArrayList;

public class King extends Piece {

    public static int[] whiteKing = new int[]{0, 4}; // location of white king
    public static boolean whiteCheck = false; // white is in check
    public static int[] blackKing = new int[]{7, 4};
    public static boolean blackCheck = false;

    public King(Player player, int row, int col) {
        super(player, row, col);
        type = Type.king;
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.horizontal);
        moveTypes.add(MoveType.diagonal);
        // if (player == Piece.Player.white) {
        //     whiteKing = new int[]{row, col};
        //     // whiteCheck = false;
        // } else {
        //     blackKing = new int[]{row, col};
        //     // blackCheck = false;
        // }
    }

    public boolean canMove(int newRow, int newCol, MoveType movetype) {
        if (cannibalCheck(newRow, newCol)) { // need to check if move is within range
            if (moveTypes.contains(movetype)) {
                Piece dummy = new Dummy(type, player, newRow, newCol);
                Board.placePiece(dummy);
                // Check if King would be in check by moving to (newRow, newCol)
                Piece dummier = new Dummy(Type.queen, player, newRow, newCol);
                ArrayList<Piece> pieces = dummier.sees();
                for (Piece piece : pieces) {
                    if (piece.seesSquare(newRow, newCol)) {
                        Board.removePiece(dummy);
                        return false;
                    }
                }
                // Need to also account for knight attacking (newRow, newCol)
                dummier = new Dummy(Type.knight, player, newRow, newCol);
                pieces = dummier.sees();
                for (Piece piece : pieces) {
                    System.out.println(piece);
                    if (piece.seesSquare(newRow, newCol)) {
                        Board.removePiece(dummy);
                        return false;
                    }       
                }
                Board.removePiece(dummy);
                return (Math.abs(row-newRow) <= 1) && (Math.abs(col-newCol) <= 1);
            }
        }
        return false;
    }

    public Piece seeThrough(Piece piece) {
        return null;
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

    public int move(int newRow, int newCol, ReturnPlay rp) {
        if (super.move(newRow, newCol, rp) == 1) { //if seenBy is not empty undo that move or something
            if (player == Piece.Player.white) {
                whiteKing[0] = newRow;
                whiteKing[1] = newCol;
            } else {
                blackKing[0] = newRow;
                blackKing[1] = newCol;
            }
            return 1;
        }
        return -1;
    }
}