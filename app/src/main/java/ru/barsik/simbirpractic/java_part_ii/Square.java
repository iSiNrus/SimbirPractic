package ru.barsik.simbirpractic.java_part_ii;

public class Square implements Shape{

    private float side;

    public Square(){
        side = 0f;
    }

    public Square(float side) {
        this.side = side;
    }

    @Override
    public void printPerimeter() {
        System.out.println(side*4);
    }

    @Override
    public void printSquare() {
        System.out.println(side*side);
    }
}
