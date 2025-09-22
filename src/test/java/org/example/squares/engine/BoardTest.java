package org.example.squares.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void clearEmptiesCell() {
        Board b = new Board(5);
        b.place(2,2, Board.Cell.WHITE);
        assertEquals(Board.Cell.WHITE, b.get(2,2));
        b.clear(2,2);
        assertEquals(Board.Cell.EMPTY, b.get(2,2));
    }

    @Test
    void copyProducesIndependentBoard() {
        Board b = new Board(4);
        b.place(1,1, Board.Cell.BLACK);
        Board c = b.copy();
        assertEquals(Board.Cell.BLACK, c.get(1,1));
        c.clear(1,1);
        assertEquals(Board.Cell.BLACK, b.get(1,1));
        assertEquals(Board.Cell.EMPTY, c.get(1,1));
    }
}
