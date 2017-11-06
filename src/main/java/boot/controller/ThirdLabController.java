package boot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@Controller
@RequestMapping("/lab3")
public class ThirdLabController {

    private static final String THIRD_LAB_PAGE = "third-lab";
    private static final String RESULT_SHA = "result_sha";
    private static final String RESULT_MD5 = "result_md5";
    private static final String MD_5 = "MD5";
    private static final String SHA_512 = "SHA-512";

    @RequestMapping(value = "/**", method = RequestMethod.GET)
    public String initPage() {
        return THIRD_LAB_PAGE;
    }

    @RequestMapping(value = "/encrypt", method = RequestMethod.POST)
    public String encryptMD5(@RequestParam(value = "inputData") String inputData,
                             Map<String, Object> model) throws NoSuchAlgorithmException {
        model.put(RESULT_MD5, new String(MessageDigest.getInstance(MD_5).digest(inputData.getBytes())));
        model.put(RESULT_SHA, new String(MessageDigest.getInstance(SHA_512).digest(inputData.getBytes())));
        return THIRD_LAB_PAGE;
    }
}
