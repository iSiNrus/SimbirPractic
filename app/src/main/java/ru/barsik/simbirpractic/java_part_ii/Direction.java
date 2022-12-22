package ru.barsik.simbirpractic.java_part_ii;

import android.graphics.Point;

public enum Direction {
    Up {
        public Point getDelta(){ return new Point(0, 1); }
    },
    Down {
        public Point getDelta(){ return new Point(0, -1); } 
    },
    Left{
        public Point getDelta(){ return new Point(-1, 0); }
    },
    Right{
        public Point getDelta(){ return new Point(1, 0); }
    };

    public abstract Point getDelta() ;
}
