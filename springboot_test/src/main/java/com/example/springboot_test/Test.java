package com.example.springboot_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicStampedReference;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException, ClassNotFoundException {
/*        AtomicStampedReference a =  new AtomicStampedReference(1,1);
        a.compareAndSet(1,3,1,2);
        a.compareAndSet(3,1,2,3);
        a.compareAndSet(1,2,3,4);
        System.out.println(a.getReference());
        List<? extends Number> list = new ArrayList<Integer>(); // OK
        Number n = list.get(0); // 读取没问题
        list.add(new Number(1) {
        }); // 编译错误 ❌，不能写入
        Class class1 = SpringbootTestApplication.class;
        class1.getAnnotation(class1);
        ConcurrentHashMap map = new ConcurrentHashMap();
        map.put()*/
        Class<?> clazz = Class.forName("com.example.springboot_test.cron.CronTask001");
        System.out.println(TimeUnit.MINUTES.toMillis(5));

    }

    public static String find(String str) {
        if (str == null || str.isEmpty()) {
            return null;
        }

        int [] cps = str.codePoints().toArray();

        Map<Integer, Integer> lastPos = new HashMap<Integer, Integer>();
        int start = 0,len = 0;
        int left = 0;

        for (int right = 0; right < cps.length; right++) {
            int cp = cps[right];
            Integer prev = lastPos.get(cp);
            if (prev != null && prev >= left) {
                left = prev + 1;
            }

            lastPos.put(cp, right);

            int curLen = right - left + 1;
            if (curLen > len) {
                len = curLen;
                start = left;
            }
        }

        return new String(cps, start, len);
    }
}
