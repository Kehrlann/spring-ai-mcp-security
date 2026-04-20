#!/usr/bin/env bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ROOT="$SCRIPT_DIR/.."

herokujar --app=mcp-auth-server --jdk=25 "$ROOT/authorization-server/target/authorization-server-0.0.1-SNAPSHOT.jar"
herokujar --app=mcp-server-appointment --jdk=25 "$ROOT/mcp-server-appointment/target/mcp-server-appointment-0.0.1-SNAPSHOT.jar"

