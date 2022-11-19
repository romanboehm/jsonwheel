#!/bin/sh
if [ -z "$1" ] || [ "-h" = "$1" ] || [ "--help" = "$1" ]
then
  echo "Usage: prepare-release.sh <version>"
  exit 1
fi

VERSION="$1"

echo "Running tests ..."
./mvnw test
if [ "$?" -eq 1 ]
then
  echo "Error: Fix test failures."
  exit 1
fi

echo "Setting \`project.version\` property ..."
./mvnw versions:set -DnewVersion="$VERSION" \
  && rm -f pom.xml.versionsBackup

echo "Copying source file to project root and removing package declaration from source file ..."
rm -f JsonWheel.java \
  && tail src/main/java/com/romanboehm/jsonwheel/JsonWheel.java --lines=+3 > JsonWheel.java

echo "Substituting version in hyperlink to source file ..."
export VERSION \
  && rm -f README.md \
  && envsubst < README.md.template > README.md \

exit 0