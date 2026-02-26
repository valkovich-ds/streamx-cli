#!/usr/bin/env bash

set -euo pipefail

if ! command -v docker &> /dev/null; then
  echo "Docker not found, skipping docker log collection."
  exec "$@"
fi

LOGS_DIR="${DOCKER_LOGS_DIR:-target/docker-logs}"

mkdir -p "$LOGS_DIR"

# React to container starts and follow their logs in the background
docker events --filter 'event=start' --format '{{.Actor.Attributes.name}} {{.ID}}' | while read -r name id; do
  short_id="${id:0:12}"
  docker logs -f "$name" >> "${LOGS_DIR}/${name}-${short_id}.log" 2>&1 &
done &
EVENTS_PID=$!

"$@"
EXIT_CODE=$?

kill $EVENTS_PID 2>/dev/null || true
# Kill any remaining background `docker logs -f` processes
pkill -P $EVENTS_PID 2>/dev/null || true
wait 2>/dev/null || true

exit $EXIT_CODE