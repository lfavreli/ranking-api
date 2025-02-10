#!/bin/bash

BASE_URL='http://localhost:8080/api/ranking/v1'

# Delete and initialize the database
curl --location --request DELETE "$BASE_URL/db"
curl --location "$BASE_URL/db"

# Function to add a player
add_player() {
  curl --location "$BASE_URL/players" \
  --header 'Content-Type: application/json' \
  --data "{ \"displayName\": \"$1\" }"
}

# Add players
add_player "Alice"
add_player "Bob"
add_player "Carol"

# Create a tournament
curl --location "$BASE_URL/tournaments" \
--header 'Content-Type: application/json' \
--data '{
  "description": "Tournament 1",
  "order": "LARGER_IS_BETTER"
}'
