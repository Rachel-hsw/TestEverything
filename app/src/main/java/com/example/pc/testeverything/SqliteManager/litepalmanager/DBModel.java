package com.example.pc.testeverything.SqliteManager.litepalmanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.tiidian.log.LogManager;
import com.tiidian.threadmanager.ThreadListener;
import com.tiidian.threadmanager.ThreadManger;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;
import org.litepal.tablemanager.callback.DatabaseListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by skyshi on 2019/2/18.
 */
public class DBModel {

    public DBModel() {
        LitePal.registerDatabaseListener(new DatabaseListener() {
            @Override
            public void onCreate() {
                LogManager.get().getLogger(LogManager.class).info("数据库操作：创建数据库");
            }

            @Override
            public void onUpgrade(int oldVersion, int newVersion) {

                ThreadManger.get().add(new ThreadListener() {
                    @Override
                    public void doAction() throws Exception {
                        try {
                            //更新数据库时的一些其他操作
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogManager.get().getLogger(LogManager.class).error("数据库操作：版本更新出现异常", e);
                        }
                    }
                });
                LogManager.get().getLogger(LogManager.class).info("数据库操作：版本更新：" + oldVersion + "版本更新到" + newVersion + "版本");
            }
        });
        SQLiteDatabase db = Connector.getDatabase();
    }

    /**
     * 保存新闻数据
     *
     * @param news
     * @return
     */
    public boolean save(News news) {
        if (news == null) {
            return false;
        }
        //根据news的唯一标识（新闻号）查询
        List<News> newsList = LitePal.where("newsNumber= ?", news.getNewsNumber()).find(News.class);
        boolean result = false;
        //已经存在做更新操作
        if (newsList != null && newsList.size() > 0) {
            int affectedRows = news.updateAll("newsNumber = ?", news.getNewsNumber());
            if (affectedRows > 0) {
                result = true;
            }
        } else {
            //不存在做插入操作
            result = news.save();
        }
        return result;
    }


    /**
     * 根据新闻号获取指定新闻
     *
     * @param newsNumber
     * @return
     */
    public List<News> getNews(String newsNumber) {
        if (TextUtils.isEmpty(newsNumber)) {
            return null;
        }
        List<News> newsList = LitePal.where("newsNumber= ?", newsNumber).find(News.class);
        return newsList;
    }

    /**
     * 获取不是今天并且标题为“郭神”的新闻列表
     *
     * @return
     */
    public List<News> getNewsNotTodayAndAboutGuoShen() {
        List<News> newsList = LitePal.where("title= ? and publishDay <> ?", "郭神威武", getDayDate(new Date())).find(News.class);
        if (newsList == null) {
            return null;
        }
        return newsList;
    }

    /**
     * 根据新闻发布日期查找新闻
     *
     * @param publishDay 发布日期
     * @return
     */
    public List<News> getNewsByDate(Date publishDay) {
        List<News> newsList = LitePal.where("publishDay= ?", getDayDate(publishDay)).find(News.class);
        if (newsList == null) {
            return null;
        }
        return newsList;
    }

    /**
     * 获取开单日期
     *
     * @param date
     * @return
     */
    private String getDayDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }


    /**
     * 根据id更新新闻标题
     *
     * @param id
     * @return
     */
    public boolean syncNewsTitle(int id) {
        News news = new News();
        news.setTitle("郭神威武");
        int affectedRows = news.updateAll("id = ?", String.valueOf(id));
        if (affectedRows > 0) {
            return true;
        }
        return false;
    }

    /**
     * 获取News表的数据量
     *
     * @return
     */
    public int getNewsCount() {
        List<News> News = LitePal.select("id").find(News.class);
        return News.size();
    }

    /**
     * 获取News表的最大ID
     *
     * @return
     */
    public int getNewsMaxId() {
        Cursor cursor = LitePal.findBySQL("select max(id) as id from News");
        int id = 0;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"));

            } while (cursor.moveToNext());
        }
        return id;
    }

    /**
     * 删除某个日期之前的新闻
     *
     * @return
     */
    public boolean deleteNewsByDay(Date publishDay) {
        int affectedRows = LitePal.deleteAll(News.class, "publishDay < ?", getDayDate(publishDay));
        boolean result = false;
        if (affectedRows > 0) {
            result = true;
        }
        return result;
    }

    /**
     * 删除某个时间之前的新闻
     *
     * @return
     */
    public boolean deleteNewsByDate(Long updateTime) {
        int affectedRows = LitePal.deleteAll(News.class, "publishDate < ?", String.valueOf(updateTime));
        boolean result = false;
        if (affectedRows > 0) {
            result = true;
        }
        return result;
    }


    /**
     * 根据新闻号删除指定新闻
     *
     * @param newsNumber
     */
    public boolean deleteNews(String newsNumber) {
        int affectedRows = LitePal.deleteAll(News.class, "newsNumber = ?", newsNumber);
        boolean result = false;
        if (affectedRows > 0) {
            result = true;
        }
        return result;
    }
}
