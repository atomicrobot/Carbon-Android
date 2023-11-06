package com.atomicrobot.carbon.ui.clickableCards

import com.atomicrobot.carbon.data.api.github.model.Author
import com.atomicrobot.carbon.data.api.github.model.DetailedCommit
import com.atomicrobot.carbon.data.api.github.model.DetailedCommitDetails
import com.atomicrobot.carbon.data.api.github.model.Tree
import com.atomicrobot.carbon.data.api.github.model.Verification

val dummyDetailedCommit = listOf(
    DetailedCommit(
        detailedCommit = DetailedCommitDetails(
            message = "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium",

            author = Author("Test Testerman", email = "email@example.com", date = "2023-04-01"),
            tree = Tree(
                url = "exampletree@example.com"
            ),
            verification = Verification(true)
        )
    )
)
