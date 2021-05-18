package com.kvm;
import com.library.LoggerUtil;
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
  public static byte[] getcodekey(String password, int len, byte[] kvm_salt, String hmac, int iterations) throws NoSuchAlgorithmException, InvalidKeySpecException {
    char[] chars = password.toCharArray();
    byte[] salt = new byte[16];
    System.arraycopy(kvm_salt, 0, salt, 0, salt.length);
    PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, len * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance(hmac);
    byte[] hash = skf.generateSecret(spec).getEncoded();
    return hash;
  }
  public static byte[] getvmmcodekey(char[] password, int len, byte[] vmm_salt, String hmac, int iterations) throws NoSuchAlgorithmException, InvalidKeySpecException {
    PBEKeySpec spec = new PBEKeySpec(password, vmm_salt, iterations, len * 8);
    SecretKeyFactory skf = SecretKeyFactory.getInstance(hmac);
    byte[] hash = skf.generateSecret(spec).getEncoded();
    return hash;
  }
  public static byte[] encry(byte[] src, int codekey, int len) {
    byte[] tem_src = null;
    byte[] tem_des = null;
    int srcLen = 0;
    byte[] key = { 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8 };
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    if (src == null || len <= 0)
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
  public static byte[] secure_encry(byte[] src, byte[] key_data, int len) {
    byte[] tem_src = null;
    byte[] tem_des = null;
    int srcLen = 0;
    byte[] keyboard_key = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    if (len <= 0 || src == null || key_data == null)
    {
      return tem_des;
    }
    srcLen = (len + 15) / 16 * 16;
    tem_src = new byte[srcLen];
    System.arraycopy(src, 0, tem_src, 0, len);
    System.arraycopy(key_data, 16, keyboard_key, 0, 16);
    System.arraycopy(key_data, 32, iv, 0, 16);
    tem_des = aes_cbc_128_encrypt(tem_src, keyboard_key, iv);
    return tem_des;
  }
  public static byte[] vmmencry(byte[] src, byte[] key_data, int len, byte[] vmm_iv) {
    int srcLen = 0;
    byte[] tem_src = null;
    byte[] tem_des = null;
    byte[] vmm_key = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    if (len <= 0 || key_data == null || vmm_iv == null || src == null)
    {
      return tem_des;
    }
    srcLen = (len + 15) / 16 * 16;
    tem_src = new byte[srcLen];
    System.arraycopy(src, 0, tem_src, 0, len);
    System.arraycopy(key_data, 0, vmm_key, 0, 16);
    tem_des = aes_cbc_128_encrypt(tem_src, vmm_key, vmm_iv);
    return tem_des;
  }
  public static byte[] vmm_decry(byte[] src, byte[] key_data, int len, byte[] vmm_iv) {
    byte[] tem_src = null;
    byte[] tem_des = null;
    if (len <= 0 || src == null || key_data == null || vmm_iv == null)
    {
      return tem_des;
    }
    tem_src = new byte[len];
    System.arraycopy(src, 0, tem_src, 0, len);
    tem_des = aes_cbc_128_decrypt(tem_src, key_data, vmm_iv);
    if (tem_des == null || 0 == tem_des.length)
    {
      LoggerUtil.error("aes_cbc_128_decrypt error");
    }
    return tem_des;
  }
  public static byte[] kvm_encry(byte[] src, byte[] key_data, int len) {
    byte[] tem_src = null;
    byte[] tem_des = null;
    int srcLen = 0;
    byte[] keyboard_key = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    if (len <= 0 || src == null || key_data == null)
    {
      return tem_des;
    }
    srcLen = (len + 15) / 16 * 16;
    tem_src = new byte[srcLen];
    System.arraycopy(src, 0, tem_src, 0, len);
    System.arraycopy(key_data, 0, keyboard_key, 0, 16);
    System.arraycopy(key_data, 32, iv, 0, 16);
    tem_des = aes_cbc_128_encrypt(tem_src, keyboard_key, iv);
    return tem_des;
  }
  public static byte[] decry(byte[] src, byte[] key_data, int len) {
    byte[] tem_src = null;
    byte[] tem_des = null;
    byte[] key_kvm = new byte[16];
    byte[] iv = new byte[16];
    if (len <= 0 || src == null || key_data == null)
    {
      return tem_des;
    }
    tem_src = new byte[len];
    System.arraycopy(src, 0, tem_src, 0, len);
    System.arraycopy(key_data, 0, key_kvm, 0, 16);
    System.arraycopy(key_data, 32, iv, 0, 16);
    tem_des = aes_cbc_128_decrypt(tem_src, key_kvm, iv);
    if (tem_des == null || 0 == tem_des.length)
    {
      LoggerUtil.error("aes_cbc_128_decrypt error");
    }
    return tem_des;
  }
  public static byte[] aes_cbc_128_encrypt(byte[] data_to_encrypt, byte[] key, byte[] iv) {
    try {
      SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
      Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
      IvParameterSpec ivs = new IvParameterSpec(iv);
      cipher.init(1, skeySpec, ivs);
      return cipher.doFinal(data_to_encrypt);
    }
    catch (RuntimeException re) {
      LoggerUtil.error(re.getClass().getName());
      byte[] tmp = null;
      return tmp;
    }
    catch (Exception e) {
      byte[] tmp = null;
      LoggerUtil.error(e.getClass().getName());
      return tmp;
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
    catch (RuntimeException e) {
      byte[] tmp = null;
      LoggerUtil.error(e.getClass().getName());
      return tmp;
    }
    catch (Exception e) {
      LoggerUtil.error(e.getClass().getName());
      byte[] tmp = null;
      return tmp;
    } 
  }
}
