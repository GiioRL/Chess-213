package chess;

import java.util.ArrayList;

public class Knight extends Piece {
    
    public Knight(Chess.Player player, int row, int col) {
        super(player, row, col);
        type = Type.knight;
        moveTypes.add(MoveType.knight);
    }

    public Piece seeThrough(Piece piece) {
        return null;
    }

    public boolean seesSquare(int newRow, int newCol) {
        int rowDelta = Math.abs(row - newRow);
        int colDelta = Math.abs(col - newCol);
        return ((rowDelta <=2 && colDelta <= 2) && (rowDelta + colDelta == 3));
    }

    public ArrayList<Piece> sees() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (int i = -2; i <= 2; i++) {
            for (int j = -2; j <= 2; j++) {
                if (Math.abs(i) + Math.abs(j) == 3) {
                    seePiece(row+i, col+j, pieces);
                }
            }
        }
        return pieces;
    }
}