/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.snk.door.demo;

/**
 * @author F
 */
public class Main1 {

    public static void main(String[] args) {
      //  long crc=ByteUtil.CreateCRC32(new byte[100],50, 50);
       // System.out.println(crc);

        /*TcpServerTest tcpServerTest = new TcpServerTest();
        tcpServerTest.syn();*/

        DemoTest test = new DemoTest();
        test.OpenDoor();
        //test.readFile();
       // test.readTransactionDatabase();
       // test.writeTransactionDatabaseReadIndex();
        //test.addPerson();
        //test.deletePerson();
        //  test.HoldDoor();
        //  test.UnlockDoor();
        // test.syn();
        // test.WriteReaderOption();
        // test.ReadReaderOption();
        //test.writeRelayOption();
        // test.readRelayOption();
        //test.writeUnlockingTime();
        //  test.readUnlockingTime();

      //  test.readExemptionVerificationOpen();
       // test.writeExemptionVerificationOpen();
       // test.readVoiceBroadcastSetting();
     //   test.writeVoiceBroadcastSetting();
        //test.readReaderIntervalTime();
      //  test.writeReaderIntervalTime();
        //test.readExpirationPrompt();
        //test.readExpirationPrompt();
        //test.OpenDoor();
        //test.syn();
   /*     String publicKey="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArfOzx+LjKF+BZKcbriPu" +
                "rWwmlrif+fQvZyqQq6hG8SWZRE52Ahp++3Fem79XdAu3U5jumvOeEKAfXMCClsfV" +
                "G9EqhLNSVA7Xb8zgnVelSHMPg9r2LX73nPSK28r3SoHAAuVNrva8f94koCYV8zym" +
                "I6W3duhDL/bfQDUkFS3MJcUb8bQcaxupKPLkxImBYGAjI3ceSMi984CFCcS8D6yU" +
                "tWGnxqy/nZVrfws7eI72FSpa2JaWkp7Bqm27bAMnirMx27rRN9uatHLjGBS60yrO" +
                "kZ1UJDkffi9tyOEIaEbNvUJWMH9rSAqiMpWH9Qdo9Vre4heMwNaxcFheYfty/o8Q" +
                "aQIDAQAB";
        try {
            System.out.println(encrypt("Fcard,system,0000",publicKey));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*TcpServerTest tcpServerTest = new TcpServerTest();
        tcpServerTest.syn();*/
    }

/*    public static String encrypt( String str, String publicKey ) throws Exception{
        //base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
        return outStr;
    }*/
}
