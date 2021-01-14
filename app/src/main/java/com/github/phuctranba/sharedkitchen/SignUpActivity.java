package com.github.phuctranba.sharedkitchen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;

public class SignUpActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Length(sequence = 2, min = 6, max = 30, trim = true, message = "Vui lòng điền tên đăng nhập, từ 6 đến 30 ký tự")
    @Pattern(regex = "[A-Za-z0-9]{6,30}", message = "Tên đăng nhập chỉ chứa số và chữ")
    EditText edtUsername;

    @Email(message = "Vui lòng nhập email hợp lệ")
    EditText edtEmail;

    @Length(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    @Password(message = "Nhập mật khẩu hợp lệ gồm cả chữ và số", scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText edtPassword;

    @Length(message = "Vui lòng nhập số điện thoại hợp lệ", min = 0, max = 14)
    EditText edtMobile;

    Button btnSignUp;

    String strUsername, strEmail, strPassword, strMobi, strMessage;

    private Validator validator;

    TextView txtLogin;
    JsonUtils jsonUtils;

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

        edtUsername = findViewById(R.id.editText_name_register);
        edtEmail = findViewById(R.id.editText_email_register);
        edtPassword = findViewById(R.id.editText_password_register);
        edtMobile = findViewById(R.id.editText_phoneNo_register);

        btnSignUp = findViewById(R.id.button_submit);
        txtLogin = findViewById(R.id.textView_login_register);
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

    @Override
    public void onValidationSucceeded() {
        strUsername = edtUsername.getText().toString().replace(" ", "%20");
        strEmail = edtEmail.getText().toString();
        strPassword = edtPassword.getText().toString();
        strMobi = edtMobile.getText().toString();

        if (JsonUtils.isNetworkAvailable(SignUpActivity.this)) {
            new MyTaskRegister(strUsername, strEmail, strPassword, strMobi).execute(Constant.URL_SIGNUP);
        } else {
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

    @SuppressLint("StaticFieldLeak")
    private class MyTaskRegister extends AsyncTask<String, Void, String> {

        JsonObject jsonObject = new JsonObject();

        private MyTaskRegister(String username, String email, String password, String mobi) {
//            JsonObject jsObj = (JsonObject) new Gson().toJsonTree(new API());
            jsonObject.addProperty("username", username);
            jsonObject.addProperty("email", email);
            jsonObject.addProperty("password", password);
            jsonObject.addProperty("phone", mobi);
        }

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUpActivity.this);
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = JsonUtils.postJSONString(params[0], jsonObject);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Bóc tách kết quả sau khi đăng ký
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (null == result || result.length() == 0) {
                showToast(getString(R.string.no_data));
            } else {

                try {
                    JSONObject mainJson = new JSONObject(result);
                    int code = mainJson.getInt(Constant.CODE);
                    if (code == 1){
                        strMessage = mainJson.getString(Constant.SUCCESS_MSG);
                        Constant.GET_SUCCESS_MSG = Constant.SUCCESS_CODE;
                    }else if (code == 2){
                        strMessage = mainJson.getString(Constant.ERROR_MSG_0);
                        Constant.GET_SUCCESS_MSG = Constant.ERROR_CODE;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setResult();
//            }

            }
        }

        public void setResult() {

            if (Constant.GET_SUCCESS_MSG == Constant.ERROR_CODE) {
                edtEmail.setText("");
                edtEmail.requestFocus();
                final PrettyDialog dialog = new PrettyDialog(SignUpActivity.this);
                dialog.setTitle(getString(R.string.dialog_error))
                        .setTitleColor(R.color.dialog_text)
                        .setMessage(strMessage)
                        .setMessageColor(R.color.dialog_text)
                        .setAnimationEnabled(false)
                        .setIcon(R.drawable.pdlg_icon_close, R.color.dialog_color, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        })
                        .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        });
                dialog.setCancelable(false);
                dialog.show();
            } else {
                final PrettyDialog dialog = new PrettyDialog(SignUpActivity.this);
                dialog.setTitle(getString(R.string.dialog_success))
                        .setTitleColor(R.color.dialog_text)
                        .setMessage(strMessage)
                        .setMessageColor(R.color.dialog_text)
                        .setAnimationEnabled(false)
                        .setIcon(R.drawable.pdlg_icon_success, R.color.dialog_color, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                            }
                        })
                        .addButton(getString(R.string.dialog_ok), R.color.dialog_white_text, R.color.dialog_color, new PrettyDialogCallback() {
                            @Override
                            public void onClick() {
                                dialog.dismiss();
                                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                dialog.setCancelable(false);
                dialog.show();

            }
        }

    }

    public void showToast(String msg) {
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}