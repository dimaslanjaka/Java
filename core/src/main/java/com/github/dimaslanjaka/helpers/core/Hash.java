package com.dimaslanjaka.tools.Helpers.core;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.AeadFactory;
import com.google.crypto.tink.aead.AeadKeyTemplates;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
  public static String key = "ThisIsThe32ByteKeyForEncryption";
  public static String aad = "These are additional authenticated data (optional)";

  public static void x(String plainText) throws GeneralSecurityException {
    AeadConfig.register();

    KeysetHandle keysetHandle = KeysetHandle.generateNew(AeadKeyTemplates.AES256_CTR_HMAC_SHA256);
    Aead aead = AeadFactory.getPrimitive(keysetHandle);
    byte[] ciphertext = aead.encrypt(plainText.getBytes(), aad.getBytes());
    byte[] decrypted = aead.decrypt(ciphertext, aad.getBytes());
  }

  public static String md5(final String s) {
    final String MD5 = "MD5";
    try {
      // Create MD5 Hash
      MessageDigest digest = java.security.MessageDigest
              .getInstance(MD5);
      digest.update(s.getBytes());
      byte[] messageDigest = digest.digest();

      // Create Hex String
      StringBuilder hexString = new StringBuilder();
      for (byte aMessageDigest : messageDigest) {
        StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
        while (h.length() < 2)
          h.insert(0, "0");
        hexString.append(h);
      }
      return hexString.toString();

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return "";
  }
}
