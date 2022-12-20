package ru.barsik.simbirpractic.java_part_ii;

import android.graphics.Point;

import java.util.Arrays;
import java.util.List;

public class DotTask {

    public Point move(Point start, Direction dir) {
        return new Point(start.x + dir.getDelta().x, start.y + dir.getDelta().y);
    }

    public Point move(Point start, List<Direction> dirs) {

        Point res = new Point(start);
        for(Direction d : dirs){
            res = move(res, d);
            System.out.println(res);
        }
        return res;
    }

    public void testMove() {
        Point location = new Point(0, 0);
        Direction[] dirs = new Direction[]{
                Direction.Up,
                Direction.Up,
                Direction.Left,
                Direction.Down,
                Direction.Left,
                Direction.Down,
                Direction.Down,
                Direction.Right,
                Direction.Right,
                Direction.Down,
                Direction.Right
        };

        location = move(location, Arrays.asList(dirs));
    }
}
