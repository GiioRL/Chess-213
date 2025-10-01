package chess;

import java.lang.Math;
import java.util.ArrayList;

public abstract class Piece {
    enum Type {king, queen, rook, bishop, knight, pawn}
    enum Player {white, black}
    enum MoveType {vertical, horizontal, diagonal, knight, illegal};
    
    Type type;
    Player player;
    int row; // 0 to 7 for rows 1 to 8
    int col; // 0 to 7 for columns a to h
    String coord;
    int range;
    ArrayList<MoveType> moveTypes;
    Piece[] seenBy;

    public Piece(Player player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
        coord = Board.coordConverter(row, col);
        moveTypes = new ArrayList<MoveType>();
    }

    public String toString() {
        return "" + player + " " + type + " " + (char)('a' + col) + (8 - row);
    }

    public abstract boolean canMove(int row, int col); // cannibalCheck, check that no pieces in the path (unless knight), make sure move doesn't result in check on self, move in range.

    public boolean cannibalCheck(int row, int col) { //check for piece on this square has same color, if return false, then illegal move
        Piece piece = Board.getPiece(row, col);
        if (piece != null) {
            System.out.println("EAT");
            return piece.player != player;
        }
        return true;
    }

    public MoveType classifyMove(int newRow, int newCol) {
        if ((row - newRow) == 0) {
            if ((col - newCol) == 0) {
                return MoveType.illegal;
            } else {
                return MoveType.horizontal;
            }
        } else if (Math.abs(col - newCol) == 0) {
            return MoveType.vertical;
        } else if (Math.abs(row - newRow) == Math.abs(col - newCol)) {
            return MoveType.diagonal;
        } else if (Math.abs(row - newRow) == 1) {
            if (Math.abs(col - newCol) == 2) {
                return MoveType.knight;
            } else {
                return MoveType.illegal;
            }
        } else if (Math.abs(row - newRow) == 2) {
            if (Math.abs(col - newCol) == 1) {
                return MoveType.knight;
            } else {
                return MoveType.illegal;
            }
        } else {
            return MoveType.illegal;
        }
    }

    public int[][] getPath(int newRow, int newCol, MoveType movetype) { // return the squares in between this piece's square and target square
        int[][] squares = null;
        int lo = -1;
        int hi = -1;
        if (movetype == MoveType.vertical) {
            squares = new int[Math.abs(row - newRow) - 1][2];
            if (row < newRow) {
                lo = row;
                hi = newRow;
            } else {
                lo = newRow;
                hi = row;
            }
        } else if (movetype == MoveType.horizontal) {
            squares = new int[Math.abs(col - newCol) - 1][2];
            if (col < newCol) {
                lo = col;
                hi = newCol;
            } else {
                lo = newCol;
                hi = col;
            }
        } else if (movetype == MoveType.diagonal) { //implement this
            // squares = new int[Math.abs(row - newRow) - 1][2];
            // int lo1 = -1;
            // int lo2 = -1;
            // int hi1 = -1;
            // int hi2 = -1;
            // if (row < newRow) {
            //     lo1 = row;
            //     hi1 = newRow;
            // } else {
            //     lo1 = newRow;
            //     hi1 = row;
            // }
            // if (col < newCol) {
            //     lo2 = col;
            //     hi2 = newCol;
            // } else {
            //     lo2 = newCol;
            //     hi2 = col;
            // }
            // int i = 0;
            // lo1++;
            // lo2++;
            // (while)            
        }
        int i = 0;
        lo++;
        while (lo < hi) {
            squares[i][0] = lo++;
            squares[i][1] = col;
        }
        return squares;
    }

    public int move(int newRow, int newCol) {
        if (moveTypes.contains(classifyMove(newRow, newCol))) { // piece is allowed to move in this direction
            // if (cannibalCheck(newRow, newCol)) { // make sure pieces can't eat their own color
                if (canMove(newRow, newCol)) { // i actually dont think this does anything anmymore really
                    ReturnPiece rp = Board.makeReturnPiece(this);
                    if (Board.hasPiece[newRow][newCol]) { // capture
                        System.out.println("munch munch munch");
                        Board.removePiece(row, col);
                        row = newRow;
                        col = newCol;
                        Board.removePiece(row, col);
                        Board.placePiece(this);
                    } else { // just move it
                        if (Board.returnPieces.remove(rp)) {
                            System.out.println("PIECE FOUND!!");
                            Board.removePiece(row, col);
                            row = newRow;
                            col = newCol;
                            Board.placePiece(this);
                        } else {
                            System.out.println("I wasn't found??");
                        }
                    }
                    return 1; // move was legal, and made
                // } else {
                //     return -1; // move is legal for this piece (may be deleted) ??
                }
            // } else { // piece is eating its own color
            //     return -1;
            // }
        // } else {
        //     return -1; //piece cannot move in this direction
        }
        return -1; //move is illegal and was not made
    }

    public int move(String coord) { // move d8 h1 did not work
        int[] newCoord = Board.coordConverter(coord);
        return move(newCoord[0], newCoord[1]);
    }
}