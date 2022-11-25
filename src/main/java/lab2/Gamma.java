// Java Program to Implement the Gamma Algorithm
package lab2;

import java.util.Arrays;

public class Gamma {

//        Буква А Б В Г Ґ Д Е Є Ж З И І
//        Код 01 02 03 04 05 06 07 08 09 10 11 12
//        Буква Ї Й К Л М Н О П Р С Т У
//        Код 13 14 15 16 17 18 19 20 21 22 23 24
//        Буква Ф Х Ц Ч Ш Щ Ь Ю Я Пробіл
//        Код 25 26 27 28 29 30 31 32 33 34
//        Цифра 0 1 2 3 4 5 6 7 8 9
//        Код 35 36 37 38 39 40 41 42 43 44

    static char[] gSymbol = new char[] {
        'А','Б','В','Г','Ґ','Д','Е','Є','Ж','З','И','І',
        'Ї','Й','К','Л','М','Н','О','П','Р','С','Т','У',
        'Ф','Х','Ц','Ч','Ш','Щ','Ь','Ю','Я','_',
        '0','1','2','3','4','5','6','7','8','9'
    };
    static int[] gCode = new int[]  {
         1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12,
        13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
        25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
        35, 36, 37, 38, 39, 40, 41, 42, 43, 44
    };

//    static String gamma = "ТИГР";
//    static String message = "ЛЕГІОН_27";
    static String gamma;// = "ВЕЛИЧАР";
    static String message;// = "АЯНОМАЙСКІЙ_200_Т";
    static char[] gammaCh;
    static char[] messageCh;

//    static String gamma = "ДОБРОСЛАВ";
    static String encryptedMessage;// = "ПФХ0ЧГПШ7Й73568ДЧ";

    //get gamma code by char
    public static int getGammaCodeByCh(final char c) {
        final int length = gSymbol.length;
        for(int i = 0; i < length; i++) {
            if (gSymbol[i] == c) {
                return gCode[i];
            }
        }
        return 0;
    }

    // get gamma char by code
    public static char getGammaChByCode(final int c) {
        final int length = gSymbol.length;
        for(int i = 0; i < length; i++) {
            if (gCode[i] == c) {
                return gSymbol[i];
            }
        }
        return ' ';
    }

    // encrypt
    public static char[] encrypt(char[] msg) {
        int j = 0;
        int code;
        char[] result = new char[msg.length];
        for(int i = 0; i < msg.length; i++) {
            code = getGammaCodeByCh(msg[i]) + getGammaCodeByCh(gamma.toCharArray()[j]);
            code = code % (gSymbol.length);
            result[i] = getGammaChByCode(code);
            j++;
            if(j == gamma.toCharArray().length)
                j = 0;
        }
        return result;
    }

    //decrypt
    public static char[] decrypt(char[] msg) {
        int j = 0;
        int code;
        char[] result = new char[msg.length];
        for(int i = 0; i < msg.length; i++) {
            code = getGammaCodeByCh(msg[i]) - getGammaCodeByCh(gamma.toCharArray()[j]) + gSymbol.length;
            code = code % (gSymbol.length);
            result[i] = getGammaChByCode(code % gSymbol.length);
            j++;
            if(j == gamma.toCharArray().length)
                j = 0;
        }
        return result;
    }

    // print
    public static void print(final char[] arr) {
        for(int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
        }
        System.out.println();
    }

    public static void main(String[] args) {

        System.out.println();

        gamma = "ВЕЛИЧАР";
        message = "АЯНОМАЙСКІЙ_200_Т";
        gammaCh = gamma.toCharArray();
        messageCh = message.toCharArray();
        System.out.println("Gamma: "+gamma);
        System.out.println("Message: "+message);
        // encrypt message
        messageCh = encrypt(messageCh);
        System.out.print("Encrypted message: ");
        print(messageCh);
//        messageCh = decrypt(messageCh);
//        System.out.print("Decrypted message: ");
//        print(messageCh);

        System.out.println();

        gamma = "ДОБРОСЛАВ";
        message = "ПФХ0ЧГПШ7Й73568ДЧ";
        gammaCh = gamma.toCharArray();
        messageCh = message.toCharArray();
        System.out.println("Gamma: "+gamma);
        System.out.println("Message: "+message);
        //decrypt message
        messageCh = decrypt(messageCh);
        System.out.print("Decrypted message: ");
        print(messageCh);
//        messageCh = encrypt(messageCh);
//        System.out.print("Encrypted message: ");
//        print(messageCh);

        System.out.println();
    }

}
