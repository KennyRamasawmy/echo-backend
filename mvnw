#!/bin/sh
# Maven Wrapper script

set -e

# Find project base directory
MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-$(cd "$(dirname "$0")" && pwd)}"

# Download Maven if necessary
MAVEN_HOME="${MAVEN_HOME:-$HOME/.m2/wrapper/dists/apache-maven-3.9.6}"
if [ ! -d "$MAVEN_HOME" ]; then
    echo "Downloading Maven..."
    mkdir -p "$MAVEN_HOME"
    curl -fsSL https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/3.9.6/apache-maven-3.9.6-bin.tar.gz | tar -xz -C "$MAVEN_HOME" --strip-components=1
fi

exec "$MAVEN_HOME/bin/mvn" "$@"
