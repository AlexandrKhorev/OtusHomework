package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.lib.SensorDataBufferedWriter;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> bufferData;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        // Используется PriorityBlockingQueue, чтобы извлекать объекты в порядке возрастания времени измерения
        this.bufferData = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        if (bufferData.size() >= bufferSize) {
            log.info("Буфер заполнен -> запись и очистка буфера");
            flush();
        }
        if (bufferData.offer(data)) {
            log.info("Данные добавлены в буфер: {}", data);
        }
    }

    public void flush() {
        try {
            if (bufferData.size() == 0) {
                log.info("Буфер пуст");
            } else {
                var bufferToWrite = new ArrayList<SensorData>();
                bufferData.drainTo(bufferToWrite);
                writer.writeBufferedData(bufferToWrite);
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
