package com.dimaslanjaka.library.helper;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @url https://stackoverflow.com/a/49187188
 */
public class ReadComplexDelimitedFile {
    private static final Pattern FIELD_DELIMITER_PATTERN = Pattern.compile("\\^\\|\\^");
    private static long total = 0;

    public static void main(String[] args) {
        String filepath = Objects.requireNonNull(ReadComplexDelimitedFile.class.getResource("input.txt")).getPath();
        //JVM wamrup
        for (int i = 0; i < 100000; i++) {
            total += i;
        }
        // We know scanner is slow-Still warming up
        ReadComplexDelimitedFile readComplexDelimitedFile = new ReadComplexDelimitedFile();
        List<Long> longList = new ArrayList<>(50);
        for (int i = 0; i < 50; i++) {
            total = 0;
            long startTime = System.nanoTime();
            //readComplexDelimitedFile.readFileUsingScanner();
            long stopTime = System.nanoTime();
            long timeDifference = stopTime - startTime;
            longList.add(timeDifference);

        }
        System.out.println("Time taken for readFileUsingScanner");
        longList.forEach(System.out::println);
        // Actual performance test starts here

        longList = new ArrayList<>(10);
        for (int i = 0; i < 10; i++) {
            total = 0;
            long startTime = System.nanoTime();
            readComplexDelimitedFile.readFileUsingBufferedReaderFileChannel(filepath);
            long stopTime = System.nanoTime();
            long timeDifference = stopTime - startTime;
            longList.add(timeDifference);

        }
        System.out.println("Time taken for readFileUsingBufferedReaderFileChannel");
        longList.forEach(System.out::println);
        longList.clear();
        for (int i = 0; i < 10; i++) {
            total = 0;
            long startTime = System.nanoTime();
            readComplexDelimitedFile.readFileUsingBufferedReader(filepath);
            long stopTime = System.nanoTime();
            long timeDifference = stopTime - startTime;
            longList.add(timeDifference);

        }
        System.out.println("Time taken for readFileUsingBufferedReader");
        longList.forEach(System.out::println);
        longList.clear();
        for (int i = 0; i < 10; i++) {
            total = 0;
            long startTime = System.nanoTime();
            readComplexDelimitedFile.readFileUsingStreams(filepath);
            long stopTime = System.nanoTime();
            long timeDifference = stopTime - startTime;
            longList.add(timeDifference);

        }
        System.out.println("Time taken for readFileUsingStreams");
        longList.forEach(System.out::println);
        longList.clear();
        for (int i = 0; i < 10; i++) {
            total = 0;
            long startTime = System.nanoTime();
            readComplexDelimitedFile.readFileUsingCustomBufferedReader(filepath);
            long stopTime = System.nanoTime();
            long timeDifference = stopTime - startTime;
            longList.add(timeDifference);

        }
        System.out.println("Time taken for readFileUsingCustomBufferedReader");
        longList.forEach(System.out::println);
        longList.clear();
        for (int i = 0; i < 10; i++) {
            total = 0;
            long startTime = System.nanoTime();
            readComplexDelimitedFile.readFileUsingLineReader(filepath);
            long stopTime = System.nanoTime();
            long timeDifference = stopTime - startTime;
            longList.add(timeDifference);
        }
        System.out.println("Time taken for readFileUsingLineReader");
        longList.forEach(System.out::println);
    }

    @SuppressWarnings("unused")
    private void readFileUsingScanner(String absolutePath) {

        String s;
        try (Scanner stdin = new Scanner(new java.io.File(absolutePath))) {
            while (stdin.hasNextLine()) {
                s = stdin.nextLine();
                String[] fields = FIELD_DELIMITER_PATTERN.split(s, 0);
                total = total + fields.length;
            }
        } catch (Exception e) {
            System.err.println("Error");
        }

    }

    //Winner
    private void readFileUsingCustomBufferedReader(String absolutePath) {

        try (CustomBufferedReader stdin = new CustomBufferedReader(new FileReader(new java.io.File(absolutePath)))) {
            String s;
            while ((s = stdin.readLine()) != null) {
                String[] fields = FIELD_DELIMITER_PATTERN.split(s, 0);
                total += fields.length;
            }
        } catch (Exception e) {
            System.err.println("Error");
        }

    }

    private void readFileUsingBufferedReader(String absolutePath) {

        try (BufferedReader stdin = new BufferedReader(new FileReader(new java.io.File(absolutePath)))) {
            String s;
            while ((s = stdin.readLine()) != null) {
                String[] fields = FIELD_DELIMITER_PATTERN.split(s, 0);
                total += fields.length;
            }
        } catch (Exception e) {
            System.err.println("Error");
        }
    }

    private void readFileUsingLineReader(String absolutePath) {
        try (LineNumberReader stdin = new LineNumberReader(new FileReader(new java.io.File(absolutePath)))) {
            String s;
            while ((s = stdin.readLine()) != null) {
                String[] fields = FIELD_DELIMITER_PATTERN.split(s, 0);
                total += fields.length;
            }
        } catch (Exception e) {
            System.err.println("Error");
        }
    }

    private void readFileUsingStreams(String absolutePath) {

        try (Stream<String> stream = Files.lines((new java.io.File(absolutePath)).toPath())) {
            total += stream.mapToInt(s -> FIELD_DELIMITER_PATTERN.split(s, 0).length).sum();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void readFileUsingBufferedReaderFileChannel(String absolutePath) {
        try (FileInputStream fis = new FileInputStream(absolutePath)) {
            try (FileChannel inputChannel = fis.getChannel()) {
                try (CustomBufferedReader stdin = new CustomBufferedReader(Channels.newReader(inputChannel, StandardCharsets.UTF_8))) {
                    String s;
                    while ((s = stdin.readLine()) != null) {
                        String[] fields = FIELD_DELIMITER_PATTERN.split(s, 0);
                        total = total + fields.length;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error");
            }
        } catch (Exception e) {
            System.err.println("Error");
        }

    }
}