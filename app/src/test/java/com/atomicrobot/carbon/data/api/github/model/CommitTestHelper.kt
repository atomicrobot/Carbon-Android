package com.atomicrobot.carbon.data.api.github.model

import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.CommitDetails

object CommitTestHelper {
    fun stubCommit(authorName: String, message: String): Commit {
        val author = Author(authorName)
        val commitDetails = CommitDetails(message, author)
        return Commit(commitDetails)
    }
}
