package com.example.pc.testeverything.SqliteManager.litepalmanager;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Rachel on 2019/3/14.
 */

public class Comment extends LitePalSupport {
    private int id;

    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
