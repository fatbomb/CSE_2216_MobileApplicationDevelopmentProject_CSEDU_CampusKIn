package New.Main.CSEDU_CampusKin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import New.Main.CSEDU_CampusKin.R;
import New.Main.CSEDU_CampusKin.Utils.FirebaseUtils;

public class GiveReviewActivity extends AppCompatActivity {
    AppCompatButton update,cancel;
    TextView started,ended,workStat,bossName,revWorkPlace,revBoss,bossLinkedin,workPlace;
    Date start,end;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_workplace_screen);
        update=findViewById(R.id.update);
        cancel=findViewById(R.id.cancel);
        started=findViewById(R.id.started_working);
        ended=findViewById(R.id.ended_working);
        workStat=findViewById(R.id.workingStatus);
        bossName=findViewById(R.id.prof_boss);
        revWorkPlace=findViewById(R.id.workplace_review);
        workPlace=findViewById(R.id.workplace_name);
        revBoss=findViewById(R.id.prof_boss_review);
        bossLinkedin=findViewById(R.id.linkedin);
        start=null;
        end=null;
        started.setOnClickListener(v -> showDatePickerDialog(started,1 ));
        ended.setOnClickListener(v-> showDatePickerDialog(ended,2));


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDiscardConfirmationDialog();
            }
        });
        pd =new ProgressDialog(this);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(workPlace.getText().toString().isEmpty()){
                    workPlace.setError("Work place not provide");
                }
                else if(start==null){
                    started.setError("Starting Date not provided");
                }
                else if(workStat.getText().toString().isEmpty()){
                    workStat.setError("Working Status not provide");
                }
                else if(bossName.getText().toString().isEmpty()){
                    bossName.setError("Boss/Professor Name not provided");
                }
                else if(revWorkPlace.getText().toString().isEmpty() && revBoss.getText().toString().isEmpty()){
                    revWorkPlace.setError("Review on Working Place not Provide");
                }
                else if(bossLinkedin.getText().toString().isEmpty()){
                    bossLinkedin.setError("Boss linked in not provide");
                }
                else {
                    pd.setTitle("Uploading.....");
                    pd.setMessage("Please wait");
                    pd.show();

                    uploadData();
                }
            }
        });


    }

    private void uploadData() {
        DatabaseReference db= FirebaseDatabase.getInstance().getReference().child("review");

        Map<String,Object> map= new HashMap<>();
        String id=db.push().getKey();
        map.put("id",id);
        map.put("userId", FirebaseUtils.currentUserId());
        map.put("nameOfWorkPlace",workPlace.getText().toString());
        map.put("workingStatus",workStat.getText().toString());
        map.put("bossName",bossName.getText().toString());
        map.put("reviewOnWorkPlace",revWorkPlace.getText().toString());
        map.put("reviewOnBoss",revBoss.getText().toString());
        map.put("bossLinkedin",bossLinkedin.getText().toString());
        map.put("started",start);
        map.put("ended",end);
        FirebaseFirestore.getInstance().collection("WorkReview").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(GiveReviewActivity.this, "Review Added", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GiveReviewActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        });




    }

    @Override
    public void onBackPressed() {
        // Override the back button to show the confirmation dialog
        showDiscardConfirmationDialog();
    }
    private void showDatePickerDialog(TextView selectedDateTextView, int dateNumber) {
        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
            // Update the appropriate class-level variable based on dateNumber
            if (dateNumber == 1) {
                Calendar startCalendar = Calendar.getInstance();
                startCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                start = startCalendar.getTime();
            } else if (dateNumber == 2) {
                Calendar endCalendar = Calendar.getInstance();
                endCalendar.set(selectedYear, selectedMonth, selectedDayOfMonth);
                end = endCalendar.getTime();
            }

            // Update the TextView with the selected date
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String formattedDate;
            if(dateNumber==1){
                formattedDate = dateFormat.format(start);
            }
            else{
                formattedDate = dateFormat.format(end);
            }

            selectedDateTextView.setText(formattedDate);

            // Now, you can use 'start' and 'end' in other parts of your activity.
        }, year, month, day);

        datePickerDialog.show();
    }




    private void showDiscardConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Discard Changes");
        builder.setMessage("Are you sure you want to discard your changes?");
        builder.setPositiveButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                // Handle the discard action here (e.g., go back or reset the form)
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}