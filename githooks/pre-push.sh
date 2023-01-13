#!/usr/bin/env bash

if [[ -n $(git status -s -uno) ]] ; then
    echo "There are uncommitted changes, aborting checks."
    exit 1
fi

echo "Running code checks..."

# Run quick static code checks
./gradlew check testing --daemon

status=$?

if [[ "$status" = 0 ]] ; then
    echo "Code checks found no problems."
    exit 0
else
    echo 1>&2 "Code checks found violations it could not fix."
    exit 1
fi