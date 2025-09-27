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

    public boolean cannibalCheck(int row, int col) { //check for piece on this square has same color, if return false, then illegal move
        Piece piece = Board.getPiece(row, col);
        if (piece != null) {
            System.out.println("EAT");
            return piece.player != player;
        }
        return true;
    }

    public int move(int newRow, int newCol) {
        // System.out.println("im going to " + newRow + newCol);
        if (canMove(newRow, newCol)) {
            // Piece piece = Board.getPiece(row, col);
            ReturnPiece rp = Board.makeReturnPiece(this);
            // String newCoord = Board.coordConverter(newRow, newCol);
            // System.out.println("newCoord: " + newCoord);
            if (Board.hasPiece[newRow][newCol]) { // capture
                System.out.println("munch munch munch");
                Board.removePiece(row, col);
                row = newRow;
                col = newCol;
                Board.removePiece(row, col);
                Board.placePiece(this);
                // Board.hasPiece[row][col] = false; // make sure to kill the captured piece
            } else { // just move it
                if (Board.returnPieces.remove(rp)) {
                    System.out.println("PIECE FOUND!!");
                    Board.removePiece(row, col);
                    // Board.hasPiece[row][col] = false;
                    row = newRow;
                    col = newCol;
                    Board.placePiece(this);
                    // rp = Board.makeReturnPiece(this);
                    // Board.returnPieces.add(rp);
                    // Board.hasPiece[newRow][newCol] = true;
                } else {
                    System.out.println("I wasn't found??");
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