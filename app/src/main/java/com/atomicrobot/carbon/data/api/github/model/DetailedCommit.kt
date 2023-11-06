package com.atomicrobot.carbon.data.api.github.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class Tree(
    @Json(name = "url") val url: String
)

@JsonClass(generateAdapter = true)
data class Verification(
    @Json(name = "verified") val verified: Boolean
)

@JsonClass(generateAdapter = true)
data class DetailedCommitDetails(
    @Json(name = "message") val message: String,
    @Json(name = "author") val author: Author,
    @Json(name = "tree") val tree: Tree,
    @Json(name = "verification") val verification: Verification
)

@JsonClass(generateAdapter = true)
data class DetailedCommit(
    @Json(name = "commit") val detailedCommit: DetailedCommitDetails
) {
    val detailedCommitMessage: String
        get() = detailedCommit.message

    val detailedCommitAuthor: String
        get() = detailedCommit.author.name

    val detailedCommitTreeURL: String
        get() = detailedCommit.tree.url

    val detailedCommitVerified: Boolean
        get() = detailedCommit.verification.verified
}
