package lab4;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigInteger;

public class ImageSteganography {

    //Get a bit from the byte on the pos.
    //@param val - byte to get bit of
    //@param pos - pos of a bit in the byte
    //@return bit from the byte on the pos
    public static byte getBit(byte val, int pos) {
        return (byte) ((val & (1 << pos)) >> pos);
    }

    //Get a byte array representation of the string.
    //@param val - string to get bytes of
    //@return byte array representation of the string
    public static byte[] getBytes(String val) {
        //return val.getBytes(StandardCharsets.US_ASCII);
        return val.getBytes();
    }

    //Get a string representation of the byte array.
    //@param byte - byte array
    //@return string representation of the byte array
    public static String getString(byte[] array) {
        //return new String(array, StandardCharsets.US_ASCII);
        return new String(array);
    }

    //Get a byte array representation of the integer number.
    //@param integer - integer number
    //@return byte - array representation of the integer number
    //@throws IOException thrown if errors while converting are occurred
    public static byte[] intToByteArray(final int integer) throws IOException {
        return BigInteger.valueOf(integer).toByteArray();
    }

    //Prepare text to encode.<br> Make a byte array containing bytes of source text and a length of source text.
    //@param text - source text
    //@return byte array of a source text and a length of it
    public static byte[] prepareTextToEncode(String text) {
        String textToEncode = String.format("%1$s %2$s", text.length(), text);
        //System.out.println(textToEncode);
        byte[] encode = getBytes(textToEncode);
        return encode;
    }

    //Make a copy of the image.
    //@param imageToCopy - image to make copy of
    //@return copy of the image
    public static BufferedImage makeImageCopy(BufferedImage imageToCopy) {
        BufferedImage result = new BufferedImage(imageToCopy.getWidth(),
                                            imageToCopy.getHeight(),
                                            imageToCopy.getType());
        Graphics g = result.getGraphics();
        g.drawImage(imageToCopy, 0, 0, null);
        return result;
    }

    //Encode text into a buffered image using Least Significant Bit algorithm.<br>
    //@param text - text to encode
    //@param bitmap - buffered image
    //@return encoded buffered image
    public static BufferedImage encode(String text, BufferedImage bitmap) throws Exception {
        int i = 0;
        int j = 8;
        //Creating a result image
        BufferedImage result = makeImageCopy(bitmap);
        //Preparing the text to encode
        byte[] encode = prepareTextToEncode(text);
        for(int y = 0; y < result.getHeight(); y++) {
            for(int x = 0; x < result.getWidth(); x++) {
                Color pixel = new Color(result.getRGB(x, y));
                int R = (j == 8) ? pixel.getRed() & 254 : pixel.getRed() & 254 | +getBit(encode[i], --j);
                int G = pixel.getGreen() & 254 | +getBit(encode[i], --j);
                int B = pixel.getBlue() & 254 | +getBit(encode[i], --j);
                Color newPixel = new Color(R, G, B);
                result.setRGB(x, y, newPixel.getRGB());
                if(j == 0) {
                    j = 8;
                    i++;
                }
                if(i == encode.length) { break; }
            }
            if(i == encode.length) { break; }
        }
        return result;
    }

    //Decode text from a buffered image using Least Significant Bit algorithm.
    //@param bitmap - encoded buffered image
    //@return decoded text
    public static String decode(BufferedImage bitmap) {
        int textLength = 0;
        boolean containsMessage = false;
        StringBuilder information = new StringBuilder();
        StringBuilder tmp = new StringBuilder();
        for(int y = 0; y < bitmap.getHeight(); y++) {
            for(int x = 0; x < bitmap.getWidth(); x++) {
                Color pixel = new Color(bitmap.getRGB( x, y ));
                tmp.append(pixel.getRed() % 2);
                tmp.append(pixel.getGreen() % 2);
                tmp.append(pixel.getBlue() % 2);
                if(tmp.length() == 9) {
                    try {
                        Integer tmpInt = Integer.parseInt(tmp.toString(), 2);
                        information.append(getString(intToByteArray(tmpInt)));
                        if(!containsMessage && tmpInt == 32) {
                            containsMessage = true;
                            information.deleteCharAt(information.length() - 1);
                            textLength = Integer.parseInt(information.toString());
                            information.setLength(0);
                        }
                        if(!containsMessage && tmpInt == 0) {
                            throw new Exception();
                        }
                        tmp.setLength(0);
                    } catch(Exception e) {
                        containsMessage = true;
                        information.setLength(0);
                        information.append( "Decode error!" );
                    }
                }
                if(containsMessage && information.length() >= textLength) { break; }
            }
            if(containsMessage && information.length() >= textLength) { break; }
        }
        return information.toString();
    }

    //Read Image from file
    //@param fName - file name
    //@return encoded buffered image
    public static BufferedImage readFile(String fName) throws IOException {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(fName));
        } catch (IOException e) {}
        return bi;
    }

    //Save Image to file
    //@param fName - file name
    //@param bi - encoded buffered image
    public static void saveFile(BufferedImage bi, String fName) throws IOException {
       try {
            File outputfile = new File(fName);
            ImageIO.write(bi, "PNG", outputfile);
        } catch (IOException e) {}
    }

    public static void main(String[] args) throws Exception {
        String fNameOrig = "one-dollar.png";
        String fNameEncrypted = "one-dollar_encrypted.png";
        BufferedImage img1, img2, img3 = null;
        //String msg = "Шифрування даних методом стеганографії";
        String msg = "Encryption of data by the method of steganography";

        System.out.println("Original Message : "+msg);
        System.out.println("Original Image file : "+fNameOrig);
        System.out.println();

        //Read Image, Encrypt Message in Image, Save Image
        System.out.println("Reading Image from file : "+fNameOrig);
        img1 = readFile(fNameOrig);
        try {
            System.out.println("Encoding...");
            img2 = encode(msg, img1);
        } catch( Exception e ) {
            return;
        }
        System.out.println("Save encrypted Image to file : "+fNameEncrypted);
        saveFile(img2, fNameEncrypted);
        System.out.println();

        //Read Image, Decode Message from encrypted Image, Return Message
        System.out.println("Reading encrypted Image from file : "+fNameEncrypted);
        img3 = readFile(fNameEncrypted);
        System.out.println("Decoding...");
        msg = decode(img3);
        System.out.println("Decoded Message : "+msg);
        System.out.println();

    }

}
