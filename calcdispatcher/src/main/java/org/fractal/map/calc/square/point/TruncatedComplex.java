package org.fractal.map.calc.square.point;

public class TruncatedComplex {

    private static final long HASH_MAGNIFIER = 10000000000L;

    private long re;
    private long im;

    public TruncatedComplex( double re, double im ) {
        this.re = truncByMagnifier( re );
        this.im = truncByMagnifier( im );
    }

    public static long calculateHash( double re, double im ) {
        return Math.round( re * HASH_MAGNIFIER ) + Math.round( im * HASH_MAGNIFIER );
    }

    public long truncByMagnifier( double value ) {
        return Math.round( value * HASH_MAGNIFIER );
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( int ) ( im ^ ( im >>> 32 ) );
        result = prime * result + ( int ) ( re ^ ( re >>> 32 ) );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) return true;
        if ( obj == null ) return false;
        if ( getClass() != obj.getClass() ) return false;
        TruncatedComplex other = ( TruncatedComplex ) obj;
        if ( im != other.im ) return false;
        if ( re != other.re ) return false;
        return true;
    }
}
