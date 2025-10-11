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
    // String coord; // i dont think this is ever used
    // int range; // i dont think this is necessary
    ArrayList<MoveType> moveTypes;
    ArrayList<Piece> seenBy;

    public Piece(Player player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
        // coord = Board.coordConverter(row, col);
        moveTypes = new ArrayList<MoveType>();
        seenBy = new ArrayList<Piece>();
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

    public boolean selfCheck(int newRow, int newCol) { //returns false if move results in self Check // infinite loop in line e2 e4; d1 h5; g7 g6; f7 f6
        // Board.removePiece(this);
        // boolean bool = true;
        // if (player == Player.white) {
        //     for (Piece piece : seenBy) {
        //         // System.out.println("using " + piece + "'s eyes");
        //         if (piece.seesSquare(King.whiteKing)) {
        //             bool = false;
        //             break;
        //         }
        //     }
        // } else {
        //     for (Piece piece : seenBy) {
        //         // System.out.println("using " + piece + "'s eyes");
        //         // System.out.println("Black King is on (" + King.blackKing[0] + ", " + King.blackKing[1] + ")");
        //         if (piece.seesSquare(King.blackKing)) {
        //             bool = false;
        //             break;
        //         }
        //     }
        // }
        // Board.placePiece(this);
        // return bool;
        for (Piece piece : seenBy) {
            Piece target = piece.seeThrough(this);
            if (target != null) {
                if (target.type == Type.king) {
                    if (target.player == player) { // check if piece is EATING this guy >:D
                        if (piece.row == newRow) {
                            if (piece.col == newCol) {
                                continue;
                            }
                        }
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean canMove(int newRow, int newCol, MoveType movetype) { // cannibalCheck, check that no pieces in the path, make sure move doesn't result in check on self.
        if (moveTypes.contains(movetype)) {
            if (cannibalCheck(newRow, newCol)) {
                if (selfCheck(newRow, newCol)) {
                    boolean bool = true;
                    // System.out.println("getting the path");
                    int[][] squares = Board.getPath(row, col, newRow, newCol, movetype);
                    if (squares != null) {
                        for (int i = 0; i < squares.length; i++) {
                            // System.out.println("checking square " + squares[i][0] + squares[i][1]);
                            bool &= !Board.hasPiece[squares[i][0]][squares[i][1]];
                        }
                    }
                    return bool;
                }
            }
        }
        return false;
    }

    public boolean seesSquare(int newRow, int newCol) { //this may or may not be questionable
        int[][] squares = Board.findPieces(row, col, classifyMove(newRow, newCol));
        for (int[] square : squares) {
            if (square[0] == newRow) {
                if (square[1] == newCol) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean seesSquare(int[] coord) {
        return seesSquare(coord[0], coord[1]);
    }

    public void seePiece(int newRow, int newCol, ArrayList<Piece> pieces) {
        Piece piece = null;
        if (Board.validSquare(newRow, newCol)) {
            piece = Board.getPiece(newRow, newCol);
        }
        if (piece != null) {
            if (player != piece.player) {
                pieces.add(piece);
            }
        }
    }

    public void seePiece(int[] coord, ArrayList<Piece> pieces) {
        seePiece(coord[0], coord[1], pieces);
    }

    public Piece seeThrough(Piece piece) {
        int newRow = piece.row;
        int newCol = piece.col;
        MoveType movetype = classifyMove(newRow, newCol);
        Board.removePiece(piece);
        int[][] squares = Board.findPieces(row, col, movetype);
        int index = -1;
        loop:
        for (int i = 0; i < squares.length; i++) {
            if (squares[i][0] == row) {
                if (squares[i][1] == col) {
                    continue;
                }
            }
            int[][] path = Board.getPath(row, col, squares[i][0], squares[i][1], movetype);
            for (int[] square: path) {
                if (square[0] == newRow) {
                    if (square[1] == newCol) {
                        index = i;
                        break loop;
                    }
                }
            }
        }
        // int[][] path = Board.getPath(newRow, newCol, squares[index][0], squares[index][col], movetype);
        // Piece target = null;
        // for (int i = 0; i < path.length; i++) {
        //     int tempRow = path[i][0];
        //     int tempCol = path[i][1];
        //     if (Board.hasPiece[tempRow][tempCol]) {
        //         target = Board.getPiece(tempRow, tempCol);
        //         break;
        //     }
        // }
        Board.placePiece(piece);
        return Board.getPiece(squares[index]);
    }

    public ArrayList<Piece> sees() { // pretty sure this works
        ArrayList<Piece> pieces = new ArrayList<Piece>();

        for (MoveType movetype : moveTypes) {
            // System.out.println("MoveType: " + movetype);
            int[][] edges = Board.findPieces(row, col, movetype);
            // System.out.println("Edges:");
            // for (int i = 0; i < edges.length; i++) {
                // System.out.println("[" + edges[i][0] + "," + edges[i][1] + "]");
            // }
            // int[][] squares;
            for (int i = 0; i < edges.length; i++) {
                int newRow = edges[i][0];
                int newCol = edges[i][1];
                if ((row == newRow) && (col == newCol)) {
                    continue;
                }
                seePiece(newRow, newCol, pieces);
            }
        }
        return pieces;
    }

    // public ArrayList<Piece> sees() { // this WORKS
    //     ArrayList<Piece> pieces = new ArrayList<Piece>();

    //     for (MoveType movetype : moveTypes) {
    //         int newRow = -1;
    //         int newCol = -1;
    //         if (movetype == MoveType.vertical) {
    //             newRow = row;
    //             newCol = col;
    //             while (newRow > 0) {
    //                 if (Board.hasPiece[--newRow][newCol]) {
    //                     break;
    //                 }
    //             }
    //             if (newRow >= 0) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //             newRow = row;
    //             while (newRow < 7) {
    //                 if (Board.hasPiece[++newRow][newCol]) {
    //                     break;
    //                 }
    //             }
    //             if (newRow <= 7) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //         } else if (movetype == MoveType.horizontal) {
    //             newRow = row;
    //             newCol = col;
    //             while (newCol > 0) {
    //                 if (Board.hasPiece[newRow][--newCol]) {
    //                     break;
    //                 } //problem here newCol
    //             }
    //             if (newCol >= 0) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //             newCol = col;
    //             while (newCol < 7) {
    //                 if (Board.hasPiece[newRow][++newCol]) {
    //                     break;
    //                 }
    //             }
    //             if (newCol <= 7) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //         } else if (movetype == MoveType.diagonal) { // and check the other direction
    //             int min = Math.min(row, col);
    //             int max = Math.min(7-row, 7-col);
    //             newRow = row;
    //             newCol = col;
    //             while (newRow > row-min) {
    //                 if (Board.hasPiece[--newRow][--newCol]) {
    //                     break;
    //                 }
    //             }
    //             if (newRow >= row-min) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //             newRow = row;
    //             newCol = col;
    //             while (newRow < row+max) {
    //                 // System.out.println("newRow: " + newRow + "\nnewCol: " + newCol);
    //                 if (Board.hasPiece[++newRow][++newCol]){
    //                     break;
    //                 }
    //             }
    //             if (newRow <= row+max) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //             min = Math.min(row, 7-col);
    //             max = Math.min(7-row, col);
    //             newRow = row;
    //             newCol = col;
    //             while (newRow > row-min) {
    //                 if (Board.hasPiece[--newRow][++newCol]) {
    //                     break;
    //                 }
    //             }
    //             if (newRow >= row-min) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //             newRow = row;
    //             newCol = col;
    //             while (newRow < row+max) {
    //                 if (Board.hasPiece[++newRow][--newCol]) {
    //                     break;
    //                 }
    //             }
    //             if (newRow <= row+max) {
    //                 seePiece(newRow, newCol, pieces);
    //             }
    //         }
    //     }
    //     return pieces;
    // }

    public void check(ReturnPlay rp) {
        rp.message = ReturnPlay.Message.CHECK;
        if (player == Player.white) {
            King.blackCheck = true;
        } else {
            King.whiteCheck = true;
        }
        checkMate(rp);
    }

    public void checkMate(ReturnPlay rp) { // check for checkmate, update rp message if necessary
        return;
    }

    public int move(int newRow, int newCol, ReturnPlay rp) {
        // if (player == Board.player) { // MUST TURN THIS BACK ON
            MoveType movetype = classifyMove(newRow, newCol);
            // if (moveTypes.contains(movetype)) { // piece is allowed to move in this direction -> merged with canMove
                // if (cannibalCheck(newRow, newCol)) { // make sure pieces can't eat their own color
                    if (canMove(newRow, newCol, movetype)) {
                        // ReturnPiece rP = Board.makeReturnPiece(this);
                        if (Board.hasPiece[newRow][newCol]) { // capture
                            // System.out.println("munch munch munch");
                            // Board.removePiece(row, col);
                            Board.removePiece(this);
                            Board.removePiece(newRow, newCol);
                            row = newRow;
                            col = newCol;
                            Board.placePiece(this);
                        } else { // just move it
                            // if (Board.returnPieces.remove(rP)) {
                                // System.out.println("PIECE FOUND!!");
                                // Board.removePiece(row, col);
                                Board.removePiece(this);
                                row = newRow;
                                col = newCol;
                                Board.placePiece(this);
                            // } else {
                                // System.out.println("I wasn't found??");
                            // }
                        }
                        ArrayList<Piece> pieces = sees();
                        // if (pieces.size() == 0) {
                            // System.out.println("im empty :((");
                        // }
                        for (Piece piece : pieces) {
                            // System.out.println("" + this + " sees " + piece);
                            piece.seenBy.add(this);
                            if (piece.type == Type.king) {
                                check(rp);
                                // System.out.println("White King: (" + King.whiteKing[0] + ", " + King.whiteKing[1] + ")");
                                // System.out.println("Black King: (" + King.blackKing[0] + ", " + King.blackKing[1] + ")");
                                // rp.message = ReturnPlay.Message.CHECK;
                                // if (player == Player.white) {
                                //     King.blackCheck = true;
                                // } else {
                                //     King.whiteCheck = true;
                                // }
                            }
                        }
                        seenBy.clear();
                        Piece dummy = new Queen(player, row, col);
                        pieces = dummy.sees();
                        for (Piece piece : pieces) {
                            // System.out.println("using " + piece + "'s eyes");
                            if (piece.seesSquare(row, col)) {
                                // System.out.println("" + piece + " sees " + this);
                                seenBy.add(piece);
                                Piece blocked = piece.seeThrough(this);
                                if (blocked != null) {
                                    blocked.seenBy.remove(piece);
                                }
                            }
                        }
                        if (player == Player.white) {
                            Board.player = Player.black;
                        } else {
                            Board.player = Player.white;
                        }
                        return 1; // move was legal, and made
                    // } else {
                    //     return -1; // move is legal for this piece (may be deleted) ??
                    }
                    // System.out.println("cant move sir");
                // } else { // piece is eating its own color
                //     return -1;
                // }
            // } else {
            //     return -1; //piece cannot move in this direction
            // }
            
        // } MUST TURN THIS BACK ON
        return -1; //move is illegal and was not made
    }

    public int move(String coord, ReturnPlay rp) { // move d8 h1 did not work
        int[] newCoord = Board.coordConverter(coord);
        return move(newCoord[0], newCoord[1], rp);
    }
}