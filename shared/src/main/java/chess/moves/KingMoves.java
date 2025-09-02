package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public final class KingMoves {
    /**
     * Calculates the moves a king can make without considering check
     */
    public static void kingMoves(ChessBoard board, ChessPosition position,
                                 ChessPiece piece, ArrayList<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();
        boolean leftValid = col - 1 > 0;
        boolean rightValid = col + 1 <= 8;
        boolean upValid = row + 1 <= 8;
        boolean downValid = row - 1 > 0;

        //left
        if(leftValid) {
            ChessPosition left = new ChessPosition(row, col - 1);
            ChessPiece.addMove(position, left, piece, board.getPiece(left), validMoves);
        }

        //right
        if(rightValid) {
            ChessPosition right = new ChessPosition(row, col + 1);
            ChessPiece.addMove(position, right, piece, board.getPiece(right), validMoves);
        }

        //up
        if(upValid) {
            ChessPosition up = new ChessPosition(row + 1, col);
            ChessPiece.addMove(position, up, piece, board.getPiece(up), validMoves);
        }

        //down
        if(downValid) {
            ChessPosition down = new ChessPosition(row - 1, col);
            ChessPiece.addMove(position, down, piece, board.getPiece(down), validMoves);
        }

        //up-left
        if(upValid && leftValid) {
            ChessPosition upLeft = new ChessPosition(row + 1, col - 1);
            ChessPiece.addMove(position, upLeft, piece, board.getPiece(upLeft), validMoves);
        }

        //up-right
        if(upValid && rightValid) {
            ChessPosition upRight = new ChessPosition(row + 1, col + 1);
            ChessPiece.addMove(position, upRight, piece, board.getPiece(upRight), validMoves);
        }

        //down-left
        if(downValid && leftValid) {
            ChessPosition downLeft = new ChessPosition(row - 1, col - 1);
            ChessPiece.addMove(position, downLeft, piece, board.getPiece(downLeft), validMoves);
        }

        //down-right
        if(downValid && rightValid) {
            ChessPosition downRight = new ChessPosition(row - 1, col + 1);
            ChessPiece.addMove(position, downRight, piece, board.getPiece(downRight), validMoves);
        }
    }
}
