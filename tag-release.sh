#!/bin/bash

VERSION=`head -n 1 tag.version`

if [ "$TRAVIS_TAG" != "" ]; then
    echo "Not releasing when tag"
    exit
fi

if [ "$VERSION" != "" ]; then
    API_JSON=$(printf '{"tag_name": "jtwig-%s","target_commitish": "master","name": "jtwig-%s","body": "Release of version %s","draft": false,"prerelease": false}' $VERSION $VERSION $VERSION)
    curl --data "$API_JSON" https://api.github.com/repos/jtwig/jtwig/releases?access_token=$GITHUB_API_KEY
fi