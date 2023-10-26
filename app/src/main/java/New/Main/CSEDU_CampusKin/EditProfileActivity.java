package New.Main.CSEDU_CampusKin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

import New.Main.CSEDU_CampusKin.Model.UserModel;

public class EditProfileActivity extends AppCompatActivity {

    ImageView profile,addImage;
    TextView firstName,lastName,works,fieldOfInt,Contact,Batch,linkedin,bio;
    Spinner workEnv;
    AppCompatButton update,cancel;
    FirebaseUser firebaseUser;
    boolean dataChanged=false;
    UserModel user;

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int GALLERY_PERMISSION_REQUEST = 101;
    private Uri imageUri;
    private FirebaseFirestore mRootRef;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        profile=findViewById(R.id.profile);
        addImage=findViewById(R.id.AddImage);
        firstName=findViewById(R.id.fstname);
        lastName=findViewById(R.id.lstname);
        works=findViewById(R.id.work);
        fieldOfInt=findViewById(R.id.filedOfInt);
        Contact= findViewById(R.id.contno);
        Batch=findViewById(R.id.bat);
        workEnv=findViewById(R.id.workEnv);
        linkedin=findViewById(R.id.linkedin);
        bio=findViewById(R.id.bio);
        update=findViewById(R.id.update);
        cancel=findViewById(R.id.cancel);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String[] items = {"Working Environment","Academia", "Industry", "Both"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workEnv.setAdapter(adapter);
        pd= new ProgressDialog(this);
        mRootRef=FirebaseFirestore.getInstance();
        FirebaseFirestore.getInstance().collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    user =  documentSnapshot.toObject(UserModel.class);
                    Picasso.get().load(user.getPhoto()).placeholder(R.drawable.human).into(profile);
                    String input = user.getUsername();
                    int lastSpaceIndex = input.lastIndexOf(" ");
                    String[] parts = {input.substring(0, lastSpaceIndex), input.substring(lastSpaceIndex + 1)};
                    firstName.setText(parts[0]);
                    lastName.setText(parts[1]);
                    works.setText(user.getWorks());
                    fieldOfInt.setText(user.getFieldOfInt());
                    Contact.setText(user.getPhoneNo());
                    Batch.setText(user.getBatch());
                    String valueToSelect = user.getWorkEnv(); // Replace with the text of the item you want to select
                    int position = adapter.getPosition(valueToSelect);

                    if (position != -1) {
                        workEnv.setSelection(position);
                    }
                    linkedin.setText(user.getLinkedin());
                    bio.setText(user.getBio());


                }
                else{

                }

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd.setMessage("Please Wait");
                pd.show();
                uploadimage();
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptions(view);
            }
        });




    }
    private String getfileExtention(Uri imageUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
    }
    private void uploadimage() {
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
                            if(!user.getUsername().equals(firstName.getText().toString()+" "+lastName.getText().toString())){
                                map.put("username",firstName.getText().toString()+" "+lastName.getText().toString());
                            }
                            if(!user.getPhoneNo().equals(Contact.getText().toString())){
                                map.put("phoneNo",Contact.getText().toString());
                            }
                            if(!user.getBatch().equals(Batch.getText().toString())){
                                map.put("batch",Batch.getText().toString());
                            }
                            if(!user.getBio().equals(bio.getText().toString())){
                                map.put("bio",bio.getText().toString());
                            }
                            if(!user.getWorks().equals(works.getText().toString())){
                                map.put("works",works.getText().toString());
                            }
                            if(!user.getWorkEnv().equals(workEnv.getSelectedItem().toString())){
                                map.put("workEnv",workEnv.getSelectedItem().toString());
                            }
                            if(!user.getLinkedin().equals(linkedin.getText().toString())){
                                map.put("linkedin",linkedin.getText().toString());
                            }
                            if(!user.getFieldOfInt().equals(fieldOfInt.getText().toString())){
                                map.put("fieldOfInt",fieldOfInt.getText().toString());
                            }
                            map.put("photo",s[0]);

                            mRootRef.collection("Users").document(firebaseUser.getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    pd.dismiss();
                                    Toast.makeText(EditProfileActivity.this, "Update successful", Toast.LENGTH_SHORT).show();
                                    finish();
                                    //startActivity((new Intent(EditProfileActivity.this, MainActivity.class)));

                                }
                            });
                            //Log.d("Downloadurl",image);
                        }
                    });
                }
            });

        }
        else {
            HashMap<String, Object> map= new HashMap<>();
            if(!user.getUsername().equals(firstName.getText().toString()+" "+lastName.getText().toString())){
                map.put("username",firstName.getText().toString()+" "+lastName.getText().toString());
            }
            if(!user.getPhoneNo().equals(Contact.getText().toString())){
                map.put("phoneNo",Contact.getText().toString());
            }
            if(!user.getBatch().equals(Batch.getText().toString())){
                map.put("batch",Batch.getText().toString());
            }
            if(!user.getBio().equals(bio.getText().toString())){
                map.put("bio",bio.getText().toString());
            }
            if(!user.getWorks().equals(works.getText().toString())){
                map.put("works",works.getText().toString());
            }
            if(!user.getWorkEnv().equals(workEnv.getSelectedItem().toString())){
                map.put("workEnv",workEnv.getSelectedItem().toString());
            }
            if(!user.getLinkedin().equals(linkedin.getText().toString())){
                map.put("linkedin",linkedin.getText().toString());
            }
            if(!user.getFieldOfInt().equals(fieldOfInt.getText().toString())){
                map.put("fieldOfInt",fieldOfInt.getText().toString());
            }
            if(map.size()==0){
                pd.dismiss();
                Toast.makeText(this, "Nothing to update", Toast.LENGTH_SHORT).show();
                return;
            }


            mRootRef.collection("Users").document(firebaseUser.getUid()).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    pd.dismiss();
                    Toast.makeText(EditProfileActivity.this, "Update Complete", Toast.LENGTH_SHORT).show();
                    //startActivity((new Intent(SignUp.this, MainActivity.class)));
                    finish();

                }
            });
        }
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
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
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
            dataChanged=true;
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
            dataChanged=true;

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