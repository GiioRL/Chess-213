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
        MoveType movetype = classifyMove(newRow, newCol);
        if (moveTypes.contains(movetype)) {
            int[][] squares = Board.findPieces(row, col, movetype);
            if (squares != null) {
                for (int[] square : squares) {
                    if (square[0] == newRow) {
                        if (square[1] == newCol) {
                            return true;
                        }
                    }
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
        if (piece.type != null) { // keep those dang dummies off the board
            Board.placePiece(piece);
        }
        if (index == -1) {
            return null; // piece on edge of the board (if not this is an issue)
        }
        return Board.getPiece(squares[index]);
    }

    public ArrayList<Piece> sees() { // pretty sure this works
        ArrayList<Piece> pieces = new ArrayList<Piece>();

        for (MoveType movetype : moveTypes) {
            int[][] edges = Board.findPieces(row, col, movetype);
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

    public void check(ReturnPlay rp) {
        rp.message = ReturnPlay.Message.CHECK;
        if (player == Player.white) {
            King.blackCheck = true;
        } else {
            King.whiteCheck = true;
        }
        checkMate(rp);
    }

    public void checkMate(ReturnPlay rp) { // check for checkmate, update rp message if necessary, check and checkmate are run on the piece checking the king
        Piece king;
        // boolean bool = false; //represents whether a legal move can be made
        if (player == Player.white) {
            king = Board.getPiece(King.blackKing);
        } else {
            king = Board.getPiece(King.whiteKing);
        }
        
        for (int i = -1; i <= 1; i++) {
            int newRow = king.row+i;
            for (int j = -1; j <= 1; j++) {
                int newCol = king.col+j;
                if (Board.validSquare(newRow, newCol)) {
                    if (king.canMove(newRow, newCol, king.classifyMove(newRow, newCol))) {
                        return; // king can move
                    }
                }
            }
        }
        for (Piece piece : seenBy) {
            if (piece.canMove(row, col, piece.classifyMove(row, col))) {
                return; // this piece can be captured
            }
        }
        int[][] path = Board.getPath(row, col, king.row, king.col, classifyMove(king.row, king.col));

        for (int i = 0; i < Board.realPieces.size(); i++) {
            Piece piece = Board.realPieces.get(i);
            if (piece.player != player) { //piece needs to be on the checked team's side
                for (int[] square: path) {
                    int tempRow = square[0];
                    int tempCol = square[1];
                    if (piece.canMove(tempRow, tempCol, piece.classifyMove(tempRow, tempCol))) {
                        return; // piece can block the check
                    }
                }
            }
        }

        //no moves can be made, it is checkmate
        if (player == Player.white) {
            rp.message = ReturnPlay.Message.CHECKMATE_WHITE_WINS;
        } else {
            rp.message = ReturnPlay.Message.CHECKMATE_BLACK_WINS;
        }
    }

    public boolean blockCheck(int newRow, int newCol) {
        if (King.whiteCheck || King.blackCheck) { // one of the kings are in check
            if (type == Type.king) {
                King.whiteCheck = false; // this might not be the best solution but it works for now i think
                King.blackCheck = false;
                return true;
            }
            Piece king;
            if (player == Player.white) {
                king = Board.getPiece(King.whiteKing);
            } else {
                king = Board.getPiece(King.blackKing);
            }
            if (king.seenBy.size() > 1) { //double checks cannot be blocked
                return false;
            }
            Piece attacker = Board.getPiece(newRow, newCol); 
            if (attacker != null) {
                if (attacker.seesSquare(king.row, king.col)) { //attacker is captured
                    King.whiteCheck = false;
                    King.blackCheck = false;
                    return true;
                }
            }
            Piece dummy = new Dummy(type, player, newRow, newCol);
            Piece dummier = new Dummy(Type.queen, player, newRow, newCol);
            ArrayList<Piece> pieces = dummier.sees();
            for (Piece piece: pieces) {
                Piece target = piece.seeThrough(dummy);
                if (target != null && target.type == Type.king) {
                    King.whiteCheck = false;
                    King.blackCheck = false;
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public int move(int newRow, int newCol, ReturnPlay rp) {
        if (player == Board.player) { // MUST TURN THIS BACK ON
            MoveType movetype = classifyMove(newRow, newCol);
            // if (moveTypes.contains(movetype)) { // piece is allowed to move in this direction -> merged with canMove
                // if (cannibalCheck(newRow, newCol)) { // make sure pieces can't eat their own color
                    if (canMove(newRow, newCol, movetype)) {
                        // if (type == Type.king || (type != Type.king && blockCheck(newRow, newCol))) {
                        if (blockCheck(newRow, newCol)) { // start making the move
                            ArrayList<Piece> pieces = sees();
                            for (Piece piece: pieces) {
                                piece.seenBy.remove(this);
                            }
                            if (Board.hasPiece[newRow][newCol]) { // capture
                                Board.removePiece(this);
                                pieces = sees();
                                for (Piece piece: pieces) {
                                    piece.seenBy.remove(this);
                                }
                                Board.removePiece(newRow, newCol);
                                row = newRow;
                                col = newCol;
                                Board.placePiece(this);
                            } else { // just move it
                                Board.removePiece(this);
                                row = newRow;
                                col = newCol;
                                Board.placePiece(this);
                            }
                            Piece dummy = new Dummy(Type.queen, player, row, col);
                            pieces = dummy.sees();
                            for (Piece piece : pieces) {
                                if (piece.seesSquare(row, col)) {
                                    seenBy.add(piece);
                                    Piece blocked = piece.seeThrough(this);
                                    if (blocked != null) {
                                        blocked.seenBy.remove(piece);
                                        if (blocked.type == Type.king) {
                                            if (player == Player.white) { //player is the same color as blocked
                                                King.whiteCheck = false;
                                            } else {
                                                King.blackCheck = false;
                                            }
                                        }
                                    }
                                }
                            }
                            dummy = new Dummy(Type.knight, player, row, col);
                            pieces = dummy.sees();
                            for (Piece piece: pieces) {
                                if (piece.type == Type.knight) {
                                    seenBy.add(piece);
                                }
                            }
                            pieces = sees();
                            for (Piece piece : pieces) {
                                piece.seenBy.add(this);
                                if (piece.type == Type.king) {
                                    check(rp);
                                }
                            }
                            seenBy.clear();
                            if (player == Player.white) {
                                Board.player = Player.black;
                            } else {
                                Board.player = Player.white;
                            }
                            return 1; // move was legal, and made
                        }                    
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
            
        }
        return -1; //move is illegal and was not made
    }

    public int move(String coord, ReturnPlay rp) { // move d8 h1 did not work
        int[] newCoord = Board.coordConverter(coord);
        return move(newCoord[0], newCoord[1], rp);
    }
}