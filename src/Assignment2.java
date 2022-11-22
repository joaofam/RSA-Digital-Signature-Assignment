
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.*;

class Assignment2
{
    private static final BigInteger BigInteger = null;
    public static void main(String [] args) throws IOException
    {
        BigInteger one = BigInteger.ONE;
        // 1: Generate two distinct 512-bit probable primes p and q
        BigInteger p = generatePrime();
        BigInteger q = generatePrime();

        // System.out.println("prime values\n" + p);
        // System.out.println(q);

        // 2: Calculate the product of these two primes n = pq
        BigInteger n = p.multiply(q);

        // 3: Calculate the Euler totient function phi(n) φ(pq) = (p - 1)(q - 1)
        BigInteger phi = phi(p, q);
        // BigInteger phi = BigInteger.valueOf(17);

        // 4: You will be using an encryption exponent e = 65537, so you will need to ensure that this is 
        //    relatively prime to phi(n). If it is not, go back to step 1 and generate new values for p and q

        BigInteger exponent = BigInteger.valueOf(65537);
        // BigInteger exponent = BigInteger.valueOf(40);

        boolean relativePrime = true;

        while(relativePrime){
            if(!gcd(phi, exponent).equals(one)){
                relativePrime = false;
            }
        }
        
        BigInteger d = multiplicateInverse(exponent, phi);
        System.out.println(d);
        
        // byte[] fileContent = Files.readAllBytes(Paths.get(args[0]));
        byte[] fileContent = readFile(args[0]);
        byte[] fileDigest = sha256Digest(fileContent);

        BigInteger message = new BigInteger(1, fileDigest);
        System.out.println(message);
        BigInteger decrypt = decrypt(message, p, q, d);

        BufferedWriter writer = new BufferedWriter(new FileWriter("Modulus.txt"));
        writer.write(n.toString(16));
        writer.close();

        BufferedWriter writer2 = new BufferedWriter(new FileWriter("Signature.txt"));
        writer2.write(decrypt.toString(16));
        writer2.close();
        
    }

    private static byte[] readFile(String filename){
        File file = new File(filename);
        byte[] fileContent = (new byte[(int) file.length()]);
        try {
            
            FileInputStream modulus = new FileInputStream(file);
            BufferedInputStream reader = new BufferedInputStream(modulus);
            reader.close();
        }  
        catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
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

  public static BigInteger multiplicateInverse(BigInteger e, BigInteger phiValue){
        BigInteger x0 = BigInteger.ONE, x1 = BigInteger.ZERO;
        BigInteger y0 = BigInteger.ZERO, y1 = BigInteger.ONE;
        BigInteger q;
        BigInteger tmp;
        BigInteger phi = phiValue;
        BigInteger result = BigInteger.ONE;
        BigInteger zero = BigInteger.ZERO;

        while(!e.equals(zero)){

        q = phiValue.divide(e); //
        tmp = x1.subtract(q.multiply(x0));

        x1 = x0;
        x0 = tmp;
        tmp = y1.subtract(q.multiply(y0));

        y1 = y0;
        y0 = tmp;
        tmp = e;
        e = phiValue.mod(e);
        phiValue = tmp;

        result = x1.add(phi).mod(phi);
        }
        return result;
    }
    private static BigInteger decrypt(BigInteger hm, BigInteger p, BigInteger q, BigInteger d){
        // h(m)^d (mod n)
        // h(m) = s^e mod(n)
        BigInteger one = BigInteger.ONE;
        BigInteger inverse = multiplicateInverse(q,p);

        BigInteger dp = d.mod(p.subtract(one));
        BigInteger dq = d.mod(q.subtract(one));

        BigInteger m1 = hm.modPow(dp, p);
        BigInteger m2 = hm.modPow(dq, q);

        BigInteger h = inverse.add(m1.subtract(m2)).mod(p);
        BigInteger m = m2.add(h.multiply(q));

        return m;
      }

    private static byte[] sha256Digest(byte[] file){
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

    private static String byteArrayToHex(byte[] byteArray){
        StringBuilder string = new StringBuilder(byteArray.length * 2);
        for(byte b: byteArray)
           string.append(String.format("%02x", b));
        return string.toString();
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
Multiplicate Inverse - https://www.sanfoundry.com/java-program-extended-euclid-algorithm/
CRT & Multiplicate Inverse - https://www.geeksforgeeks.org/weak-rsa-decryption-chinese-remainder-theorem/
                             https://medium.com/free-code-camp/how-to-implement-the-chinese-remainder-theorem-in-java-db88a3f1ffe0
                             https://en.wikibooks.org/wiki/Algorithm_Implementation/Mathematics/Extended_Euclidean_algorithm
                             https://loop.dcu.ie/mod/resource/view.php?id=1880897
Modulus Power - https://www.google.com/search?q=biginteger+mod+to+the+power+of&oq=biginteger+mod+to+the+power+of+&aqs=chrome..69i57.7439j0j1&sourceid=chrome&ie=UTF-8
Hashing - https://www.baeldung.com/java-digital-signature#:~:text=Technically%20speaking%2C%20a%20digital%20signature,algorithm%20are%20all%20then%20sent.
          https://www.baeldung.com/java-digital-signature#:~:text=Technically%20speaking%2C%20a%20digital%20signature,algorithm%20are%20all%20then%20sent.
          https://www.cesarsotovalero.net/blog/encoding-encryption-hashing-and-obfuscation-in-java.html
BigInteger to hex - https://stackoverflow.com/questions/11918123/how-to-convert-biginteger-value-to-hex-in-java
String to BigInteger - https://stackoverflow.com/questions/15717240/how-do-i-convert-a-string-to-a-biginteger
Read File - https://stackoverflow.com/questions/22630999/cannot-create-path-object-from-a-string
            https://www.baeldung.com/java-byte-arrays-hex-strings
File to bytes - https://stackoverflow.com/questions/858980/file-to-byte-in-java
*/
