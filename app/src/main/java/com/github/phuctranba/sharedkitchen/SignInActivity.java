package com.github.phuctranba.sharedkitchen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.refactor.library.SmoothCheckBox;
import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;

public class SignInActivity extends AppCompatActivity implements Validator.ValidationListener {

    String strEmail, strPassword, strMessage, strName, strId, strUsername, strAvata;
//    @Email(message = "Email không hợp lệ")
//    EditText edtEmail;

    @Length(sequence = 2, min = 5, max = 30, trim = true, message = "Vui lòng điền tên đăng nhập, từ 5 đến 30 ký tự")
    @Pattern(regex = "[A-Za-z0-9]{5,30}", message = "Tên đăng nhập chỉ chứa số và chữ")
    EditText edtUsername;

    @Length(min = 1, message = "Mật khẩu tối thiểu 6 ký tự")
//    @Password(message = "Nhập mật khẩu hợp lệ gồm cả chữ và số", scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText edtPassword;

    private Validator validator;
    Button btnSingIn;
    //    Button btnSkip;
    MyApplication myApplication;
    TextView textForgot, textSignUp;
    JsonUtils jsonUtils;
    public static final String mypreference = "mypref";
    public static final String pref_email = "pref_email";
    public static final String pref_password = "pref_password";
    public static final String pref_check = "pref_check";
    private String pref_username = "pref_username";
    private static SharedPreferences pref;
    private static SharedPreferences.Editor editor;
    boolean iswhichscreen;
    String detail_screen;

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
        setContentView(R.layout.activity_sign_in);
        JsonUtils.setStatusBarGradiant(SignInActivity.this);
        jsonUtils = new JsonUtils(this);
        pref = getSharedPreferences(mypreference, 0); // 0 - for private mode
        editor = pref.edit();

        myApplication = MyApplication.getAppInstance();

//        edtEmail = findViewById(R.id.editText_email_login_activity);
        edtPassword = findViewById(R.id.editText_password_login_activity);
        btnSingIn = findViewById(R.id.button_login_activity);
//        btnSkip = findViewById(R.id.button_skip_login_activity);
        edtUsername = findViewById(R.id.editText_name_login);

        textForgot = findViewById(R.id.textView_forget_password_login);
        textSignUp = findViewById(R.id.textView_signup_login);

//        Intent intent=getIntent();
//        iswhichscreen=intent.getBooleanExtra("isfromdetail",false);
//        detail_screen=intent.getStringExtra("isid");

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

//        btnSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });

        textForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        validator = new Validator(this);
        validator.setValidationListener(this);

    }

    @Override
    public void onValidationSucceeded() {
        strUsername = edtUsername.getText().toString();
        strPassword = edtPassword.getText().toString();

        if (JsonUtils.isNetworkAvailable(SignInActivity.this)) {
            new MyTaskLogin(strUsername, strPassword).execute(Constant.URL_SIGNIN);
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


    /**
     * Luồng chạy gọi login ở background
     */
    @SuppressLint("StaticFieldLeak")
    private class MyTaskLogin extends AsyncTask<String, Void, String> {
        JsonObject jsonObject = new JsonObject();

        private MyTaskLogin(String strUsername, String strPassword) {
            jsonObject.addProperty("username", strUsername);
            jsonObject.addProperty("password", strPassword);
        }

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInActivity.this);
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            Quá trình đăng nhập code ở đây
            String result = JsonUtils.postJSONString(params[0], jsonObject);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
//            Bóc tách kết quả trả về sau khi đăng nhập ở đây
            super.onPostExecute(result);
            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

            if (null == result || result.length() == 0) {
                showToast(getString(R.string.no_data));
            } else {
                try {
                    JSONObject mainJson = new JSONObject(result);
                    int code = mainJson.getInt(Constant.CODE);
                    if (code == 1) {
                        strMessage = mainJson.getString(Constant.SUCCESS_MSG);
                        strName = mainJson.getString(Constant.USER_NAME);
                        strId = mainJson.getString(Constant.USER_ID);
                        strAvata = Constant.SERVER_URL + mainJson.getString(Constant.USER_AVATAR);
                        Constant.GET_SUCCESS_MSG = Constant.SUCCESS_CODE;
                    } else {
                        strMessage = mainJson.getString(Constant.ERROR_MSG);
                        Constant.GET_SUCCESS_MSG = Constant.ERROR_CODE;
                    }
                } catch (JSONException e) {
                    strMessage = getString(R.string.error_login_message);
                    Constant.GET_SUCCESS_MSG = Constant.ERROR_CODE;
                    e.printStackTrace();
                }
                setResult();

            }
        }

        public void setResult() {

            if (Constant.GET_SUCCESS_MSG == Constant.ERROR_CODE) {
                final PrettyDialog dialog = new PrettyDialog(SignInActivity.this);
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
                myApplication.saveIsLogin(true);
                myApplication.saveLogin(strId, strName, strAvata);
//                if (iswhichscreen) {
//                Intent i = new Intent(SignInActivity.this, ActivityDetail.class);
//                i.putExtra("isfromdetail",detail_screen);
//                i.putExtra("Id",Constant.LATEST_RECIPE_IDD);
//                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(i);
//                finish();
//                } else {
                ActivityCompat.finishAffinity(SignInActivity.this);
                Intent i = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(i);
                finish();
//                }

            }
        }

        public void showToast(String msg) {
            Toast.makeText(SignInActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
