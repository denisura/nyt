package com.github.denisura.nytseacher.data.network;

import com.github.denisura.nytseacher.data.model.ArticleSearchResponse;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface NewYorkTimesApi {

    String BASE_URL = "https://api.nytimes.com/";

    //svc/search/v2/articlesearch.json?begin_date=20160112&sort=oldest&fq=news_desk:(%22Education%22%20%22Health%22)&api-key=227c750bb7714fc39ef1559ef1bd8329&fl=headline,multimedia,web_url&page=2
//    @GET("svc/search/v2/articlesearch.json?fl=headline,multimedia,web_url")
//    Observable<ArticleSearchResponse> articleSearch(
//            @Query("begin_date") String beginDate,
//            @Query("sort") String sort,
//            @Query("fq") String filterQueryFields,
//            @Query("page") int page
//    );

    @GET("svc/search/v2/articlesearch.json")
    Observable<ArticleSearchResponse> articleSearch(
            @QueryMap ArticleSearchOptions filter
    );
}
