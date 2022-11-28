
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

class Assignment2
{
    public static void main(String [] args) throws IOException
    {
        BigInteger one = BigInteger.ONE;
        // 1: Generate two distinct 512-bit probable primes p and q
        BigInteger p = generatePrime();
        BigInteger q = generatePrime();

        // 2: Calculate the product of these two primes n = pq
        BigInteger n = p.multiply(q);

        // 3: Calculate the Euler totient function phi(n) φ(pq) = (p - 1)(q - 1)
        BigInteger phi = phi(p, q);

        /* 4: You will be using an encryption exponent e = 65537, so you will need to ensure that this is 
        relatively prime to phi(n). If it is not, go back to step 1 and generate new values for p and q */

        BigInteger exponent = BigInteger.valueOf(65537);

        boolean relativePrime = true;
        while(relativePrime){
            if(!gcd(phi, exponent).equals(one)){
                relativePrime = false;
            }
        }
        /* 5. Compute the value for the decryption exponent d, which is the multiplicative inverse 
        of e (mod phi(n)). This should use your own implementation of the extended Euclidean GCD 
        algorithm to calculate the inverse rather than using a library method for this purpose.*/
        BigInteger d = multiplicateInverse(exponent, phi);
        
        byte[] fileContent = Files.readAllBytes(Paths.get(args[0]));

        byte[] fileDigest = sha256Digest(fileContent);

        BigInteger message = new BigInteger(1, fileDigest);

        BigInteger decrypt = decrypt(message, p, q, d);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Modulus.txt"));
            writer.write(n.toString(16));
            writer.close();

            BufferedWriter writer2 = new BufferedWriter(new FileWriter("Signature.txt"));
            writer2.write(decrypt.toString(16));
            writer2.close();
            
            System.out.print(decrypt.toString(16));
        } 
        catch (Exception e) {
            e.getStackTrace();
          }
    }
    // sourced - https://stackoverflow.com/questions/20925656/how-to-compute-eulers-totient-function-%CF%86-in-java
    private static BigInteger phi(BigInteger p, BigInteger q){
        BigInteger one = BigInteger.ONE;
        BigInteger phi = p.subtract(one).multiply(q.subtract(one));
        return phi;
    }

    // sourced - https://stackoverflow.com/questions/15056184/how-do-i-generate-a-160-bit-prime-number-in-java
    private static BigInteger generatePrime(){
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

    // sourced - https://stackoverflow.com/questions/39437988/java-modular-multiplicative-inverse/67897488#67897488
    private static BigInteger[] extendedGCD(BigInteger a, BigInteger b) {
        if(b.equals(BigInteger.ZERO))
            return new BigInteger[] {a, BigInteger.ONE, BigInteger.ZERO};
        else {
            BigInteger[] arr = extendedGCD(b, a.mod(b));

            BigInteger gcd = arr[0];
            BigInteger Y = arr[1].subtract(((a.divide(b))).multiply(arr[2]));
            BigInteger X = arr[2];

        return new BigInteger[] {gcd, X, Y};
        }
    }

    private static BigInteger multiplicateInverse(BigInteger exponent, BigInteger phi){
        BigInteger[] extGCDValues = extendedGCD(exponent, phi);

        if (!extGCDValues[0].equals(BigInteger.ONE))
            return BigInteger.ZERO;
        if (extGCDValues[1].compareTo(BigInteger.ZERO) == 1)
            return extGCDValues[1];
        else
            return extGCDValues[1].add(phi);
    }

    // sourced - https://www.geeksforgeeks.org/weak-rsa-decryption-chinese-remainder-theorem/
    private static BigInteger decrypt(BigInteger hm, BigInteger p, BigInteger q, BigInteger d){
        // h(m)^d (mod n)
        // h(m) = s^e mod(n)
        BigInteger one = BigInteger.ONE;
        BigInteger inverse = multiplicateInverse(q,p);

        BigInteger dp = d.mod(p.subtract(one));
        BigInteger dq = d.mod(q.subtract(one));

        BigInteger m1 = hm.modPow(dp, p);
        BigInteger m2 = hm.modPow(dq, q);

        BigInteger h = inverse.multiply(m1.subtract(m2)).mod(p);
        BigInteger m = m2.add(h.multiply(q));

        return m;
    }
    // sourced - https://www.baeldung.com/java-digital-signature#:~:text=Technically%20speaking%2C%20a%20digital%20signature,algorithm%20are%20all%20then%20sent
    private static byte[] sha256Digest(byte[] file){;
        MessageDigest digest;
        byte[] messageHash = new byte[0];
        try {
            digest = MessageDigest.getInstance("SHA-256");
            messageHash = digest.digest(file);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return messageHash;
    }
}

/* 
SOURCES used in aid of project 
BigInteger - https://www.geeksforgeeks.org/biginteger-valueof-method-in-java/
             https://docs.oracle.com/javase/7/docs/api/java/math/BigInteger.html
             https://www.geeksforgeeks.org/biginteger-compareto-method-in-java/
gcd - https://www.baeldung.com/java-greatest-common-divisor
Probable Primes - https://www.tutorialspoint.com/java/math/biginteger_probableprime.htm
Phi - https://stackoverflow.com/questions/20925656/how-to-compute-eulers-totient-function-%CF%86-in-java
Relative Primes - https://www.baeldung.com/java-two-relatively-prime-numbers
RSA - https://stackoverflow.com/questions/52548429/coding-rsa-algorithm-java
CRT & Multiplicate Inverse - https://stackoverflow.com/questions/39437988/java-modular-multiplicative-inverse/67897488#67897488
                             https://www.geeksforgeeks.org/weak-rsa-decryption-chinese-remainder-theorem/
                             https://medium.com/free-code-camp/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0
                             https://en.wikibooks.org/wiki/Algorithm_Implementation/Mathematics/Extended_Euclidean_algorithm
                             https://loop.dcu.ie/mod/resource/view.php?id=1880897
                             https://www.sanfoundry.com/java-program-extended-euclid-algorithm/
Modulus Power - https://www.google.com/search?q=biginteger+mod+to+the+power+of&oq=biginteger+mod+to+the+power+of+&aqs=chrome..69i57.7439j0j1&sourceid=chrome&ie=UTF-8
Hashing - https://www.baeldung.com/java-digital-signature#:~:text=Technically%20speaking%2C%20a%20digital%20signature,algorithm%20are%20all%20then%20sent.
          https://www.baeldung.com/java-digital-signature#:~:text=Technically%20speaking%2C%20a%20digital%20signature,algorithm%20are%20all%20then%20sent.
          https://www.cesarsotovalero.net/blog/encoding-encryption-hashing-and-obfuscation-in-java.html
BigInteger to hex - https://stackoverflow.com/questions/11918123/how-to-convert-biginteger-value-to-hex-in-java
String to BigInteger - https://stackoverflow.com/questions/15717240/how-do-i-convert-a-string-to-a-biginteger
Read File - https://stackoverflow.com/questions/22630999/cannot-create-path-object-from-a-string
            https://www.baeldung.com/java-byte-arrays-hex-strings
            https://www.programiz.com/java-programming/bufferedwriter
File to bytes - https://stackoverflow.com/questions/858980/file-to-byte-in-java
*/
