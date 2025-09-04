package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor turn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.turn = TeamColor.WHITE;

        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && turn == chessGame.turn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, turn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);

        if(piece != null) {
            HashSet<ChessMove> moves = (HashSet<ChessMove>) piece.pieceMoves(board, startPosition);
            HashSet<ChessMove> invalidMoves = new HashSet<ChessMove>();
            for(ChessMove move : moves) {
                if(! validateMove(move)) {
                    invalidMoves.add(move);
                }
            }

            moves.removeAll(invalidMoves);
            return moves;
        }

        return null;
    }

    /**
     * Checks if a move would leave the king in check
     * @param move the move to be tested
     * @return a boolean of if the move is valid or not
     */
    private boolean validateMove(ChessMove move) {
        ChessBoard simulatedBoard = simulateMove(move);
        return !isInCheck(simulatedBoard, turn);
    }

    /**
     * Simulates making a move without affecting the data members
     * @param move the move to simulate
     * @return the simulated chess board after the move has been made.
     */
    private ChessBoard simulateMove(ChessMove move) {
        ChessBoard simulatedBoard = new ChessBoard();

        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                ChessPosition currentPos = new ChessPosition(row, col);
                simulatedBoard.addPiece(currentPos, board.getPiece(currentPos));
            }
        }

        makeMove(simulatedBoard, move);

        return simulatedBoard;
    }

    /**
     * Makes a move on a chess board. The move may not be valid
     *
     * @param board the board on which to make the move
     * @param move the move to make
     */
    private void makeMove(ChessBoard board, ChessMove move) {
        if(move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(),
                    board.getPiece(move.getStartPosition()));
            board.addPiece(move.getStartPosition(), null);
        }
        else {
            board.addPiece(move.getEndPosition(),
                    new ChessPiece(turn, move.getPromotionPiece()));
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Checks if a team is in check on a given board
     * @param board the board to check
     * @param teamColor the team to check
     * @return True if the team is in check on the board
     */
    private boolean isInCheck(ChessBoard board, TeamColor teamColor) {

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
