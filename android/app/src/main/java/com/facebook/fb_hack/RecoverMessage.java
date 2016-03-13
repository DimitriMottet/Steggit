package com.facebook.fb_hack;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

public class RecoverMessage extends AppCompatActivity {

    private Uri loadedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_message);

        // Retrieve the image
        Intent intent = getIntent();
        loadedImageUri = intent.getParcelableExtra("imageUri");
        if (loadedImageUri != null) {
            // Show the image in the imageView control and store it for later use
            ImageView imgView = (ImageView) findViewById(R.id.imageView);
            imgView.setImageURI(loadedImageUri);
        }

        final Button encodeButton = (Button) findViewById(R.id.buttonRecover);
        encodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recoverMessageFromImage();
            }
        });
    }

    private void recoverMessageFromImage() {
        final EditText editPassphrase = (EditText) findViewById(R.id.editPassphrase);
        final EditText editPlainText = (EditText) findViewById(R.id.editPlainText);
        String passphrase = editPassphrase.getText().toString();
        passphrase = "kkkkkkkkkkkkkkkk";

        try {
            System.out.println("OK");
            Image img = new Image(MediaStore.Images.Media.getBitmap(this.getContentResolver(), loadedImageUri));
            System.out.println("OK");
            byte[] encodedText = img.getText();
            System.out.println("OK");
            String plaintext = TextEncryption.decrypt(encodedText, passphrase);
            System.out.println("OK");
            editPlainText.setText(plaintext);
            System.out.println("OK");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
