package com.mycompany.myapp.data.api.github.model

import com.google.gson.annotations.SerializedName

data class Author(
        @SerializedName("name") val name: String)

data class CommitDetails(
        @SerializedName("message") val message: String,
        @SerializedName("author") val author: Author)

data class Commit(
        @SerializedName("commit") val commit: CommitDetails) {

    val commitMessage: String
        get() = commit.message

    val author: String
        get() = commit.author.name
}
