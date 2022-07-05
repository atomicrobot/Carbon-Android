package com.atomicrobot.carbon.data.api.github.model

object CommitTestHelper {
    fun stubCommit(authorName: String, message: String): Commit {
        val author = Author(authorName)
        val commitDetails = CommitDetails(message, author)
        return Commit(commitDetails)
    }
}
