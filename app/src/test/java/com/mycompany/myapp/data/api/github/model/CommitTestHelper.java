package com.mycompany.myapp.data.api.github.model;

import com.mycompany.myapp.data.api.github.model.Commit.Author;
import com.mycompany.myapp.data.api.github.model.Commit.CommitDetails;

public class CommitTestHelper {
    public static Commit stubCommit(String authorName, String message) {
        Author author = new Author();
        author.name = authorName;

        CommitDetails commitDetails = new CommitDetails();
        commitDetails.author = author;
        commitDetails.message = message;

        Commit commit = new Commit();
        commit.commit = commitDetails;
        return commit;
    }
}
