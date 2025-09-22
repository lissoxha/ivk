package org.example.squares.engine.strategy;

import org.example.squares.engine.Board;
import org.example.squares.engine.Game;
import org.example.squares.engine.SquareDetector;

import java.util.List;
import java.util.Optional;

public class SimpleHeuristicStrategy implements MoveStrategy {
    @Override
    public Optional<Board.Point> chooseMove(Game game, Board.Cell forColor) {
        if (game.getResult() != Game.Result.IN_PROGRESS) return Optional.empty();
        var board = game.getBoard();
        // Win now
        for (Board.Point p : board.freeCells()) {
            board.place(p.x, p.y, forColor);
            boolean win = SquareDetector.hasAnySquare(board, forColor);
            board.clear(p.x, p.y);
            if (win) return Optional.of(p);
        }
        // Block opponent
        Board.Cell opp = (forColor == Board.Cell.WHITE) ? Board.Cell.BLACK : Board.Cell.WHITE;
        for (Board.Point p : board.freeCells()) {
            board.place(p.x, p.y, opp);
            boolean oppWin = SquareDetector.hasAnySquare(board, opp);
            board.clear(p.x, p.y);
            if (oppWin) return Optional.of(p);
        }
        // Center preference
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
}
