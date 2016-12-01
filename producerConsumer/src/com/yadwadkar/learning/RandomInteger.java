package com.yadwadkar.learning;

import java.security.SecureRandom;
import java.util.Random;

public class RandomInteger implements DAO<Integer> {
    private final Random random = new SecureRandom();

    @Override
    public Integer getNext() {
        return random.nextInt();
    }
}
