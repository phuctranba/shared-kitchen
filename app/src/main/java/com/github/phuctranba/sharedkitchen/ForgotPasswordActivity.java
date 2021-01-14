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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

    String strEmail, strMessage;
    private Validator validator;
    Button btnSubmit;
    JsonUtils jsonUtils;
    Toolbar toolbar;

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
        setContentView(R.layout.activity_forgot_password);
        JsonUtils.setStatusBarGradiant(ForgotPasswordActivity.this);

        jsonUtils = new JsonUtils(this);

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

    /**
     * Thiết lập actionbar
     * Tham khảo thêm tại: https://xuanthulab.net/toolbar-actionbar-trong-lap-trinh-android.html
     * */
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
        strEmail = edtEmail.getText().toString();
        if (JsonUtils.isNetworkAvailable(ForgotPasswordActivity.this)) {
            new MyTaskForgot(strEmail).execute();
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
    private class MyTaskForgot extends AsyncTask<String, Void, String> {

        String email;

        private MyTaskForgot(String email) {
            this.email = email;
        }

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ForgotPasswordActivity.this);
            pDialog.setMessage(getString(R.string.loading));
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (null != pDialog && pDialog.isShowing()) {
                pDialog.dismiss();
            }

//            if (null == result || result.length() == 0) {
//                showToast(getString(R.string.no_data));
//
//            } else {
//
//                try {
//                    JSONObject mainJson = new JSONObject(result);
//                    JSONArray jsonArray = mainJson.getJSONArray(Constant.ARRAY_NAME);
//                    JSONObject objJson;
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        objJson = jsonArray.getJSONObject(i);
//                        if (objJson.has("status")) {
//                            showToast(getString(R.string.no_data));
//                        } else {
//                            strMessage = objJson.getString(Constant.MSG);
//                            Constant.GET_SUCCESS_MSG = objJson.getInt(Constant.SUCCESS);
//                        }
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            strMessage = "Gửi email xác nhận thành công!";
            Constant.GET_SUCCESS_MSG = 1;
            setResult();
//            }
        }
    }

    public void setResult() {

        if (Constant.GET_SUCCESS_MSG == 0) {
            edtEmail.setText("");
            edtEmail.requestFocus();
            final PrettyDialog dialog = new PrettyDialog(this);
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
            final PrettyDialog dialog = new PrettyDialog(this);
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
                            Intent intent_co = new Intent(ForgotPasswordActivity.this, SignInActivity.class);
                            intent_co.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent_co);
                            finish();
                        }
                    });
            dialog.setCancelable(false);
            dialog.show();

        }
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
