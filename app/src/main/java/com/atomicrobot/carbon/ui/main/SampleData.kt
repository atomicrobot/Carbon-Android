package com.atomicrobot.carbon.ui.main

import com.atomicrobot.carbon.data.api.github.model.*

val dummyCommits = listOf(
    Commit(commit = CommitDetails(message = "Sample github commit message #1", author = Author("Smitty Joe"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #2", author = Author("Joe Smitty"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #3", author = Author("Smith Joe"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #4", author = Author("Joe Smith"))),
    Commit(commit = CommitDetails(message = "Sample github commit message #5", author = Author("Joe Smoe")))
)

val dummyDetailedCommits = listOf(
    DetailedCommit(detailedCommit = DetailedCommitDetails(
        message = "A test message passed through to a preview, but what happens if the message is really " +
                "long. Like really really long, like, ok, I'm just going to find a block of text to copy," +
                "Sed ut perspiciatis, unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam eaque ipsa, quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt, explicabo. Nemo enim ipsam voluptatem, quia voluptas sit, aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos, qui ratione voluptatem sequi nesciunt, neque porro quisquam est, qui dolorem ipsum, quia dolor sit amet consectetur adipisci[ng] velit, sed quia non numquam [do] eius modi tempora inci[di]dunt, ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum[d] exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? [D]Quis autem vel eum i[r]ure reprehenderit, qui in ea voluptate velit esse, quam nihil molestiae consequatur, vel illum, qui dolorem eum fugiat, quo voluptas nulla pariatur? [33] At vero eos et accusamus et iusto odio dignissimos ducimus, qui blanditiis praesentium voluptatum deleniti atque corrupti, quos dolores et quas molestias excepturi sint, obcaecati cupiditate non provident, similique sunt in culpa, qui officia deserunt mollitia animi, id est laborum et dolorum fuga. Et harum quidem rerum facilis est et expedita distinctio. Nam libero tempore, cum soluta nobis est eligendi optio, cumque nihil impedit, quo minus id, quod maxime placeat, facere possimus, omnis voluptas assumenda est, omnis dolor repellendus. Temporibus autem quibusdam et aut officiis debitis aut rerum necessitatibus saepe eveniet, ut et voluptates repudiandae sint et molestiae non recusandae. Itaque earum rerum hic tenetur a sapiente delectus, ut aut reiciendis voluptatibus maiores alias consequatur aut perferendis doloribus asperiores repellat.",
        author = Author("Test Testerman"),
        node_id = "A_Node_ID_String",
        html_url = "example@example.com",
        tree = Tree(
            path = "A_path_to_a_tree",
            type = "A Tree Type",
            size = 1,
            url = "exampletree@example.com"),
        )
    )
)
