package New.Main.CSEDU_CampusKin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.OutputStream;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import New.Main.CSEDU_CampusKin.Activity.Profile;

public class SignUp extends AppCompatActivity {
    boolean isPasswordVisible = false;
    boolean isPasswordVisible1 = false;

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int GALLERY_PERMISSION_REQUEST = 101;
    ImageView profile;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //private final String[] Gender = new String[]{"Male","Female","Other"};
    private EditText email;
    String image="";

    private EditText firstname,lastname,batch,phnno,registration;


    private FirebaseAuth auth;
    private FirebaseFirestore mRootRef;

    private Uri imageUri;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        //getSupportActionBar().hide();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        firstname=findViewById(R.id.fstname);
        lastname=findViewById(R.id.lstname);
        batch=findViewById(R.id.bat);
        phnno=findViewById(R.id.contno);
        registration=findViewById(R.id.regno);

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
        mRootRef= FirebaseFirestore.getInstance();
        pd= new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String getEmail = email.getText().toString();
                String s_pass1 = passwordEditText.getText().toString();
                String name=firstname.getText().toString()+" "+lastname.getText().toString();
                String regnum=registration.getText().toString();
                String phone=phnno.getText().toString();
                String gen=gender.getSelectedItem().toString();
                String bat=batch.getText().toString();
                String s_pass2 = confirmpass.getText().toString();
                if (getEmail.isEmpty()|| !getEmail.matches(emailpattern)) email.setError("Enter correct e-mail");
                else if(name.isEmpty())firstname.setError("Name is Empty");
                else if(regnum.isEmpty()) registration.setError("No registration Number Provided");
                else if(phone.isEmpty()) phnno.setError("Phone no not provided");
                else if(gen=="Gender") {
                    ((TextView) gender.getSelectedView()).setError("Please select an option");
                    ((TextView) gender.getSelectedView()).requestFocus();
                }
                else if(bat.isEmpty())batch.setError("Batch no not provided");
                else if (s_pass1.isEmpty()) passwordEditText.setError("Password field can't be empty.");
                else if (s_pass1.length() < 6) passwordEditText.setError("Password length must be at least 6");
                else if (!s_pass1.equals(s_pass2)) confirmpass.setError("Password didn't match");
                else if(imageUri==null){
                    Toast.makeText(SignUp.this, "Please add a photo", Toast.LENGTH_SHORT).show();
                }
                else{
                    pd.setMessage("Please Wait");
                    pd.show();

//                    if(image==""){
//                        Toast.makeText(SignUp.this, "Image String Haray Geche", Toast.LENGTH_SHORT).show();
//                    }
                    auth.createUserWithEmailAndPassword(getEmail,s_pass1).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                if (user != null) {
                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Email verification sent
                                                        //Toast.makeText(SignUp.this, "Please Confirm your email to complete registration", Toast.LENGTH_SHORT).show();
                                                        //adduser(name,getEmail,regnum,phone,bat,gen);
                                                        uploadimage(name,getEmail,regnum,phone,bat,gen);

                                                    } else {
                                                        Toast.makeText(SignUp.this, "Please add a valid email Address", Toast.LENGTH_SHORT).show();
                                                        // Failed to send verification email
                                                    }
                                                }
                                            });
                                }


                                //startActivity((new Intent(SignUp.this, Profile.class)));
                            }
                            else {
                                pd.dismiss();
                                Toast.makeText(SignUp.this, "Registration faild", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });




                }

            }
        });

    }

    private String uploadimage(String name, String email, String regno, String phnno, String batch, String gender) {
        final String[] s = {""};
        //String img;

        if(imageUri!=null){
            StorageReference fileRef= FirebaseStorage.getInstance().getReference().child("Profile pictures").child(System.currentTimeMillis()+"."+getfileExtention(imageUri));
            fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            s[0] =uri.toString();
                            HashMap<String, Object> map= new HashMap<>();
                            map.put("username",name);
                            map.put("email",email);
                            map.put("registrationNo",regno);
                            map.put("phoneNo",phnno);
                            map.put("batch",batch);
                            map.put("gender",gender);
                            map.put("photo",s[0]);
                            map.put("createdTimeStamp", Timestamp.now());
                            map.put("userID",auth.getCurrentUser().getUid());
                            map.put("bio","");
                            map.put("works","");
                            map.put("workEnv","");
                            map.put("linkedin","");
                            map.put("fieldOfInt","");
                            mRootRef.collection("Users").document(auth.getCurrentUser().getUid()).set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    Toast.makeText(SignUp.this, "Please Confirm your email to complete registration", Toast.LENGTH_SHORT).show();
                                    startActivity((new Intent(SignUp.this, MainActivity.class)));

                                }
                            });
                            //Log.d("Downloadurl",image);
                        }
                    });
                }
            });

        }
        return s[0];
    }

    private String getfileExtention(Uri imageUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }

//    void adduser(String name, String email, String regno, String phnno, String batch, String gender){
//        HashMap<String, Object> map= new HashMap<>();
//        map.put("Name",name);
//        map.put("Email",email);
//        map.put("Registration no",regno);
//        map.put("Phone no",phnno);
//        map.put("Batch",batch);
//        map.put("Gender",gender);
//        map.put("Photo",image);
//        mRootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                pd.dismiss();
//                Toast.makeText(SignUp.this, "Please Confirm your email to complete registration", Toast.LENGTH_SHORT).show();
//                startActivity((new Intent(SignUp.this, MainActivity.class)));
//
//            }
//        });
//
//    }

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
        if(android.os.Build.VERSION.SDK_INT>=33){
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        GALLERY_PERMISSION_REQUEST);
            } else {
                openGallery();
            }
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        GALLERY_PERMISSION_REQUEST);
            } else {
                openGallery();
            }
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
            imageUri = data.getData();
            profile.setImageURI(imageUri);
        } else if (requestCode == CAMERA_PERMISSION_REQUEST && resultCode == RESULT_OK) {
            // Assuming you have a Bitmap named "thumbnail"
            Bitmap thumbnail = data.getParcelableExtra("data");

// Create a ContentResolver
            ContentResolver resolver = getContentResolver();

// Define the content values for the image
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

// Insert the image into the MediaStore and get the Uri
            imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

// Open an OutputStream to write the Bitmap to the Uri
            try {
                OutputStream outStream = resolver.openOutputStream(imageUri);
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

// Now, "imageUri" contains the Uri for the Bitmap

            profile.setImageBitmap(thumbnail);
            // Do other work with full size photo saved in locationForPhotos.
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(String.valueOf(imageUri), R.id.profile); // Replace with your image resource
    }
}


