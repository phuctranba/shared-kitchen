package com.github.phuctranba.sharedkitchen;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.github.phuctranba.core.util.Constant;
import com.github.phuctranba.core.util.JsonUtils;
import com.google.gson.JsonObject;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Length(min = 1, message = "Mật khẩu tối thiểu 6 ký tự")
    EditText oldPassword;

    @Length(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    @Password(message = "Mật khẩu bao gồm chữ và số", scheme = Password.Scheme.ALPHA_NUMERIC)
    EditText newPassword;

    @Length(min = 6, message = "Mật khẩu tối thiểu 6 ký tự")
    @ConfirmPassword(message = "Nhập lại mật khẩu không chính xác")
    EditText reNewPassword;

    String strOldPassword, strNewPassword, strMessage;

    Button btnSubmit;
    Validator validator;

    MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        myApplication = MyApplication.getAppInstance();

        oldPassword = findViewById(R.id.editText_old_password);
        newPassword = findViewById(R.id.editText_new_password);
        reNewPassword = findViewById(R.id.editText_new_password2);
        btnSubmit = findViewById(R.id.button_submit);
        validator = new Validator(this);
        validator.setValidationListener(this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validator.validate();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override
    public void onValidationSucceeded() {
        strOldPassword = oldPassword.getText().toString();
        strNewPassword = newPassword.getText().toString();
        new MyTaskLogin(myApplication.getUserName(), strOldPassword).execute(Constant.URL_SIGNIN);
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
            pDialog = new ProgressDialog(ChangePasswordActivity.this);
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

            if (Constant.GET_SUCCESS_MSG == Constant.SUCCESS_CODE) {
                new changePassword(strOldPassword, strNewPassword).execute(Constant.URL_CHANGE_PASSWORD);
            } else {
                showToast(strMessage);
                oldPassword.setText("");
                newPassword.setText("");
                reNewPassword.setText("");
                oldPassword.forceLayout();
            }

        }

    }

    /**
     * Luồng chạy gọi login ở background
     */
    @SuppressLint("StaticFieldLeak")
    private class changePassword extends AsyncTask<String, Void, String> {
        String strOldPassword, strNewPassword;

        private changePassword(String oldPassword, String newPassword) {
            strOldPassword = oldPassword;
            strNewPassword = newPassword;
        }

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ChangePasswordActivity.this);
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result = JsonUtils.getJSONString(String.format(params[0], myApplication.getUserId(), strOldPassword, strNewPassword));
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

            if (Constant.GET_SUCCESS_MSG == Constant.SUCCESS_CODE) {
                showToast(getString(R.string.change_password_success));
                finish();
            } else {
                showToast(strMessage);
                oldPassword.setText("");
                newPassword.setText("");
                reNewPassword.setText("");
                oldPassword.forceLayout();
            }

        }
    }

    public void showToast(String msg) {
        Toast.makeText(ChangePasswordActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
