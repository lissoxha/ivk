package org.example.squares.engine;

import java.util.ArrayList;
import java.util.List;

public final class SquareDetector {
    private SquareDetector() {}

    public static boolean hasAnySquare(Board board, Board.Cell color) {
        int n = board.getSize();
        // collect points of this color
        List<int[]> pts = new ArrayList<>();
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                if (board.get(x, y) == color) pts.add(new int[]{x, y});
            }
        }
        int m = pts.size();
        // Check all quadruples; optimized by using vector math
        for (int i = 0; i < m; i++) {
            int[] a = pts.get(i);
            for (int j = i + 1; j < m; j++) {
                int[] b = pts.get(j);
                int vx = b[0] - a[0];
                int vy = b[1] - a[1];
                // Build two other corners using perpendicular vectors
                int cx = a[0] - vy;
                int cy = a[1] + vx;
                int dx = b[0] - vy;
                int dy = b[1] + vx;
                if (inside(n, cx, cy) && inside(n, dx, dy)
                        && board.get(cx, cy) == color && board.get(dx, dy) == color) {
                    return true;
                }
                // other orientation
                cx = a[0] + vy;
                cy = a[1] - vx;
                dx = b[0] + vy;
                dy = b[1] - vx;
                if (inside(n, cx, cy) && inside(n, dx, dy)
                        && board.get(cx, cy) == color && board.get(dx, dy) == color) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean inside(int n, int x, int y) { return x >= 0 && y >= 0 && x < n && y < n; }
}
