package com.github.phuctranba.sharedkitchen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.github.phuctranba.core.item.ItemUser;
import com.github.phuctranba.core.util.JsonUtils;
import com.github.phuctranba.core.util.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Length(sequence = 2, min = 6, max = 30, trim = true, message = "Vui lòng điền tên hiển thị, từ 6 đến 30 ký tự")
    @Pattern(regex = "[A-Za-z]{6,30}", message = "Tên đăng nhập chỉ chứa chữ")
    EditText edtUsername;

    @Email(message = "Vui lòng nhập email hợp lệ")
    EditText edtEmail;

    @Length(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    @Password(message = "Nhập mật khẩu hợp lệ gồm cả chữ và số", scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText edtPassword;

    @ConfirmPassword
    EditText edtRePassword;

    Button btnSignUp;
    FirebaseAuth mauth;
    String strUsername, strEmail, strPassword;

    private Validator validator;

    TextView txtLogin;

    /**
     * Tùy chỉnh phông chữ
     * Xem thêm tại https://github.com/InflationX/Calligraphy
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        JsonUtils.setStatusBarGradiant(SignUpActivity.this);
        Init();

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                validator.validate(true);
            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    void Init() {
        mauth = FirebaseAuth.getInstance();
        edtUsername = findViewById(R.id.editText_name_register);
        edtEmail = findViewById(R.id.editText_email_register);
        edtPassword = findViewById(R.id.editText_password_register);
        edtRePassword = findViewById(R.id.editText_repassword_register);

        btnSignUp = findViewById(R.id.button_submit);
        txtLogin = findViewById(R.id.textView_login_register);
    }

    @Override
    public void onValidationSucceeded() {
        if (JsonUtils.isNetworkAvailable(SignUpActivity.this)) {
            Signup();
        } else {
            showToast(getString(R.string.network_msg));
        }
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void Signup() {

        ProgressDialog pDialog;
        pDialog = new ProgressDialog(SignUpActivity.this);
        pDialog.setMessage(getString(R.string.signuping));
        pDialog.setCancelable(false);
        pDialog.show();


        strUsername = edtUsername.getText().toString().replace(" ", "%20");
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();

        mauth.createUserWithEmailAndPassword(strEmail, strPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    final ItemUser user = new ItemUser(strUsername, false, strEmail);
                    user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    final PrettyDialog dialog = new PrettyDialog(SignUpActivity.this);

                    FirebaseDatabase.getInstance().getReference("users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                MySharedPreferences.setPrefUser(SignUpActivity.this, user);
                                MySharedPreferences.setLogin(SignUpActivity.this, true);
                                pDialog.dismiss();

                                dialog.setTitle(getString(R.string.dialog_success))
                                        .setTitleColor(R.color.dialog_text)
                                        .setMessage(getString(R.string.dialog_signup_success))
                                        .setMessageColor(R.color.dialog_text)
                                        .setAnimationEnabled(false)
                                        .setIcon(R.drawable.pdlg_icon_success, R.color.dialog_color_success, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                dialog.dismiss();
                                                ActivityCompat.finishAffinity(SignUpActivity.this);
                                                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        })
                                        .addButton(getString(R.string.dialog_come_to_kitchen), R.color.dialog_white_text, R.color.dialog_color_success, new PrettyDialogCallback() {
                                            @Override
                                            public void onClick() {
                                                dialog.dismiss();
                                                ActivityCompat.finishAffinity(SignUpActivity.this);
                                                Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                            } else {
                                pDialog.dismiss();

                                dialog.setTitle(getString(R.string.dialog_error))
                                        .setTitleColor(R.color.dialog_text)
                                        .setMessage(getString(R.string.dialog_signup_fail))
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
                            }
                            dialog.setCancelable(false);
                            dialog.show();
                        }
                    });
                }
            }
        });
    }

    public void showToast(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}