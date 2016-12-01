package com.yadwadkar.learning;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

class Main {
    public static void main(String[] args) {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            BatchSeries es = BatchSeries.fromReader(in);
            Result r = es.getResult();
            System.out.println(r.toString());
        } catch (NumberFormatException | IOException n) {
            System.out.println("FAILURE => WRONG INPUT (LINE 1)");
        }
    }

}

class BatchSeries {

    int expectedLines = 0;
    Map<Integer, Batch> lineToBatchMap = new HashMap<>();

    static BatchSeries fromReader(BufferedReader in) throws IOException, NumberFormatException {
        BatchSeries inEvents = new BatchSeries();
        String line = in.readLine();
        int i = 1;
        while (line != null) {
            if (i == 1) {
                inEvents.expectedLines = validateNumTestCases(line);
            } else {
                Batch batch = validateAndGetBatch(line);
                inEvents.lineToBatchMap.put(i, batch);
            }
            i++;
            line = in.readLine();
        }

        return inEvents;
    }

    Result getResult() {
        Result result = new Result();

        int numLinesProcessed = 0;

        for (Map.Entry<Integer, Batch> batchEntry : lineToBatchMap.entrySet()) {
            if (batchEntry.getKey() - 1 > expectedLines || batchEntry.getValue().status.equals(BatchInputStatus.INVALID)) {
                LineResult lineResult = LineResult.builder()
                        .withReason("WRONG INPUT (LINE " + batchEntry.getKey() + ")")
                        .withStatus(ResultStatus.FAILURE)
                        .build();
                result.resultMap.put(batchEntry.getKey(), lineResult);
            } else {
                LineResult lineResult = isConsistent(batchEntry);
                result.resultMap.put(batchEntry.getKey(), lineResult);
            }
            numLinesProcessed++;
        }

        if (numLinesProcessed != expectedLines) {
            int deficitLines = expectedLines - numLinesProcessed;
            for (int i = 0; i < deficitLines; i++) {
                int lineNum = numLinesProcessed + 2 + i;

                LineResult lr = LineResult.builder()
                        .withReason("WRONG INPUT (LINE " + lineNum + ")")
                        .withStatus(ResultStatus.FAILURE)
                        .build();
                result.resultMap.put(lineNum, lr);
            }
        }
        return result;
    }

    private LineResult isConsistent(Map.Entry<Integer, Batch> batchEntry) {
        Set<Integer> processedInts = new HashSet<>();

        List<Integer> inputIds = batchEntry.getValue().inputIds;
        int max = -1;

        for (Integer integer : inputIds) {
            if (integer > max) {
                max = integer;
            }

            if (processedInts.contains(integer)) {
                return LineResult.builder()
                        .withReason("WRONG INPUT (LINE " + batchEntry.getKey() + ")")
                        .withStatus(ResultStatus.FAILURE)
                        .build();
            } else {
                processedInts.add(integer);
            }
        }

        for (int i = 1; i <= max; i++) {
            if (!processedInts.contains(i)) {
                return LineResult.builder()
                        .withReason("RECEIVED: " + processedInts.size() + ", EXPECTED: " + max)
                        .withStatus(ResultStatus.FAILURE)
                        .build();
            }
        }

        return LineResult.builder()
                .withStatus(ResultStatus.SUCCESS)
                .withReason("RECEIVED: " + max)
                .build();
    }

    private static Batch validateAndGetBatch(String line) {
        Batch batch = new Batch();

        if (line == null) {
            batch.status = BatchInputStatus.INVALID;
            return batch;
        }

        String[] inputRaw = line.split("\\s+");
        for (String s : inputRaw) {
            try {
                Integer inputVal = Integer.valueOf(s);

                if (inputVal > 0) {
                    batch.inputIds.add(inputVal);
                } else {
                    batch.status = BatchInputStatus.INVALID;
                }
            } catch (NumberFormatException e) {
                batch.status = BatchInputStatus.INVALID;
            }
        }

        return batch;

    }

    private static int validateNumTestCases(String line) {
        int numCases = Integer.valueOf(line);
        if (numCases < 0) {
            throw new NumberFormatException();
        }
        return numCases;
    }
}

class Batch {
    List<Integer> inputIds = new ArrayList<>();
    BatchInputStatus status = BatchInputStatus.VALID;
}


enum BatchInputStatus {
    VALID,
    INVALID,
}

enum ResultStatus {

    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    String value;

    ResultStatus(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}

class LineResult {
    ResultStatus resultStatus = ResultStatus.SUCCESS;
    String reason;

    @Override
    public String toString() {
        return resultStatus.toString() + " => " + reason;
    }

    static Builder builder() {
        return new Builder();
    }

    static class Builder {
        ResultStatus resultStatus;
        String reason;

        public Builder withReason(String reason) {
            this.reason = reason;
            return this;
        }

        public Builder withStatus(ResultStatus resultStatus) {
            this.resultStatus = resultStatus;
            return this;
        }

        public LineResult build() {
            LineResult lr = new LineResult();
            lr.reason = this.reason;
            lr.resultStatus = this.resultStatus;
            return lr;
        }
    }

}

class Result {
    Map<Integer, LineResult> resultMap = new HashMap<>();

    @Override
    public String toString() {
        StringBuilder strBuf = new StringBuilder();
        for (LineResult lineResult : resultMap.values()) {
            strBuf.append(lineResult.toString());
            strBuf.append("\n");
        }
        return strBuf.toString();
    }
}
