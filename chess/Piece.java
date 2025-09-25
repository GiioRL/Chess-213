package chess;

public class Piece {
    enum Type { king, queen, rook, bishop, knight, pawn }
    enum Player { white, black }
    
    Type type;
    Player player;
    int row; // 0 to 7 for rows 1 to 8
    int col; // 0 to 7 for columns a to h

    public Piece(Type type, Player player, int row, int col) {
        this.type = type;
        this.player = player;
        this.row = row;
        this.col = col;
    }
}