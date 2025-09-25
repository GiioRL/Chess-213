package chess;

public abstract class Piece {
    enum Type { king, queen, rook, bishop, knight, pawn }
    enum Player { white, black }
    
    Type type;
    Player player;
    int row; // 0 to 7 for rows 1 to 8
    int col; // 0 to 7 for columns a to h
    int range;
    Piece[] seenBy;

    public Piece(Player player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
    }

    public String toString() {
        return "" + player + " " + type + " " + (char)('a' + col) + (8 - row);
    }

    public abstract boolean canMove(int row, int col);
    public abstract void move(int row, int col);
}