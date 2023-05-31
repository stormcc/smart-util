package github.com.stormcc.generator;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

/**
 * Create By: Jimmy Song
 * Create At: 2023-05-31 13:54
 */
public class NumberLoopGenerator {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final AtomicInteger number ;
    private int max = 20;
    private int min = 0;

    public NumberLoopGenerator(int min, int max){
        this.number = new AtomicInteger(min);
        this.max = max;
        this.min = min;
    }

    public int newNumber(){
        Lock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            int n = number.getAndIncrement();
            if ( n <= max ) {
                return n;
            }
        } finally {
            readLock.unlock();
        }
        Lock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            int n1 = number.getAndIncrement();
            if (n1 <= max) {
                return n1;
            }
            number.set(min);
            return number.getAndIncrement();
        } finally {
            writeLock.unlock();
        }
    }
}
