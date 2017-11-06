package boot.controller;

import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pasko.lab1.DesAesCryptHelper;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/lab1")
public class FirstLabController {

    private static final String FIRST_LAB_PAGE = "first-lab";
    private static final String SECRET_KEY = "secretKey";
    private static final String SELECTED_METHOD = "selectedMethod";
    private static final String ENCRYPTED_VALUE = "encryptedValue";
    private static final String ENCRYPTED_METHOD_TYPE = "encryptedMethodType";

    @Value("classpath:keys/defaultDesAesKey.txt")
    private Resource resource;

    @Autowired
    private DesAesCryptHelper cryptHelper;

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public String welcome(Map<String, Object> model) {
        initMethods(model);
        initMethodTypes(model);
        return FIRST_LAB_PAGE;
    }

    @RequestMapping(value = "/generateKey", method = RequestMethod.POST)
    public String generateKey(@RequestParam("method") String method, Map<String, Object> model,
                              HttpSession httpSession) {
        initMethods(model);
        initMethodTypes(model);
        SecretKey secretKey = this.cryptHelper.getDefaultSecretKey(method);
        httpSession.setAttribute(SECRET_KEY, secretKey);
        httpSession.setAttribute(SELECTED_METHOD, method);
        return FIRST_LAB_PAGE;
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public String encrypt(@RequestParam("methodType") String methodType, @RequestParam("inputData") String inputData,
                          @RequestParam(required = false, value = "isFileUsed") boolean isFileUsed,
                          Map<String, Object> model, HttpSession session) throws Exception {
        initMethods(model);
        initMethodTypes(model);
        String selectedMethod = (String) session.getAttribute(SELECTED_METHOD);
        SecretKey secretKey = getSecretKey(isFileUsed, session, selectedMethod);
        byte[] result = this.cryptHelper.getEncryptedValue(inputData, secretKey, methodType, selectedMethod);
        session.setAttribute(ENCRYPTED_VALUE, result);
        session.setAttribute(ENCRYPTED_METHOD_TYPE, methodType);
        model.put("result", new String(result));
        return FIRST_LAB_PAGE;
    }

    @RequestMapping(value = "/decrypt", method = RequestMethod.POST)
    public String decrypt(@RequestParam(required = false, value = "isFileUsed") boolean isFileUsed,
                          Map<String, Object> model, HttpSession session) throws Exception {
        initMethods(model);
        initMethodTypes(model);
        byte[] result = (byte[]) session.getAttribute(ENCRYPTED_VALUE);
        String selectedMethod = (String) session.getAttribute(SELECTED_METHOD);
        String methodType = (String) session.getAttribute(ENCRYPTED_METHOD_TYPE);
        SecretKey secretKey = getSecretKey(isFileUsed, session, selectedMethod);
        String decryptedResult = this.cryptHelper.getDecryptedValue(result, secretKey, methodType, selectedMethod);
        model.put("initValue", decryptedResult);
        return FIRST_LAB_PAGE;
    }

    private SecretKey getSecretKey(boolean isFileUsed, HttpSession session, String selectedMethod) {
        SecretKey secretKey = (SecretKey) session.getAttribute(SECRET_KEY);
        if (isFileUsed) {
            byte[] inputBytes = readKeyFromFile();
            secretKey = new SecretKeySpec(inputBytes, selectedMethod);
            System.out.println("For key file is used");
        }
        return secretKey;
    }

    private void initMethodTypes(Map<String, Object> model) {
        Map<String, String> types = new LinkedHashMap<>();
        types.put("ECB", "ECB");
        types.put("CBC", "CBC");
        types.put("PCBC", "PCBC");
        types.put("CFB", "CFB");
        types.put("OFB", "OFB");
        model.put("typeList", types);
    }

    private void initMethods(Map<String, Object> model) {
        Map<String, String> methods = new LinkedHashMap<>();
        methods.put("DES", "DES");
        methods.put("AES", "AES");
        model.put("methodList", methods);
    }

    private byte[] readKeyFromFile() {
        byte[] inputBytes = null;
        try {
            InputStream inputStream = resource.getInputStream();
            inputBytes = ByteStreams.toByteArray(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputBytes;
    }

}
