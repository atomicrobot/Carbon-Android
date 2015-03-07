package com.mycompany.myapp.data.api.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.parceler.Parcel;

@Parcel
public class Commit {
    @Parcel
    public static class Author {
        @JsonProperty("name")
        String name;
    }

    @Parcel
    public static class CommitDetails {
        @JsonProperty("message")
        String message;

        @JsonProperty("author")
        Author author;
    }

    @JsonProperty("commit")
    CommitDetails commit;


    public String getCommitMessage() {
        return commit.message;
    }

    public String getAuthor() {
        return commit.author.name;
    }
}
