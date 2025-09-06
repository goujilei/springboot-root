package com.example.springboot_test;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

public class InterruptibleLockDemo {
    private static final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
/*        Thread thread1 = new Thread(() -> {
            lock.lock(); // 线程1加锁，不释放
            try {
                System.out.println("线程1 获得锁，睡眠10秒");
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("线程1 被中断");
            } finally {
                lock.unlock();
                System.out.println("线程1 释放锁");
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                System.out.println("线程2 尝试获取锁（可中断）...");
                lock.lockInterruptibly(); // 关键点：可中断加锁
                try {
                    System.out.println("线程2 获得锁");
                } finally {
                    lock.unlock();
                    System.out.println("线程2 释放锁");
                }
            } catch (InterruptedException e) {
                System.out.println("线程2 等锁过程中被中断");
            }
        });

        thread1.start();
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        thread2.start();
        try {
            Thread.sleep(2000); // 等2秒
            System.out.println("主线程 中断 线程2");
            thread2.interrupt(); // ✅ 中断线程2，模拟请求取消
        } catch (InterruptedException ignored) {}*/
        ExecutorService pool = Executors.newFixedThreadPool(3); // 固定线程数线程池
        Future<String> submit = pool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println(123);
            }
        }, "固定返回值");
        System.out.println("线程执行结果:"+submit.get());

    }
}
