package com.yadwadkar.learning;

public class A {
    B b;

    public A(B b) {
        this.b = b;

        String name = Thread.currentThread().getName();
        System.out.println("Started A with thread " + name);

    }

    synchronized void foo() {

        String name = Thread.currentThread().getName();

        System.out.println(name + " entered A.foo()");

        try{
            Thread.sleep(500);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Trying to call B.last()");
        b.last();
    }

    synchronized public void last() {
        System.out.println("Inside A.last()");
    }
}
