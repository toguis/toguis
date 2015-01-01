/***********************************************************************************************
 * Project: Tourist Guide System Toguis
 * University: UNIAJC
 * Authors: Julieth Candia and Carlos Morante
 * Year: 2014 - 2015
 * Version: 1.0 
 * License: GPL V2
 ***********************************************************************************************/
package co.edu.uniajc.vtf.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestManager {
    private static String convertToHex(byte[] pData) {
        StringBuilder lsBuffer = new StringBuilder();
        for (byte b : pData) {
            int liHalfByte = (b >>> 4) & 0x0F;
            int liTwoHalfs = 0;
            do {
            	lsBuffer.append((0 <= liHalfByte) && (liHalfByte <= 9) ? (char) ('0' + liHalfByte) : (char) ('a' + (liHalfByte - 10)));
                liHalfByte = b & 0x0F;
            } while (liTwoHalfs++ < 1);
        }
        return lsBuffer.toString();
    }

    public static String digestSHA1(String pText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest loMessageDigest = MessageDigest.getInstance("SHA-1");
        loMessageDigest.update(pText.getBytes("iso-8859-1"), 0, pText.length());
        byte[] lbySha1Hash = loMessageDigest.digest();
        return convertToHex(lbySha1Hash);
    }
}
