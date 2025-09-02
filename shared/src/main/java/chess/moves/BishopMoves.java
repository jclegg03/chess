package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public final class BishopMoves {
    /**
     * Calculates the moves a bishop can make
     */
    public static void bishopMoves(ChessBoard board, ChessPosition currentPosition,
                                   ChessPiece piece, ArrayList<ChessMove> validMoves) {
        int row = currentPosition.getRow();
        int col = currentPosition.getColumn();

        //up-left
        int newRow = row + 1;
        int newCol = col - 1;

        while(
                newRow <= 8 && //not above the top of the board
                newCol >= 1    //not too far to the left
        ) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPos = board.getPiece(newPos);
            ChessPiece.addMove(currentPosition, newPos, piece, pieceAtNewPos, validMoves);

            if(pieceAtNewPos != null) break;

            newRow++;
            newCol--;
        }

        //down-right
        newRow = row - 1;
        newCol = col + 1;

        while(
                newRow >= 1 && //not below the bottom of the board
                newCol <= 8    //not too far to the right
        ) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPos = board.getPiece(newPos);
            ChessPiece.addMove(currentPosition, newPos, piece, pieceAtNewPos, validMoves);

            if(pieceAtNewPos != null) break;

            newRow--;
            newCol++;
        }

        //up-right
        newRow = row + 1;
        newCol = col + 1;

        while(
                newRow <= 8 && //not above the top of the board
                newCol <= 8    //not too far to the right
        ) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPos = board.getPiece(newPos);
            ChessPiece.addMove(currentPosition, newPos, piece, pieceAtNewPos, validMoves);

            if(pieceAtNewPos != null) break;

            newRow++;
            newCol++;
        }

        //down-left
        newRow = row - 1;
        newCol = col - 1;

        while(
                newRow >= 1 && //not below the bottom of the board
                newCol >= 1    //not too far to the left
        ) {
            ChessPosition newPos = new ChessPosition(newRow, newCol);
            ChessPiece pieceAtNewPos = board.getPiece(newPos);
            ChessPiece.addMove(currentPosition, newPos, piece, pieceAtNewPos, validMoves);

            if(pieceAtNewPos != null) break;

            newRow--;
            newCol--;
        }
    }
}
