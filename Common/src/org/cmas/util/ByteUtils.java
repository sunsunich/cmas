package org.cmas.util;

import java.security.SecureRandom;
import java.util.Arrays;

public final class ByteUtils {

    private static final int MASK = 0xff;

    private ByteUtils() {
    }

    public static void longToBytes(long x, byte[] bytes, int from, int to) {
        int size = to - from;
        for (int i = from; i < to; ++i) {
            bytes[i] = (byte) (x >> (size - (i - from) - 1 << 3));
        }
    }

    public static long bytesToLong(byte[] bytes, int from, int to) {
        long value = 0L;
        for (int i = from; i < to; i++) {
            value = (value << 8) + (long) (bytes[i] & MASK);
        }
        return value;
    }

    public static void intToBytes(int x, byte[] bytes, int from, int to) {
        int size = to - from;
        for (int i = from; i < to; ++i) {
            bytes[i] = (byte) (x >> (size - (i - from) - 1 << 3));
        }
    }

    public static int bytesToInt(byte[] bytes, int from, int to) {
        int value = 0;
        for (int i = from; i < to; i++) {
            value = (value << 8) + (bytes[i] & MASK);
        }
        return value;
    }

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        {
//            int size = 8;
//            byte bytes[] = new byte[size];
//            for (int i = 0; i < 20; i++) {
//                long l = Math.abs(random.nextLong());
//                System.out.println("init value l=" + l);
//                longToBytes(l, bytes, 0, size);
//                for (int j = 0; j < size; j++) {
//                    System.out.print(bytes[j] + ",");
//                }
//                System.out.println();
//                long newValue = bytesToLong(bytes, 0, size);
//                System.out.println("new value l=" + newValue);
//            }
        }
        System.out.println("____________________________________-");
        {
            int size = 4;
            byte bytes[] = new byte[size];
            int n = 1;// Math.abs(random.nextInt(Integer.MAX_VALUE));
            System.out.println("init value n=" + n);
            intToBytes(n, bytes, 0, size);
            System.out.println(Arrays.toString(bytes));
            int newValue = bytesToInt(bytes, 0, size);
            System.out.println("new value n=" + newValue);
//            for (int i = 0; i < 20; i++) {
//                int n = Math.abs(random.nextInt(Integer.MAX_VALUE));
//                System.out.println("init value n=" + n);
//                intToBytes(n, bytes, 0, size);
//                for (int j = 0; j < size; j++) {
//                    System.out.print(bytes[j] + ",");
//                }
//                System.out.println();
//                int newValue = bytesToInt(bytes, 0, size);
//                System.out.println("new value n=" + newValue);
//            }
        }
    }
}
