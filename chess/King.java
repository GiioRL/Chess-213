package chess;

import java.lang.Math;
import java.util.ArrayList;

public class King extends Piece {

    public static int[] whiteKing = new int[]{7, 4}; // location of white king
    public static boolean whiteCheck = false; // white is in check
    public static int[] blackKing = new int[]{0, 4};
    public static boolean blackCheck = false;
    boolean hasMoved;

    public King(Player player, int row, int col) {
        super(player, row, col);
        type = Type.king;
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.horizontal);
        moveTypes.add(MoveType.diagonal);
        hasMoved = false;
    }

    public boolean canMove(int newRow, int newCol, MoveType movetype) {
        if (canCastle(newRow, newCol, movetype))
            return true;
        if (cannibalCheck(newRow, newCol)) { // need to check if move is within range
            if (moveTypes.contains(movetype) && !Board.squareUnderCheck(newRow, newCol, player)) {
                return (Math.abs(row-newRow) <= 1) && (Math.abs(col-newCol) <= 1);
            }
        }
        return false;
    }

    public boolean canCastle(int newRow, int newCol, MoveType moveType)
    {
        if (moveType != MoveType.horizontal || Math.abs(newCol - col) != 2 || Math.abs(newRow - row) != 0 || hasMoved) // some illegal criteria
            return false;
        if ((player == Piece.Player.white && whiteCheck) || (player == Piece.Player.black && blackCheck)) // check if king under check
            return false;
        if (Board.squareUnderCheck(newRow, newCol, player) || Board.hasPiece[newRow][newCol]) // cannot move into check or capture a piece
            return false;
        if (newCol > col) // short castle
        {
            if (Board.squareUnderCheck(row, col + 1, player) || Board.hasPiece[row][col + 1]) // cannot move through check or through a piece
                return false;
            Piece rook = Board.getPiece(row, 7);
            if (rook == null || rook.type != Piece.Type.rook || rook.player != player || ((Rook) rook).hasMoved) // rook must exist and not have moved
                return false;
            return true;
        }
        else // long castle
        {
            if (Board.squareUnderCheck(row, col - 1, player) || Board.hasPiece[row][col - 1] || Board.hasPiece[row][1]) // king cannot move through check or through a piece, and rook cannot move through knight
                return false;
            Piece rook = Board.getPiece(row, 0);
            if (rook == null || rook.type != Piece.Type.rook || rook.player != player || ((Rook) rook).hasMoved) // rook must exist and not have moved
                return false;
            return true;
        }
    }

    public Piece seeThrough(Piece piece) {
        return null;
    }

    public ArrayList<Piece> sees() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((i != 0) && (j != 0)) {
                    seePiece(row+i, col+j, pieces);
                }
            }
        }
        return pieces;
    }

    public int move(int newRow, int newCol, ReturnPlay rp) {
        // castle if requested, otherwise regular move
        if (castle(newRow, newCol, rp) == 1 || super.move(newRow, newCol, rp) == 1) { //if seenBy is not empty undo that move or something
            if (player == Piece.Player.white) {
                whiteKing[0] = newRow;
                whiteKing[1] = newCol;
            } else {
                blackKing[0] = newRow;
                blackKing[1] = newCol;
            }
            hasMoved = true;
            return 1;
        }
        return -1;
    }

    public int castle(int newRow, int newCol, ReturnPlay rp) 
    {
        if (!canCastle(newRow, newCol, classifyMove(newRow, newCol)))
            return -1;
        if (newCol > col) // short castle
        {
            Piece rook = Board.getPiece(row, 7);
            if (rook.move(row, 5, rp) == -1)
                return -1;
        }
        else // long castle
        {
            Piece rook = Board.getPiece(row, 0);
            if (rook.move(row, 3, rp) == -1)
                return -1;
        }
        Board.removePiece(this);
        row = newRow;
        col = newCol;
        Board.placePiece(this);
        return 1;
    }
}