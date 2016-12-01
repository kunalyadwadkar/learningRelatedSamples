package com.yadwadkar.learning;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;


class Producer<T> {

    private final BlockingQueue<T> blockingQueue;
    private final DAO<T> dao;
    private final int maxResultsToProduce;
    private final AtomicInteger processed = new AtomicInteger(0);
    private final ListeningExecutorService executorService;
    private final int producerThreads;

    private Producer(BlockingQueue<T> blockingQueue,
                     Integer maxResultsToProduce,
                     Integer producerThreads,
                     DAO<T> dao) {
        this.blockingQueue = blockingQueue;
        this.maxResultsToProduce = maxResultsToProduce;

        this.executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(producerThreads));
        this.dao = dao;
        this.producerThreads = producerThreads;
    }

    public static <T> Producer<T> createWithQueue(BlockingQueue<T> blockingQueue,
                                                  Integer maxResultsToProduce,
                                                  Integer producerThreads,
                                                  DAO<T> dao) {
        return new Producer<T>(blockingQueue, maxResultsToProduce, producerThreads, dao);
    }


    public void start() {
        List<ListenableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < producerThreads; i++) {
            futures.add(executorService.submit(() -> {

                System.out.println();
                int processedCnt = processed.getAndIncrement();
                while (processedCnt < maxResultsToProduce) {
                    blockingQueue.put(dao.getNext());
                    Thread.sleep(200);
                    if (processedCnt % 200 == 0) {
                        System.out.println("Processed " + processedCnt + " results so far");
                    }
                }

                return null;

            }));
        }

        Futures.allAsList(futures);
        System.out.println("Finished producing all results");

    }


}
