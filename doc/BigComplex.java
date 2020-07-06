package number;

import java.math.BigDecimal;

/*This class does the basic arithmetic
  of high precision complex numbers 
  *
  * This class came out of McBilliards, written by Rich Schwartz and Pat Hooper.
  */

public class BigComplex {
   public  BigDecimal x,y;
    
    /** Create the complex number 0 */
    public BigComplex() {
        this.x=new BigDecimal(0);
        this.y=new BigDecimal(0);
    } 
    
    /** Create the complex number x+i*y */
    public BigComplex(BigDecimal x,BigDecimal y) {
        this.x=x;
        this.y=y;
    }
    
    /** Copy constructor */
    public BigComplex(BigComplex z) {
        this.x=z.x;
        this.y=z.y;
    }

   public static BigComplex times(BigComplex z1,BigComplex z2) {
       BigDecimal X0=z1.x.multiply(z2.x);
       BigDecimal X1=z1.y.multiply(z2.y);
       BigDecimal X=X0.subtract(X1);
       BigDecimal Y0=z1.x.multiply(z2.y);
       BigDecimal Y1=z1.y.multiply(z2.x);
       BigDecimal Y=Y0.add(Y1);
       BigComplex Z=new BigComplex(X,Y);
       return(Z);
   }

   public static BigComplex plus(BigComplex z1,BigComplex z2) {
       BigComplex Z=new BigComplex();
       Z.x=z1.x.add(z2.x);
       Z.y=z1.y.add(z2.y);
       return(Z);
   }

   public static BigComplex minus(BigComplex z1,BigComplex z2) {
       BigComplex Z=new BigComplex();
       Z.x=z1.x.subtract(z2.x);
       Z.y=z1.y.subtract(z2.y);
       return(Z);
   }

    public static BigComplex conjugate(BigComplex z) {
        return new BigComplex(z.x,z.y.negate());
    }

    public static BigDecimal squareNorm(BigComplex z) {
	BigComplex w=conjugate(z);
	BigComplex n=BigComplex.times(z,w);
	return(n.x);
    }



    public static BigComplex inverse(BigComplex z) {
        BigDecimal n=BigComplex.squareNorm(z);
	BigComplex w=BigComplex.conjugate(z);
	BigDecimal X=w.x.divide(n,100,BigDecimal.ROUND_FLOOR);
	BigDecimal Y=w.y.divide(n,100,BigDecimal.ROUND_FLOOR);
	return(new BigComplex(X,Y));
    }


    public static BigComplex divide(BigComplex z,BigComplex w) {
	return(times(z,inverse(w)));
    }


    public static BigComplex recognizeInteger(BigComplex z,int tolerance) {
	BigComplex w=new BigComplex();
	w.x=recognizeInteger(z.x,tolerance);
	w.y=recognizeInteger(z.y,tolerance);
	return(w);
    }

    public static BigDecimal recognizeInteger(BigDecimal x,int tolerance) {
	BigDecimal one=new BigDecimal(1);
	BigDecimal xx=x.divide(one,tolerance,BigDecimal.ROUND_HALF_DOWN);
	return(xx);
    }


    public void print() {
	System.out.print("BigComplex ");
	System.out.print(x.toString());
	System.out.print(" ");
	System.out.println(y.toString());
    }

}