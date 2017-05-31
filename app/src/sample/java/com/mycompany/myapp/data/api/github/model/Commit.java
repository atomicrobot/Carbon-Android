package com.mycompany.myapp.data.api.github.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Commit {
    @Parcel
    public static class Author {
        @SerializedName("name") String name;
    }

    @Parcel
    public static class CommitDetails {
        @SerializedName("message") String message;
        @SerializedName("author") Author author;
    }

    @SerializedName("commit") CommitDetails commit;

    public String getCommitMessage() {
        return commit.message;
    }

    public String getAuthor() {
        return commit.author.name;
    }
}
