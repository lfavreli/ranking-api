package fr.lfavreli.ranking.features.dynamodb

import fr.lfavreli.ranking.repository.DynamoDBRepository
import org.koin.core.annotation.Single
import software.amazon.awssdk.services.dynamodb.model.*

@Single
class CreateDatabaseHandler(private val dynamoDBRepository: DynamoDBRepository) {

    fun handle() {
        val createTournamentTableRequest = createTournamentTableRequest()
        dynamoDBRepository.createTable(createTournamentTableRequest)

        val createPlayerTableRequest = createPlayerTableRequest()
        dynamoDBRepository.createTable(createPlayerTableRequest)

        val createLeaderboardTableRequest = createLeaderboardTableRequest()
        dynamoDBRepository.createTable(createLeaderboardTableRequest)
    }

    private fun createTournamentTableRequest(): CreateTableRequest {
        return CreateTableRequest.builder()
            .tableName(TOURNAMENT_TABLE)
            .keySchema(KeySchemaElement.builder().attributeName(TOURNAMENT_ID).keyType(KeyType.HASH).build())
            .attributeDefinitions(AttributeDefinition.builder().attributeName(TOURNAMENT_ID).attributeType(ScalarAttributeType.S).build())
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build()
    }

    private fun createPlayerTableRequest(): CreateTableRequest {
        return CreateTableRequest.builder()
            .tableName(PLAYER_TABLE)
            .keySchema(KeySchemaElement.builder().attributeName(PLAYER_ID).keyType(KeyType.HASH).build())
            .attributeDefinitions(AttributeDefinition.builder().attributeName(PLAYER_ID).attributeType(ScalarAttributeType.S).build())
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build()
    }

    private fun createLeaderboardTableRequest(): CreateTableRequest {
        return CreateTableRequest.builder()
            .tableName(LEADERBOARD_TABLE)
            .keySchema(
                listOf(
                    KeySchemaElement.builder().attributeName(TOURNAMENT_ID).keyType(KeyType.HASH).build(),
                    KeySchemaElement.builder().attributeName(PLAYER_ID).keyType(KeyType.RANGE).build()
                )
            )
            .attributeDefinitions(
                listOf(
                    AttributeDefinition.builder().attributeName(TOURNAMENT_ID).attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName(PLAYER_ID).attributeType(ScalarAttributeType.S).build(),
                    AttributeDefinition.builder().attributeName(LEADERBOARD_SCORE).attributeType(ScalarAttributeType.N).build()
                )
            )
            .globalSecondaryIndexes(
                listOf(
                    GlobalSecondaryIndex.builder()
                        .indexName(LEADERBOARD_SCORE_INDEX) // GSI for sorting by score
                        .keySchema(
                            listOf(
                                KeySchemaElement.builder().attributeName(TOURNAMENT_ID).keyType(KeyType.HASH).build(),
                                KeySchemaElement.builder().attributeName(LEADERBOARD_SCORE).keyType(KeyType.RANGE).build()
                            )
                        )
                        .projection(Projection.builder().projectionType(ProjectionType.ALL).build()) // Include all attributes in GSI
                        .build()
                )
            )
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .build()
    }
}
