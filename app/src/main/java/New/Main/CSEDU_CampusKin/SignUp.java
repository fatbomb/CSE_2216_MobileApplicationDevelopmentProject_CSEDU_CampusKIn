package New.Main.CSEDU_CampusKin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.PrivateKey;
import java.util.List;

import New.Main.CSEDU_CampusKin.Activity.Profile;

public class SignUp extends AppCompatActivity {
    boolean isPasswordVisible = false;
    boolean isPasswordVisible1 = false;

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int GALLERY_PERMISSION_REQUEST = 101;
    ImageView profile;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private final String[] Gender = new String[]{"Male","Female","Other"};
    private EditText email;

    private EditText firstname,lastname,batch;


    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        //getSupportActionBar().hide();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Spinner gender = findViewById(R.id.gender);
        String[] items = {"Gender", "Male", "Female", "Others"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);


        EditText passwordEditText = findViewById(R.id.passwordEditText);
        profile = findViewById(R.id.profile);
        ImageView addImage = findViewById(R.id.AddImage);
        final Button register= findViewById(R.id.signup);
        email=findViewById(R.id.email);




        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        isPasswordVisible = !isPasswordVisible;

                        if (isPasswordVisible) {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.outline_remove_red_eye_24, 0);
                        } else {
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.hide_pass, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
        EditText confirmpass = findViewById(R.id.confirmpass);


        confirmpass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_END = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {
                        isPasswordVisible1 = !isPasswordVisible1;

                        if (isPasswordVisible1) {
                            confirmpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            confirmpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.outline_remove_red_eye_24, 0);
                        } else {
                            confirmpass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            //passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.outline_lock_24, 0);
                            confirmpass.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.outline_lock_24, 0, R.drawable.hide_pass, 0);
                        }

                        return true;
                    }
                }
                return false;
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptions(view);
            }
        });
        auth=FirebaseAuth.getInstance();
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getEmail = email.getText().toString();
                String s_pass1 = passwordEditText.getText().toString();
                String s_pass2 = confirmpass.getText().toString();
                if (!getEmail.matches(emailpattern)) email.setError("Enter correct e-mail");
                else if (s_pass1.isEmpty()) passwordEditText.setError("Password field can't be empty.");
                else if (s_pass1.length() < 6) passwordEditText.setError("Password length must be at least 6");
                else if (!s_pass1.equals(s_pass2)) confirmpass.setError("Password didn't match");
                else{
                    auth.createUserWithEmailAndPassword(getEmail,s_pass1).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignUp.this, "Register Sucessful", Toast.LENGTH_SHORT).show();
                                startActivity((new Intent(SignUp.this, Profile.class)));
                            }
                            else {
                                Toast.makeText(SignUp.this, "Registration faild", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                }

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
            profile.setImageURI(imageUri);
        } else if (requestCode == CAMERA_PERMISSION_REQUEST && resultCode == RESULT_OK) {
            Bitmap thumbnail = data.getParcelableExtra("data");
            profile.setImageBitmap(thumbnail);
            // Do other work with full size photo saved in locationForPhotos.
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("imageResource", R.id.profile); // Replace with your image resource
    }
}


