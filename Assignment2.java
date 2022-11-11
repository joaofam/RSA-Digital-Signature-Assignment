import java.math.BigInteger;
import java.security.*;
import java.util.Random;
import javax.crypto.*;

class Assignment2
{
    public static void main(String [] args)
    {
        // create a random object
        SecureRandom rnd = new SecureRandom();
        // Generate two distinct 512-bit probable primes p and q
        BigInteger p = BigInteger.probablePrime(512, rnd);
        BigInteger q = BigInteger.probablePrime(512, rnd);

        System.out.println(p);
    }
}

// Generate two distinct 512-bit probable primes p and q
// Calculate the product of these two primes n = pq
// Calculate the Euler totient function phi(n)
// You will be using an encryption exponent e = 65537, so you will need to ensure that this is relatively prime to phi(n). If it is not, go back to step 1 and generate new values for p and q
// Compute the value for the decryption exponent d, which is the multiplicative inverse of e (mod phi(n)). This should use your own implementation of the extended Euclidean GCD algorithm to calculate the inverse rather than using a library method for this purpose.

/* 
SOURCES used in aid of project 
Probable Primes - https://www.tutorialspoint.com/java/math/biginteger_probableprime.htm

*/