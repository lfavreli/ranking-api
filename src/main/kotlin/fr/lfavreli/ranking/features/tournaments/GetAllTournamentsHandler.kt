package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.Tournament
import fr.lfavreli.ranking.repository.TournamentRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun getAllTournamentsHandler(dynamoDbClient: DynamoDbClient): List<Tournament> {
    // 1. Retrieve all Tournaments
    return TournamentRepository.fetchAll(dynamoDbClient)
}
