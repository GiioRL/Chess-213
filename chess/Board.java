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

    public static void removePiece(int row, int col) {
        board[row][col] = null;
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