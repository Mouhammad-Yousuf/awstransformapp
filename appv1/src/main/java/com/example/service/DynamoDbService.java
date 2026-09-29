package com.example.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.PutItemResult;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.AmazonClientException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DynamoDbService {

    private final AmazonDynamoDB dynamoDbClient;

    public DynamoDbService() {
        this.dynamoDbClient = AmazonDynamoDBClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    public PutItemResult putItem(String tableName, Map<String, AttributeValue> item) {
        PutItemRequest request = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);
        return dynamoDbClient.putItem(request);
    }

    public Map<String, AttributeValue> getItem(String tableName, String keyName, String keyValue) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(keyName, new AttributeValue().withS(keyValue));

        GetItemRequest request = new GetItemRequest()
                .withTableName(tableName)
                .withKey(key);

        GetItemResult result = dynamoDbClient.getItem(request);
        return result.getItem();
    }

    public List<Map<String, AttributeValue>> scanTable(String tableName) {
        try {
            ScanRequest request = new ScanRequest().withTableName(tableName);
            ScanResult result = dynamoDbClient.scan(request);
            return result.getItems();
        } catch (AmazonClientException e) {
            System.err.println("Error scanning table: " + e.getMessage());
            throw e;
        }
    }
}
