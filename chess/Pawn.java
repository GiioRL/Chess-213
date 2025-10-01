package chess;

public class Pawn extends Piece {
    
    public Pawn(Player player, int row, int col) {
        super(player, row, col);
        type = Type.pawn;
        range = 2; // updated after first move
        moveTypes.add(MoveType.vertical);
        moveTypes.add(MoveType.diagonal);
    }

    public boolean canMove(int newRow, int newCol) {
        MoveType movetype = classifyMove(newRow, newCol);
        if (cannibalCheck(newRow, newCol)) {
            if (movetype == MoveType.vertical) {
                if (!Board.hasPiece[newRow][newCol]) {
                    if (player == Player.white) {
                        System.out.println("" + player + row + newRow);
                        return ((row > newRow) && (row - newRow) <= range);
                    } else {
                        return ((newRow > row) && (newRow - row) <= range);
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
                } else { // can condense all return falses
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public int move(int newRow, int newCol) {
        int num = super.move(newRow, newCol);
        if (num == 1) {
            range = 1;
        }
        return num;
    }

    // public int move(int newRow, int newCol) {
    //     if (moveTypes.contains(classifyMove(newRow, newCol))) { // piece is allowed to move in this direction
    //         if (cannibalCheck(newRow, newCol)) { // make sure pieces can't eat their own color
    //             if (canMove(newRow, newCol)) { // i actually dont think this does anything anmymore really
    //                 ReturnPiece rp = Board.makeReturnPiece(this);
    //                 if (Board.hasPiece[newRow][newCol]) { // capture
    //                     System.out.println("munch munch munch");
    //                     Board.removePiece(row, col);
    //                     row = newRow;
    //                     col = newCol;
    //                     Board.removePiece(row, col);
    //                     Board.placePiece(this);
    //                 } else { // just move it
    //                     if (Board.returnPieces.remove(rp)) {
    //                         System.out.println("PIECE FOUND!!");
    //                         Board.removePiece(row, col);
    //                         row = newRow;
    //                         col = newCol;
    //                         Board.placePiece(this);
    //                     } else {
    //                         System.out.println("I wasn't found??");
    //                     }
    //                 }
    //                 return 1; // move was legal, and made
    //             // } else {
    //             //     return -1; // move is legal for this piece (may be deleted) ??
    //             }
    //         // } else { // piece is eating its own color
    //         //     return -1;
    //         }
    //     // } else {
    //     //     return -1; //piece cannot move in this direction
    //     }
    //     return -1; //move is illegal and was not made
    // }

    // public int move (int newRow, int newCol) {
    //        }
}