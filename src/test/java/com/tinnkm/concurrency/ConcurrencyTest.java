package com.tinnkm.concurrency;

import com.tinnkm.concurrency.annotations.NotThreadSafe;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author tinnkm
 * @version 1.0
 * @classname ConcurrencyTest
 * @description TODO
 * @date 2018/10/21 22:11
 **/
@NotThreadSafe
public class ConcurrencyTest extends ConcurrencyApplicationTests {
    // 请求总数
    private final static int CLIENTTOTAL = 5000;
    // 同时并发执行的线程数
    private final static int THREADTOTAL = 200;

    private int count;

    @Test
    public void testConcurrency() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Semaphore semaphore = new Semaphore(THREADTOTAL);
        CountDownLatch countDownLatch = new CountDownLatch(CLIENTTOTAL);
        for (int i = 0; i < CLIENTTOTAL; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println(count);
    }

    /**
     * 此方法不是原子性的，会出现线程问题
     */
    private void add(){
        count++;
    }
}
