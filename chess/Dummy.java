package chess;

import java.util.ArrayList;

public class Dummy extends Piece {
    Piece mimic;
    Dummy (Type type, Chess.Player player, int row, int col) {
        super(player, row, col);
        switch(type) {
            case Type.pawn:
            mimic = new Pawn(player, row, col);
            break;
            case Type.rook:
            mimic = new Rook(player, row, col);
            break;
            case Type.knight:
            mimic = new Knight(player, row, col);
            break;
            case Type.bishop:
            mimic = new Bishop(player, row, col);
            break;
            case Type.queen:
            mimic = new Queen(player, row, col);
            break;
            case Type.king:
            mimic = new King(player, row, col);
            break;
            default:
            break;
        }
        moveTypes = mimic.moveTypes;
    }

    public boolean canMove(int newRow, int newCol, MoveType movetype) {
        return mimic.canMove(newRow, newCol, movetype);
    }

    public boolean seesSquare(int newRow, int newCol) {
        return mimic.seesSquare(newRow, newCol);
    }

    public Piece seeThrough(Piece piece) {
        return mimic.seeThrough(piece);
    }

    public ArrayList<Piece> sees() {
        return mimic.sees();
    }
}
