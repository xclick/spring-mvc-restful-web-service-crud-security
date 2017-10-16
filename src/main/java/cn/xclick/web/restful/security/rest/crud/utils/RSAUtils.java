package cn.xclick.web.restful.security.rest.crud.utils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.io.IOUtils;
import org.springframework.util.Base64Utils;

/**
 * 这个类测试了非对称加密，生成非对称加密的公钥、私钥；并用公钥加密、私钥解密
 * @author Huwei
 *
 */
public class RSAUtils {

	public static void main(String[] args) 
		throws Exception{
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		random.nextInt();
		keyPairGen.initialize(1024, random);
	    KeyPair keyPair = keyPairGen.generateKeyPair();
	    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
	    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
	    
	    String publicKeyStr = getPublicKeyStr(publicKey);
	    String privateKeyStr = getPrivateKeyStr(privateKey);

	    System.out.println("publicKeyStr:" + publicKeyStr);
	    System.out.println("publicKeyStr:" + privateKeyStr);
	    {
	    	byte[] decoded = Base64Utils.decodeFromString(publicKeyStr);
	        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
	        KeyFactory kf = KeyFactory.getInstance("RSA");
	        RSAPublicKey generatePublic = (RSAPublicKey) kf.generatePublic(spec);
	        
	        byte [] pkcs8EncodedBytes = Base64Utils.decodeFromString(privateKeyStr);
	        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
	        PrivateKey privKey = kf.generatePrivate(keySpec);
	        
	    	String encStr = encryptByPublicKeyBase64("#### Auto Get Token(No need sign up, use device id) #### Auto Get Token(No need sign up, use device id) #### Auto Get Token(No need sign up, use device id) #### Auto Get Token(No need sign up, use device id)",generatePublic);
	    	System.out.println("encStr:" + encStr);
	    	String decStr = decryptByPrivateKeyBase64(encStr,privKey);
	    	System.out.println("decStr:" + decStr);
	    }
	}

	public static String getPrivateKeyStr(PrivateKey privateKey)
		throws Exception{
		return new String(Base64Utils.encode(privateKey.getEncoded()));
	}

	public static String getPublicKeyStr(PublicKey publicKey) throws Exception {
	    return new String(Base64Utils.encode(publicKey.getEncoded()));
	}
	
	/**
	 * 公钥加密
	 *
	 * @param plainBytes
	 *            明文bytes
	 * @param publicKey
	 *            公钥
	 * @return 密文bytes
	 */
	public static byte[] encryptByPublicKey(byte[] plainBytes, PublicKey publicKey) {
		ByteArrayOutputStream out = null;
		try {
			// Cipher在非对称加密场景，没有必要自己做thread cache，JCE实现的provider默认做了~
			Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int inputLen = plainBytes.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			int keyLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
			int plainLength = keyLength - 11;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > plainLength) {
					cache = cipher.doFinal(plainBytes, offSet, plainLength);
				} else {
					cache = cipher.doFinal(plainBytes, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * plainLength;
			}
			byte[] encryptedData = out.toByteArray();
			return encryptedData;
		} catch (Exception e) {
			throw new RuntimeException("publicKey encrypt fail:" + e.getMessage());
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static String encryptByPublicKeyBase64(String plainText, PublicKey publicKey) 
		throws Exception{
		return new String(Base64Utils.encode(encryptByPublicKey(plainText.getBytes("UTF-8"),publicKey)));
	}
	
	public static byte[] decryptByPrivateKey(byte[] encryptedBytes, PrivateKey privateKey) {
		ByteArrayOutputStream out = null;
		try {
			Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int inputLen = encryptedBytes.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > keyLength) {
					cache = cipher.doFinal(encryptedBytes, offSet, keyLength);
				} else {
					cache = cipher.doFinal(encryptedBytes, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * keyLength;
			}
			byte[] decryptedData = out.toByteArray();
			return decryptedData;
		} catch (Exception e) {
			throw new RuntimeException("privateKey decrypt fail:" + e.getMessage());
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	public static String decryptByPrivateKeyBase64(String encryptedBase64, PrivateKey privateKey) {
		byte[] plainBytes = decryptByPrivateKey(Base64Utils.decodeFromString(encryptedBase64), privateKey); 
		try {
			return new String(plainBytes, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的字符集:" + e.getMessage());
		}
	}
}
