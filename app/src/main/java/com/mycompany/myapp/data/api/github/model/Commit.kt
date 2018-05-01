package com.mycompany.myapp.data.api.github.model

import com.google.gson.annotations.SerializedName

data class Author(
        @SerializedName("login") val name: String,
        @SerializedName("avatar_url") val avatarUrl: String)

data class CommitDetails(
        @SerializedName("message") val message: String)

data class Commit(
        @SerializedName("commit") val commit: CommitDetails,
        @SerializedName("author") val author: Author) {

    val commitMessage: String
        get() = commit.message
}
