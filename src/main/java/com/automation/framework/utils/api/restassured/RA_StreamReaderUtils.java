package com.automation.framework.utils.api.restassured;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import io.restassured.response.Response;
import java.util.function.Consumer;

public class RA_StreamReaderUtils {

    public static List<String> readStreamForDuration(Response response, long durationMillis,
        Consumer<String> lineConsumer) throws InterruptedException {
        List<String> lines = new ArrayList<>();
        InputStream stream = response.asInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        long start = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(1);

        Thread streamReaderThread = new Thread(() -> {
            try {
                String line;
                while ((System.currentTimeMillis() - start) < durationMillis &&
                        (line = reader.readLine()) != null) {
                    if (lineConsumer != null) {
                        lineConsumer.accept(line);
                    }
                    lines.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeResources(reader, stream);
                latch.countDown();
            }
        });

        streamReaderThread.start();

        // Wait for the thread to complete with a small buffer time
        latch.await(durationMillis + 2000, TimeUnit.MILLISECONDS);

        return lines;
    }

    /**
     * Overloaded method that prints each line to System.out
     * 
     * @param response       The streaming response from REST-assured
     * @param durationMillis How long to read the stream in milliseconds
     * @return List of all lines read from the stream
     * @throws InterruptedException If thread is interrupted
     */
    public static List<String> readStreamForDuration(Response response, long durationMillis)
            throws InterruptedException {
        return readStreamForDuration(response, durationMillis,
                line -> System.out.println("Stream: " + line));
    }

    /**
     * Helper method to safely close resources
     */
    private static void closeResources(BufferedReader reader, InputStream stream) {
        try {
            if (reader != null) {
                reader.close();
            }
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /// Helper method to read the full stream as a String for a given duration
    public static String readFullStreamAsString(Response response, long durationMillis) throws InterruptedException {
        InputStream stream = response.asInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder fullStream = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(1);
    
        Thread streamReaderThread = new Thread(() -> {
            try {
                String line;
                long start = System.currentTimeMillis();
                while ((System.currentTimeMillis() - start) < durationMillis &&
                        (line = reader.readLine()) != null) {
                    fullStream.append(line).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeResources(reader, stream);
                latch.countDown();
            }
        });
    
        streamReaderThread.start();
        latch.await(durationMillis + 2000, TimeUnit.MILLISECONDS);
        return fullStream.toString();
    }
    
}
