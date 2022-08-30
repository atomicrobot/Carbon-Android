package com.atomicrobot.carbon.ui.main

import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.Commit
import com.atomicrobot.carbon.data.api.github.model.CommitDetails

val dummyCommits = listOf(
    Commit(commit = CommitDetails(message = "Sample github commit message #1", author = Author("Smitty Joe"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #2", author = Author("Joe Smitty"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #3", author = Author("Smith Joe"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #4", author = Author("Joe Smith"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #5", author = Author("Joe Smoe")))
)
