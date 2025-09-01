package chess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private HashMap<ChessPosition, ChessPiece> board;

    public ChessBoard() {
        this.board = new HashMap<ChessPosition, ChessPiece>();
    }

    /**
     * Adds a chess piece to the chessboard
     * Replaces old piece
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board.put(position, piece);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        addPieces(ChessGame.TeamColor.WHITE);
        addPieces(ChessGame.TeamColor.BLACK);
    }

    private void addPieces(ChessGame.TeamColor color) {
        ChessPiece.PieceType type = ChessPiece.PieceType.ROOK;
        int row = 1;
        int pawnRow = 2;
        if(color == ChessGame.TeamColor.BLACK) {
            row = 8;
            pawnRow = 7;
        }

        addPiece(new ChessPosition(row, 1), new ChessPiece(color, type));
        addPiece(new ChessPosition(row, 8), new ChessPiece(color, type));

        type = ChessPiece.PieceType.KNIGHT;
        addPiece(new ChessPosition(row, 2), new ChessPiece(color, type));
        addPiece(new ChessPosition(row, 7), new ChessPiece(color, type));

        type = ChessPiece.PieceType.BISHOP;
        addPiece(new ChessPosition(row, 3), new ChessPiece(color, type));
        addPiece(new ChessPosition(row, 6), new ChessPiece(color, type));

        type = ChessPiece.PieceType.PAWN;
        for(int i = 1; i <= 8; i++) {
            addPiece(new ChessPosition(pawnRow, i), new ChessPiece(color, type));
        }

        addPiece(new ChessPosition(row, 4), new ChessPiece(color, ChessPiece.PieceType.QUEEN));
        addPiece(new ChessPosition(row, 5), new ChessPiece(color, ChessPiece.PieceType.KING));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(board);
    }

    @Override
    public String toString() {
        String text = "";
        for(int row = 8; row >= 1; row--) {
            for(int col = 1; col <=8; col++) {
                ChessPiece piece = board.get(new ChessPosition(row, col));
                if(piece != null) {
                    text += "|" + piece.toString() + "|";
                }
                else text += "| |";
            }
            text += "\n";
        }

        return text;
    }
}
