#!/bin/sh
if [ -z "$1" ] || [ "-h" = "$1" ] || [ "--help" = "$1" ]
then
  echo "Usage: prepare-release.sh <version>"
  exit 1
fi

VERSION="$1"

echo "Setting \`project.version\` property ..."
./mvnw versions:set -DnewVersion="$VERSION" && rm --force pom.xml.versionsBackup

echo "Copying source file to project root ..."
cp --force src/main/java/com/romanboehm/jsonwheel/JsonWheel.java JsonWheel.java

echo "Removing package declaration from source file ..."
sed --in-place=.bak '1,2d' JsonWheel.java && rm JsonWheel.java.bak

echo "Substituting version in hyperlink to source file ..."
export VERSION \
  && envsubst < README.md > README.md.subst \
  && mv README.md.subst README.md \
  && rm --force README.md.subst

exit 0