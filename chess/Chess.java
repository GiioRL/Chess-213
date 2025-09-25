package chess;

import java.util.ArrayList;

public class Chess {

        enum Player { white, black }
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
	public static ReturnPlay play(String move) {

		/* FILL IN THIS METHOD */
		
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		return null;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		System.out.println("holy crap chat we're about to play chess\n");

		int i = 0;
		Piece.Type type;
		Piece.Player player;
		while (i < 32 ) {
			while (i < 16) {
				player = Piece.Player.white;
				if (i < 8) {
					type = Piece.Type.pawn;
					pieces[i] = new Piece(type, player, 6, i);
				} else {
					switch (i) {
						case 8:
						case 9:
							type = Piece.Type.rook;
							break;
						case 10:
						case 11:
							type = Piece.Type.knight;
							break;
						case 12:
						case 13:
							type = Piece.Type.bishop;
							break;
						case 14:
							type = Piece.Type.queen;
							break;
						default:
							type = Piece.Type.king;
					}
					pieces[i] = new Piece(type, player, 7, i - 8);
				}
				i++;
			}
			player = Piece.Player.black;
			if (i < 24) {
				type = Piece.Type.pawn;
				pieces[i] = new Piece(type, player, 1, i - 16);
			} else {
				switch (i) {
					case 24:
					case 25:
						type = Piece.Type.rook;
						break;
					case 26:
					case 27:
						type = Piece.Type.knight;
						break;
					case 28:
					case 29:
						type = Piece.Type.bishop;
						break;
					case 30:
						type = Piece.Type.queen;
						break;
					default:
						type = Piece.Type.king;	
				}
				pieces[i] = new Piece(type, player, 0, i - 24);
			}
			i++;
		}
		for (Piece piece : pieces) {
			Board.placePiece(piece);
		}
		Board.printBoard();
	}
}
