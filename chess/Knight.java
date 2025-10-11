package chess;

import java.lang.Math;
import java.util.ArrayList;

public class Knight extends Piece {
    
    public Knight(Player player, int row, int col) {
        super(player, row, col);
        type = Type.knight;
        moveTypes.add(MoveType.knight);
    }

    public Piece seeThrough(Piece piece) {
        return null;
    }

    public ArrayList<Piece> sees() {
        ArrayList<Piece> pieces = new ArrayList<Piece>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (Math.abs(i) + Math.abs(j) == 3) {
                    seePiece(row+i, row+j, pieces);
                }
            }
        }
        return pieces;
    }
}