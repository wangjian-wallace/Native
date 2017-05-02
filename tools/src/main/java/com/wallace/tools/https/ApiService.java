package com.wallace.tools.https;

import com.wallace.tools.bean.Information;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ApiService {
//    @FormUrlEncoded
//    @POST("/post")
//    Observable<Information> getInformationInfo(@Field("username")String username, @Field("password")String password);

    @POST("{Get}.aspx")
    Observable<Information> getInformationInfo(@Path("Get") String api, @QueryMap Map<String, String> map);
}