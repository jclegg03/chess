package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collections;

public class BoardPrinter {
    public static void print(ChessBoard board, ChessGame.TeamColor perspective) {
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

        String string = EscapeSequences.SET_TEXT_BOLD;
        boolean isLight = true;
        for(int i = 0; i < 10; i++) {
            isLight = !isLight;
            for(int j = 0; j < 10; j++) {
                boolean isSquareOnBoard = i > 0 && i < 9 && j > 0 && j < 9;
                if(isSquareOnBoard) {
                    string += isLight ? EscapeSequences.SET_BG_COLOR_BLUE : EscapeSequences.SET_BG_COLOR_BLACK;
                    isLight = !isLight;
                    var piece = board.getPiece(ChessPosition.fromString("" + colNames.get(j - 1) + rowNums.get(i - 1)));
                    var pieceString = piece == null ? " " : piece.toString();
                    string += " " + pieceString + " ";
                }
                else {
                    string += EscapeSequences.SET_BG_COLOR_DARK_GREY;

                    boolean isCorner = (i == 0 || i == 9) && (j == 0 || j == 9);
                    if(isCorner) {
                        string += "   ";
                    }

                    boolean isRowOfColNames = (i == 0 || i == 9) && j < 9 && j > 0;
                    if(isRowOfColNames) {
                        string += " " + colNames.get(j - 1) + " ";
                    }

                    boolean isColOfRowNums = (j == 0 || j == 9) && i < 9 && i > 0;
                    if(isColOfRowNums) {
                        string += " " + rowNums.get(i - 1) + " ";
                    }

                }
            }

            string += EscapeSequences.RESET_BG_COLOR;
            string += "\n";
        }

        System.out.println(string);
    }
}
