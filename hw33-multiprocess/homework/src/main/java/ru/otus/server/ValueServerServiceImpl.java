package ru.otus.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.generated.RemoteValueServiceGrpc;
import ru.otus.generated.ValueMessage;
import ru.otus.generated.ValueResultMessage;

public class ValueServerServiceImpl extends RemoteValueServiceGrpc.RemoteValueServiceImplBase {

    private final Logger logger = LoggerFactory.getLogger(ValueServerServiceImpl.class);

    @Override
    public void getValue(ValueMessage request, StreamObserver<ValueResultMessage> responseObserver) {
        // Запускается цикл от полученных начального до конечного значений.
        // В цикле отправляется значение счетчика, счетчик инкрементируется, сервер засыпает на 2 секунды.

        int count = request.getFirstValue();
        logger.info("Get request from client: firstValue - {}, lastValue - {}", request.getFirstValue(), request.getLastValue());

        while (count <= request.getLastValue()) {
            logger.info("Send value - {}", count);
            responseObserver.onNext(getNextValue(count));
            count++;
            sleep();
        }
        responseObserver.onCompleted();
    }

    private ValueResultMessage getNextValue(int value) {
        return ValueResultMessage.newBuilder().setResult(value + 1).build();
    }

    private void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
