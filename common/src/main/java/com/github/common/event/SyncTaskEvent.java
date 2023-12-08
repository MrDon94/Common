package com.github.common.event;

/**
 * @author: cd
 * @Description: 同步任务事件
 * @date: 2021/6/29
 */
public class SyncTaskEvent extends BaseLiveEvent {
    private String tag;
    private int status;

    public SyncTaskEvent(String tag, int status) {
        this.tag = tag;
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
