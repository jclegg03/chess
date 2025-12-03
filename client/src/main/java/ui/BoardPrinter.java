package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collections;

public class BoardPrinter {
    private static final ArrayList<Integer> ROW_NUMBERS = new ArrayList<Integer>();

    private static final ArrayList<Character> COL_NAMES = new ArrayList<Character>();

    public static void print(ChessBoard board, ChessGame.TeamColor perspective) {
        print(board, perspective, null, new ArrayList<>());
    }

    public static void print(ChessBoard board, ChessGame.TeamColor perspective, ChessPosition piecePos,
                             ArrayList<ChessPosition> highlight) {
        for(int i = 8; i >= 1; i--) {
            ROW_NUMBERS.add(i);
        }

        for(char c = 'a'; c <= 'h'; c++) {
            COL_NAMES.add(c);
        }

        if(perspective == ChessGame.TeamColor.BLACK) {
            Collections.reverse(ROW_NUMBERS);
            Collections.reverse(COL_NAMES);
        }

        StringBuilder string = new StringBuilder(EscapeSequences.SET_TEXT_BOLD);
        boolean isLight = true;
        for(int i = 0; i < 10; i++) {
            isLight = !isLight;
            for(int j = 0; j < 10; j++) {
                boolean isSquareOnBoard = i > 0 && i < 9 && j > 0 && j < 9;
                if(isSquareOnBoard) {
                    String bg = getBg(getPosition(i, j), isLight, piecePos, highlight);
                    string.append(bg);
                    isLight = !isLight;
                    var piece = board.getPiece(getPosition(i, j));
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
                        string.append(" ").append(COL_NAMES.get(j - 1)).append(" ");
                    }

                    boolean isColOfRowNums = (j == 0 || j == 9) && i < 9 && i > 0;
                    if(isColOfRowNums) {
                        string.append(" ").append(ROW_NUMBERS.get(i - 1)).append(" ");
                    }

                }
            }

            string.append(EscapeSequences.RESET_BG_COLOR);
            string.append("\n");
        }

        System.out.println(string);
    }

    private static String getBg(ChessPosition printingPos, boolean isLight, ChessPosition piecePos,
                                 ArrayList<ChessPosition> highlight) {
        String bg = isLight ? EscapeSequences.SET_BG_COLOR_BLUE : EscapeSequences.SET_BG_COLOR_BLACK;

        if(isHighlighted(printingPos, highlight)) {
            bg = isLight ? EscapeSequences.SET_BG_COLOR_GREEN : EscapeSequences.SET_BG_COLOR_DARK_GREEN;
        }
        bg = printingPos.equals(piecePos) ? EscapeSequences.SET_BG_COLOR_YELLOW : bg;

        return bg;
    }

    private static boolean isHighlighted(ChessPosition pos, ArrayList<ChessPosition> highlight) {
        for(var position : highlight) {
            if(position.equals(pos)) {
                return true;
            }
        }

        return false;
    }

    private static ChessPosition getPosition(int i, int j) {
        return ChessPosition.fromString("" + COL_NAMES.get(j - 1) + ROW_NUMBERS.get(i - 1));
    }
}
