package com.tiidian.threadmanager.eb;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by skyshi on 2016/11/14 0014.
 */

public class EBBaseEntity {

    protected String type;
    protected Object tag;

    public EBBaseEntity(String type) {
        this.type = type;
    }

    @JSONField(serialize = false, deserialize = false)
    public String getType() {
        return type;
    }

    public Object getTag() {
        return this.tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }
}
