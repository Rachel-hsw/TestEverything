package com.example.pc.testeverything.SqliteManager.litepalmanager;

import org.litepal.crud.LitePalSupport;

/**
 * Created by Rachel on 2019/3/14.
 */

public class IntroductionAuthor extends LitePalSupport {
    private String author;
    private String from;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
