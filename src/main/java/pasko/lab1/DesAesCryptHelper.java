package pasko.lab1;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.util.NoSuchElementException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

@Component
public class DesAesCryptHelper {

    private static final String DES = "DES";
    private static final String ECB = "ECB";
    private static final String PKCS5_PADDING = "PKCS5Padding";
    private static final char DELIMITER = '/';


    public byte[] getEncryptedValue(String inputData, SecretKey secretKey, String methodType,
                                     String selectedMethod) throws Exception {
        byte[] result;
        if (methodType.equals("ECB")) {
            result = cryptECB(inputData.getBytes(), ENCRYPT_MODE, secretKey, selectedMethod);
        } else {
            result = encrypt(inputData.getBytes(), ENCRYPT_MODE, secretKey, selectedMethod, methodType);
        }
        return result;
    }

    public String getDecryptedValue(byte[] inputData, SecretKey secretKey, String methodType,
                                     String selectedMethod) throws Exception {
        byte[] result;
        if (methodType.equals("ECB")) {
            result = cryptECB(inputData, DECRYPT_MODE, secretKey, selectedMethod);
        } else {
            result = encrypt(inputData, DECRYPT_MODE, secretKey, selectedMethod, methodType);
        }
        return new String(result);
    }

    private byte[] cryptECB(byte[] inputData, int mode, SecretKey key, String algorithm) throws Exception {
        Cipher desCipher = Cipher.getInstance(algorithm + DELIMITER + ECB + DELIMITER + PKCS5_PADDING);
        desCipher.init(mode, key);
        return desCipher.doFinal(inputData);
    }

    private byte[] encrypt(byte[] inputData, int mode, SecretKey key, String algorithm, String algorithmMode)
        throws Exception {
        Cipher desCipher = Cipher.getInstance(algorithm + DELIMITER + algorithmMode + DELIMITER + PKCS5_PADDING);
        IvParameterSpec ivParameterSpec;
        if (algorithm.equals(DES)) {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0};
            ivParameterSpec = new IvParameterSpec(iv);
        } else {
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            ivParameterSpec = new IvParameterSpec(iv);
        }
        desCipher.init(mode, key, ivParameterSpec);
        return desCipher.doFinal(inputData);
    }

    public SecretKey getDefaultSecretKey(String algorithm) {
        return getKeyGenerator(algorithm).generateKey();
    }

    private static KeyGenerator getKeyGenerator(String algorithm) {
        try {
            return KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchElementException(e.getMessage());
        }
    }
}
