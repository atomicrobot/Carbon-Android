package com.atomicrobot.carbon.ui.main

import com.atomicrobot.carbon.data.api.github.model.*

val dummyCommits = listOf(
    Commit(sha = "", commit = CommitDetails(message = "Sample github commit message #1", author = Author("Smitty Joe", email = "email@example.com", date = "2023-01-01"))),
    Commit(sha = "", commit = CommitDetails(message = "Sample github commit message #2", author = Author("Joe Smitty", email = "email@example.com", date = "2023-01-01"))),
    Commit(sha = "", commit = CommitDetails(message = "Sample github commit message #3", author = Author("Smith Joe", email = "email@example.com", date = "2023-01-01"))),
    Commit(sha = "", commit = CommitDetails(message = "Sample github commit message #4", author = Author("Joe Smith", email = "email@example.com", date = "2023-01-01"))),
    Commit(sha = "", commit = CommitDetails(message = "Sample github commit message #5", author = Author("Joe Smoe", email = "email@example.com", date = "2023-01-01")))
)

