package com.mycompany.myapp.data.api.github.model

object CommitTestHelper {
    fun stubCommit(authorName: String, message: String): Commit {
        val author = Author(authorName)
        val commitDetails = CommitDetails(message, author)
        return Commit(commitDetails)
    }
}
