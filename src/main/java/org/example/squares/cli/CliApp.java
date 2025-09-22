package org.example.squares.cli;

import org.example.squares.engine.Board;
import org.example.squares.engine.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.Optional;

public class CliApp {
    private enum PlayerType { USER, COMP }

    private static class Players {
        PlayerType p1; Board.Cell c1;
        PlayerType p2; Board.Cell c2;
        Board.Cell first() { return c1; }
    }

    private Game game;
    private Players players;

    public static void main(String[] args) throws Exception { new CliApp().run(); }

    public void run() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line;
        System.out.println("Type HELP for commands");
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            if (cmd(line, "EXIT")) { break; }
            else if (cmd(line, "HELP")) { printHelp(); }
            else if (line.startsWith("GAME")) { startGame(line); }
            else if (line.startsWith("MOVE")) { move(line); }
            else { System.out.println("Incorrect command"); }
            maybeAutoMove();
        }
    }

    private void startGame(String line) {
        try {
            String args = line.substring(4).trim();
            if (args.startsWith("")) args = args.replaceFirst("^[ ,]+", "");
            String[] parts = args.split(",");
            int n = Integer.parseInt(parts[0].trim());
            String u1 = parts[1].trim();
            String u2 = parts[2].trim();
            players = parsePlayers(u1, u2);
            game = new Game(n, players.first());
            System.out.println("New game started");
        } catch (Exception e) {
            System.out.println("Incorrect command");
        }
    }

    private Players parsePlayers(String u1, String u2) {
        Players p = new Players();
        String[] a = u1.split(" ");
        p.p1 = a[0].equalsIgnoreCase("user") ? PlayerType.USER : PlayerType.COMP;
        p.c1 = a[1].equalsIgnoreCase("W") ? Board.Cell.WHITE : Board.Cell.BLACK;
        a = u2.split(" ");
        p.p2 = a[0].equalsIgnoreCase("user") ? PlayerType.USER : PlayerType.COMP;
        p.c2 = a[1].equalsIgnoreCase("W") ? Board.Cell.WHITE : Board.Cell.BLACK;
        return p;
    }

    private void move(String line) {
        if (game == null) { System.out.println("Incorrect command"); return; }
        try {
            String args = line.substring(4).trim();
            String[] parts = args.split(",");
            int x = Integer.parseInt(parts[0].trim());
            int y = Integer.parseInt(parts[1].trim());
            game.move(x, y);
            announceResult();
        } catch (Exception e) {
            System.out.println("Incorrect command");
        }
    }

    private void maybeAutoMove() {
        if (game == null) return;
        Game.Result r = game.getResult();
        if (r != Game.Result.IN_PROGRESS) return;
        Board.Cell turn = game.getTurn();
        boolean comp = (players.c1 == turn && players.p1 == PlayerType.COMP) || (players.c2 == turn && players.p2 == PlayerType.COMP);
        if (!comp) return;
        Optional<Board.Point> mv = game.computeComputerMove(turn);
        if (mv.isPresent()) {
            Board.Point p = mv.get();
            System.out.println(colorChar(turn) + " (" + p.x + ", " + p.y + ")");
            game.move(p.x, p.y);
            announceResult();
            maybeAutoMove();
        }
    }

    private void announceResult() {
        Game.Result r = game.getResult();
        if (r == Game.Result.WHITE_WINS) System.out.println("Game finished. W wins!");
        else if (r == Game.Result.BLACK_WINS) System.out.println("Game finished. B wins!");
        else if (r == Game.Result.DRAW) System.out.println("Game finished. Draw");
    }

    private boolean cmd(String s, String name) { return s.equalsIgnoreCase(name); }

    private String colorChar(Board.Cell c) { return (c == Board.Cell.WHITE) ? "W" : "B"; }

    private void printHelp() {
        System.out.println("Commands:\n" +
                "GAME N, TYPE C, TYPE C\n" +
                "MOVE X, Y\n" +
                "HELP\n" +
                "EXIT");
    }
}
