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
}