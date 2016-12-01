package com.yadwadkar.learning;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer<T> {
    private final BlockingQueue<T> blockingQueue;
    private final AtomicInteger processed = new AtomicInteger(0);
    private final ListeningExecutorService executorService;
    private final int maxConsumers;

    private Consumer(BlockingQueue<T> blockingQueue,
                     int maxConsumers) {
        this.blockingQueue = blockingQueue;
        this.executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(maxConsumers));
        this.maxConsumers = maxConsumers;
    }

    public static <T> Consumer<T> createWithQueue(BlockingQueue<T> blockingQueue,
                                                  int maxConsumers) {
        return new Consumer<>(blockingQueue, maxConsumers);
    }

    public void start() {
        System.out.println("Starting consumer");

        List<ListenableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < maxConsumers; i++) {
            futures.add(executorService.submit(() -> {

            }));
        }
    }
}
