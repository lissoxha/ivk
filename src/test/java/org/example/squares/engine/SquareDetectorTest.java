package org.example.squares.engine;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SquareDetectorTest {

    @Test
    void detectsAxisAlignedSquare() {
        Game g = new Game(5, Board.Cell.WHITE);
        g.getBoard().place(1,1, Board.Cell.WHITE);
        g.getBoard().place(3,1, Board.Cell.WHITE);
        g.getBoard().place(1,3, Board.Cell.WHITE);
        g.getBoard().place(3,3, Board.Cell.WHITE);
        assertEquals(Game.Result.WHITE_WINS, g.getResult());
    }

    @Test
    void detectsRotatedSquare() {
        Game g = new Game(7, Board.Cell.WHITE);
        // Diamond with center at (3,3), radius 2 (rotated square)
        g.getBoard().place(3,1, Board.Cell.BLACK);
        g.getBoard().place(1,3, Board.Cell.BLACK);
        g.getBoard().place(5,3, Board.Cell.BLACK);
        g.getBoard().place(3,5, Board.Cell.BLACK);
        assertEquals(Game.Result.BLACK_WINS, g.getResult());
    }

    @Test
    void drawOnFullBoard() {
        Game g = new Game(3, Board.Cell.WHITE);
        // Fill the board without any 4 of same color making a square
        Board.Cell[][] p = {
                {Board.Cell.WHITE, Board.Cell.BLACK, Board.Cell.WHITE},
                {Board.Cell.BLACK, Board.Cell.WHITE, Board.Cell.BLACK},
                {Board.Cell.WHITE, Board.Cell.BLACK, Board.Cell.WHITE}
        };
        for (int y=0;y<3;y++) for (int x=0;x<3;x++) g.getBoard().place(x,y,p[y][x]);
        assertEquals(Game.Result.DRAW, g.getResult());
    }
}
