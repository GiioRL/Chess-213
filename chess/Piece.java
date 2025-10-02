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
    // int range; // i dont think this is necessary
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

    public boolean cannibalCheck(int row, int col) { //check for piece on this square has same color, if return false, then illegal move
        Piece piece = Board.getPiece(row, col);
        if (piece != null) {
            // System.out.println("EAT");
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

    public boolean canMove(int row, int col, MoveType movetype) { // cannibalCheck, check that no pieces in the path, make sure move doesn't result in check on self.
        if (moveTypes.contains(movetype)) {
            if (cannibalCheck(row, col)) {
                boolean bool = true;
                // System.out.println("getting the path");
                int[][] squares = getPath(row, col, movetype);
                if (squares != null) {
                    for (int i = 0; i < squares.length; i++) {
                        // System.out.println("checking square " + squares[i][0] + squares[i][1]);
                        bool &= !Board.hasPiece[squares[i][0]][squares[i][1]];
                    }
                }
                return bool;
            }
        }
        return false;
    }

    public int[][] getPath(int newRow, int newCol, MoveType movetype) { // return the squares in between this piece's square and target square
        // System.out.println("row: " + row + "\ncol: " + col + "\nnewRow: " + newRow + "\nnewCol: " + newCol);
        int[][] squares = null;
        int lo = -1;
        int hi = -1;
        int i = 0;
        if (movetype == MoveType.vertical) {
            squares = new int[Math.abs(row - newRow) - 1][2];
            if (row < newRow) {
                lo = row;
                hi = newRow;
            } else {
                lo = newRow;
                hi = row;
            }
            // System.out.println("lo: " + lo + "hi: " + hi);
            lo++;
            while (lo < hi) {
                squares[i][0] = lo++;
                squares[i++][1] = col;
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
            // System.out.println("lo: " + lo + "hi: " + hi);
            lo++;
            while (lo < hi) {
                squares[i][0] = row;
                squares[i++][1] = lo++;
            }
        } else if (movetype == MoveType.diagonal) {
            squares = new int[Math.abs(row - newRow) - 1][2];
            boolean bool = false;
            int temp = -1;
            if (row < newRow) {
                lo = row;
                hi = newRow;
                bool = (col < newCol);
                temp = col;
            } else {
                lo = newRow;
                hi = row;
                bool = (newCol < col);
                temp = newCol;
            }
            // System.out.println("lo: " + lo + "\nhi: " + hi + "\nbool: " + bool);

            lo++;
            if (bool) {
                temp++;
                while (lo < hi) {
                    squares[i][0] = lo++;
                    squares[i++][1] = temp++;
                }
            } else {
                temp--;
                while (lo < hi) {
                    squares[i][0] = lo++;
                    squares[i++][1] = temp--;
                } 
            }
        }
        return squares;
    }

    public boolean sees(int newRow, int newCol) { //this may or may not be questionable
        boolean bool = true;
        for (MoveType movetype : moveTypes) {
            bool &= canMove(newRow, newCol, movetype);
        }
        return bool;
    }

    public int move(int newRow, int newCol) {
        MoveType movetype = classifyMove(newRow, newCol);
        // if (moveTypes.contains(movetype)) { // piece is allowed to move in this direction -> merged with canMove
            // if (cannibalCheck(newRow, newCol)) { // make sure pieces can't eat their own color
                if (canMove(newRow, newCol, movetype)) {
                    ReturnPiece rp = Board.makeReturnPiece(this);
                    if (Board.hasPiece[newRow][newCol]) { // capture
                        // System.out.println("munch munch munch");
                        Board.removePiece(row, col);
                        row = newRow;
                        col = newCol;
                        Board.removePiece(row, col);
                        Board.placePiece(this);
                    } else { // just move it
                        if (Board.returnPieces.remove(rp)) {
                            // System.out.println("PIECE FOUND!!");
                            Board.removePiece(row, col);
                            row = newRow;
                            col = newCol;
                            Board.placePiece(this);
                        } else {
                            // System.out.println("I wasn't found??");
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
        // }
        return -1; //move is illegal and was not made
    }

    public int move(String coord) { // move d8 h1 did not work
        int[] newCoord = Board.coordConverter(coord);
        return move(newCoord[0], newCoord[1]);
    }
}