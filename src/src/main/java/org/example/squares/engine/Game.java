package org.example.squares.engine;

import java.util.List;
import java.util.Optional;

public class Game {
    public enum Result { IN_PROGRESS, WHITE_WINS, BLACK_WINS, DRAW }

    private final Board board;
    private Board.Cell turn; // who moves next

    public Game(int size, Board.Cell first) {
        this.board = new Board(size);
        this.turn = first;
    }

    public Board getBoard() { return board; }
    public Board.Cell getTurn() { return turn; }

    public Result getResult() {
        if (SquareDetector.hasAnySquare(board, Board.Cell.WHITE)) return Result.WHITE_WINS;
        if (SquareDetector.hasAnySquare(board, Board.Cell.BLACK)) return Result.BLACK_WINS;
        if (board.isFull()) return Result.DRAW;
        return Result.IN_PROGRESS;
    }

    public void move(int x, int y) {
        if (getResult() != Result.IN_PROGRESS) throw new IllegalStateException("Game finished");
        board.place(x, y, turn);
        turn = (turn == Board.Cell.WHITE) ? Board.Cell.BLACK : Board.Cell.WHITE;
    }

    public Optional<Board.Point> computeComputerMove(Board.Cell forColor) {
        if (getResult() != Result.IN_PROGRESS) return Optional.empty();
        // 1) Win if possible
        for (Board.Point p : board.freeCells()) {
            board.place(p.x, p.y, forColor);
            boolean win = SquareDetector.hasAnySquare(board, forColor);
            undo(p);
            if (win) return Optional.of(p);
        }
        // 2) Block opponent win
        Board.Cell opp = (forColor == Board.Cell.WHITE) ? Board.Cell.BLACK : Board.Cell.WHITE;
        for (Board.Point p : board.freeCells()) {
            board.place(p.x, p.y, opp);
            boolean oppWin = SquareDetector.hasAnySquare(board, opp);
            undo(p);
            if (oppWin) return Optional.of(p);
        }
        // 3) Otherwise pick the center-most free cell (simple heuristic)
        List<Board.Point> free = board.freeCells();
        if (free.isEmpty()) return Optional.empty();
        Board.Point best = free.get(0);
        double bestScore = Double.POSITIVE_INFINITY;
        double mid = (board.getSize() - 1) / 2.0;
        for (Board.Point p : free) {
            double d = Math.hypot(p.x - mid, p.y - mid);
            if (d < bestScore) { bestScore = d; best = p; }
        }
        return Optional.of(best);
    }

    private void undo(Board.Point p) {
        // Simple undo by resetting cell to EMPTY
        try {
            java.lang.reflect.Field f = Board.class.getDeclaredField("cells");
            f.setAccessible(true);
            Board.Cell[][] cells = (Board.Cell[][]) f.get(board);
            cells[p.y][p.x] = Board.Cell.EMPTY;
        } catch (Exception e) {
            throw new IllegalStateException("Undo failed", e);
        }
    }
}
