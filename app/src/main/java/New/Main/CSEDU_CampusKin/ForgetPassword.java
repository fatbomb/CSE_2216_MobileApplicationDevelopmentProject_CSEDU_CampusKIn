package New.Main.CSEDU_CampusKin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPassword extends AppCompatActivity {

    EditText email;
    AppCompatButton send, cancel;
    private String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth firebaseAuth;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle setInstanceState) {
        super.onCreate(setInstanceState);
        setContentView(R.layout.forget_password);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        email=findViewById(R.id.email);
        send=findViewById(R.id.send);
        cancel=findViewById(R.id.cancel);
        firebaseAuth=FirebaseAuth.getInstance();
        pd= new ProgressDialog(this);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPassword.this,MainActivity.class));
                finish();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String st=email.getText().toString();
                if(st.isEmpty() ||!st.matches(emailpattern)){
                    email.setError("Invalid Email Provided");
                }
                else{
                    pd.setMessage("Please Wait");
                    pd.show();
                    resetPassWord(email.getText().toString());
                }

            }
        });

    }

    private void resetPassWord(String mEmail) {
        firebaseAuth.sendPasswordResetEmail(mEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ForgetPassword.this, "Reset email successfully sent", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ForgetPassword.this,MainActivity.class));
                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                email.setError("Please provide a valid email Address");
                pd.dismiss();
            }
        });

    }
}
