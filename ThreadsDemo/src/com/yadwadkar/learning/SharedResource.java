package com.yadwadkar.learning;

public class SharedResource {

    private String[] resource = new String[100];
    private int index = -1;

    public void addString(String toAdd) {
        try {
            Thread.sleep(1000);
            resource[++index] = toAdd;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String pop() {
        return resource[index--];
    }

    public boolean isEmpty() {
        return index == -1;
    }

    public int size() {
        return index + 1;
    }
}
