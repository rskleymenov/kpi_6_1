package pasko.lab2;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.Random;

@Component
public class ELGamalEncryption {

    public class KeyHolder {
        private PrivateKey privateKey;
        private PublicKey publicKey;

        public KeyHolder(PrivateKey privateKey, PublicKey publicKey) {
            this.privateKey = privateKey;
            this.publicKey = publicKey;
        }

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public PublicKey getPublicKey() {
            return publicKey;
        }

    }

    public class PrivateKey {
        private BigInteger x;
        private BigInteger p;

        public PrivateKey(BigInteger x, BigInteger p) {
            this.x = x;
            this.p = p;
        }

        public BigInteger getX() {
            return x;
        }

        public BigInteger getP() {
            return p;
        }

        @Override
        public String toString() {
            return "PrivateKey{" +
                    "x=" + x +
                    '}';
        }
    }

    public class PublicKey {
        private BigInteger p;
        private BigInteger g;
        private BigInteger y;

        PublicKey(BigInteger p, BigInteger g, BigInteger y) {
            this.p = p;
            this.g = g;
            this.y = y;
        }

        public BigInteger getP() {
            return p;
        }

        public BigInteger getG() {
            return g;
        }

        public BigInteger getY() {
            return y;
        }

        @Override
        public String toString() {
            return "PublicKey{" +
                    "p=" + p +
                    ", g=" + g +
                    ", y=" + y +
                    '}';
        }
    }

    public class EncryptedValue {
        private BigInteger a;
        private BigInteger b;

        public EncryptedValue(BigInteger a, BigInteger b) {
            this.a = a;
            this.b = b;
        }

        public BigInteger getA() {
            return a;
        }

        public BigInteger getB() {
            return b;
        }

        @Override
        public String toString() {
            return "EncryptedValue{" +
                    "a=" + a +
                    ", b=" + b +
                    '}';
        }
    }

    public KeyHolder generateKeys(String initValue) {
        Random r = new Random();
        BigInteger p = getNextPrime(initValue);
        BigInteger g = getPrimitiveRoot(p, r);
        // Pick a secret x.
        BigInteger x = new BigInteger(p.bitCount() - 1, r);
        // Calculate the corresponding public y.
        BigInteger y = g.modPow(x, p);
        PublicKey publicKey = new PublicKey(p, g, y);
        PrivateKey privateKey = new PrivateKey(x, p);
        return new KeyHolder(privateKey, publicKey);
    }

    public EncryptedValue encrypt(String inputString, PublicKey publicKey) {
        Random r = new Random();
        BigInteger p = publicKey.getP();
        BigInteger g = publicKey.getG();
        BigInteger y = publicKey.getY();
        // When we send a message, the sender picks a random k.
        BigInteger k = new BigInteger(p.bitCount() - 1, r);
        // Here, the sender starts calculating parts of the ciphertext that
        // don't involve the actual message.
        BigInteger a = g.modPow(k, p);
        BigInteger b = y.modPow(k, p);
        // Here we get the message from the user.
        BigInteger m = new BigInteger(inputString);
        // Now, we can calculate the rest of the second ciphertext.
        b = b.multiply(m);
        b = b.mod(p);
        return new EncryptedValue(a, b);
    }

    public String decrypt(EncryptedValue encryptedValue, PrivateKey privateKey) {
        BigInteger a = encryptedValue.getA();
        BigInteger b = encryptedValue.getB();
        // First, determine the inverse of c1 raised to the a power mod p.
        BigInteger x = privateKey.getX();
        BigInteger p = privateKey.getP();
        BigInteger temp = a.modPow(x, p);
        temp = temp.modInverse(p);
        // Now, just multiply this by the second ciphertext
        BigInteger recover = temp.multiply(b);
        recover = recover.mod(p);
        return recover.toString();
    }

    public boolean isPrime(long n) {
        if (n > 2 && (n & 1) == 0)
            return false;
        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;
        return true;
    }

    // Incrementally tries each BigInteger starting at the value passed
    // in as a parameter until one of them is tests as being prime.
    public static BigInteger getNextPrime(String ans) {
        BigInteger one = new BigInteger("1");
        BigInteger test = new BigInteger(ans);
        while (!test.isProbablePrime(99))
            test = test.add(one);
        return test;
    }

    // Precondition - p is prime and it's reasonably small, say, no more than
    //				  5,000,000. If it's larger, this method will be quite
    //                time-consuming.
    // Postcondition - if a generator for p can be found, then it is returned
    //                 if no generator is found after 1000 tries, null is
    //                 returned.
    public static BigInteger getPrimitiveRoot(BigInteger p, Random r) {
        int numtries = 0;
        // Try finding a generator at random 100 times.
        while (numtries < 1000) {
            // Here's what we're trying as the generator this time.
            BigInteger rand = new BigInteger(p.bitCount() - 1, r);
            BigInteger exp = BigInteger.ONE;
            BigInteger next = rand.mod(p);
            // We exponentiate our generator until we get 1 mod p.
            while (!next.equals(BigInteger.ONE)) {
                next = (next.multiply(rand)).mod(p);
                exp = exp.add(BigInteger.ONE);
            }
            // If the first time we hit 1 is the exponent p-1, then we have
            // a generator.
            if (exp.equals(p.subtract(BigInteger.ONE)))
                return rand;
        }
        // None of the 1000 values we tried was a generator.
        return null;
    }
}
