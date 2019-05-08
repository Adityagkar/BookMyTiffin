package com.example.bookmytiffin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText email,password;
    Button signup,login,googleSignIn;
    private String username;
    TextView forgot;
    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FirebaseApp.initializeApp(LoginActivity.this);
        mAuth = FirebaseAuth.getInstance();

        googleSignIn = findViewById(R.id.button3);
        forgot = findViewById(R.id.forgot);
        email = findViewById(R.id.editText6);
        password = findViewById(R.id.editText7);
        signup = findViewById(R.id.button2);
        login = findViewById(R.id.button);
        remember = findViewById(R.id.remember);

        SharedPreferences sharedpreferences = getSharedPreferences("MyDetails", Context.MODE_PRIVATE);
        String emailExists = sharedpreferences.getString("ID","NEWUSER");

        if(!emailExists.equals("NEWUSER")){
            String emailString = sharedpreferences.getString("ID","NEWUSER");
            String pwdString = sharedpreferences.getString("PWD","");
            OrderData orderData = new OrderData();
            orderData.setId(emailString);
            email.setText(emailString);
            password.setText(pwdString);

        }

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                final EditText edittext = new EditText(getApplicationContext());
                alert.setMessage("Forgot Password");
                alert.setTitle("Enter your registered email id");

                alert.setView(edittext);

                alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //What ever you want to do with the value
                        Editable YouEditTextValue = edittext.getText();
                        //firebase method to send reset Email for forget password
                        FirebaseAuth.getInstance().sendPasswordResetEmail(YouEditTextValue.toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this,"Check your mail inbox",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                    }
                });

                alert.setNegativeButton("Not Interested", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                      finish();
                    }
                });

                alert.show();

            }
        });
        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields are blank !",Toast.LENGTH_SHORT).show();
                }
                else {
                    if(password.getText().toString().length()<8 &&!isValidPassword(password.getText().toString())){
                        Snackbar.make(findViewById(R.id.lck), "Not a valid password !", Snackbar.LENGTH_SHORT).show();
                    }else
                    setSignup();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields are blank !",Toast.LENGTH_SHORT).show();
                }
                else {
                    setLogin();
                }
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address
            String email = user.getEmail();


            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }


    }

    private void setLogin() {

        // LOGIN - existing user
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TEST", "signInWithEmail:success");
                            Snackbar.make(findViewById(R.id.lck), "Login Successful !", Snackbar.LENGTH_SHORT).show();
                            DecisionScreen();
                            OrderData orderData = new OrderData();
                            String user = email.getText().toString().replace("@","-");
                            user = user.replace(".","-");
                            orderData.setId(user);
                            FirebaseUser userf = mAuth.getCurrentUser();
                             username = userf.getDisplayName();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TEST", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                            Snackbar.make(findViewById(R.id.lck), "Login Failed !", Snackbar.LENGTH_SHORT).show();


                        }

                    }
                });
    }

    private void DecisionScreen() {
        Intent intent = new Intent(LoginActivity.this,DecisionActivity.class);

        startActivity(intent);
    }

    void setSignup(){

        //SignUP - adding new user to Firebase
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Snackbar.make(findViewById(R.id.lck), "SignUp Successful !", Snackbar.LENGTH_SHORT).show();
                            String user = email.getText().toString().replace("@","-");
                            user = user.replace(".","-");
                            Log.d("TEST", "createUserWithEmail:success"+user);
                            if(remember.isChecked()) {
                                SharedPreferences sharedpreferences = getSharedPreferences("MyDetails", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("ID",email.getText().toString());
                                editor.putString("PWD",password.getText().toString());
                                editor.commit();
                                OrderData orderData = new OrderData();
                                orderData.setId(user);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("TEST", "createUserWithEmail:failure", task.getException());
                            Snackbar.make(findViewById(R.id.lck), "SignUp Failed !", Snackbar.LENGTH_SHORT).show();


                        }
                    }
                });

    }
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }



}
