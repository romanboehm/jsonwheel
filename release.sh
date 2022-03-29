#!/bin/sh
if [ -z "$1" ] || [ "-h" = "$1" ] || [ "--help" = "$1" ]
then
  echo "Usage: release.sh <version>"
  exit 1
fi

VERSION="$1"

echo "Tagging commit ..."
git tag "$VERSION"

echo "Pushing commit ..."
git push origin

echo "Pushing tag ..."
git push origin "$VERSION"