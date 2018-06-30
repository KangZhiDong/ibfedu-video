package com.ibf.live.common.util;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignatureUtil {

	public static String bceSign(String signingKey,String message) throws Exception {
		try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(signingKey.getBytes("utf8"), "HmacSHA256"));
            return new String(Hex.encodeHex(mac.doFinal(message.getBytes("utf8"))));
        } catch (Exception e) {
            throw e;
        }
	}
	public static String bceGetToken(String signature, String uid, long expirationTime){
		return signature+"_"+uid+"_"+expirationTime;
	}
}

