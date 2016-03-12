package com.facebook.fb_hack;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Image {

    String filename;
    String extension;
    Bitmap image;
    int width, height;
    byte imageData[];

    public Image(String filename) {
        this.filename = filename;
        this.extension = getFileExtension(filename);
        this.image = BitmapFactory.decodeFile(filename);
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.imageData = new byte[4 * width * height];
        getBytesFromBitmap();
    }

    private String getFileExtension(String fileName) {
        String ext = "";
        int mode = 0;
        for(int i = 0; i < fileName.length(); i ++){
            if(mode == 0 && fileName.charAt(i) == '.'){
                mode = 1;
            }
            else if (mode == 1){
                ext += fileName.charAt(i);
            }
        }
        return ext;
    }

    /** Add text to the buffered image
     * @param s
     */
    public void addText(String s) {
        // Convert to byte arrays
        byte text[] = s.getBytes();
        byte length[]  = bitConversion(text.length);
        // Add the text
        addBytes(imageData, length, 0); // 0 = first position
        addBytes(imageData, text, 32); //  4 bytes of space for length: 4bytes*8bit = 32 bits
    }

    /** Get the text from buffered image
     */
    public String getText() {
        return new String(getEncodedBytes(imageData));
    }

    /** Add bytes to bytes at a certain offset
     */
    private static byte[] addBytes(byte[] init, byte[] toAdd, int offset) {
        if (toAdd.length + offset > init.length)
            return null;

        byte newBytes[] = init;
        for(int i = 0; i < toAdd.length; ++i) {
            int add = toAdd[i];
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (add >>> bit) & 1;
                newBytes[offset] = (byte)((init[offset] & 0xFE) | b);
            }
        }
        return init;
    }

    /** Get the added bytes
     */
    private byte[] getEncodedBytes(byte[] image) {
        int length = 0;
        int offset = 32;
        for (int i = 0; i < 32; ++i) {
            length = (length << 1) | (image[i] & 1);
        }

        byte[] result = new byte[length];
        for (int b = 0; b < result.length; ++b) {
            for (int i = 0; i < 8; ++i, ++offset) {
                result[b] = (byte) ((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }

    /** Get array of bytes from buffered image
     */
    private void getBytesFromBitmap() {
        int pixels[] = new int[width * height];
        image.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < width * height; i++) {
            imageData[4*i]   = (byte)((pixels[i] & 0xFF000000) >>> 24);
            imageData[4*i+1] = (byte)((pixels[i] & 0x00FF0000) >>> 16);
            imageData[4*i+2] = (byte)((pixels[i] & 0x0000FF00) >>> 8 );
            imageData[4*i+3] = (byte)((pixels[i] & 0x000000FF)       );
        }
    }

    /** Convert an integer into bytes
     */
    private byte[] bitConversion(int i) {
        byte byte3 = (byte)((i & 0xFF000000) >>> 24);
        byte byte2 = (byte)((i & 0x00FF0000) >>> 16);
        byte byte1 = (byte)((i & 0x0000FF00) >>> 8 );
        byte byte0 = (byte)((i & 0x000000FF)       );
        return new byte[]{byte3, byte2, byte1, byte0};
    }

    /** Save a new image file
     */
    public void saveFile(String filename) {
        try {
            FileOutputStream out = new FileOutputStream(filename);
            image.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


