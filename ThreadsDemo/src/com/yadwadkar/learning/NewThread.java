package com.yadwadkar.learning;

public class NewThread extends Thread {

    String name;
    Thread thread;

    NewThread(String name) {
        this.name = name;
        thread = new Thread(this, name);
        System.out.println("New Thread: " + thread);
        thread.start();
    }

    public void run() {
        try {
            for (int i = 5; i > 0; i--) {
                System.out.println(name + ": " + i);
                Thread.sleep(5000);
            }
        } catch (InterruptedException e) {
            System.out.println(name + " interrupted");

        }

        System.out.println(name + " exiting...");

    }
}
