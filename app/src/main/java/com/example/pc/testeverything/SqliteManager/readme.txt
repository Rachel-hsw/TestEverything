litepal数据库测试
目前有四个表News(新闻表)、Introduction（新闻的简介表）【IntroductionAuthor（新闻简介的作者表）】、Comment（新闻的评论表）
最好在子线程处理
        //创建数据库,sqlite原生
        SQLiteOpenHelper dbHelper = new MySQLiteHelper(this, "my.db", null, 1);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        DBManager.getInstance().saveNews();
        //懒加载(多表联查)
        News news=DBManager.getInstance().queryNews();
        Introduction introduction = news.getIntroduction();
        IntroductionAuthor author=introduction.getIntroductionAuthor();
        List<Comment> commentList=news.getCommentList();
        introduction.setIntroductionAuthor(author);
        news.setIntroduction(introduction);
        news.setCommentList(commentList);