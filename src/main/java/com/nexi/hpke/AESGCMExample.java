package com.nexi.hpke;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCMExample {

    // Lunghezza tag in bit
    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12; // standard per GCM

    public static void main(String[] args) throws Exception {
        // 1. Generazione chiave AES-256
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 256-bit key
        SecretKey key = keyGen.generateKey();

        // 2. Testo in chiaro
        String plaintext = "Messaggio segreto!";
        // 3. Dati associati (AAD)
        String aadData = "headerAutenticato";

        // 4. Generazione IV (nonce)
        byte[] iv = new byte[IV_LENGTH_BYTE];
        SecureRandom random = new SecureRandom();
        random.nextBytes(iv);

        // 5. Cifratura
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(TAG_LENGTH_BIT, iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, spec);
        cipher.updateAAD(aadData.getBytes(StandardCharsets.UTF_8));
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        System.out.println("Ciphertext (Base64): " + Base64.getEncoder().encodeToString(ciphertext));
        System.out.println("IV (Base64): " + Base64.getEncoder().encodeToString(iv));

        // 6. Decifratura
        Cipher decipher = Cipher.getInstance("AES/GCM/NoPadding");
        decipher.init(Cipher.DECRYPT_MODE, key, spec);
        decipher.updateAAD(aadData.getBytes(StandardCharsets.UTF_8));
        byte[] decrypted = decipher.doFinal(ciphertext);

        System.out.println("Decrypted: " + new String(decrypted, StandardCharsets.UTF_8));
    }
}
