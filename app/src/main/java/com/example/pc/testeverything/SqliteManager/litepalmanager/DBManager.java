package com.example.pc.testeverything.SqliteManager.litepalmanager;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rachel on 2019/3/13.
 */

public class DBManager {
    private static DBManager dbManager;
    private DBModel dbModel;
    private static final Object objectLock = new Object(); // 定义同步锁

    public synchronized static DBManager getInstance() {
        if (dbManager == null) {
            synchronized (objectLock) {
                if (null == dbManager) {
                    dbManager = new DBManager();
                }
            }
        }
        return dbManager;
    }

    public DBManager() {
        dbModel = new DBModel();
    }

    public void saveNews() {
        News news = new News();
        news.setTitle("这是一条新闻标题");
        news.setContent("这是一条新闻内容");
        news.setPublishDate(new Date());
        HashMap<String, String> needPreparedDetailList = new HashMap<>();
        needPreparedDetailList.put("你吃饭了吗", "吃了");
        news.setNeedPreparedDetailList(needPreparedDetailList);
        Introduction introduction = new Introduction();
        introduction.setGuide("这是新闻导语");
        introduction.setDigest("这是新闻摘要");
        IntroductionAuthor author = new IntroductionAuthor();
        author.setAuthor("黄烁文");
        author.setFrom("来自宝瓶州");
        List<Comment> commentList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Comment comment = new Comment();
            comment.setContent("这是第" + (i + 1) + "条评论");
            comment.save();
            commentList.add(comment);
        }
        news.setCommentList(commentList);
        introduction.setIntroductionAuthor(author);
        news.setIntroduction(introduction);
        author.save();
        introduction.save();
        news.save();
    }

    public News queryNews() {
//        List<News> orderDBList = LitePal.where("id= ?", "1").find(News.class,true);激进查询
        News news = LitePal.find(News.class, 1);
        return news;
    }
}
