package com.nexi.hpke;

import io.vertx.core.Vertx;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

@ApplicationScoped
public class ReceiverBean {

    private KeyPair receiverKeyPair;

    @Inject
    Vertx vertx;

    @PostConstruct
    void init() throws Exception {
        // Genera chiavi P-256
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec("secp256r1"));
        receiverKeyPair = kpg.generateKeyPair();

        // Registra consumer sull’event bus
        vertx.eventBus().consumer("hpke.messages", message -> {
            Map<String, byte[]> data = (Map<String, byte[]>) message.body();
            try {
                byte[] sharedSecret = decapsulate(data.get("enc"), receiverKeyPair.getPrivate());
                byte[] pt = decrypt(data.get("ct"), sharedSecret, data.get("nonce"));
                System.out.println("Messaggio ricevuto: " + new String(pt));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private byte[] decapsulate(byte[] enc, PrivateKey skR) throws Exception {
        KeyFactory kf = KeyFactory.getInstance("EC");
        PublicKey pkE = kf.generatePublic(new X509EncodedKeySpec(enc));
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(skR);
        ka.doPhase(pkE, true);
        return ka.generateSecret();
    }

    private byte[] decrypt(byte[] ct, byte[] sharedSecret, byte[] nonce) throws Exception {
        SecretKeySpec key = deriveAEADKey(sharedSecret);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(128, nonce));
        return cipher.doFinal(ct);
    }

    private SecretKeySpec deriveAEADKey(byte[] sharedSecret) throws Exception {
        // Usa HKDF SHA-256 (semplificato)
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] okm = sha256.digest(sharedSecret);
        return new SecretKeySpec(okm, 0, 16, "AES");
    }

    public PublicKey getReceiverPublicKey() {
        return receiverKeyPair.getPublic();
    }
}
