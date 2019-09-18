package com.example.chatapp.fragments

import com.example.chatapp.notifications.MyResponse
import retrofit2.Call
import retrofit2.http.Headers
import retrofit2.http.POST
import com.example.chatapp.notifications.Sender
import retrofit2.http.Body


interface APIService {
    @Headers (
            "Content-Type:application/json",
            "Authorization:Key=AAAAiTFJhI4:APA91bFfpD6RRgpxnVgjgTvEyiMtZJZH6B_hm8XkfTTZISbFIEr5sxEGtlXGFYEA-K2qkFWNfr0UKXkCphOXZd2O0WQW-Gw62L9UzfpTz7l0HMnuf-EIZgRoVm0aW0GUVCNDw368Qe3L"
    )

    @POST ("fcm/send")
    fun sendNotification(@Body body: Sender): Call<MyResponse>

}