package com.example.todolist.vo

import com.google.gson.annotations.SerializedName

class DogCatResVO {
    @SerializedName(value="fileUrl", alternate=["url", "file"])
    val fileUrl : String = ""
}