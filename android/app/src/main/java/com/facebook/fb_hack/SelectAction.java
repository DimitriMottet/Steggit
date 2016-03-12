package com.facebook.fb_hack;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class SelectAction extends AppCompatActivity {

    private Uri loadedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_action);

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_SEND.equals(action) && type != null && type.startsWith("image/")) {
            loadedImageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
            if (loadedImageUri != null) {
                // Show the image in the imageView control and store it for later use
                ImageView imgView = (ImageView) findViewById(R.id.imageView);
                imgView.setImageURI(loadedImageUri);
            }
        }

        final Button insertButton = (Button) findViewById(R.id.buttonInsert);
        insertButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startInsertMessage();
            }
        });

        final Button recoverButton = (Button) findViewById(R.id.buttonRecover);
        recoverButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startRecoverMessage();
            }
        });
    }

    // Launch insert message activity
    private void startInsertMessage() {
        Intent intent = new Intent(this, InsertMessage.class);
        intent.putExtra("imageUri", loadedImageUri);
        startActivity(intent);
    }

    // Launch recover message activity
    private void startRecoverMessage() {
        Intent intent = new Intent(this, RecoverMessage.class);
        intent.putExtra("imageUri", loadedImageUri);
        startActivity(intent);
    }

}
