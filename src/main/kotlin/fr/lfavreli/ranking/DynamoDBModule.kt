package fr.lfavreli.ranking

import org.koin.dsl.module
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

val dynamoDBModule = module {
    single<DynamoDbClient> {
        DynamoDbClient.builder()
            .region(Region.EU_WEST_1)
            .endpointOverride(java.net.URI("http://localhost:8000"))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create("local", "local"))
            )
            .build()
    }

    single<DynamoDBService> {
        DynamoDBService(get())
    }
}