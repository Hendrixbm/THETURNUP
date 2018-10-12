package theturnup.solutionhub.com.theturnup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity
{
    private EditText UserEmail, UserPassword, UserConfirmPassword;
    private Button CreateAccountButton;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();

        UserEmail = (EditText) findViewById(R.id.register_email);
        UserPassword = (EditText) findViewById(R.id.register_password);
        UserConfirmPassword = (EditText) findViewById(R.id.register_confirm_password);
        CreateAccountButton = (Button) findViewById(R.id.create_account_button);
        loadingBar = new ProgressDialog(this);


        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                CreateNewAccount();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            SendUserToMainActivity();
        }
    }

    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();
    }

    private void CreateNewAccount()
    {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please write your email!.....", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password....", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(this, "Please confirm your password!....", Toast.LENGTH_SHORT).show();
        }
        else if(!password.equals(confirmPassword))
        {
            Toast.makeText(this, "Your password does not match...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait while we create your new account....");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


          mAuth.createUserWithEmailAndPassword(email, password)
                  .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                      @Override
                      public void onComplete(@NonNull Task<AuthResult> task)
                      {
                         if(task.isSuccessful())
                         {
                             SendUserToSetUpActivity();


                             Toast.makeText(RegisterActivity.this, "Authentication successful", Toast.LENGTH_SHORT).show();
                         }
                         else
                         {
                             //The variable message shows an error which has occured  in firebase database for example no internet connection or that the password is already in use.
                             String messege = task.getException().getMessage();
                             Toast.makeText(RegisterActivity.this, "Error occured: " + messege, Toast.LENGTH_SHORT).show();
                             loadingBar.dismiss();
                         }

                      }
                  });
        }
    }

    private void SendUserToSetUpActivity()
    {
        Intent setupIntent = new Intent(RegisterActivity.this, SetupActivity.class);
        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(setupIntent);
    }
}
