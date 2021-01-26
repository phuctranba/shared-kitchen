package com.github.phuctranba.sharedkitchen;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;

import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;

public class ForgotPasswordActivity extends AppCompatActivity implements Validator.ValidationListener {


    @Email(message = "Vui lòng nhập email hợp lệ")
    EditText edtEmail;

    String strEmail;
    private Validator validator;
    Button btnSubmit;
    JsonUtils jsonUtils;
    Toolbar toolbar;
    FirebaseAuth mauth;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        JsonUtils.setStatusBarGradiant(ForgotPasswordActivity.this);

        jsonUtils = new JsonUtils(this);
        mauth = FirebaseAuth.getInstance();
        setupActionbar();

        edtEmail = findViewById(R.id.editText_fp);
        btnSubmit = findViewById(R.id.button_fp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                validator.validate();
            }
        });


        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void setupActionbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.forgot_password));

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public void onValidationSucceeded() {
        if (JsonUtils.isNetworkAvailable(ForgotPasswordActivity.this)) {
            ResetPassword();
        }else {
            showToast(getString(R.string.network_msg));
        }
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

    private void ResetPassword(){
        strEmail = edtEmail.getText().toString();
        ProgressDialog pDialog;
        pDialog = new ProgressDialog(ForgotPasswordActivity.this);
        pDialog.setMessage(getString(R.string.reseting));
        pDialog.setCancelable(false);
        pDialog.show();

        mauth.sendPasswordResetEmail(strEmail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        final PrettyDialog dialog = new PrettyDialog(ForgotPasswordActivity.this);
                        pDialog.dismiss();
                        if (task.isSuccessful()) {
                            dialog.setTitle(getString(R.string.dialog_success))
                                    .setTitleColor(R.color.dialog_text)
                                    .setMessage(getString(R.string.dialog_reset_success))
                                    .setMessageColor(R.color.dialog_text)
                                    .setAnimationEnabled(false)
                                    .setIcon(R.drawable.pdlg_icon_success, R.color.dialog_color_success, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            dialog.dismiss();
                                            onBackPressed();
                                        }
                                    })
                                    .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color_success, new PrettyDialogCallback() {
                                        @Override
                                        public void onClick() {
                                            dialog.dismiss();
                                            onBackPressed();
                                        }
                                    });
                        }else {
                            dialog.setTitle(getString(R.string.dialog_error))
                                    .setTitleColor(R.color.dialog_text)
                                    .setMessage(getString(R.string.dialog_reset_fail))
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

    public void showToast(String msg) {
        Toast.makeText(ForgotPasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
