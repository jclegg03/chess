package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.HashSet;

public final class QueenMoves {
    public static void queenMoves(ChessBoard board, ChessPosition position,
                                  ChessPiece piece, HashSet<ChessMove> validMoves) {
        RookMoves.rookMoves(board, position, piece, validMoves);
        BishopMoves.bishopMoves(board, position, piece, validMoves);
    }
}
