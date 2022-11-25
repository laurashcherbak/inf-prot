// Java Program to Implement the RSA Algorithm
package lab3;

import java.math.*;
import java.text.DecimalFormat;

class RSAEncryption {

    //    Буква А Б В Г Д Е Ї Ж З И Й К
    //    Код 01 02 03 04 05 06 07 08 09 10 11 12
    //    Буква Л М Н О П Р С Т У Ф Х Ц
    //    Код 13 14 15 16 17 18 19 20 21 22 23 24
    //    Буква Ч Ш Щ Є Ґ Ь І Ю Я Пробіл
    //    Код 25 26 27 28 29 30 31 32 33 34
    //    Цифра 0 1 2 3 4 5 6 7 8 9
    //    Код 35 36 37 38 39 40 41 42 43 44

    static char[] symbol = new char[] {
         'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ї', 'Ж', 'З', 'И', 'Й', 'К',
        //01   02   03   04   05   06   07   08   09   10   11   12
         'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц',
        //13   14   15   16   17   18   19   20   21   22   23   24
         'Ч', 'Ш', 'Щ', 'Є', 'Ґ', 'Ь', 'І', 'Ю', 'Я', ' ',
        //25   26   27   28   29   30   31   32   33   34
         '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
        //35   36   37   38   39   40   41   42   43   44
    };

    static int p;// = 13;// 1st prime number p
    static int q;// = 17;// 2nd prime number q
    static int n, e, d, z, j;
    static String originalMsg;
    static double[] encryptedMsg;
    static char[] decryptedMsg;

    // greatest common divisor (GCD) / Найбільший спільний дільник
    static int gcd(int e, int z)
    {
        if (e == 0)
            return z;
        else
            return gcd(z % e, e);
    }

    // get code by char
    public static int getCodeByChar(final char c) {
        final int length = symbol.length;
        for(int i = 0; i < length; i++) {
            if (symbol[i] == c) {
                return ++i;//gCode[i];
            }
        }
        return 0;
    }

    // get char by code
    public static char getCharByCode(final int c) {
        final int length = symbol.length;
        for(int i = 0; i < length; i++) {
            //if (gCode[i] == c) {
            if ((i+1) == c) {
                return symbol[i];
            }
        }
        return ' ';
    }

    public static double[] encrypt(char[] msg, int e, int n) {
        double[] result = new double[msg.length];
//        BigInteger N = BigInteger.valueOf(n);// converting int value of n to BigInteger
        for(int i = 0; i < msg.length; i++) {
//            BigInteger C = BigDecimal.valueOf(getCodeByChar(msg[i])).toBigInteger();// converting double value of msg[i] to BigInteger
//            int code = (C.pow(e)).mod(N).intValue();
//            result[i] = getCharByCode(code);
            result[i] = ((Math.pow(getCodeByChar(msg[i]), e)) % n);
        }
        return result;
    }

    public static char[] decrypt(double[] msg, int d, int n) {
        char[] result = new char[msg.length];
        BigInteger N = BigInteger.valueOf(n);// converting int value of n to BigInteger
        for(int i = 0; i < msg.length; i++) {
            BigInteger C = BigDecimal.valueOf(msg[i]).toBigInteger();// converting double value of msg[i] to BigInteger
            int code = (C.pow(d)).mod(N).intValue();
            result[i] = getCharByCode(code);
//            result[i] = (char) code;
        }
        return result;
    }

    public static void print(final String s, final char[] arr) {
        System.out.print(s);
        for(int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    public static void print(final String s, final double[] arr) {
        System.out.print(s);
        for(int i = 0; i < arr.length; i++) {
            System.out.print(new DecimalFormat("#0").format(arr[i]));
            if (i < arr.length - 1) System.out.print(", ");
        }
        System.out.println();
    }

    public static void getKeysPair(int p, int q) {
        System.out.println("Prime numbers (p, q) : ("+p+", "+q+")");
        System.out.println();

        n = p * q;
        z = (p - 1) * (q - 1);//z - Euler function
        System.out.println("the value of z = " + z);

        for (e = 2; e < z; e++) {
            // e is for public key exponent
            if (gcd(e, z) == 1) {
                break;
            }
        }
        System.out.println("- PublicKey (e, n) : ("+e+", "+n+")");

        d = 0;
        for (int i = 0; i <= 9; i++) {
            int x = 1 + (i * z);
            // d is for private key exponent
            if (x % e == 0) {
                d = x / e;
                break;
            }
        }
        System.out.println("- PrivateKey (d, n) : ("+d+", "+n+")");
    }

    public static void main(String args[])
    {
        System.out.println("Examples from lection :");

        getKeysPair(7, 13);
        System.out.println();

        System.out.println("PART 1 (encryption) :");
        originalMsg = "ДВГУПС";
        System.out.println("- Original message : "+originalMsg);
        encryptedMsg = encrypt(originalMsg.toCharArray(), e, n);
        print("- Encrypted message : ", encryptedMsg);
        decryptedMsg = decrypt(encryptedMsg, d, n);
        print("- Decrypted message : ", decryptedMsg);

        System.out.println("PART 2 (decryption) :");
        encryptedMsg = new double[] {31, 61, 23, 21, 75, 80};
        print("- Original Encrypted message : ", encryptedMsg);
        decryptedMsg = decrypt(encryptedMsg, d, n);
        print("- Decrypted message : ", decryptedMsg);
        encryptedMsg = encrypt(decryptedMsg, e, n);
        print("- Encrypted message : ", encryptedMsg);

        System.out.println();

        System.out.println("Lab 3 :");

        getKeysPair(13, 17);
        System.out.println();

//        System.out.println("PART 1 (encryption) :");
        originalMsg = "БАГАЛ4";
        System.out.println("- Original message : "+originalMsg);
        encryptedMsg = encrypt(originalMsg.toCharArray(), e, n);
        print("- Encrypted message : ", encryptedMsg);
        decryptedMsg = decrypt(encryptedMsg, d, n);
        print("- Decrypted message : ", decryptedMsg);

//        System.out.println("PART 2 (decryption) :");
//        //encryptedMsg = new double[] {32, 1, 140, 1, 13, 65};
//        encryptedMsg = new double[] {27, 152, 208, 141, 142, 200, 208, 1};
//        print("- Original Encrypted message : ", encryptedMsg);
//        decryptedMsg = decrypt(encryptedMsg, d, n);
//        print("- Decrypted message : ", decryptedMsg);
//        encryptedMsg = encrypt(decryptedMsg, e, n);
//        print("- Encrypted message : ", encryptedMsg);

//        getKeysPair(13, 31);
//        System.out.println();
//
//        System.out.println("PART 1 (encryption) :");
//        originalMsg = "ПАЙ_50";
//        System.out.println("- Original message : "+originalMsg);
//        encryptedMsg = encrypt(originalMsg.toCharArray(), e, n);
//        print("- Encrypted message : ", encryptedMsg);
//        decryptedMsg = decrypt(encryptedMsg, d, n);
//        print("- Decrypted message : ", decryptedMsg);
//
//        System.out.println("PART 2 (decryption) :");
//        //encryptedMsg = new double[] {0, 0, 0};
//        encryptedMsg = new double[] {40, 336, 307, 207, 291, 307, 329, 396};
//        print("- Original Encrypted message : ", encryptedMsg);
//        decryptedMsg = decrypt(encryptedMsg, d, n);
//        print("- Decrypted message : ", decryptedMsg);
//        encryptedMsg = encrypt(decryptedMsg, e, n);
//        print("- Encrypted message : ", encryptedMsg);

    }

}
