package org.example.squares.engine.strategy;

import org.example.squares.engine.Board;
import org.example.squares.engine.Game;

import java.util.Optional;

public interface MoveStrategy {
    Optional<Board.Point> chooseMove(Game game, Board.Cell color);
}
