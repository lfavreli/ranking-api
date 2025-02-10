package fr.lfavreli.ranking.features.tournaments

import fr.lfavreli.ranking.features.tournaments.model.CreateTournamentRequest
import fr.lfavreli.ranking.features.tournaments.model.Tournament
import fr.lfavreli.ranking.repository.TournamentRepository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.time.OffsetDateTime
import java.util.*

private const val NO_PLAYERS = 0

fun createTournamentHandler(request: CreateTournamentRequest, dynamoDbClient: DynamoDbClient): Tournament {
    // 1. Generate tournament details
    val tournament = generateTournamentEntity(request)

    // 2. Save the tournament in the database
    TournamentRepository.save(tournament, dynamoDbClient)

    return tournament
}

private fun generateTournamentEntity(request: CreateTournamentRequest): Tournament {
    val tournamentId = UUID.randomUUID().toString()
    val lastUpdated = OffsetDateTime.now().toString()

    return Tournament(
        tournamentId = tournamentId,
        description = request.description,
        order = request.order,
        numPlayers = NO_PLAYERS, // Assuming the tournament has no players yet
        lastUpdated = lastUpdated
    )
}
