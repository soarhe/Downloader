package com.soarhe.downloader.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by baidu on 16/6/16.
 */
public class RunningList<T extends AbsTask> extends ArrayList<T> {

    void checkstatus() {
        Iterator<T> iter = this.iterator();
        while (iter.hasNext()) {
            T obj = iter.next();
            if (obj.mInfo.mStatus != TaskInfo.Status.RUNNING) {
                iter.remove();
            }
        }
    }

    List<AbsTask> checkRunningNum(int maxNum) {
        if (this.size() <= maxNum) {
            return null;
        } else {
            List<AbsTask> moreTasks = new ArrayList<>();
            while (this.size() > maxNum) {
                AbsTask outTask = this.remove(this.size() - 1);
                outTask.pause();
                outTask.mInfo.mStatus = TaskInfo.Status.WAITING;
                moreTasks.add(outTask);
            }
            return moreTasks;
        }
    }

    /**
     * 获取running list剩余的可执行任务数
     * @param maxNum 最大可执行任务数
     * @return 剩余
     */
    int getRemainNum(int maxNum) {
        if (this.size() < maxNum) {
            return maxNum - this.size();
        } else {
            return 0;
        }
    }

    @Override
    public boolean add(T object) {
        object.start();
        return super.add(object);
    }
}
