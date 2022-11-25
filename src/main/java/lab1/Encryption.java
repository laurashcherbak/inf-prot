package lab1;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Encryption {
    static String key = "binary";
    static String text;// = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static char[] sorted_key;
    static int[][] a_key;
    static int x;
    static int y;
    static char[][] arr1;
    static char[][] arr2;
    static Map<Integer, Integer> dict_key = new HashMap<Integer, Integer>();

    //get index of symbol in array
    public static int getIndex(final char c, char[] arr) {
        final int length = arr.length;
        for(int i = 0; i < length; i++) {
            if (arr[i] == c) {
                return i;
            }
        }
        return -1;
    }

    // read text(english alphabet) from text file
    public static void readTextFile() {
        final Path file = Paths.get("./lab1.txt");
        try {
            text = Files.readString(file);
        } catch (final Exception e) {
            System.out.println(e);
        }
    }

    // save table (as object) with encrypted text to binary file
    public static void saveFile(char[][] arr) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("lab1.encrypted"));
            outputStream.writeObject(arr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // read table (as object) with encrypted text from binary file
    public static void readEncryptedFile() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("lab1.encrypted"));
            arr2 = (char[][])inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    //print array as table
    public static void print(final char[][] arr) {
        for(int i=0; i < y; i++) {
            for(int j=0; j < x; j++) {
                System.out.print(arr[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //prepare and fill table with key, indexes and text
    public static void fillTable() {
        for(int j=0; j < x; j++) {
            arr1[0][j] = key.toCharArray()[j];
            arr1[1][j] = (dict_key.get(j)).toString().toCharArray()[0];
        }

        int k = 0;
        for(int i=2; i < y; i++) {
            for(int j=0; j < x; j++) {
                if (k < text.toCharArray().length) {
                    arr1[i][j] = text.toCharArray()[k];
                    k++;
                }
                else {
                    arr1[i][j] = ' ';
                }
            }
        }
    }

    //encrypt table
    public static void encrypt() {
        for(int j=0; j < x; j++) {
            arr2[0][j] = sorted_key[j];
            arr2[1][j] = String.valueOf(j).toCharArray()[0];
        }
        for(int i=2; i < y; i++) {
            for(int j=0; j < x; j++) {
                arr2[i][j] = arr1[i][(getIndex(sorted_key[j], key.toCharArray()))];
            }
        }
    }


    //decrypt table
    public static void decrypt() {
        for(int j=0; j < x; j++) {
            arr1[0][j] = key.toCharArray()[j];
            arr1[1][j] = (dict_key.get(j)).toString().toCharArray()[0];
        }
        for(int i=2; i < y; i++) {
            for(int j=0; j < x; j++) {
                arr1[i][j] = arr2[i][dict_key.get(j)];
            }
        }
    }

    public static void main(String[] args) {
        //read text from text file
        readTextFile();

        // sort symbols in array(key) by alphabet order
        //sorted_key = key.toCharArray();
        //Arrays.sort(sorted_key);
        sorted_key =
                Stream.of(key.split(""))
                        .sorted()
                        .collect(Collectors.joining()).toCharArray();
        x = sorted_key.length;//column
        y = (int) Math.ceil((double)text.toCharArray().length/sorted_key.length) + 2;//row
        //decrypted table
        arr1 = new char[y][x];
        //encrypted table
        arr2 = new char[y][x];

        //prepare dictionary with indexes for key
        for(int i=0; i < sorted_key.length; i++) {
            dict_key.put(getIndex(sorted_key[i], key.toCharArray()), i);
        }

        System.out.println("Key: "+key);
        System.out.println("String: "+text);
        System.out.println();

        System.out.println("Table (Key+String) before encryption: ");
        fillTable();
        print(arr1);

        //encryption
        System.out.println("Table (Key+String) after encryption: ");
        encrypt();
        print(arr2);

        saveFile(arr2);

        System.out.println("String after encryption (converted to Octal): "+"");
        for(int i=0; i < x; i++) {
            for(int j=2; j < y; j++) {
                //System.out.print(Integer.toBinaryString((int)result_text[i][dict_key.get(j)]));
                //System.out.print(Integer.toOctalString((int)result_text[i][dict_key.get(j)]));
                //System.out.print(arr2[j][i]);
                System.out.print(Integer.toOctalString((int) arr2[j][i]));
            }
        }
        System.out.println();
        System.out.println();

        //decryption
        System.out.println("Table (Key+String) after decryption: "+"");
        readEncryptedFile();
        decrypt();
        print(arr1);

        System.out.println("String after decryption: ");
        for(int i=2; i < y; i++) {
            for(int j=0; j < x; j++) {
                System.out.print(arr1[i][j]);
            }
        }
        System.out.println();


    }

}
