#!/bin/sh
if [ -z "$1" ] || [ "-h" = "$1" ] || [ "--help" = "$1" ]
then
  echo "Usage: prepare-release.sh <version>"
  exit 1
fi

VERSION="$1"

echo "Setting \`project.version\` property ..."
./mvnw versions:set -DnewVersion="$VERSION" && rm --force pom.xml.versionsBackup

echo "Copying source file to project root and removing package declaration from source file ..."
rm JsonWheel.java \
  && tail src/main/java/com/romanboehm/jsonwheel/JsonWheel.java --lines=+3 > JsonWheel.java

echo "Substituting version in hyperlink to source file ..."
export VERSION \
  && rm --force README.md \
  && envsubst < README.md.template > README.md \

exit 0