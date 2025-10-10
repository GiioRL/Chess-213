package chess;

import java.util.ArrayList;

public class Board {
    // static Piece[][] board = new Piece[8][8];
    // static boolean[][] hasPiece = new boolean[8][8];
    // static ArrayList<ReturnPiece> returnPieces = new ArrayList<ReturnPiece>();
    // static Piece.Player player = Piece.Player.white;
    static Piece[][] board;
    static boolean[][] hasPiece;
    static ArrayList<ReturnPiece> returnPieces = new ArrayList<ReturnPiece>();
    static Piece.Player player;

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

    public static int removePiece(Piece piece) { // is this concerning
        int row = piece.row;
        int col = piece.col;
        if (removePiece(row, col) != null) {
            return 1;
        }
        return 0;
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

    public static int[][] findEdges(int row, int col, Piece.MoveType movetype) {
        // for (MoveType movetype : moveTypes) {
            int newRow = -1;
            int newCol = -1;
            int[][] squares = null;
            if (movetype == Piece.MoveType.vertical) {
                squares = new int[2][2];
                newRow = row;
                newCol = col;
                while (newRow > 0) {
                    if (Board.hasPiece[--newRow][newCol]) {
                        break;
                    }
                }
                squares[0][0] = newRow;
                squares[0][1] = newCol;
                newRow = row;
                while (newRow < 7) {
                    if (Board.hasPiece[++newRow][newCol]) {
                        break;
                    }
                }
                squares[1][0] = newRow;
                squares[1][1] = newCol;
            } else if (movetype == Piece.MoveType.horizontal) {
                squares = new int[2][2];
                newRow = row;
                newCol = col;
                while (newCol > 0) {
                    if (Board.hasPiece[newRow][--newCol]) {
                        break;
                    }
                }
                squares[0][0] = newRow;
                squares[0][1] = newCol;
                newCol = col;
                while (newCol < 7) {
                    if (Board.hasPiece[newRow][++newCol]) {
                        break;
                    }
                }
                squares[1][0] = newRow;
                squares[1][1] = newCol;
            } else if (movetype == Piece.MoveType.diagonal) { // and check the other direction
                squares = new int[4][2];
                int min = Math.min(row, col);
                int max = Math.min(7-row, 7-col);
                newRow = row;
                newCol = col;
                while (newRow > row-min) {
                    if (Board.hasPiece[--newRow][--newCol]) {
                        break;
                    }
                }
                squares[0][0] = newRow;
                squares[0][1] = newCol;
                newRow = row;
                newCol = col;
                while (newRow < row+max) {
                    // System.out.println("newRow: " + newRow + "\nnewCol: " + newCol);
                    if (Board.hasPiece[++newRow][++newCol]){
                        break;
                    }
                }
                squares[1][0] = newRow;
                squares[1][1] = newCol;
                min = Math.min(row, 7-col);
                max = Math.min(7-row, col);
                newRow = row;
                newCol = col;
                while (newRow > row-min) {
                    if (Board.hasPiece[--newRow][++newCol]) {
                        break;
                    }
                }
                squares[2][0] = newRow;
                squares[2][1] = newCol;
                newRow = row;
                newCol = col;
                while (newRow < row+max) {
                    if (Board.hasPiece[++newRow][--newCol]) {
                        break;
                    }
                }
                squares[3][0] = newRow;
                squares[3][1] = newCol;
            }
            return squares;
        // }
    }

    public static int[][] getPath(int row, int col, int newRow, int newCol, Piece.MoveType movetype) { // return IN ORDER the squares in between this piece's square and target square
        // System.out.println("row: " + row + "\ncol: " + col + "\nnewRow: " + newRow + "\nnewCol: " + newCol);
        int[][] squares = null;
        // int lo = -1; // kill
        // int hi = -1; //kill
        int i = 0;
        boolean bool = false;
        int tempRow = -1;
        int tempCol = -1;
        if (movetype == Piece.MoveType.vertical) {
            squares = new int[Math.abs(row - newRow) - 1][2];
            if (row < newRow) {
                bool = true;
                tempRow = row + 1;
            } else {
                bool = false;
                tempRow = row - 1;
            }
            // System.out.println("lo: " + lo + "hi: " + hi);
            tempCol = col;

            if (bool) {
                while (row != newRow) {
                    squares[i][0] = tempRow++;
                    squares[i++][1] = tempCol;
                }
            } else {
                while (tempRow != newRow) {
                    squares[i][0] = tempRow--;
                    squares[i++][1] = tempCol;
                }
            }

        } else if (movetype == Piece.MoveType.horizontal) {
            squares = new int[Math.abs(col - newCol) - 1][2];
            if (col < newCol) {
                bool = true;
                tempCol = col + 1;
            } else {
                bool = false;
                tempCol = col - 1;
            }
            // System.out.println("lo: " + lo + "hi: " + hi);
            tempRow = row;
            if (bool) {
                while (tempCol != newCol) {
                    squares[i][0] = tempRow;
                    squares[i++][1] = tempCol++;
                }
            } else {
                while (tempCol != newCol) {
                    squares[i][0] = tempRow;
                    squares[i++][1] = tempCol--;
                }
            }
        } else if (movetype == Piece.MoveType.diagonal) {
            squares = new int[Math.abs(row - newRow) - 1][2];
            boolean bool2 = false;
            int temp = -1;
            if (row < newRow) {
                bool = true;
                tempRow = row + 1;
            } else {
                bool = false;
                tempRow = row - 1;
            }
            if (col < newCol) {
                bool2 = true;
                tempCol = col + 1;
            } else {
                bool2 = false;
                tempCol = col - 1;
            }
            // System.out.println("lo: " + lo + "\nhi: " + hi + "\nbool: " + bool);

            // lo++;
            // if (bool) {
            //     temp++;
            //     while (lo < hi) {
            //         squares[i][0] = lo++;
            //         squares[i++][1] = temp++;
            //     }
            // } else {
            //     temp--;
            //     while (lo < hi) {
            //         squares[i][0] = lo++;
            //         squares[i++][1] = temp--;
            //     } 
            // }
            if (bool) {
                if (bool2) {
                    while (tempRow != newRow) {
                        squares[i][0] = tempRow++;
                        squares[i++][1] = tempCol++;
                    }
                } else {
                    while (tempRow != newRow) {
                        squares[i][0] = tempRow++;
                        squares[i++][1] = tempCol--;
                    }
                }
            } else {
                if (bool2) {
                    while (tempRow != newRow) {
                        squares[i][0] = tempRow--;
                        squares[i++][1] = tempCol++;
                    }
                } else {
                    while (tempRow != newRow) {
                        squares[i][0] = tempRow--;
                        squares[i++][1] = tempCol--;
                    }
                }
            }
        }
        return squares;
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

    public static void reset() {
        board = new Piece[8][8];
        hasPiece = new boolean[8][8];
        returnPieces.clear();
        player = Piece.Player.white;
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