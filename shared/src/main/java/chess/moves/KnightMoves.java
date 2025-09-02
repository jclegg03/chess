package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class KnightMoves {
    /**
     * Calculates the moves a knight can make
     */
    public static void knightMoves(ChessBoard board, ChessPosition position,
                                   ChessPiece piece, ArrayList<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();
        boolean up2 = row + 2 <= 8;
        boolean up = row + 1 <= 8;
        boolean down2 = row -2 >= 1;
        boolean down = row - 1 >= 1;
        boolean left2 = col - 2 >= 1;
        boolean left = col - 1 >= 1;
        boolean right2 = col + 2 <= 8;
        boolean right = col + 1 <= 8;

        ChessPosition newPos;

        //up2
        if(up2) {
            if(right) {
                newPos = new ChessPosition(row + 2, col + 1);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);
            }
            if(left) {
                newPos = new ChessPosition(row + 2, col - 1);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);
            }
        }

        //left2
        if(left2) {
            if(up) {
                newPos = new ChessPosition(row + 1, col - 2);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);
            }
            if(down) {
                newPos = new ChessPosition(row - 1, col - 2);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);
            }
        }

        //right2
        if(right2) {
            if(up) {
                newPos = new ChessPosition(row + 1, col + 2);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);
            }
            if(down) {
                newPos = new ChessPosition(row - 1, col + 2);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);
            }
        }

        //down2
        if(down2) {
            if(right) {
                newPos = new ChessPosition(row - 2, col + 1);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);
            }
            if(left) {
                newPos = new ChessPosition(row - 2, col - 1);
                ChessPiece.addMove(position, newPos, piece, board.getPiece(newPos), validMoves);

            }
        }
    }
}
