package boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pasko.lab2.ELGamalEncryption;
import pasko.lab2.RSAEncryption;

import javax.servlet.http.HttpSession;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

@Controller
@RequestMapping("/lab2")
public class SecondLabController {

    private static final String SECOND_LAB_PAGE = "second-lab";
    private static final String ENCRYPTED_RSA_VALUE = "ecnryptedRSAValue";
    private static final String ENCRYPTED_GAMAL_VALUE = "encryptedGamalValue";
    private static final String PUBLIC_GAMAL_KEY = "publicGamalKey";
    private static final String PRIVATE_GAMAL_KEY = "privateGamalKey";

    @Autowired
    private RSAEncryption rsaEncryption;
    @Autowired
    private ELGamalEncryption gamalEncryption;

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public String initPage() {
        return SECOND_LAB_PAGE;
    }

    @RequestMapping(value = "/generateRSAKeys", method = RequestMethod.POST)
    public String generateKeys(HttpSession httpSession) {
        rsaEncryption.generateKeys();
        httpSession.setAttribute("isRSAKeyGenerated", true);
        return SECOND_LAB_PAGE;
    }

    @RequestMapping(value = "/encryptRSA", method = RequestMethod.POST)
    public String encryptRSA(@RequestParam("inputText") String inputText,
                             @RequestParam(required = false, value = "isDefaultKeysUsed") boolean isDefaultKeysUsed,
                             Map<String, Object> model,
                             HttpSession httpSession) {
        PublicKey publicKey;
        if (isDefaultKeysUsed) {
            publicKey = rsaEncryption.getDefaultPublicKey();
        } else {
            publicKey = rsaEncryption.getGeneratedPublicKey();
        }
        byte[] encryptedResult = rsaEncryption.encrypt(inputText, publicKey);
        httpSession.setAttribute(ENCRYPTED_RSA_VALUE, encryptedResult);
        model.put("rsaEncryptedResult", new String(encryptedResult));
        return SECOND_LAB_PAGE;
    }

    @RequestMapping(value = "/decryptRSA", method = RequestMethod.POST)
    public String decryptRSA(@RequestParam(required = false, value = "isDefaultKeysUsed") boolean isDefaultKeysUsed,
                             Map<String, Object> model, HttpSession httpSession) {
        PrivateKey privateKey;
        if (isDefaultKeysUsed) {
            privateKey = rsaEncryption.getDefaultPrivateKey();
        } else {
            privateKey = rsaEncryption.getGeneratedPrivateKey();
        }
        byte[] encryptedValue = (byte[]) httpSession.getAttribute(ENCRYPTED_RSA_VALUE);
        String decryptedResult = rsaEncryption.decrypt(encryptedValue, privateKey);
        model.put("rsaDecryptedResult", decryptedResult);
        return SECOND_LAB_PAGE;
    }

    @RequestMapping(value = "/generateGamal", method = RequestMethod.POST)
    public String generateGamalKes(@RequestParam("initValue") String initValue, HttpSession httpSession) {
        ELGamalEncryption.KeyHolder keyHolder = gamalEncryption.generateKeys(initValue);
        httpSession.setAttribute(PUBLIC_GAMAL_KEY, keyHolder.getPublicKey());
        httpSession.setAttribute(PRIVATE_GAMAL_KEY, keyHolder.getPrivateKey());
        return SECOND_LAB_PAGE;
    }

    @RequestMapping(value = "/encryptGamal", method = RequestMethod.POST)
    public String encryptGamal(@RequestParam("inputText") String inputText, HttpSession httpSession) {
        ELGamalEncryption.PublicKey publicKey = (ELGamalEncryption.PublicKey) httpSession.getAttribute(PUBLIC_GAMAL_KEY);
        ELGamalEncryption.EncryptedValue encryptedValue = gamalEncryption.encrypt(inputText, publicKey);
        httpSession.setAttribute(ENCRYPTED_GAMAL_VALUE, encryptedValue);
        return SECOND_LAB_PAGE;
    }

    @RequestMapping(value = "/decryptGamal", method = RequestMethod.POST)
    public String decryptGamal(Map<String, Object> model, HttpSession httpSession) {
        ELGamalEncryption.PrivateKey privateKey = (ELGamalEncryption.PrivateKey) httpSession.getAttribute(PRIVATE_GAMAL_KEY);
        ELGamalEncryption.EncryptedValue encryptedValue = (ELGamalEncryption.EncryptedValue)
                httpSession.getAttribute(ENCRYPTED_GAMAL_VALUE);
        String initValue = gamalEncryption.decrypt(encryptedValue, privateKey);
        model.put("initDecryptedValue", initValue);
        return SECOND_LAB_PAGE;
    }

    @RequestMapping(value = "/isPrime", method = RequestMethod.POST)
    public String isPrime(@RequestParam String maybePrime, Map<String, Object> model) {
        boolean isPrime = gamalEncryption.isPrime(Long.valueOf(maybePrime));
        model.put("isPrime", isPrime);
        return SECOND_LAB_PAGE;
    }

}
