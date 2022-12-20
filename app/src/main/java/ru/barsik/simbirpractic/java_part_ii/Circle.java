package ru.barsik.simbirpractic.java_part_ii;

public class Circle implements Shape{

    private float diameter;

    public Circle(){
        diameter = 0f;
    }

    public Circle(float diameter) {
        this.diameter = diameter;
    }

    @Override
    public void printPerimeter() {
        System.out.println(Math.PI*diameter);
    }

    @Override
    public void printSquare() {
        System.out.println(Math.PI* (diameter/2) * (diameter/2) );
    }
}
