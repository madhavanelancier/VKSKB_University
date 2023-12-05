package com.vskub.certificate.common;

import java.security.MessageDigest;
//import sun.misc.BASE64Encoder;
import java.security.NoSuchAlgorithmException;

public class MD5Sample {
    /**
     * Cipher for encode
     */
    private final MessageDigest md;

    public MD5Sample() throws SecurityException {
        try {
            md = MessageDigest.getInstance("MD5");
        }catch(Exception se) {
            throw new SecurityException("In MD5 constructor " + se);
        }
    }

    /*public String encode(String in) throws Exception {
        if (in == null) {
            return null;
        }
        try {
            byte[] raw = null;
            byte[] stringBytes = null;
            stringBytes = in.getBytes("UTF8");
            synchronized(md) {
                raw = md.digest(stringBytes);
            }
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(raw);
        } catch (Exception se) {
            throw new Exception("Exception while encoding " + se);
        }

    }*/
    public String decode(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            System.out.println("decode : "+hexString.toString());
            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /*public String decode(String in) {
        throw new RuntimeException("NOT SUPPORTED");
    }*/

    /**
     * Test harness
     * @param args
     */
    public static void main(String[] args) {
        String clearText = "a5204f1e876d662473fb21c8fe8ab5b9";
        try {
            MD5Sample app = new MD5Sample();
            String encryptedHash = app.decode(clearText);
            System.out.println(encryptedHash);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}