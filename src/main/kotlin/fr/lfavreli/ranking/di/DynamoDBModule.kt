package fr.lfavreli.ranking.di

import org.koin.dsl.module
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

val dynamoDBModule = module {
    single<DynamoDbClient> {
        val endpoint = System.getenv("DYNAMODB_ENDPOINT") ?: "http://localhost:8000"
        DynamoDbClient.builder()
            .region(Region.EU_WEST_1)
            .endpointOverride(java.net.URI(endpoint))
            .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("local", "local")))
            .build()
    }
}
