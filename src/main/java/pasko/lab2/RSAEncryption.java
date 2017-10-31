package pasko.lab2;


import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@Component
public class RSAEncryption {

    private static final String ALGORITHM = "RSA";
    private static final String PRIVATE_KEY_FILE = "src/main/resources/keys/RSA_private.key";
    private static final String PUBLIC_KEY_FILE = "src/main/resources/keys/RSA_public.key";
    private static final String DEFAULT_PRIVATE_KEY_FILE = "src/main/resources/keys/RSADefault_private.key";
    private static final String DEFAULT_PUBLIC_KEY_FILE = "src/main/resources/keys/RSADefault_public.key";

    public void generateKeys() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
            keyGen.initialize(1024);
            final KeyPair key = keyGen.generateKeyPair();

            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            System.out.println(privateKeyFile.getAbsolutePath());
            File publicKeyFile = new File(PUBLIC_KEY_FILE);

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();

            // Saving the Public key in a file
            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile));
            publicKeyOS.writeObject(key.getPublic());
            publicKeyOS.close();

            // Saving the Private key in a file
            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile));
            privateKeyOS.writeObject(key.getPrivate());
            privateKeyOS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            //"RSA/ECB/NoPadding"
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public String decrypt(byte[] text, PrivateKey key) {
        byte[] dectyptedText = null;
        try {
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new String(dectyptedText);
    }

    public PublicKey getGeneratedPublicKey() {
        return getPublicKey(PUBLIC_KEY_FILE);
    }

    public PublicKey getDefaultPublicKey() {
        return getPublicKey(DEFAULT_PUBLIC_KEY_FILE);
    }

    private PublicKey getPublicKey(String path) {
        PublicKey publicKey = null;
        try {
            ObjectInputStream  inputStream = new ObjectInputStream(new FileInputStream(path));
            publicKey = (PublicKey) inputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public PrivateKey getGeneratedPrivateKey() {
        return getPrivateKey(PRIVATE_KEY_FILE);
    }

    public PrivateKey getDefaultPrivateKey() {
        return getPrivateKey(DEFAULT_PRIVATE_KEY_FILE);
    }

    private PrivateKey getPrivateKey(String path) {
        PrivateKey privateKey = null;
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            privateKey = (PrivateKey) inputStream.readObject();
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return privateKey;
    }
}
