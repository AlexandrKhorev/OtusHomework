package ru.otus.client;

import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.generated.RemoteValueServiceGrpc;
import ru.otus.generated.ValueMessage;
import ru.otus.generated.ValueResultMessage;

import java.util.concurrent.CountDownLatch;

public class ValueClientImpl implements ValueClient {

    private static final Logger logger = LoggerFactory.getLogger(ValueClientImpl.class);

    private final RemoteValueServiceGrpc.RemoteValueServiceStub stub;

    private final int CYCLE_COUNT = 50;
    private final int FIRST_VALUE = 0;
    private final int LAST_VALUE = 30;

    private int lastValueFromServer = 0;
    private int currentValue = 0;


    public ValueClientImpl(ManagedChannel channel) {
        this.stub = RemoteValueServiceGrpc.newStub(channel);
    }


    @Override
    public void start() {
        // Инициализируем получение значений от сервера.
        // Запускаем цикл от 0 до CYCLE_COUNT.
        // В лог пишется текущее значение, увеличивается на 1, прибавляется число от сервера (lastValueFromServer)
        // lastValueFromServer обнуляется после прибавления, чтобы на следующей итерации оно не учитывалось
        // (пока не придет другое число от сервера).
        // Если пришло значение от сервера, оно присваивается к lastValueFromServer.
        nextValueFromServer();

        for (int count = 0; count <= CYCLE_COUNT; count++) {
            nextCurrentValue();
            logger.info("CurrentValue: {}", currentValue);
            sleep();
        }

        logger.info("Cycle from 0 to {} completed", CYCLE_COUNT);
    }

    private void nextValueFromServer() {

        var latch = new CountDownLatch(1);
        StreamObserver<ValueResultMessage> observer = new StreamObserver<ValueResultMessage>() {
            @Override
            public void onNext(ValueResultMessage value) {
                int valueFromServer = value.getResult();
                logger.info("Get result from server {}", value);
                lastValueFromServer = valueFromServer;
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                logger.info("finished getting values from the server ");
                latch.countDown();
            }
        };

        ValueMessage message = ValueMessage
                .newBuilder()
                .setFirstValue(FIRST_VALUE)
                .setLastValue(LAST_VALUE)
                .build();

        stub.getValue(message, observer);
    }

    private void nextCurrentValue() {
        currentValue = currentValue + lastValueFromServer + 1;
        lastValueFromServer = 0;
    }

    private void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
