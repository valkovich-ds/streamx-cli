#!/usr/bin/env bash
# release.sh - Usage: ./release.sh [patch|minor|major]

set -euo pipefail

TYPE=${1:-patch}
BRANCH=$(git rev-parse --abbrev-ref HEAD)

# Get current version from pom.xml
CURRENT=$(mvn help:evaluate -Dexpression=project.version -q -DforceStdout)
echo "Current version: $CURRENT"
echo "Branch: $BRANCH"

# Ensure it's a SNAPSHOT
if [[ "$CURRENT" != *-SNAPSHOT ]]; then
  echo "Error: Current version ($CURRENT) is not a SNAPSHOT. Nothing to release."
  exit 1
fi

# Strip -SNAPSHOT
BASE=${CURRENT%-SNAPSHOT}

IFS='.' read -r MAJOR MINOR PATCH <<< "$BASE"

# Calculate release version
RELEASE="${MAJOR}.${MINOR}.${PATCH}"

# Calculate next SNAPSHOT version
case "$TYPE" in
  patch) NEXT="${MAJOR}.${MINOR}.$((PATCH + 1))-SNAPSHOT" ;;
  minor) NEXT="${MAJOR}.$((MINOR + 1)).0-SNAPSHOT" ;;
  major) NEXT="$((MAJOR + 1)).0.0-SNAPSHOT" ;;
  *) echo "Usage: $0 [patch|minor|major]"; exit 1 ;;
esac

echo ""
echo "Release version:         $RELEASE"
echo "Next development version: $NEXT"
echo ""
read -p "Are you sure you want to release $RELEASE? [y/N] " -n 1 -r
echo
[[ $REPLY =~ ^[Yy]$ ]] || { echo "Aborted."; exit 0; }

# Set release version, commit, tag
mvn versions:set -DnewVersion="$RELEASE" -DgenerateBackupPoms=false
git add pom.xml
git commit -m "Release $RELEASE [skip ci]"
git tag "$RELEASE"

# Set next SNAPSHOT version, commit
mvn versions:set -DnewVersion="$NEXT" -DgenerateBackupPoms=false
git add pom.xml
git commit -m "Prepare for next development iteration $NEXT [skip ci]"

# Push branch first (skipped by CI), then tag (triggers the real build+release)
git push origin "$BRANCH"
git push origin "$RELEASE"

echo "Done! Released $RELEASE, now on $NEXT"