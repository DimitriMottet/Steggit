package com.facebook.fb_hack;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Image {

    Bitmap image;
    int width, height;
    byte[] imageData;

    public Image(Bitmap bitmap) {
        this.image = bitmap;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.imageData = new byte[4 * width * height];
        // Copy raw image data to imageData
        ByteBuffer buffer = ByteBuffer.wrap(imageData);
        image.copyPixelsToBuffer(buffer);
    }

    /** Add text to the buffered image
     */
    public void addText(byte[] text) {
        // Convert to byte arrays
        byte length[]  = bitConversion(text.length);
        // Add the text
        addBytes(length, 0); // 0 = first position
        addBytes(text, 32); //  4 bytes of space for length: 4bytes*8bit = 32 bits
    }

    /** Get the text from buffered image
     */
    public byte[] getText() {
        return getEncodedBytes(imageData);
    }

    /** Save a new image file
     */
    public void saveFile(String filename) {
        // Save raw image data from imageData
        ByteBuffer buffer = ByteBuffer.wrap(imageData);
        image.copyPixelsFromBuffer(buffer);
        try {
            // Save to a file
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), filename);
            FileOutputStream out = new FileOutputStream(file);
            image.setHasAlpha(true);
            if (!image.compress(Bitmap.CompressFormat.PNG, 0, out))
                System.err.println("Oooops!");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** Add bytes to bytes at a certain offset
     */
    private void addBytes(byte[] toAdd, int offset) {
        if (toAdd.length + offset > imageData.length) {
            System.err.println("Picture too small");
            return;
        }

        for(int i = 0; i < toAdd.length; ++i) {
            int add = toAdd[i];
            for (int bit = 7; bit >= 0; --bit, ++offset) {
                int b = (add >>> bit) & 1;
                imageData[offset] = (byte)((imageData[offset] & 0xFE) | b);
            }
        }
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

    /** Convert an integer into bytes
     */
    private byte[] bitConversion(int i) {
        byte byte3 = (byte)((i & 0xFF000000) >>> 24);
        byte byte2 = (byte)((i & 0x00FF0000) >>> 16);
        byte byte1 = (byte)((i & 0x0000FF00) >>> 8 );
        byte byte0 = (byte)((i & 0x000000FF)       );
        return new byte[]{byte3, byte2, byte1, byte0};
    }

}


