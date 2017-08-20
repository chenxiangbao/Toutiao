package com.nowcoder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static java.lang.Thread.sleep;

public class ThreadTest {
//------------------------------线程测试-------------------------------------
    public static void testMythread(){
        for (int i=0;i<10;i++){
            new MyThread(i).start();
        }
    }
//-----------------------------BlockingQueue测试----------------------------------
    public static void tetsBlockingQueue(){
        //队列的空间大小
        BlockingQueue<String > q = new ArrayBlockingQueue<String>(10);
        new Thread(new Produce(q)).start();
        new Thread(new Consumer(q),"consumer1").start();
        new Thread(new Consumer(q),"consumer2").start();

    }
//---------------------------ThreadLocal测试---------------------------------------------
    private static ThreadLocal<Integer> threadLocalUserIds = new ThreadLocal<>();
    private static int userId;

    public static void testThreadLocal() {
        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //每个ThreadLocal可以保存自己的副本，在延时之前，10个副本已经保存完毕
                    threadLocalUserIds.set(finalI);
                    sleep(1000);
                    System.out.println("ThreadLocal: " + threadLocalUserIds.get());
                }
            }).start();
        }

        for (int i = 0; i < 10; ++i) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //在延时之前，10个中只有一个留在userId中
                    userId = finalI;
                    sleep(1000);
                    System.out.println("NonThreadLocal: " + userId);
                }
            }).start();
        }
    }
    public static void sleep(int mails){
        try {
            Thread.sleep(mails);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void testHandLocal(){
        testThreadLocal();
    }
//---------------------------main函数-------------------------------------------
    public static void main(String[] args){
        //testMythread();
       // tetsBlockingQueue();
        testHandLocal();
    }
}
//--------------------向BlockingQueue测试队列中添加数据-------------------------------
class Produce implements Runnable{
    private BlockingQueue<String> q;
    //初始化时传入BlockingQueue的参数
    public Produce(BlockingQueue<String> q){
        this.q=q;
    }
    @Override
    public void run() {
        for(int i=0;i<10;++i){
            try {
                sleep(1000);
                q.put(String.valueOf(i));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
class Consumer implements Runnable{
    private BlockingQueue<String> q;
    //初始化时传入BlockingQueue的参数
    public Consumer(BlockingQueue<String> q){
        this.q=q;
    }
    @Override
    public void run() {
        while(true){
            try {
                System.out.println(Thread.currentThread().getName()+":"+q.take());
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
//-----------------------------------------------------------------------------
class MyThread extends Thread{
    public int tid;
    MyThread(int tid){
        this.tid=tid;
    }
    @Override
    public void run() {
        try {
            for(int i=0;i<10;i++){
                sleep(1000);
                System.out.println("T__"+tid+":"+i);
            }
        }catch (Exception e){
            e.getMessage();
        }
    }
}