package chess;

import java.util.ArrayList;

public class Board {
    static Piece[][] board = new Piece[8][8];
    static boolean[][] hasPiece = new boolean[8][8];
    static ArrayList<ReturnPiece> returnPieces = new ArrayList<ReturnPiece>();

    public static void placePiece(Piece piece) { // should we populate white on row 1 and 2, black on 7 and 8 or the other way around becuase how a chess board visually works
        hasPiece[piece.row][piece.col] = true;
        board[piece.row][piece.col] = piece;
        returnPieces.add(makeReturnPiece(piece));
    }

    public static Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public static Piece getPiece (int[] coord) {
        return board[coord[0]][coord[1]];
    }

    // public static Piece getPiece(String square) {
    //     int col = square.charAt(0) - 'a';
    //     int row = 8 - (square.charAt(1) - '0'); // replace with coord converter
    //     return getPiece(row, col);
    // }

    public static Piece getPiece(String square) {
        return getPiece(coordConverter(square));
    }

    public static Piece removePiece(int row, int col) { //return piece
        if (!hasPiece[row][col]) {
            return null;
        }
        Piece piece = board[row][col];
        board[row][col] = null;
        hasPiece[row][col] = false;
        returnPieces.remove(makeReturnPiece(piece));
        return piece;
    }

    public static String coordConverter(int row, int col) { // check this guy
        return "" + (char)('a' + col) + (row + '0');
    }

    public static int[] coordConverter(String coord) {
        int[] newCoord = new int[] {8 - (coord.charAt(1) - '0'), (coord.charAt(0) - 'a')};
        // System.out.println("coord converting " + coord + " to " + newCoord[0] + newCoord[1]);
        return newCoord;
    }

    public static boolean validSquare(String square) {
        if (square.length() != 2) {
            return false;
        }
        int[] coord = coordConverter(square);
        return validSquare(coord[0], coord[1]);
    }

    public static boolean validSquare(int row, int col) {
        return (row >= 0 && row <= 7 && col >= 0 && col <= 7);
    }

    public static ReturnPiece makeReturnPiece(Piece piece) {
		ReturnPiece rp = new ReturnPiece();
		switch (piece.type) {
			case pawn:
				rp.pieceType = piece.player == Piece.Player.white ? ReturnPiece.PieceType.WP : ReturnPiece.PieceType.BP;
				break;
			case rook:
				rp.pieceType = piece.player == Piece.Player.white ? ReturnPiece.PieceType.WR : ReturnPiece.PieceType.BR;
				break;
			case knight:
				rp.pieceType = piece.player == Piece.Player.white ? ReturnPiece.PieceType.WN : ReturnPiece.PieceType.BN;
				break;
			case bishop:
				rp.pieceType = piece.player == Piece.Player.white ? ReturnPiece.PieceType.WB : ReturnPiece.PieceType.BB;
				break;
			case queen:
				rp.pieceType = piece.player == Piece.Player.white ? ReturnPiece.PieceType.WQ : ReturnPiece.PieceType.BQ;
				break;
			case king:
				rp.pieceType = piece.player == Piece.Player.white ? ReturnPiece.PieceType.WK : ReturnPiece.PieceType.BK;
				break;
		}
		rp.pieceFile = ReturnPiece.PieceFile.values()[piece.col];
		rp.pieceRank = 8 - piece.row;
		return rp;
	}

    public static void printBoard() {
        PlayChess.printBoard(returnPieces);
    }

    // public static void printBoard() {
    //     for (int r = 0; r < 8; r++) {
    //         for (int c = 0; c < 8; c++) {
    //             if (hasPiece[r][c]) {
    //                 System.out.print("CP");
    //             } else {
    //                 if ((r + c) % 2 == 0) {
    //                 System.out.print("  ");
    //                 } else {
    //                     System.out.print("##");
    //                 }
    //             }
    //             System.out.print(" ");
    //         }
    //         System.out.println((8 - r));
    //     }
    //     System.out.println(" a  b  c  d  e  f  g  h");
    // }
}