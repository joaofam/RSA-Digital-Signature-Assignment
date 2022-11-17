
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.*;

class Assignment2
{
    public static void main(String [] args)
    {
        BigInteger one = BigInteger.ONE;
        // 1: Generate two distinct 512-bit probable primes p and q
        BigInteger p = generatePrime();
        BigInteger q = generatePrime();

        System.out.println("prime values\n" + p);
        System.out.println(q);

        // 2: Calculate the product of these two primes n = pq
        BigInteger primeProduct = p.multiply(q);

        // 3: Calculate the Euler totient function phi(n) φ(pq) = (p - 1)(q - 1)
        BigInteger phi = phi(p, q);

        BigInteger exponent = BigInteger.valueOf(65537);

        boolean relativePrime = true;

        while(relativePrime){
            if(!gcd(phi, exponent).equals(one)){
                relativePrime = false;
            }
        }   


        


    }
    private static BigInteger phi(BigInteger p, BigInteger q){
        BigInteger one = BigInteger.ONE;
        BigInteger phi = p.subtract(one).multiply(q.subtract(one));
        return phi;
    }

    private static BigInteger generatePrime(){
        // create a random object
        SecureRandom rnd = new SecureRandom();
        BigInteger p = BigInteger.probablePrime(512, rnd);
        return p;
    }
    private static BigInteger gcd(BigInteger x, BigInteger y){
        BigInteger one = BigInteger.ONE;
        if (y.equals(one))
            return x;
        return gcd(y, x.mod(y));
    }
        // function to perform modular exponentiation 
    private static BigInteger modularExp(BigInteger a, BigInteger x, BigInteger n)
    {
        BigInteger y = new BigInteger("1");
        int k = x.bitLength();
        
        for(int i = k-1; i >= 0; i--)
        {
            y = y.multiply(y).mod(n);
            if(x.testBit(i))
            {
                y = y.multiply(a).mod(n);
            }
        }
        return y;
    }
}


// You will be using an encryption exponent e = 65537, so you will need to ensure that this is 
// relatively prime to phi(n). If it is not, go back to step 1 and generate new values for p and q

// Compute the value for the decryption exponent d, which is the multiplicative inverse of 
//e (mod phi(n)). This should use your own implementation of the extended Euclidean GCD algorithm 
//to calculate the inverse rather than using a library method for this purpose.

/* 
SOURCES used in aid of project 
BigInteger - https://www.geeksforgeeks.org/biginteger-valueof-method-in-java/
             https://docs.oracle.com/javase/7/docs/api/java/math/BigInteger.html
gcd - https://www.baeldung.com/java-greatest-common-divisor
Probable Primes - https://www.tutorialspoint.com/java/math/biginteger_probableprime.htm
Phi - https://stackoverflow.com/questions/20925656/how-to-compute-eulers-totient-function-%CF%86-in-java
Relative Primes - https://www.baeldung.com/java-two-relatively-prime-numbers
RSA - https://stackoverflow.com/questions/52548429/coding-rsa-algorithm-java

*/
