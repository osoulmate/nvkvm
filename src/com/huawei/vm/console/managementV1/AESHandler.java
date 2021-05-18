package com.huawei.vm.console.managementV1;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
public class AESHandler
{
  static final String AES_ALGORITHM = "AES";
  static final String AES_ALGORITHM_CBC = "AES/CBC/NOPadding";
  public static final byte[] iv = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  static final byte[] key = new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8 };
  static final byte[] salt = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
  static final int iter_times = 5000;
  public static byte[] encry(byte[] src, int codekey, int len) {
    byte[] tem_src = null;
    byte[] tem_des = null;
    int srcLen = 0;
    if (len <= 0 || src == null)
    {
      return tem_des;
    }
    srcLen = (len + 15) / 16 * 16;
    tem_src = new byte[srcLen];
    System.arraycopy(src, 0, tem_src, 0, len);
    key[0] = (byte)(codekey >> 24);
    key[1] = (byte)(codekey >> 16);
    key[2] = (byte)(codekey >> 8);
    key[3] = (byte)codekey;
    tem_des = aes_cbc_128_encrypt(tem_src, key, iv);
    return tem_des;
  }
  public static byte[] encry_bytes(byte[] src, byte[] kbdKey, int len) {
    byte[] tem_src = null;
    byte[] tem_des = null;
    int srcLen = 0;
    if (len <= 0 || src == null)
    {
      return tem_des;
    }
    srcLen = (len + 15) / 16 * 16;
    tem_src = new byte[srcLen];
    System.arraycopy(src, 0, tem_src, 0, len);
    tem_des = aes_cbc_128_encrypt(tem_src, kbdKey, iv);
    return tem_des;
  }
  public static byte[] aes_cbc_128_encrypt(byte[] data_to_encrypt, byte[] key) {
    try {
      SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(1, skeySpec);
      return cipher.doFinal(data_to_encrypt);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  public static byte[] aes_cbc_128_encrypt(byte[] data_to_encrypt, byte[] key, byte[] iv) {
    try {
      SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
      IvParameterSpec ivs = new IvParameterSpec(iv);
      cipher.init(1, skeySpec, ivs);
      return cipher.doFinal(data_to_encrypt);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  public static byte[] aes_cbc_128_decrypt(byte[] data_to_decrypt, byte[] key) {
    try {
      SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES");
      cipher.init(2, skeySpec);
      return cipher.doFinal(data_to_decrypt);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  public static byte[] aes_cbc_128_decrypt(byte[] data_to_decrypt, byte[] key, byte[] iv) {
    try {
      SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
      IvParameterSpec ivs = new IvParameterSpec(iv);
      cipher.init(2, skeySpec, ivs);
      return cipher.doFinal(data_to_decrypt);
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    } 
  }
  public static byte[] generateStoredPasswordHash(int plainKey, int passLen) throws NoSuchAlgorithmException, InvalidKeySpecException {
    return generateStoredPasswordHash(String.valueOf(plainKey).toCharArray(), passLen);
  }
  public static byte[] generateStoredPasswordHash(char[] plainKey, int passLen) throws NoSuchAlgorithmException, InvalidKeySpecException {
    PBEKeySpec spec = new PBEKeySpec(plainKey, salt, 5000, passLen * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    return skf.generateSecret(spec).getEncoded();
  }
}
