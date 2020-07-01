package org.fractal.map.monitor;

public abstract class AbstractDoubleKpi implements DoubleKpi {

    private double value;

    public double getValue() {
        return value;
    }

    public void setValue( double value ) {
        this.value = value;
    }
}
