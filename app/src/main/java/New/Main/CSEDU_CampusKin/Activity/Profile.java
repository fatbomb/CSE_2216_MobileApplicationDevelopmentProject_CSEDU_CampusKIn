package New.Main.CSEDU_CampusKin.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import New.Main.CSEDU_CampusKin.HomePage;
import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.SignUp;

public class Profile extends AppCompatActivity {
    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int GALLERY_PERMISSION_REQUEST = 101;
    ImageView profile;
    ImageView cover;
    boolean select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiledemo);
        //getSupportActionBar().hide();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        profile = findViewById(R.id.profile);
        cover = findViewById(R.id.coverPhoto);
        ImageView addImage = findViewById(R.id.AddImage);
        final ImageView home= findViewById(R.id.AddImage0);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select=true;
                showImageOptions(view);
            }
        });
        ImageView addImage1 = findViewById(R.id.AddImage2);
        addImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select=false;
                showImageOptions(view);
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity((new Intent(Profile.this, HomePage.class)));
            }
        });
    }
    public void showImageOptions(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose an Option")
                .setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            requestCameraPermission();
                        } else if (which == 1) {
                            requestGalleryPermission();
                        }
                    }
                });
        builder.show();
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        } else {
            openCamera();
        }
    }

    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    GALLERY_PERMISSION_REQUEST);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                // Handle permission denial
                return;
            }
        } else if (requestCode == GALLERY_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                // Handle permission denial
                return;
            }
        }
    }

    private void openCamera() {
        // Implement camera opening logic here
        Intent data = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(data, CAMERA_PERMISSION_REQUEST);
    }

    private void openGallery() {
        // Implement gallery opening logic here
        Intent data = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(data, GALLERY_PERMISSION_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PERMISSION_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if(select) {
                profile.setImageURI(imageUri);
            }
            else {
                cover.setImageURI(imageUri);
            }
        } else if (requestCode == CAMERA_PERMISSION_REQUEST && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            if(select) {
                profile.setImageBitmap(thumbnail);
            }
            else {
                cover.setImageBitmap(thumbnail);
            }
            // Do other work with full size photo saved in locationForPhotos.
        }

    }
}

