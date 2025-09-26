package chess;

import java.util.ArrayList;

public class Chess {

        enum Player { white, black }
		static Piece[] pieces = new Piece[32];
		static ArrayList<ReturnPiece> returnPieces = new ArrayList<ReturnPiece>();
    
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {

		String[] squares = move.split(" ");
		ReturnPlay rp = new ReturnPlay(); // maybe we instantiate it here
		rp.piecesOnBoard = returnPieces; // update if legal move is made

		if (squares.length != 2) { // illegal input
			rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
		} else if (Board.getPiece(squares[0]) == null) { // no piece exists on square
			rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
		} else { // legal move (not yet legal but heres the legal move code)
			Piece piece = Board.getPiece(squares[0]);
			piece.move(squares[1]); // coord converter
		}

		return rp;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		// System.out.println("holy crap chat we're about to play chess\n");
		int i = 0;
		Piece.Type type;
		Piece.Player player;
		while (i < 32 ) {
			while (i < 16) {
				player = Piece.Player.white;
				if (i < 8) {
					pieces[i] = new Pawn(player, 6, i);
				} else {
					switch (i) {
						case 8:
						case 9:
							pieces[i] = new Rook(player, 7, i - 8);
							break;
						case 10:
						case 11:
							pieces[i] = new Knight(player, 7, i - 8);
							break;
						case 12:
						case 13:
							pieces[i] = new Bishop(player, 7, i - 8);
							break;
						case 14:
							pieces[i] = new Queen(player, 7, i - 8);
							break;
						default:
							pieces[i] = new King(player, 7, i - 8);
					}
				}
				i++;
			}
			player = Piece.Player.black;
			if (i < 24) {
				pieces[i] = new Pawn(player, 1, i - 16);
			} else {
				switch (i) {
					case 24:
					case 25:
						pieces[i] = new Rook(player, 0, i - 24);
						break;
					case 26:
					case 27:
						pieces[i] = new Knight(player, 0, i - 24);
						break;
					case 28:
					case 29:
						pieces[i] = new Bishop(player, 0, i - 24);
						break;
					case 30:
						pieces[i] = new Queen(player, 0, i - 24);
						break;
					default:
						pieces[i] = new King(player, 0, i - 24);
				}
			}
			i++;
		}
		for (Piece piece : pieces) {
			Board.placePiece(piece);
			returnPieces.add(makeReturnPiece(piece));
		}
		// Board.printBoard();
		PlayChess.printBoard(returnPieces);
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
}
