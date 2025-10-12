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
        if (canEnPassant(newRow, newCol, movetype))
            return true;
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

    public boolean canEnPassant(int newRow, int newCol, MoveType moveType)
    {
        // some illegal criteria
        if (moveType != MoveType.diagonal || Board.hasPiece[newRow][newCol] || Chess.prevMove.equals(""))
            return false;
        if (player == Player.white && !(row == 3 && newRow == 2))
            return false;
        if (player == Player.black && !(row == 4 && newRow == 5))
            return false;
        if (Math.abs(col - newCol) != 1)
            return false;
        if (!Board.hasPiece[row][newCol]) // check if there exists opponent pawn adjacent to self
            return false;
        Piece opponentPawn = Board.getPiece(row, newCol); // get opponent pawn adjacent to self
        if (opponentPawn.player == player || opponentPawn.type != Type.pawn) // some more illegal criteria
            return false;
        // Check prevMove to see if opponnent pawn just moved
        String[] prevMoveSquares = Chess.prevMove.split(" ");
        if (!prevMoveSquares[1].equalsIgnoreCase(Board.coordConverter(opponentPawn.row, opponentPawn.col)))
            return false;
        if (opponentPawn.player == Player.white && !prevMoveSquares[0].equalsIgnoreCase(Board.coordConverter(opponentPawn.row + 2, opponentPawn.col)))
            return false;
        if (opponentPawn.player == Player.black && !prevMoveSquares[0].equalsIgnoreCase(Board.coordConverter(opponentPawn.row - 2, opponentPawn.col)))
            return false;
        return true;
    }

    public boolean seesSquare(int newRow, int newCol) {
        if (player == Player.white) {
            return ((row - newRow == 1) && (Math.abs(col - newCol) == 1));
        } else {
            return ((newRow - row == 1) && (Math.abs(newCol - col) == 1));
        }
    }

    public Piece seeThrough(Piece piece) {
        return null;
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

    public void promotion(String type, ReturnPlay rp) {
        Piece newPiece = null;
        switch(type) {
            case "R":
            newPiece = new Rook(player, row, col);
            break;
            case "N":
            newPiece = new Knight(player, row, col);
            break;
            case "B":
            newPiece = new Bishop(player, row, col);
            break;
            case "Q":
            newPiece = new Queen(player, row, col);
            break;
            default:
            return;
        }
        Board.removePiece(this);
        newPiece.seenBy = seenBy;
        Board.placePiece(newPiece); // check for check with new piece
        ArrayList<Piece> pieces = newPiece.sees();
        for (Piece piece: pieces) {
            piece.seenBy.add(newPiece);
            if (piece.type == Type.king) {
                newPiece.check(rp);
                // rp.message = ReturnPlay.Message.CHECK;
                // if (player == Player.white) {
                //     King.blackCheck = true;
                // } else {
                //     King.whiteCheck = true;
                // }
            }
        }
    }

    public int enPassant(int newRow, int newCol, ReturnPlay rp)
    {
        if (!canEnPassant(newRow, newCol, classifyMove(newRow, newCol)))
            return -1;
        Dummy dummy = new Dummy(Type.pawn, (player == Player.white ? Player.black : Player.white), newRow, newCol);
        Board.placePiece(dummy);
        return super.move(newRow, newCol, rp);
    }

    public int move(int newRow, int newCol, ReturnPlay rp) {
        int num = enPassant(newRow, newCol, rp);
        if (num == -1)
            num = super.move(newRow, newCol, rp);
        if (num == 1) {
            range = 1;
            if (player == Player.white) {
                if (newRow == 0) {
                    promotion("Q", rp);
                }
            } else {
                if (newRow == 7) {
                    promotion("Q", rp);
                }
            }
        }
        
        return num;
    }

    public int move(int newRow, int newCol, String newType, ReturnPlay rp) {
        if (canMove(newRow, newCol, classifyMove(newRow, newCol))) {
            if (newType.equals("R") || newType.equals("N") || newType.equals("B") || newType.equals("Q")) {
                if (super.move(newRow, newCol, rp) == 1) {
                    promotion(newType, rp);
                    return 1;
                }
            }
        }
        return -1;
    }
}