package org.fractal.map.util.buffer;

public class Buffer {

    private final byte[] bytes;
    private int usedCount = 0;

    public Buffer( int bufferSize ) {
        this.bytes = new byte[bufferSize];
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount( int usedCount ) {
        this.usedCount = usedCount;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
