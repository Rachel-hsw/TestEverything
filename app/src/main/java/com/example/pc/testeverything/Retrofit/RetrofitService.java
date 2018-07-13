package com.example.pc.testeverything.Retrofit;

import com.example.pc.testeverything.moudle.Book;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by PC on 2018/7/2.
 */

/**
 * https://api.douban.com/v2/book/search?q=金瓶梅&tag=&start=0&count=1
 */
public interface RetrofitService {
    @GET("book/search")
    Call<Book> getSearchBook(@Query("q") String name,
                             @Query("tag") String tag,
                             @Query("start") int start,
                             @Query("count") int count);

}

