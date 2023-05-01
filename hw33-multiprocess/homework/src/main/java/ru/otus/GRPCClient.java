package ru.otus;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import ru.otus.client.ValueClient;
import ru.otus.client.ValueClientImpl;

public class GRPCClient {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;


    public static void main(String[] args) {

        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();

        ValueClient client = new ValueClientImpl(channel);

        client.start();
        channel.shutdown();
    }
}
