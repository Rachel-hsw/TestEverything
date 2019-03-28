package com.example.pc.testeverything.SqliteManager.litepalmanager;

import com.alibaba.fastjson.annotation.JSONField;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rachel on 2019/2/21.
 */

public class News extends LitePalSupport {

    private int id;

    private String title;

    private String content;

    private Date publishDate;

    private String publishDay;

    private int commentCount;

    private String newsNumber;

    private Introduction introduction;

    private List<Comment> commentList = new ArrayList<Comment>();

    private HashMap<String, String> needPreparedDetailList;

    public String getPublishDay() {
        return publishDay;
    }

    public void setPublishDay(String publishDay) {
        this.publishDay = publishDay;
    }

    public List<Comment> getCommentList() {
        List<Comment> introductions = LitePal.where("news_id = ?", String.valueOf(id)).find(Comment.class);

        return introductions;
    }

    public String getNewsNumber() {
        return newsNumber;
    }

    public void setNewsNumber(String newsNumber) {
        this.newsNumber = newsNumber;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }

    public HashMap<String, String> getNeedPreparedDetailList() {
        return needPreparedDetailList;
    }

    public void setNeedPreparedDetailList(HashMap<String, String> needPreparedDetailList) {
        this.needPreparedDetailList = needPreparedDetailList;
    }

    public Introduction getIntroduction() {
        List<Introduction> introductions = LitePal.where("news_id = ?", String.valueOf(id)).find(Introduction.class);
        if (introductions == null || introductions.size() == 0) {
            return null;
        }
        return introductions.get(0);
    }

    public void setIntroduction(Introduction introduction) {
        this.introduction = introduction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}