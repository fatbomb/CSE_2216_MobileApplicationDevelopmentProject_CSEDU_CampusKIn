package New.Main.CSEDU_CampusKin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.hendraanggrian.appcompat.socialview.widget.SocialAutoCompleteTextView;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PostActivity extends AppCompatActivity {
    ImageView backButton;
    ImageView imageAdd;
    private TextView post;
    SocialAutoCompleteTextView description;
    private Uri imageUri;
    private String imageUrl;

    private static final int CAMERA_PERMISSION_REQUEST = 100;
    private static final int GALLERY_PERMISSION_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        backButton = findViewById(R.id.close);
        imageAdd = findViewById(R.id.cropImageView);
        post = findViewById(R.id.post);
        description = findViewById(R.id.description);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(PostActivity.this);

                // Set the title and message for the dialog
                builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want to discard?");

                // Add a cancel button
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog (cancel)
                        dialog.dismiss();
                    }
                });

                // Add a positive button (OK)
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Finish the activity
                        finish();
                    }
                });

                // Create and show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageOptions(view);
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upload();
            }
        });


    }

    private void upload() {
        ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();
        if(imageUri!=null){
            StorageReference filePath= FirebaseStorage.getInstance().getReference("Posts").child(System.currentTimeMillis()+"."+getfileExtention(imageUri));
            StorageTask uploadtask= filePath.putFile(imageUri);
            uploadtask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Uri downloadUri =task.getResult();
                    imageUrl = downloadUri.toString();
                    DatabaseReference rr= FirebaseDatabase.getInstance().getReference("Posts");
                    FirebaseFirestore ref= FirebaseFirestore.getInstance();
                    String postId = rr.push().getKey();
                    HashMap<String, Object> mp=new HashMap<>();
                    mp.put("postID",postId);
                    mp.put("postImage",imageUrl);
                    mp.put("postDescription",description.getText().toString());
                    mp.put("postedBy", FirebaseAuth.getInstance().getCurrentUser().getUid());
                    mp.put("postedDate", Timestamp.now());
                    mp.put("postLike",0);
                    mp.put("commentCount",0);
                    ref.collection("Post").document(postId).set(mp);
                    List<String> hashTags=description.getHashtags();

                    if(!hashTags.isEmpty()){
                        for(String tag:hashTags){
                           mp.clear();
                           mp.put("Tag",tag.toLowerCase());
                           mp.put("postID",postId);
                           ref.collection("HashTags").document(rr.getRef().toString()).set(mp);

                        }
                    }
                    pd.dismiss();
                    finish();




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            DatabaseReference rr= FirebaseDatabase.getInstance().getReference("Posts");
            FirebaseFirestore ref= FirebaseFirestore.getInstance();
            String postId = rr.push().getKey();
            HashMap<String, Object> mp=new HashMap<>();
            mp.put("postID",postId);
            mp.put("postImage","");
            mp.put("postDescription",description.getText().toString());
            mp.put("postedBy", FirebaseAuth.getInstance().getCurrentUser().getUid());
            mp.put("postedDate", Timestamp.now());
            mp.put("postLike",0);
            mp.put("commentCount",0);
            ref.collection("Post").document(postId).set(mp);
            List<String> hashTags=description.getHashtags();

            if(!hashTags.isEmpty()){
                for(String tag:hashTags){
                    mp.clear();
                    mp.put("Tag",tag.toLowerCase());
                    mp.put("postID",postId);
                    ref.collection("HashTags").document(rr.getRef().toString()).set(mp);

                }
            }
            pd.dismiss();
            finish();

        }
    }
    private String getfileExtention(Uri imageUri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
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
            imageAdd.setImageURI(imageUri);
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

            imageAdd.setImageBitmap(thumbnail);
            // Do other work with full size photo saved in locationForPhotos.
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(String.valueOf(imageUri), R.id.profile); // Replace with your image resource
    }
}