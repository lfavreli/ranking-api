package fr.lfavreli.ranking

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest

class DynamoDBService(private val client: DynamoDbClient) {

    fun putItem(tableName: String, id: String, data: String) {
        val item = mapOf(
            "id" to AttributeValue.builder().s(id).build(),
            "data" to AttributeValue.builder().s(data).build()
        )

        val request = PutItemRequest.builder()
            .tableName(tableName)
            .item(item)
            .build()

        client.putItem(request)
    }


}