package chess;

import chess.moves.BishopMoves;
import chess.moves.KingMoves;

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
            KingMoves.kingMoves(board, myPosition, this, validMoves);
        if(this.pieceType == PieceType.BISHOP)
            BishopMoves.bishopMoves(board, myPosition, this, validMoves);

        return validMoves;
    }

    /**
     * Adds a move to the valid moves list if the new position is unoccupied or has an opposing piece.
     */
    public static void addMove(ChessPosition oldPosition, ChessPosition newPosition,
                         ChessPiece piece, ChessPiece otherPiece, ArrayList<ChessMove> moves) {
        if(otherPiece == null || otherPiece.pieceColor != piece.pieceColor)
            moves.add(new ChessMove(oldPosition, newPosition));
    }
}