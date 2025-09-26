package chess;

public abstract class Piece {
    enum Type { king, queen, rook, bishop, knight, pawn }
    enum Player { white, black }
    
    Type type;
    Player player;
    int row; // 0 to 7 for rows 1 to 8
    int col; // 0 to 7 for columns a to h
    String coord;
    int range;
    Piece[] seenBy;

    public Piece(Player player, int row, int col) {
        this.player = player;
        this.row = row;
        this.col = col;
        coord = Board.coordConverter(row, col);
    }

    public String toString() {
        return "" + player + " " + type + " " + (char)('a' + col) + (8 - row);
    }

    public abstract boolean canMove(int row, int col);

    public int move(int newRow, int newCol) {
        System.out.println("im going to " + newRow + newCol);
        if (canMove(newRow, newCol)) {
            // Piece piece = Board.getPiece(row, col);
            ReturnPiece rp = Chess.makeReturnPiece(this);
            // String newCoord = Board.coordConverter(newRow, newCol);
            // System.out.println("newCoord: " + newCoord);
            if (Board.hasPiece[newRow][newCol]) { // capture
                Board.hasPiece[row][col] = false; // make sure to kill the captured piece
            } else { // just move it
                if (Chess.returnPieces.remove(rp)) {
                    System.out.println("PIECE FOUND!!");
                    row = newRow;
                    col = newCol;
                    rp = Chess.makeReturnPiece(this);
                    Chess.returnPieces.add(rp);
                    Board.hasPiece[row][col] = false;
                    Board.hasPiece[newRow][newCol] = true;
                }
            }
            return 1;
        } else {
            return -1;
        }
    }

    public int move(String coord) { // move d8 h1 did not work
        int[] newCoord = Board.coordConverter(coord);
        return move(newCoord[0], newCoord[1]);
    }
}