package com.facebook.fb_hack;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

        final Button encodeButton = (Button) findViewById(R.id.buttonInsert);
        encodeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recoverMessageFromImage();
            }
        });
    }

    private void recoverMessageFromImage() {

    }

}
