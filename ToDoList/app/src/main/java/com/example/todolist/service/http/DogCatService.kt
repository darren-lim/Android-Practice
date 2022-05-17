package com.example.todolist.service.http

import com.example.todolist.vo.DogCatResVO
import com.google.gson.GsonBuilder
import okhttp3.*

class DogCatService {
    var client = OkHttpClient()

    fun getDogOrCat(input: String): DogCatResVO {
        val url = if(input == "cat") {
            "https://aws.random.cat/meow"
        } else {
            "https://random.dog/woof.json"
        }

        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()

        val body = response.body?.string()

        val gson = GsonBuilder().create()

        return gson.fromJson(body, DogCatResVO::class.java)
    }
}