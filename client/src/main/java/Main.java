import chess.*;
import ui.BoardPrinter;

public class Main {
    public static void main(String[] args) {
        var board = new ChessBoard();
        board.resetBoard();
        BoardPrinter.print(board, ChessGame.TeamColor.BLACK);
    }
}