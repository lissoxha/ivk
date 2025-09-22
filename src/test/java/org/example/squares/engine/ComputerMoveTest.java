package org.example.squares.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComputerMoveTest {

    @Test
    void winsIfPossible() {
        Game g = new Game(6, Board.Cell.WHITE);
        // Three corners present; putting the fourth should be suggested
        g.getBoard().place(1,1, Board.Cell.WHITE);
        g.getBoard().place(4,1, Board.Cell.WHITE);
        g.getBoard().place(1,4, Board.Cell.WHITE);
        var mv = g.computeComputerMove(Board.Cell.WHITE);
        assertTrue(mv.isPresent());
        assertEquals(4, mv.get().x);
        assertEquals(4, mv.get().y);
    }

    @Test
    void blocksOpponentsImmediateWin() {
        Game g = new Game(6, Board.Cell.WHITE);
        // Opponent BLACK threatens square
        g.getBoard().place(2,2, Board.Cell.BLACK);
        g.getBoard().place(5,2, Board.Cell.BLACK);
        g.getBoard().place(2,5, Board.Cell.BLACK);
        var mv = g.computeComputerMove(Board.Cell.WHITE);
        assertTrue(mv.isPresent());
        // Best block is (5,5)
        assertEquals(5, mv.get().x);
        assertEquals(5, mv.get().y);
    }
}
