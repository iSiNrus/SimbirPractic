package ru.barsik.simbirpractic.java_part_ii;

public class LambdaTask {

    Runnable myClosure = () -> {
        System.out.println("I love Java");
    };

    public void repeatTask (int times, Runnable task){
        int i=0;
        while(i<times){
            task.run();
            i++;
        }
    }
}
