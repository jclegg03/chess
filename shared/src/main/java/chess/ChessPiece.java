package chess;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType pieceType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }

    @Override
    public String toString() {
        String text = " ";

        if(this.pieceType == PieceType.KING) text = "k";
        else if(this.pieceType == PieceType.QUEEN) text = "q";
        else if(this.pieceType == PieceType.BISHOP) text = "b";
        else if(this.pieceType == PieceType.KNIGHT) text = "n";
        else if(this.pieceType == PieceType.ROOK) text = "r";
        else if(this.pieceType == PieceType.PAWN) text = "p";

        if(this.pieceColor == ChessGame.TeamColor.WHITE) text = text.toUpperCase();

        return text;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> validMoves = new ArrayList<ChessMove>();

        if(this.pieceType == PieceType.KING)
            kingMoves(board, myPosition, validMoves);

        return validMoves;
    }

    /**
     * Calculates the moves a king can make without considering check
     */
    private void kingMoves(ChessBoard board, ChessPosition position, ArrayList<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();
        boolean leftValid = col - 1 > 0;
        boolean rightValid = col + 1 <= 8;
        boolean upValid = row + 1 <= 8;
        boolean downValid = row - 1 > 0;

        //left
        if(leftValid) {
            ChessPosition left = new ChessPosition(row, col - 1);
            addMove(position, left, board.getPiece(left), validMoves);
        }

        //right
        if(rightValid) {
            ChessPosition right = new ChessPosition(row, col + 1);
            addMove(position, right, board.getPiece(right), validMoves);
        }

        //up
        if(upValid) {
            ChessPosition up = new ChessPosition(row + 1, col);
            addMove(position, up, board.getPiece(up), validMoves);
        }

        //down
        if(downValid) {
            ChessPosition down = new ChessPosition(row - 1, col);
            addMove(position, down, board.getPiece(down), validMoves);
        }

        //up-left
        if(upValid && leftValid) {
            ChessPosition upLeft = new ChessPosition(row + 1, col - 1);
            addMove(position, upLeft, board.getPiece(upLeft), validMoves);
        }

        //up-right
        if(upValid && rightValid) {
            ChessPosition upRight = new ChessPosition(row + 1, col + 1);
            addMove(position, upRight, board.getPiece(upRight), validMoves);
        }

        //down-left
        if(downValid && leftValid) {
            ChessPosition downLeft = new ChessPosition(row - 1, col - 1);
            addMove(position, downLeft, board.getPiece(downLeft), validMoves);
        }

        //down-right
        if(downValid && rightValid) {
            ChessPosition downRight = new ChessPosition(row - 1, col + 1);
            addMove(position, downRight, board.getPiece(downRight), validMoves);
        }
    }

    /**
     * Adds a move to the valid moves list if the new position is unoccupied or has an opposing piece.
     */
    private void addMove(ChessPosition oldPosition, ChessPosition newPosition,
                         ChessPiece otherPiece, ArrayList<ChessMove> moves) {
        if(otherPiece == null || otherPiece.pieceColor != this.pieceColor)
            moves.add(new ChessMove(oldPosition, newPosition));
    }
}
