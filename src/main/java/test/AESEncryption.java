package test;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESEncryption {

    public static void main(String[] args) throws
            NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, InvalidAlgorithmParameterException {

        String message = "Розробка системи шифрування/розшифрування в режимі AES";

        System.out.println("Original message : "+message);

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecretKey secretKey = keyGenerator.generateKey();
        String secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated key : "+secretKeyString);

        //Encrypt our message
        Cipher encryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] InitVectorBytes = keyGenerator.generateKey().getEncoded();
        IvParameterSpec parameterSpec = new IvParameterSpec(InitVectorBytes);
        encryptionCipher.init(Cipher.ENCRYPT_MODE,secretKey,parameterSpec);
        byte[] encryptedMessageBytes = encryptionCipher.doFinal(message.getBytes());
        String encryptedMessage = Base64.getEncoder().encodeToString(encryptedMessageBytes);
        System.out.println("Encrypted message : "+encryptedMessage);

        //Decrypt the encrypted message
        Cipher decryptionCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptionCipher.init(Cipher.DECRYPT_MODE,secretKey,parameterSpec);
        byte[] decryptedMessageBytes = decryptionCipher.doFinal(encryptedMessageBytes);
        String decryptedMessage = new String(decryptedMessageBytes);
        System.out.println("Decrypted message : "+decryptedMessage);

    }
}
