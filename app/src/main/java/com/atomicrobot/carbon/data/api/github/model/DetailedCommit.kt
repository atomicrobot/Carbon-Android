package com.atomicrobot.carbon.data.api.github.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tree(
    @Json(name = "path") val path: String,
    @Json(name = "type") val type: String,
    @Json(name = "size") val size: Int,
    @Json(name = "url") val url: String
)

@JsonClass(generateAdapter = true)
data class DetailedCommitDetails(
    @Json(name = "message") val message: String,
    @Json(name = "author") val author: Author,
    @Json(name = "node_id") val node_id: String,
    @Json(name = "html_url") val html_url: String,
    @Json(name = "tree") val tree: Tree
)

@JsonClass(generateAdapter = true)
data class DetailedCommit(
    @Json(name = "commit") val detailedCommit: DetailedCommitDetails
) {
    val detailedCommitMessage: String
        get() = detailedCommit.message

    val detailedCommitAuthor: String
        get() = detailedCommit.author.name

    val detailedCommitNode_id: String
        get() = detailedCommit.node_id

    val detailedCommitHtml_url: String
        get() = detailedCommit.html_url

    val detailedCommitTreePath: String
        get() = detailedCommit.tree.path

    val detailedCommitTreeType: String
        get() = detailedCommit.tree.type

    val detailedCommitTreeSize: Int
        get() = detailedCommit.tree.size

    val detailedCommitTreeURL: String
        get() = detailedCommit.tree.url

}
