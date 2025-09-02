package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public final class QueenMoves {
    public static void queenMoves(ChessBoard board, ChessPosition position,
                                  ChessPiece piece, ArrayList<ChessMove> validMoves) {
        RookMoves.rookMoves(board, position, piece, validMoves);
        BishopMoves.bishopMoves(board, position, piece, validMoves);
    }
}
