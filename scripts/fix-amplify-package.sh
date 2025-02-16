#!/bin/bash

# Path to the package.json file
PACKAGE_JSON="node_modules/@aws-amplify/ui-angular/package.json"

# Check if the file exists
if [ ! -f "$PACKAGE_JSON" ]; then
    echo "Error: $PACKAGE_JSON not found"
    exit 1
fi

# Create a temporary file
TMP_FILE=$(mktemp)

# Use jq to modify the exports field
jq '.exports["./theme.css"] = "./theme.css"' "$PACKAGE_JSON" > "$TMP_FILE"

# Check if jq command was successful
if [ $? -ne 0 ]; then
    echo "Error: Failed to modify package.json"
    rm "$TMP_FILE"
    exit 1
fi

# Replace the original file with the modified version
mv "$TMP_FILE" "$PACKAGE_JSON"

echo "Successfully added theme.css to exports in $PACKAGE_JSON"
