package com.example.pc.testeverything.SqliteManager.litepalmanager;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * Created by Rachel on 2019/3/12.
 */

public class Introduction extends LitePalSupport {
    //导语和摘要，我们把这两个字段放在一张introduction表中，作为新闻的简介。

    public Introduction(String guide, String digest) {
        this.guide = guide;
        this.digest = digest;
    }

    public Introduction() {
    }

    private int id;

    private String guide;

    private String digest;
    private IntroductionAuthor introductionAuthor;

    public IntroductionAuthor getIntroductionAuthor() {
        List<IntroductionAuthor> introductions = LitePal.where("introduction_id = ?", String.valueOf(id)).find(IntroductionAuthor.class);
        if (introductions == null || introductions.size() == 0) {
            return null;
        }
        return introductions.get(0);
    }

    public void setIntroductionAuthor(IntroductionAuthor introductionAuthor) {
        this.introductionAuthor = introductionAuthor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }
}
