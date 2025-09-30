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

    public abstract boolean canMove(int row, int col);

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

    public int move(int newRow, int newCol) {
        if (moveTypes.contains(classifyMove(newRow, newCol))) { // piece is allowed to move in this direction
            if (cannibalCheck(newRow, newCol)) { // make sure pieces can't eat their own color
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
            }
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