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
     * Gets all moves a team can make
     * @param teamColor the team to check
     * @return moves the team can make. Null if there are no moves.
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
     * Makes a move on the chess board without updating turn or running any checks
     * @param move the move to make
     */
    private void simulateMove(ChessMove move) {
        if(move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(),
                    board.getPiece(move.getStartPosition()));
        }
        else {
            board.addPiece(move.getEndPosition(), new ChessPiece(turn, move.getPromotionPiece()));
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
        if(board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("There isn't a piece there. Nice try.");
        }

        if(board.getPiece(move.getStartPosition()).getTeamColor() != turn) {
            throw new InvalidMoveException("Uh its not your turn bub.");
        }

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

        if(turn == TeamColor.BLACK) {
            turn = TeamColor.WHITE;
        }
        else {
            turn = TeamColor.BLACK;
        }
    }

    /**
     * gives the position of the specified king
     * @param teamcolor the team to look for
     * @return the king's position or null if not found
     */
    private ChessPosition getKingPos(TeamColor teamcolor) {
        ChessPosition kingPos = null;

        for(int row = 1; row <= 8; row++) {
            for(int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                if(piece != null && piece.getPieceType() == ChessPiece.PieceType.KING
                    && piece.getTeamColor() == teamcolor) {
                    kingPos = pos;
                }
            }
        }

        return kingPos;
    }

    /**
     * Gives the opposing team
     * @param teamColor the team
     * @return the opposing team
     */
    private TeamColor getOtherTeam(TeamColor teamColor) {
        if(teamColor == TeamColor.BLACK) {
            return TeamColor.WHITE;
        }
        else {
            return TeamColor.BLACK;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor otherTeam = getOtherTeam(teamColor);
        ChessPosition kingPosition = getKingPos(teamColor);

        Collection<ChessMove> otherTeamMoves = getAllMoves(otherTeam);

        for(ChessMove move: otherTeamMoves) {
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
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(! isInCheck(teamColor)) {
            return false;
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
