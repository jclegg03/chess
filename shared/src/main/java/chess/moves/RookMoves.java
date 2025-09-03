package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public final class RookMoves {
    /**
     * Calculates the moves a rook can make
     */
    public static void rookMoves(ChessBoard board, ChessPosition position,
                                 ChessPiece piece, ArrayList<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();

        //right
        int newCol = col + 1;

        while(newCol <= 8) {
            ChessPosition newPos = new ChessPosition(row, newCol);
            ChessPiece pieceAtPos = board.getPiece(newPos);
            ChessPiece.addMove(position, newPos, piece, pieceAtPos, validMoves);

            if(pieceAtPos != null) {
                break;
            }

            newCol++;
        }

        //left
        newCol = col - 1;

        while(newCol >= 1) {
            ChessPosition newPos = new ChessPosition(row, newCol);
            ChessPiece pieceAtPos = board.getPiece(newPos);
            ChessPiece.addMove(position, newPos, piece, pieceAtPos, validMoves);

            if(pieceAtPos != null) {
                break;
            }

            newCol--;
        }

        //up
        int newRow = row + 1;

        while(newRow <= 8) {
            ChessPosition newPos = new ChessPosition(newRow, col);
            ChessPiece pieceAtPos = board.getPiece(newPos);
            ChessPiece.addMove(position, newPos, piece, pieceAtPos, validMoves);

            if(pieceAtPos != null) {
                break;
            }

            newRow++;
        }

        //down
        newRow = row - 1;

        while(newRow >= 1) {
            ChessPosition newPos = new ChessPosition(newRow, col);
            ChessPiece pieceAtPos = board.getPiece(newPos);
            ChessPiece.addMove(position, newPos, piece, pieceAtPos, validMoves);

            if(pieceAtPos != null) {
                break;
            }

            newRow--;
        }
    }
}
