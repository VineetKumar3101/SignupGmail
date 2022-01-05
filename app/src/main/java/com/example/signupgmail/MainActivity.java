package com.example.signupgmail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {

SignInButton signInButton;
GoogleSignInClient googleSignInClient;
FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInButton=(SignInButton)findViewById(R.id.signin);
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("447991535769-t3abv8hig3drpqfgtfgt40b71iq73kmm.apps.googleusercontent.com").requestEmail().build();
        googleSignInClient= GoogleSignIn.getClient(MainActivity.this,googleSignInOptions);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=googleSignInClient.getSignInIntent();
                startActivityForResult(i,100);
            }
        });
    firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            Intent i=new Intent(MainActivity.this,Second.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            Task<GoogleSignInAccount>signInAccountTask=GoogleSignIn.getSignedInAccountFromIntent(data);
            if(signInAccountTask.isSuccessful())
            {
                Toast.makeText(this, "Sign In Succesfull", Toast.LENGTH_SHORT).show();
                try
                {
                    GoogleSignInAccount googleSignInAccount=signInAccountTask.getResult(ApiException.class);
                    if(googleSignInAccount!=null)
                    {
                        AuthCredential authCredential= GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(),null);
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(MainActivity.this, "Firebase Database Updated", Toast.LENGTH_SHORT).show();
                                    Intent j= new Intent(MainActivity.this,Second.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(j);
                                }
                                else
                                {
                                    Toast.makeText(MainActivity.this, "Database Updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }catch (ApiException e)
                {
                    e.printStackTrace();
                }

            }

        }
    }
}