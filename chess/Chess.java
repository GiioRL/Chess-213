package chess;

public class Chess {

        enum Player { white, black } // chat i have not been using this at all.. feel free to convert to this one
		static Piece[] pieces = new Piece[32];
    
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) { //check if input is valid

		String[] squares = move.split(" ");
		ReturnPlay rp = new ReturnPlay(); // maybe we instantiate it here
		rp.piecesOnBoard = Board.returnPieces; // update if legal move is made wait this is an object it auto updates right

		if (squares.length < 1 || squares.length > 3) // definitely illegal
			rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
		if (squares.length == 1) // Resign is the only legal move with length 1
		{
			if (squares[0].equalsIgnoreCase("resign"))
				rp.message = (Board.player == Piece.Player.white ? ReturnPlay.Message.RESIGN_BLACK_WINS : ReturnPlay.Message.RESIGN_WHITE_WINS);
			else
				rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
		}
		else if (squares.length == 3 && !squares[2].equalsIgnoreCase("draw?")) // doesn't take into account pawn promotion which is also length 3
			rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
		 else if (!Board.validSquare(squares[0]) || !Board.validSquare(squares[1])) { //fake square
			rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
		} else if (Board.getPiece(squares[0]) == null) { // no piece exists on square
			System.out.println("no piece there bro");
			rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
		} else { // legal move (not yet legal but heres the legal move code)
			Piece piece = Board.getPiece(squares[0]);
			if (piece.move(squares[1], rp) == -1) {
				rp.message = ReturnPlay.Message.ILLEGAL_MOVE;
			}
			else if (squares.length == 3 && squares[2].equalsIgnoreCase("draw?"))
				rp.message = ReturnPlay.Message.DRAW;
		}

		return rp;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		// System.out.println("holy crap chat we're about to play chess\n");
		Board.reset();
		int i = 0;
		Piece.Player player;
		while (i < 32 ) {
			while (i < 16) {
				player = Piece.Player.white;
				if (i < 8) {
					pieces[i] = new Pawn(player, 6, i);
				} else {
					switch (i) {
						case 8:
						case 15:
							pieces[i] = new Rook(player, 7, i - 8);
							break;
						case 9:
						case 14:
							pieces[i] = new Knight(player, 7, i - 8);
							break;
						case 10:
						case 13:
							pieces[i] = new Bishop(player, 7, i - 8);
							break;
						case 11:
							pieces[i] = new Queen(player, 7, i - 8);
							break;
						case 12:
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
					case 31:
						pieces[i] = new Rook(player, 0, i - 24);
						break;
					case 25:
					case 30:
						pieces[i] = new Knight(player, 0, i - 24);
						break;
					case 26:
					case 29:
						pieces[i] = new Bishop(player, 0, i - 24);
						break;
					case 27:
						pieces[i] = new Queen(player, 0, i - 24);
						break;
					case 28:
						pieces[i] = new King(player, 0, i - 24);
				}
			}
			i++;
		}
		for (Piece piece : pieces) {
			Board.placePiece(piece);
			// returnPieces.add(makeReturnPiece(piece));
		}
		Board.printBoard();
		// PlayChess.printBoard(returnPieces);
	}
}
