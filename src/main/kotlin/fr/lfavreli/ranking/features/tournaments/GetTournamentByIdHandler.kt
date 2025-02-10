package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.Tournament
import fr.lfavreli.ranking.repository.TournamentRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

fun getTournamentByIdHandler(tournamentId: String, dynamoDbClient: DynamoDbClient) : Tournament {
    // 1. Retrieve player details from Player table
    return TournamentRepository.getById(tournamentId, dynamoDbClient)
}
