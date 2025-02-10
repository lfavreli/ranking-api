#!/bin/bash

BASE_URL='http://localhost:8080/api/ranking/v1'

# Delete and initialize the database
curl --location --request DELETE "$BASE_URL/db"
curl --location "$BASE_URL/db"

# Function to add a player and extract playerId
add_player() {
  RESPONSE=$(curl --silent --location "$BASE_URL/players" \
  --header 'Content-Type: application/json' \
  --data "{ \"displayName\": \"$1\" }")
  PLAYER_ID=$(echo "$RESPONSE" | sed -n 's/.*"playerId":"\([^"]*\)".*/\1/p')
  echo "$PLAYER_ID"
}

# Function to create a tournament and extract tournamentId
create_tournament() {
  RESPONSE=$(curl --silent --location "$BASE_URL/tournaments" \
  --header 'Content-Type: application/json' \
  --data '{
    "description": "Tournament 1",
    "order": "LARGER_IS_BETTER"
  }')
  TOURNAMENT_ID=$(echo "$RESPONSE" | sed -n 's/.*"tournamentId":"\([^"]*\)".*/\1/p')
  echo "$TOURNAMENT_ID"
}

# Create tournament and store ID
TOURNAMENT_ID=$(create_tournament)

# Add players and store their IDs
ALICE_ID=$(add_player "Alice")
BOB_ID=$(add_player "Bob")
CAROL_ID=$(add_player "Carol")

# Function to update player score in the tournament
update_score() {
  curl --location --request PATCH "$BASE_URL/tournaments/$TOURNAMENT_ID/players/$1/score" \
  --header 'Content-Type: application/json' \
  --data "{ \"newScore\": $2 }"
}

# Update scores for players
update_score "$ALICE_ID" 99  # Alice
update_score "$BOB_ID" 85    # Bob
update_score "$CAROL_ID" 72  # Carol
