package com.offhome.app.data.retrofit

import retrofit2.Call
import retrofit2.http.POST

interface SignUpService {

    @POST(":username/create") // TODO el ":"?? va el user allà?
    fun createProfile(/*@algo*/ username: String): Call<String> // retorna algo per saber si ha funcionat suposo. no se si ha de ser string

/* make sure you add @Query or @Field for all the parameters , depending on whether you issuing GET/POST.
eg:
@GET("/api/Books/GetAll")
void GetRecentBooks(@Query int Offset, Callback<List<Book>> cb);*/
}
