package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int row;
    private int col;

    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.col;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }

    @Override
    public String toString() {
        String string = "";
        if(col == 1) {
            string = "a" + row;
        }
        else if(col == 2) {
            string = "b" + row;
        }
        else if(col == 3) {
            string = "c" + row;
        }
        else if(col == 4) {
            string = "d" + row;
        }
        else if(col == 5) {
            string = "e" + row;
        }
        else if(col == 6) {
            string = "f" + row;
        }
        else if(col == 7) {
            string = "g" + row;
        }
        else {
            string = "h" + row;
        }
        return string;
    }
}
