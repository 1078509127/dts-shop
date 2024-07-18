package com.qiguliuxing.dts.wx.web;

public class SnowflakeIdGenerator {
    // 起始时间戳（毫秒）
    private static final long START_TIMESTAMP = 1609459200000L;

    // 工作机器 ID（0-31）
    private static final long WORKER_ID = 1;

    // 数据中心 ID（0-31）
    private static final long DATACENTER_ID = 1;

    // 每一部分占用的位数
    private static final long SEQUENCE_BITS = 12;
    private static final long WORKER_ID_BITS = 5;
    private static final long DATACENTER_ID_BITS = 5;

    // 最大序列号
    private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BITS);

    // 机器 ID 左移位数
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

    // 数据中心 ID 左移位数
    private static final long DATACENTER_ID_SHIFT = WORKER_ID_BITS + SEQUENCE_BITS;

    // 时间戳左移位数
    private static final long TIMESTAMP_SHIFT = DATACENTER_ID_BITS + WORKER_ID_BITS + SEQUENCE_BITS;

    // 序列号
    private long sequence = 0L;

    // 上一次生成 ID 的时间戳
    private long lastTimestamp = -1L;

    public synchronized int generateId() {
        long currentTimestamp = System.currentTimeMillis();

        // 如果时间戳有回拨，则等待直到时间戳正确
        if (currentTimestamp < lastTimestamp) {
            try {
                Thread.sleep(lastTimestamp - currentTimestamp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentTimestamp = System.currentTimeMillis();
        }

        if (lastTimestamp == currentTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                // 序列号溢出，等待下一毫秒
                currentTimestamp = waitNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        // 生成 ID
        long id = ((currentTimestamp - START_TIMESTAMP) << TIMESTAMP_SHIFT)
                | (DATACENTER_ID << DATACENTER_ID_SHIFT)
                | (WORKER_ID << WORKER_ID_SHIFT)
                | sequence;

        // 截取或转换为 7 位数
        return (int)(id % 10000000);
    }

    private long waitNextMillis(long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}