package com.offhome.app.data.retrofit

import com.offhome.app.data.model.SignedUpUser
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface SignUpService {

    @POST("/users/{username}/create")
    fun createProfile(@Path("username") username: String, @Body signedUpUser: SignedUpUser): Call<String>

/* make sure you add @Query or @Field for all the parameters , depending on whether you issuing GET/POST.
eg:
@GET("/api/Books/GetAll")
void GetRecentBooks(@Query int Offset, Callback<List<Book>> cb);*/
}
