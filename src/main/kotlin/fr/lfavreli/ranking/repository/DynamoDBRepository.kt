package fr.lfavreli.ranking.repository

import fr.lfavreli.ranking.exception.InternalServerErrorException
import org.koin.core.annotation.Single
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.*

@Single
class DynamoDBRepository(private val client: DynamoDbClient) {

    fun createTable(createTableRequest: CreateTableRequest) {
        try {
            client.createTable(createTableRequest)
        } catch (e: Exception) {
            throw InternalServerErrorException("Error creating table!", e)
        }
    }

    fun deleteTable(deleteTableRequest: DeleteTableRequest) {
        try {
            client.deleteTable(deleteTableRequest)
        } catch (e: Exception) {
            throw InternalServerErrorException("Error deleting table!", e)
        }
    }

    fun getItem(
        tableName: String,
        key: Map<String, AttributeValue>
    ): MutableMap<String, AttributeValue> {
        return try {
            client.getItem(
                GetItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .build()
            ).item()
        } catch (e: Exception) {
            throw InternalServerErrorException("Error fetching item from $tableName!", e)
        }
    }

    fun putItem(
        tableName: String,
        item: Map<String, AttributeValue>
    ) {
        try {
            client.putItem(
                PutItemRequest.builder()
                    .tableName(tableName)
                    .item(item)
                    .build()
            )
        } catch (e: Exception) {
            throw InternalServerErrorException("\"Error putting item into $tableName!", e)
        }
    }

    fun deleteItem(
        tableName: String,
        key: Map<String, AttributeValue>? = null
    ) {
        try {
            client.deleteItem(
                DeleteItemRequest.builder()
                    .tableName(tableName)
                    .key(key)
                    .build()
            )
        } catch (e: Exception) {
            throw InternalServerErrorException("Error deleting item from !", e)
        }
    }

    fun scan(
        tableName: String,
        filterExpression: String? = null,
        expressionAttributeValues: Map<String, AttributeValue>? = null
    ): List<MutableMap<String, AttributeValue>> {
        return try {
            client.scan(
                ScanRequest.builder()
                    .tableName(tableName)
                    .filterExpression(filterExpression)
                    .expressionAttributeValues(expressionAttributeValues)
                    .build()
            ).items()
        } catch (e: Exception) {
            throw InternalServerErrorException("Error scanning $tableName!", e)
        }
    }

    fun query(
        tableName: String,
        indexName: String? = null,
        keyConditionExpression: String,
        expressionAttributeValues: Map<String, AttributeValue>,
        scanIndexForward: Boolean = true
    ): MutableList<MutableMap<String, AttributeValue>> {
        return try {
            client.query(
                QueryRequest.builder()
                    .tableName(tableName)
                    .indexName(indexName)
                    .keyConditionExpression(keyConditionExpression)
                    .expressionAttributeValues(expressionAttributeValues)
                    .scanIndexForward(scanIndexForward)
                    .build())
                .items()
        } catch (e: Exception) {
            throw InternalServerErrorException("Error querying $tableName with key condition expression '$keyConditionExpression'!", e)
        }
    }
}
