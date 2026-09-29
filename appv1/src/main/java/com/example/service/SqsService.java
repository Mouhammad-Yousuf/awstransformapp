package com.example.service;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.CreateQueueResult;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.amazonaws.AmazonServiceException;

import java.util.List;

public class SqsService {

    private final AmazonSQS sqsClient;

    public SqsService() {
        this.sqsClient = AmazonSQSClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();
    }

    public String createQueue(String queueName) {
        CreateQueueRequest request = new CreateQueueRequest(queueName);
        CreateQueueResult result = sqsClient.createQueue(request);
        return result.getQueueUrl();
    }

    public SendMessageResult sendMessage(String queueUrl, String messageBody) {
        SendMessageRequest request = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);
        return sqsClient.sendMessage(request);
    }

    public List<Message> receiveMessages(String queueUrl, int maxMessages) {
        try {
            ReceiveMessageRequest request = new ReceiveMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMaxNumberOfMessages(maxMessages)
                    .withWaitTimeSeconds(10);
            ReceiveMessageResult result = sqsClient.receiveMessage(request);
            return result.getMessages();
        } catch (AmazonServiceException e) {
            System.err.println("Error receiving messages: " + e.getErrorMessage());
            throw e;
        }
    }

    public void deleteMessage(String queueUrl, String receiptHandle) {
        sqsClient.deleteMessage(queueUrl, receiptHandle);
    }
}
