package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collections;

public class BoardPrinter {
    public static void print(ChessBoard board, ChessGame.TeamColor perspective) {
        print(board, perspective, new ArrayList<>());
    }

    public static void print(ChessBoard board, ChessGame.TeamColor perspective, ArrayList<ChessPosition> highlight) {
        var rowNums = new ArrayList<Integer>();
        for(int i = 8; i >= 1; i--) {
            rowNums.add(i);
        }

        var colNames = new ArrayList<Character>();
        for(char c = 'a'; c <= 'h'; c++) {
            colNames.add(c);
        }

        if(perspective == ChessGame.TeamColor.BLACK) {
            Collections.reverse(rowNums);
            Collections.reverse(colNames);
        }

        StringBuilder string = new StringBuilder(EscapeSequences.SET_TEXT_BOLD);
        boolean isLight = true;
        for(int i = 0; i < 10; i++) {
            isLight = !isLight;
            for(int j = 0; j < 10; j++) {
                boolean isSquareOnBoard = i > 0 && i < 9 && j > 0 && j < 9;
                if(isSquareOnBoard) {
                    String bg = isLight ? EscapeSequences.SET_BG_COLOR_BLUE : EscapeSequences.SET_BG_COLOR_BLACK;
                    bg = isHighlighted("" + colNames.get(j - 1) + rowNums.get(i - 1), highlight) ?
                            EscapeSequences.SET_BG_COLOR_GREEN : bg;
                    string.append(bg);
                    isLight = !isLight;
                    var piece = board.getPiece(ChessPosition.fromString("" + colNames.get(j - 1) + rowNums.get(i - 1)));
                    var pieceString = piece == null ? " " : piece.toString();
                    string.append(" ").append(pieceString).append(" ");
                }
                else {
                    string.append(EscapeSequences.SET_BG_COLOR_DARK_GREY);

                    boolean isCorner = (i == 0 || i == 9) && (j == 0 || j == 9);
                    if(isCorner) {
                        string.append("   ");
                    }

                    boolean isRowOfColNames = (i == 0 || i == 9) && j < 9 && j > 0;
                    if(isRowOfColNames) {
                        string.append(" ").append(colNames.get(j - 1)).append(" ");
                    }

                    boolean isColOfRowNums = (j == 0 || j == 9) && i < 9 && i > 0;
                    if(isColOfRowNums) {
                        string.append(" ").append(rowNums.get(i - 1)).append(" ");
                    }

                }
            }

            string.append(EscapeSequences.RESET_BG_COLOR);
            string.append("\n");
        }

        System.out.println(string);
    }

    private static boolean isHighlighted(String positionString, ArrayList<ChessPosition> highlight) {
        var pos = ChessPosition.fromString(positionString);
        for(var position : highlight) {
            if(position.equals(pos)) {
                return true;
            }
        }

        return false;
    }
}
