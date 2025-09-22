package org.example.squares.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    public enum Cell { EMPTY, WHITE, BLACK }

    private final int size;
    private final Cell[][] cells;

    public Board(int size) {
        if (size < 3) throw new IllegalArgumentException("Size must be >= 3");
        this.size = size;
        this.cells = new Cell[size][size];
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                cells[y][x] = Cell.EMPTY;
            }
        }
    }

    private Board(int size, Cell[][] cells) {
        this.size = size;
        this.cells = cells;
    }

    public int getSize() { return size; }

    public Cell get(int x, int y) { return cells[y][x]; }

    public boolean isInside(int x, int y) { return x >= 0 && y >= 0 && x < size && y < size; }

    public boolean isFree(int x, int y) { return isInside(x, y) && get(x, y) == Cell.EMPTY; }

    public void place(int x, int y, Cell cell) {
        if (!isFree(x, y)) throw new IllegalArgumentException("Cell not free or out of bounds");
        cells[y][x] = Objects.requireNonNull(cell);
    }

    public void clear(int x, int y) {
        if (!isInside(x, y)) throw new IllegalArgumentException("Out of bounds");
        cells[y][x] = Cell.EMPTY;
    }

    public Board copy() {
        Cell[][] copy = new Cell[size][size];
        for (int y = 0; y < size; y++) {
            System.arraycopy(cells[y], 0, copy[y], 0, size);
        }
        return new Board(size, copy);
    }

    public boolean isFull() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (cells[y][x] == Cell.EMPTY) return false;
            }
        }
        return true;
    }

    public List<Point> freeCells() {
        List<Point> pts = new ArrayList<>();
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (cells[y][x] == Cell.EMPTY) pts.add(new Point(x, y));
            }
        }
        return pts;
    }

    public static class Point {
        public final int x;
        public final int y;
        public Point(int x, int y) { this.x = x; this.y = y; }
    }
}
