package fr.lfavreli.ranking.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import java.net.URI

@Module
@ComponentScan("fr.lfavreli.ranking")
class AppModule {

    @Single
    fun dynamoDbClient(): DynamoDbClient {
        val endpoint = System.getenv("DYNAMODB_ENDPOINT") ?: "http://localhost:8000"
        return DynamoDbClient.builder()
            .region(Region.EU_WEST_1)
            .endpointOverride(URI(endpoint))
            .credentialsProvider(
                StaticCredentialsProvider.create(AwsBasicCredentials.create("local", "local"))
            )
            .build()
    }

}
