package com.mycompany.myapp.data.api.github.model;

public class CommitTestHelper {
    public static Commit stubCommit(String authorName, String message) {
        Author author = new Author(authorName);
        CommitDetails commitDetails = new CommitDetails(message, author);
        return new Commit(commitDetails);
    }
}
