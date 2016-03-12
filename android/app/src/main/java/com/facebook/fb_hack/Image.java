package com.facebook.fb_hack;

import java.io.File;
import java.io.IOException;

public class Image {

    BufferedImage image;
    String filename;
    String extension;

    public Image(String filename){
        File f = new File(filename);
        BufferedImage image	= null;
        try{
            image = ImageIO.read(f);
        } catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
        }
        this.image=image;
        this.filename=filename;
        this.extension= Text.getFileExtension(filename);
    }


    /** Add text to the buffered image
     * @param s
     */
    public void addText(String s){
        // Convert to byte arrays
        byte img[] = getBytesFromBufferedImage();
        byte text[] = s.getBytes();
        byte length[]  = bitConversion(text.length);

        // Add the text
        try{
            addBytes(img,length,0); // 0 = first position
            addBytes(img,text,32); //  4 bytes of space for length: 4bytes*8bit = 32 bits
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
        }

    }

    /** Get the text from buffered image
     * @return
     */
    public String getText(){
        byte image[] = getBytesFromBufferedImage();
        byte text[] = getEncodedBytes(image);
        return new String(text);
    }

    /** Add bytes to bytes at a certain offset
     * @param init
     * @param toAdd
     * @param offset
     * @return
     */
    private static byte[] addBytes(byte[] init, byte[] toAdd, int offset){
        byte newBytes[]=init;
        if(toAdd.length + offset > init.length){
            throw new IllegalArgumentException("File not long enough!");
        }

        for(int i=0; i<toAdd.length; ++i){
            int add = toAdd[i];
            for(int bit=7; bit>=0; --bit, ++offset){
                int b = (add >>> bit) & 1;
                newBytes[offset] = (byte)((init[offset] & 0xFE) | b );
            }
        }
        return init;
    }

    /** Get the added bytes
     * @param image
     * @return
     */
    private byte[] getEncodedBytes(byte[] image){
        int length = 0;
        int offset = 32;
        for(int i=0; i<32; ++i){
            length = (length << 1) | (image[i] & 1);
        }

        byte[] result = new byte[length];

        for(int b=0; b<result.length; ++b ){
            for(int i=0; i<8; ++i, ++offset){
                result[b] = (byte)((result[b] << 1) | (image[offset] & 1));
            }
        }
        return result;
    }

    /** Get array of bytes from buffered image
     * @return
     */
    private byte[] getBytesFromBufferedImage(){
        WritableRaster raster = image.getRaster();
        DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        return buffer.getData();
    }

    /** Convert an integer into bytes
     * @param i
     * @return
     */
    private byte[] bitConversion(int i){
        byte byte3 = (byte)((i & 0xFF000000) >>> 24);
        byte byte2 = (byte)((i & 0x00FF0000) >>> 16);
        byte byte1 = (byte)((i & 0x0000FF00) >>> 8 );
        byte byte0 = (byte)((i & 0x000000FF)       );
        return(new byte[]{byte3,byte2,byte1,byte0});
    }

    /** Save a new image file
     */
    public File getFile(String s){
        File outputfile = new File(s);
        outputfile.delete();
        try {
            ImageIO.write(image, extension, outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputfile;
    }

}

