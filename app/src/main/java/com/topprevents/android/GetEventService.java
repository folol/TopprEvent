package com.topprevents.android;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by devansh on 9/24/16.
 */
public interface GetEventService {

    @GET("toppr_events")
    Call<EventData> get( @Query("type") String type ,  @Query("query") String query);

}
