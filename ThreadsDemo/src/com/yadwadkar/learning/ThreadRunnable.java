package com.yadwadkar.learning;

class ThreadRunnable implements Runnable {

    private final String name;
    Thread t;
    private final int sleepTime;
    private SharedResource sharedStack;

    ThreadRunnable(String name, int sleepTime, SharedResource sharedStack) {
        this.name = name;
        this.sleepTime = sleepTime;
        this.sharedStack = sharedStack;
        t = new Thread(this, name);
        t.start();
    }

    @Override
    public void run() {
        System.out.println("Started Thread " + name);

        for (int i = 5; i > 0; i--) {
            System.out.println("Thread " + name + ": " + i);
            sharedStack.addString("Thread " + name + " " + i);
            try {
                System.out.println("Sleeping for " + sleepTime);
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted: " + name);
            }
        }
    }
}
