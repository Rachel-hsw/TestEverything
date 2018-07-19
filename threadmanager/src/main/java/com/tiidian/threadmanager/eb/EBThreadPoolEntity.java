package com.tiidian.threadmanager.eb;

/**
 * Created by skyshi on 2017/1/18 0018.
 */

public class EBThreadPoolEntity extends EBBaseEntity {
    private String uuid;

    public EBThreadPoolEntity(String type) {
        super(type);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
