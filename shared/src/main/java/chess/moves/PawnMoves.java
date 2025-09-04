package chess.moves;

import chess.*;

import java.util.HashSet;

public final class PawnMoves {
    public static void pawnMoves(ChessBoard board, ChessPosition position,
                                 ChessPiece piece, HashSet<ChessMove> validMoves) {
        int row = position.getRow();
        int col = position.getColumn();

        //black
        if(piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition newPos = new ChessPosition(row - 1, col);
            ChessPiece pieceAtNewPos = board.getPiece(newPos);
            if (pieceAtNewPos == null) {
                if(newPos.getRow() == 1) {
                    promotions(position, newPos, piece.getTeamColor(), validMoves);
                }
                else {
                    ChessPiece.addMove(position, newPos, piece, pieceAtNewPos, validMoves);
                }


                //sprint
                if (row == 7) {
                    ChessPosition sprintPos = new ChessPosition(row - 2, col);
                    ChessPiece pieceAtSprintPos = board.getPiece(sprintPos);
                    if (pieceAtSprintPos == null) {
                        ChessPiece.addMove(position, sprintPos, piece, pieceAtSprintPos, validMoves);
                    }
                }
            }

            //right take
            ChessPosition right = new ChessPosition(row - 1, col + 1);
            ChessPiece pieceAtRight = board.getPiece(right);
            if(pieceAtRight != null && pieceAtRight.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if(right.getRow() == 1) {
                    promotions(position, right, piece.getTeamColor(), validMoves);
                }
                else {
                    ChessPiece.addMove(position, right, piece, pieceAtRight, validMoves);
                }
            }

            //left take
            ChessPosition left = new ChessPosition(row - 1, col - 1);
            ChessPiece pieceAtLeft = board.getPiece(left);
            if(pieceAtLeft != null && pieceAtLeft.getTeamColor() == ChessGame.TeamColor.WHITE) {
                if(left.getRow() == 1) {
                    promotions(position, left, piece.getTeamColor(), validMoves);
                }
                else {
                    ChessPiece.addMove(position, left, piece, pieceAtLeft, validMoves);
                }
            }
        }

        //white
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition newPos = new ChessPosition(row + 1, col);
            ChessPiece pieceAtNewPos = board.getPiece(newPos);
            if(pieceAtNewPos == null) {
                if(newPos.getRow() == 8) {
                    promotions(position, newPos, piece.getTeamColor(), validMoves);
                }
                else {
                    ChessPiece.addMove(position, newPos, piece, pieceAtNewPos, validMoves);
                }

                //sprint
                if(row == 2) {
                    ChessPosition sprintPos = new ChessPosition(row + 2, col);
                    ChessPiece pieceAtSprintPos = board.getPiece(sprintPos);
                    if(pieceAtSprintPos == null) {
                        ChessPiece.addMove(position, sprintPos, piece, pieceAtSprintPos, validMoves);
                    }
                }
            }

            //right take
            ChessPosition right = new ChessPosition(row + 1, col + 1);
            ChessPiece pieceAtRight = board.getPiece(right);
            if(pieceAtRight != null && pieceAtRight.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if(right.getRow() == 8) {
                    promotions(position, right, piece.getTeamColor(), validMoves);
                }
                else {
                    ChessPiece.addMove(position, right, piece, pieceAtRight, validMoves);
                }
            }

            //left take
            ChessPosition left = new ChessPosition(row + 1, col - 1);
            ChessPiece pieceAtLeft = board.getPiece(left);
            if(pieceAtLeft != null && pieceAtLeft.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if(left.getRow() == 8) {
                    promotions(position, left, piece.getTeamColor(), validMoves);
                }
                else {
                    ChessPiece.addMove(position, left, piece, pieceAtLeft, validMoves);
                }
            }
        }
    }

    private static void promotions(ChessPosition oldPos, ChessPosition newPos,
                                   ChessGame.TeamColor teamColor, HashSet<ChessMove> validMoves) {
        ChessPiece.PieceType[] promotionPieces =
                {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT,
                        ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK};

        for(ChessPiece.PieceType type : promotionPieces) {
            validMoves.add(new ChessMove(oldPos, newPos, type));
        }
    }
}
