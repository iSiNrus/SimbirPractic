package ru.barsik.simbirpractic.java_part_ii;

public class Rectangle implements Shape{

    private float width;
    private float height;

    public Rectangle(){
        width = 0;
        height = 0;
    }

    public Rectangle(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void printPerimeter() {
        System.out.println((width+height)*2);
    }

    @Override
    public void printSquare() {
        System.out.println(width*height);
    }
}
