package com;

import org.junit.Test;
import prepare.util.Util;

import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.assertEquals;

public class ReentrantLockTest {

    public static class Counter implements Runnable {
        ReentrantLock lock = new ReentrantLock();
        private int count = 0;

        @Override
        public void run() {
            try {
                lock.lock();
                Util.sleep(100);
                count++;
                validate();
            } finally {
                lock.unlock();
            }
        }

        private void validate() {
            if (count == 2) {
                throw new RuntimeException();
            }
        }
    }

    @Test
    public void testLock() throws InterruptedException {
        Counter count = new Counter();
        Thread thread1 = new Thread(count);
        Thread thread2 = new Thread(count);
        Thread thread3 = new Thread(count);

        thread1.start();
        thread2.start();
        thread3.start();

        Thread.sleep(1000);

        assertEquals(3, count.count);

        System.out.println("Main exit: " + count.count);
    }
}