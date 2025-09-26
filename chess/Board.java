package chess;

public class Board {
    static Piece[][] board = new Piece[8][8];
    static boolean[][] hasPiece = new boolean[8][8];

    public static void placePiece(Piece piece) { // should we populate white on row 1 and 2, black on 7 and 8 or the other way around becuase how a chess board visually works
        hasPiece[piece.row][piece.col] = true;
        board[piece.row][piece.col] = piece;
    }

    public static Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public static Piece getPiece (int[] coord) {
        return board[coord[0]][coord[1]];
    }

    public static Piece getPiece(String square) {
        int col = square.charAt(0) - 'a';
        int row = 8 - (square.charAt(1) - '0'); // replace with coord converter
        return getPiece(row, col);
    }

    public static void removePiece(int row, int col) {
        board[row][col] = null;
    }

    public static String coordConverter(int row, int col) { // check this guy
        return "" + (char)('a' + col) + (row + '0');
    }

    public static int[] coordConverter(String coord) {
        int[] newCoord = new int[] {8 - (coord.charAt(1) - '0'), (coord.charAt(0) - 'a')};
        System.out.println("coord converting " + coord + " to " + newCoord[0] + newCoord[1]);
        return newCoord;
    }

    public boolean validSquare(String square) {
        return true; //implement
    }

    public static void printBoard() {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (hasPiece[r][c]) {
                    System.out.print("CP");
                } else {
                    if ((r + c) % 2 == 0) {
                    System.out.print("  ");
                    } else {
                        System.out.print("##");
                    }
                }
                System.out.print(" ");
            }
            System.out.println((8 - r));
        }
        System.out.println(" a  b  c  d  e  f  g  h");
    }
}