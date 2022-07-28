package com.demo.demobase.ReentrantLock;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @ClassName SynchronizedByKey
 * @Description
 * @Author sunjie
 * @Date 2022/6/27 16:44
 * @Version 1.0
 */
@Component
public class SynchronizedByKey {
    Map<String, ReentrantLock> mutexCache = new ConcurrentHashMap<>();

    /**
     * @author: chaoqun.jiang
     * @description: 同步锁6217880800019816653
     * @date: 2022/2/12 10:26
     * @param: key
     * @param: statement
     * @return: void
     * @thorws:
     */
    public void exec(String key, Runnable statement) {
        ReentrantLock mutex4Key = null;
        ReentrantLock mutexInCache;
        do {
            if (mutex4Key != null) {
                mutex4Key.unlock();
            }
            mutex4Key = mutexCache.computeIfAbsent(key, k -> new ReentrantLock());
            mutex4Key.lock();
            mutexInCache = mutexCache.get(key);
            //1.mutexInCache==null 锁被删除
            //2.mutex4Key!= mutexInCache 锁不同
        } while (mutexInCache == null || mutex4Key != mutexInCache);
        try {
            statement.run();
        } finally {
            if (mutex4Key.getQueueLength() == 0) {
                mutexCache.remove(key);
            }
            mutex4Key.unlock();
            System.out.println("DAs");
        }
    }

    public void test() {
        this.exec("关键锁", () -> {
            //业务代码
        });
    }


}
