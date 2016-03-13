package com.facebook.fb_hack;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InsertMessage extends AppCompatActivity {

    private Uri loadedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_message);

        // Retrieve the image
        Intent intent = getIntent();
        loadedImageUri = intent.getParcelableExtra("imageUri");
        if (loadedImageUri != null) {
            // Show the image in the imageView control and store it for later use
            ImageView imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageURI(loadedImageUri);
        }

        final Button encodeButton = (Button) findViewById(R.id.buttonEncode);
        encodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                encodeMessageOnImage();
            }
        });
    }

    private void encodeMessageOnImage() {
        final EditText editMessage = (EditText) findViewById(R.id.editMessage);
        final EditText editPassphrase = (EditText) findViewById(R.id.editPassphrase);
        if (editMessage == null || editMessage.getText() == null || editMessage.getText().toString().isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "A text is required", Toast.LENGTH_SHORT).show();
        } else if (editPassphrase == null || editPassphrase.getText() == null || editPassphrase.getText().toString().isEmpty()) {
            Toast.makeText(this.getApplicationContext(), "A passphrase is required", Toast.LENGTH_SHORT).show();
        } else {
            String message = editMessage.getText().toString();
            String passphrase = editPassphrase.getText().toString();
            String filename = getRealPathFromURI(loadedImageUri);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inDither = false;
            options.inMutable = true;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            options.inScaled = false;
            Image img = new Image(BitmapFactory.decodeFile(filename, options));

            byte[] encodedText = TextEncryption.encrypt(message, passphrase);
            img.addText(encodedText);
            img.saveFile("testfile.png");
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        if (contentUri.getPath().startsWith("/storage"))
            return contentUri.getPath();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, filePathColumn, null, null, null);
        String realPath = null;
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            realPath = cursor.getString(columnIndex);
        }
        cursor.close();
        return realPath;
    }

}
