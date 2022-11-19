#!/bin/sh
if [ -z "$1" ] || [ "-h" = "$1" ] || [ "--help" = "$1" ]
then
  echo "Usage: release.sh <version>"
  exit 1
fi

VERSION="$1"

if [ "$(git diff HEAD --name-only | wc -l)" -gt 0 ]
then
  echo "Error: You have uncommitted changes!"
  exit 1
fi

echo "Tagging commit ..."
git tag "$VERSION"

echo "Pushing commit ..."
git push origin --force-with-lease

echo "Pushing tag ..."
git push origin "$VERSION"