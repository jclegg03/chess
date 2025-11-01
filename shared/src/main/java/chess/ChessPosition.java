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
    private Column col;

    public enum Column {
        a(1), b(2), c(3), d(4), e(5), f(6), g(7), h(8);

        private final int col;

        Column(int col) {
            this.col = col;
        }

        public int getCol() {
            return col;
        }
    }

    public ChessPosition(int row, int col) {
        this.row = row;
        for(Column name : Column.values()) {
            if(name.getCol() == col) {
                this.col = name;
                break;
            }
        }
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
        return this.col.getCol();
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
        return col.name() + row;
    }

    public static ChessPosition fromString(String key) {
        String colString = key.substring(0, 1);
        int row = Integer.parseInt(key.substring(1));
        int col = 0;
        for(Column name : Column.values()) {
            if(name.name().equals(colString)) {
                col = name.getCol();
                break;
            }
        }
        return new ChessPosition(row, col);
    }
}
