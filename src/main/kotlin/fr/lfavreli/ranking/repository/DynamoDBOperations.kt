package fr.lfavreli.ranking.repository

import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*

object DynamoDBOperations {

    fun getItem(
        tableName: String,
        key: Map<String, AttributeValue>,
        client: DynamoDbClient
    ): MutableMap<String, AttributeValue> {
        return try {
            client.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build()
            ).item()
        } catch (e: Exception) {
            throw RuntimeException("Error fetching item from $tableName: ${e.message}", e)
        }
    }

    fun putItem(
        tableName: String,
        item: Map<String, AttributeValue>,
        client: DynamoDbClient
    ) {
        try {
            client.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build()
            )
        } catch (e: Exception) {
            throw RuntimeException("Error putting item into $tableName: ${e.message}", e)
        }
    }

    fun scan(
        tableName: String,
        client: DynamoDbClient,
        filterExpression: String? = null,
        expressionAttributeValues: Map<String, AttributeValue>? = null
    ): List<MutableMap<String, AttributeValue>> {
        return try {
            val requestBuilder = ScanRequest.builder().tableName(tableName)
            filterExpression?.let { requestBuilder.filterExpression(it) }
            expressionAttributeValues?.let { requestBuilder.expressionAttributeValues(it) }
            client.scan(requestBuilder.build()).items()
        } catch (e: Exception) {
            throw RuntimeException("Error scanning $tableName: ${e.message}", e)
        }
    }

    fun query(
        tableName: String,
        indexName: String? = null,
        keyConditionExpression: String,
        expressionAttributeValues: Map<String, AttributeValue>,
        scanIndexForward: Boolean = true,
        client: DynamoDbClient
    ): QueryResponse {
        return try {
            val queryRequest = QueryRequest.builder()
                .tableName(tableName)
                .indexName(indexName)
                .keyConditionExpression(keyConditionExpression)
                .expressionAttributeValues(expressionAttributeValues)
                .scanIndexForward(scanIndexForward)
                .build()

            client.query(queryRequest)
        } catch (e: Exception) {
            throw RuntimeException("Error querying $tableName with key condition expression '$keyConditionExpression': ${e.message}", e)
        }
    }


}
