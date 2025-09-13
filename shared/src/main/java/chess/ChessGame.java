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
    private ChessPosition whiteKingPos;
    private ChessPosition blackKingPos;

    public ChessGame() {
        this.board = new ChessBoard();
        this.turn = TeamColor.WHITE;

        this.board.resetBoard();
        this.whiteKingPos = new ChessPosition(1, 5);
        this.blackKingPos = new ChessPosition(8, 5);
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
     * Calculates all valid moves for the given team
     * @param teamColor the team to find moves for
     * @return a collection containing all the valid moves for that color.
     */
    private Collection<ChessMove> getAllValidMoves(ChessBoard board, TeamColor teamColor) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();

        //iterate through the entire board
        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);

                //check if the piece is the correct team and add its moves
                if(piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = piece.pieceMoves(board, position);
                    if(validMoves != null) {
                        moves.addAll(validMoves);
                    }
                }

                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    if(piece.getTeamColor() == TeamColor.BLACK) {
                        blackKingPos = new ChessPosition(row, col);
                    }
                    else {
                        whiteKingPos = new ChessPosition(row, col);
                    }
                }
            }
        }

        return moves;
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
            TeamColor team = piece.getTeamColor();
            Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
            HashSet<ChessMove> invalidMoves = new HashSet<>();

            for(ChessMove move : moves) {
                ChessBoard boardBeforeMove = board.clone();
                simulateMove(move);
                if(isInCheck(team)) {
                    invalidMoves.add(move);
                }
                board = boardBeforeMove;
            }

            moves.removeAll(invalidMoves);

            return moves;
        }

        return null;
    }

    /**
     * Checks if a move would leave the king in check
     * @param move the move to be tested
     * @return true if the move is valid
     */
    private Collection<ChessMove> getAllMoves(TeamColor teamColor) {
        Collection<ChessMove> allMoves = null;

        for(int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                TeamColor pieceColor = null;

                if (piece != null) {
                    pieceColor = piece.getTeamColor();
                }

                if (pieceColor == teamColor) {
                    Collection<ChessMove> moves = piece.pieceMoves(board, pos);
                    if (allMoves == null && moves != null) {
                        allMoves = moves;
                    } else if (allMoves != null && moves != null) {
                        allMoves.addAll(moves);
                    }
                }
            }
        }

        return allMoves;
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
        }
        else {
            board.addPiece(move.getEndPosition(),
                    new ChessPiece(turn, move.getPromotionPiece()));
        }

        board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        TeamColor pieceColor = board.getPiece(move.getStartPosition()).getTeamColor();
        if(pieceColor != turn) {
            throw new InvalidMoveException("Uh, its not your turn bub.");
        }

        Collection<ChessMove> moves = validMoves(move.getStartPosition());
        if(moves != null) {
            for (ChessMove validMove : moves) {
                if (validMove.equals(move)) {
                    makeMove(board, move);

        ChessBoard boardBeforeMove = board.clone();
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        simulateMove(move);
        if(isInCheck(turn)) {
            board = boardBeforeMove;
            throw new InvalidMoveException("You must construct additional pylons, er I mean protect your king!");
        }


        if(! validMoves.contains(move)) {
            board = boardBeforeMove;
            throw new InvalidMoveException("Woah there! That isn't legal and the " +
                    "programmer is too lazy to tell you why.");
        }

                    //update team turn
                    if(turn == TeamColor.WHITE) {
                        turn = TeamColor.BLACK;
                    }
                    else {
                        turn = TeamColor.WHITE;
                    }
                    return;
                }
            }
        }
        else {
            throw new InvalidMoveException(move + " is invalid because there is no piece at "
                    + move.getStartPosition());
        }
    }

    /**
     * Checks if a team is in check on a given board
     * @param board the board to check
     * @param teamColor the team to check
     * @return True if the team is in check on the board
     */
    private boolean isInCheck(ChessBoard board, TeamColor teamColor) {
        ChessPosition kingPosition;
        TeamColor otherTeam;
        if(teamColor == TeamColor.BLACK) {
            kingPosition = blackKingPos;
            otherTeam = TeamColor.WHITE;
        }
        else {
            kingPosition = whiteKingPos;
            otherTeam = TeamColor.BLACK;
        }

        Collection<ChessMove> moves = getAllValidMoves(board, otherTeam);

        //check if any of the moves would capture the king
        for(ChessMove move : moves) {
            if(move.getEndPosition().equals(kingPosition)) {
                return true;
            }
        }

        return false;
    }

    private boolean canMakeMoves(TeamColor teamColor) {
        Collection<ChessMove> moves = getAllMoves(teamColor);

        for(ChessMove move : moves) {
            ChessPiece pieceAtEndPos = board.getPiece(move.getEndPosition());
            ChessPiece pieceAtStartPos = board.getPiece(move.getStartPosition());

            //make the move
            simulateMove(move);

            //assess if they are still in check
            boolean inCheck = isInCheck(teamColor);

            //undo the move
            board.addPiece(move.getStartPosition(), pieceAtStartPos);
            board.addPiece(move.getEndPosition(), pieceAtEndPos);


            if(! inCheck) {
                return false;
            }
        }

        return true;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(board, teamColor);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(teamColor == TeamColor.BLACK) {
            return isInCheck(teamColor) && getAllValidMoves(board, TeamColor.BLACK).isEmpty();
        }

        return canMakeMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(isInCheck(teamColor)) {
            return false;
        }

        return canMakeMoves(teamColor);
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
