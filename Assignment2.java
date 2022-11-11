
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

        // Calculate the product of these two primes n = pq
        BigInteger primeProduct = p.multiply(q);

        // Calculate the Euler totient function phi(n) φ(pq) = (p - 1)(q - 1)
        BigInteger one = new BigInteger("1");
        BigInteger phi = p.subtract(one).multiply(q.subtract(one));

        BigInteger exponent = new BigInteger("65537");


    }

    private static BigInteger relativePrime(BigInteger a, BigInteger b)
    {
        BigInteger zero = new BigInteger("0");
        if (b.equals(zero)){
            return a;
        }
        else{
            return relativePrime(b, a.mod(b));
        }
    }
}


// You will be using an encryption exponent e = 65537, so you will need to ensure that this is 
// relatively prime to phi(n). If it is not, go back to step 1 and generate new values for p and q

// Compute the value for the decryption exponent d, which is the multiplicative inverse of 
//e (mod phi(n)). This should use your own implementation of the extended Euclidean GCD algorithm 
//to calculate the inverse rather than using a library method for this purpose.

/* 
SOURCES used in aid of project 
Probable Primes - https://www.tutorialspoint.com/java/math/biginteger_probableprime.htm
Phi - https://stackoverflow.com/questions/20925656/how-to-compute-eulers-totient-function-%CF%86-in-java
Relative Primes - https://www.baeldung.com/java-two-relatively-prime-numbers


*/
