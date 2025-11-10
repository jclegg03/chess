import chess.*;
import ui.BoardPrinter;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to chess! Type help for a list of commands, or quit to exit.");
        var board = new ChessBoard();
        board.resetBoard();
        BoardPrinter.print(board, ChessGame.TeamColor.BLACK);
    }
}