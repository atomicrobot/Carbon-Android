package com.atomicrobot.carbon.data.api.github.model

object CommitTestHelper {
    fun stubCommit(authorName: String, message: String): Commit {
        val author = Author(authorName, email = "example@example.com", date = "01/01/2023")
        val commitDetails = CommitDetails(message, author)
        return Commit(commitDetails, sha = "")
    }
}
