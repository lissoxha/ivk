package org.example.squares.api;

import org.example.squares.engine.Board;
import org.example.squares.engine.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public class ApiApplication {
    public static void main(String[] args) { SpringApplication.run(ApiApplication.class, args); }
}

@RestController
@RequestMapping("/api")
class GameController {
    @PostMapping("/next-move")
    public ResponseEntity<?> nextMove(@RequestBody GameState state) {
        Game game = state.toGame();
        Optional<Board.Point> mv = game.computeComputerMove(state.turn);
        return mv.<ResponseEntity<?>>map(point -> ResponseEntity.ok(Map.of("x", point.x, "y", point.y)))
                .orElseGet(() -> ResponseEntity.badRequest().body(Map.of("error", "No move")));
    }

    @PostMapping("/status")
    public Map<String, Object> status(@RequestBody GameState state) {
        Game g = state.toGame();
        Game.Result r = g.getResult();
        return Map.of("result", r.name());
    }
}

class GameState {
    public int size;
    public String[] rows; // strings of '.', 'W', 'B'
    public Board.Cell turn;

    Game toGame() {
        Game game = new Game(size, turn);
        for (int y = 0; y < size; y++) {
            String row = rows[y];
            for (int x = 0; x < size; x++) {
                char ch = row.charAt(x);
                if (ch == 'W') game.getBoard().place(x, y, Board.Cell.WHITE);
                else if (ch == 'B') game.getBoard().place(x, y, Board.Cell.BLACK);
            }
        }
        return game;
    }
}
