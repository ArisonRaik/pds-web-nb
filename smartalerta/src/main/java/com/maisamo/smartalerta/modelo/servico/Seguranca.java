/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.maisamo.smartalerta.modelo.servico;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author wagner
 */
public class Seguranca {

    public static String Criptografar(String entrada) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(entrada.getBytes(), 0, entrada.length());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}