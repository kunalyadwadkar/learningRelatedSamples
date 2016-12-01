package com.yadwadkar.learning;

public class Main {

    public static void main(String[] args) {
	// write your code here
        SharedResource sharedStack = new SharedResource();

        ThreadRunnable t1 = new ThreadRunnable("One", 0, sharedStack);
        ThreadRunnable t2 = new ThreadRunnable("Two", 4, sharedStack);
        ThreadRunnable t3 = new ThreadRunnable("Three", 7, sharedStack);


        try{
            System.out.println("--------------------------------");
            System.out.println("Waiting for all threads to finish");
            t1.t.join();
            System.out.println("--------------------------------");
            System.out.println("Thread 1 alive : " + t1.t.isAlive());
            System.out.println("Thread 2 alive : " + t2.t.isAlive());
            System.out.println("Thread 3 alive : " + t3.t.isAlive());
            System.out.println("--------------------------------");
            t2.t.join();
            System.out.println("--------------------------------");
            System.out.println("Thread 1 alive : " + t1.t.isAlive());
            System.out.println("Thread 2 alive : " + t2.t.isAlive());
            System.out.println("Thread 3 alive : " + t3.t.isAlive());
            System.out.println("--------------------------------");
            t3.t.join();
            System.out.println("--------------------------------");
            System.out.println("Thread 1 alive : " + t1.t.isAlive());
            System.out.println("Thread 2 alive : " + t2.t.isAlive());
            System.out.println("Thread 3 alive : " + t3.t.isAlive());
            System.out.println("--------------------------------\n\n");
        } catch (InterruptedException e) {
            System.out.println("Main thread interrupted");
        }

        System.out.println(sharedStack.size() + "\n\n");
        while (!sharedStack.isEmpty()) {
            System.out.println(sharedStack.pop());
        }

        System.out.println("Main thread exiting...");
    }
}
