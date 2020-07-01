package org.fractal.map.monitor;

public abstract class AbstractLongKpi implements LongKpi {

    private long value;

    public long getValue() {
        return value;
    }

    public void setValue( long value ) {
        this.value = value;
    }
}
