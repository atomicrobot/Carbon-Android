package com.mycompany.myapp.data.api.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

@Parcel
public class Commit {
    @Parcel
    public static class CommitDetails {
        @JsonProperty("message")
        String message;
    }

    @Parcel
    public static class Author {
        @JsonProperty("name")
        String name;
    }

    @JsonProperty("commit")
    CommitDetails commit;

    @JsonProperty("author")
    Author author;

    public String getCommitMessage() {
        return commit.message;
    }

    public String getAuthor() {
        return author.name;
    }
}
