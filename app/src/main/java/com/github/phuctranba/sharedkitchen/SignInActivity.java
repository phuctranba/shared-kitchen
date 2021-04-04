package com.github.phuctranba.sharedkitchen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.phuctranba.core.item.ItemRecipe;
import com.github.phuctranba.core.item.ItemUser;
import com.github.phuctranba.core.util.DatabaseHelper;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.core.util.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignInActivity extends AppCompatActivity implements Validator.ValidationListener {

    String strEmail, strPassword;
    TextView txtMyCabinet;
    ProgressDialog pDialog;

    @Email(message = "Vui lòng nhập email hợp lệ")
    EditText edtEmail;

    @Length(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    @Password(message = "Nhập mật khẩu hợp lệ gồm cả chữ và số", scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText edtPassword;

    private Validator validator;
    Button btnSingIn;
    MyApplication myApplication;
    TextView textForgot, textSignUp;
    JsonUtils jsonUtils;
    FirebaseAuth mauth;
    DatabaseHelper databaseHelper;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);
        JsonUtils.setStatusBarGradiant(SignInActivity.this);

        Init();

        txtMyCabinet.setText(Html.fromHtml(getString(R.string.view_my_cabinets)));

        btnSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validator.validate();
            }
        });

        textSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        txtMyCabinet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, MyRecipeActivity.class);
                startActivity(intent);
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    void Init() {
        databaseHelper = new DatabaseHelper(this);
        jsonUtils = new JsonUtils(this);
        mauth = FirebaseAuth.getInstance();
        myApplication = MyApplication.getAppInstance();

        edtEmail = findViewById(R.id.editText_email_login);
        edtPassword = findViewById(R.id.editText_password_login_activity);
        btnSingIn = findViewById(R.id.button_login_activity);
        txtMyCabinet = findViewById(R.id.txt_my_cabinet);

        textForgot = findViewById(R.id.textView_forget_password_login);
        textSignUp = findViewById(R.id.textView_signup_login);
    }

    @Override
    public void onValidationSucceeded() {
        if (JsonUtils.isNetworkAvailable(SignInActivity.this)) {
            Signin();
        } else {
            showToast(getString(R.string.network_msg));
        }

    }

    private void Signin() {
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();

        pDialog = new ProgressDialog(SignInActivity.this);
        pDialog.setMessage(getString(R.string.signining));
        pDialog.setCancelable(false);
        pDialog.show();

        mauth.signInWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ItemUser user = snapshot.getValue(ItemUser.class);
                                    MySharedPreferences.setPrefUser(SignInActivity.this, user);
                                    MySharedPreferences.setLogin(SignInActivity.this, true);

                                    loadUserData();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    System.out.println("The read failed: " + error.getCode());
                                }
                            });

                } else {
                    PrettyDialog dialog = new PrettyDialog(SignInActivity.this);

                    pDialog.dismiss();
                    dialog.setTitle(getString(R.string.dialog_error))
                            .setTitleColor(R.color.dialog_text)
                            .setMessage(getString(R.string.error_login_message))
                            .setMessageColor(R.color.dialog_text)
                            .setAnimationEnabled(false)
                            .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color_fail, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            })
                            .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color_fail, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            });
                    dialog.setCancelable(false);
                    dialog.show();
                }
            }
        });
    }

    private void loadUserData(){
        List<ItemRecipe> recipeList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = reference.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("myRecipes");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ItemRecipe recipe = item.getValue(ItemRecipe.class);
                        recipeList.add(recipe);
                    }
                    databaseHelper.addListRecipe(recipeList);
                }

                pDialog.dismiss();
                ActivityCompat.finishAffinity(SignInActivity.this);
                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showToast(String msg) {
        Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
