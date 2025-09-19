package com.nexi.hpke;

import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class SenderBean {

    @Inject
    Vertx vertx;

    @Inject
    ReceiverBean receiver;

    public void send(String msg) throws Exception {
        KeyPair ephemeral = generateEphemeral();
        byte[] sharedSecret = encapsulate(ephemeral.getPrivate(), receiver.getReceiverPublicKey());

        byte[] nonce = new byte[12]; // GCM nonce
        new SecureRandom().nextBytes(nonce);
        byte[] ct = encrypt(msg.getBytes(), sharedSecret, nonce);

        Map<String, byte[]> data = new HashMap<>();
        data.put("enc", ephemeral.getPublic().getEncoded());
        data.put("ct", ct);
        data.put("nonce", nonce);

        vertx.eventBus().publish("hpke.messages", data);
    }

    private KeyPair generateEphemeral() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("EC");
        kpg.initialize(new ECGenParameterSpec("secp256r1"));
        return kpg.generateKeyPair();
    }

    private byte[] encapsulate(PrivateKey skE, PublicKey pkR) throws Exception {
        KeyAgreement ka = KeyAgreement.getInstance("ECDH");
        ka.init(skE);
        ka.doPhase(pkR, true);
        return ka.generateSecret();
    }

    private byte[] encrypt(byte[] pt, byte[] sharedSecret, byte[] nonce) throws Exception {
        SecretKeySpec key = deriveAEADKey(sharedSecret);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, nonce));
        return cipher.doFinal(pt);
    }

    private SecretKeySpec deriveAEADKey(byte[] sharedSecret) throws Exception {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] okm = sha256.digest(sharedSecret);
        return new SecretKeySpec(okm, 0, 16, "AES");
    }
}
