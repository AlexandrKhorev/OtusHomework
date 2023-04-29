package ru.otus;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.server.ValueServerServiceImpl;

import java.io.IOException;

public class GRPCServer {

    private static final Logger logger = LoggerFactory.getLogger(GRPCServer.class);
    private static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {

        var valueService = new ValueServerServiceImpl();

        Server server = ServerBuilder
                .forPort(SERVER_PORT)
                .addService(valueService)
                .build();

        logger.info("Starting server...");
        server.start();

        logger.info("Server waiting for client connections...");
        server.awaitTermination();
    }
}
