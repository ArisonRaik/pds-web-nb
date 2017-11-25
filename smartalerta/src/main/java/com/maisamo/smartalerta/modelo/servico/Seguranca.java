/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maisamo.smartalerta.modelo.servico;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

/**
 *
 * @author wagner
 */
public class Seguranca {

    public static String paraMD5(String mensagem) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(mensagem.getBytes(), 0, mensagem.length());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static String paraRSA(String mensagem) throws Exception {
        PrivateKey privateKey = buildKeyPair().getPrivate();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        byte[] criptografado = cipher.doFinal(mensagem.getBytes());
        return new String(criptografado);
    }

    public static String deRSA(String mensagem_rsa) throws Exception {
        PublicKey publicKey = buildKeyPair().getPublic();
        
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        byte[] decriptografado = cipher.doFinal(mensagem_rsa.getBytes());
        return new String(decriptografado);
    }

    private static KeyPair buildKeyPair() throws NoSuchAlgorithmException {
        final int keySize = 2048;
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(keySize);
        return keyPairGenerator.genKeyPair();
    }
}
