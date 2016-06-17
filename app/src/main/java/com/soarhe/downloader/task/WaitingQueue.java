package com.soarhe.downloader.task;

import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * Created by baidu on 16/6/16.
 */
public class WaitingQueue<T extends AbsTask> extends PriorityQueue<T> {

    void checkstatus() {
        Iterator<T> iter = this.iterator();
        while (iter.hasNext()) {
            T obj = iter.next();
            if (obj.mInfo.mStatus != TaskInfo.Status.WAITING) {
                iter.remove();
            }
        }
    }

}
