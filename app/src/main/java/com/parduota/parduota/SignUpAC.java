package com.parduota.parduota;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.parduota.parduota.abtract.MActivity;
import com.parduota.parduota.ion.Constant;
import com.parduota.parduota.ion.ION;
import com.parduota.parduota.model.signup.SignUp;

/**
 * Created by huy_quynh on 6/19/17.
 */

public class SignUpAC extends MActivity implements FutureCallback, Constant {

    @Override
    protected int setLayoutId() {
        return R.layout.act_sign_up;
    }


    private EditText et_name, et_email, et_password, et_password_2;
    private FutureCallback futureCallback;

    @Override
    protected void initView() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        futureCallback = this;

        et_name = findViewById(R.id.et_name);

        et_email = findViewById(R.id.et_email);

        et_password = findViewById(R.id.et_password);
        et_password_2 = findViewById(R.id.et_password_2);
        CheckBox checkBox = findViewById(R.id.checkbox);

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();

                String name = et_name.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String password_2 = et_password_2.getText().toString().trim();
                if (name.matches("") | email.matches("") | password.matches("") | password_2.matches("")) {
                    Toast.makeText(getApplicationContext(), getString(R.string.notify_input), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(password_2)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.notify_wrong_password), Toast.LENGTH_SHORT).show();
                    return;
                }
                ION.postData(SignUpAC.this, Constant.URL_SIGN_UP, ION.signUP(email, name, password), SignUp.class, futureCallback);
            }
        });
    }

    @Override
    public void onCompleted(Exception e, Object result) {
        super.onCompleted(e, result);
        hideLoading();
        SignUp signUp = (SignUp) result;
        if (signUp.getError() != null) {
            Toast.makeText(this, getString(R.string.notify_error_sign_up), Toast.LENGTH_SHORT).show();
          /*  if (signUp.getError().getErrors() != null) {
                String message_email = signUp.getError().getErrors().getEmail().get(0);
                if (message_email != null) {

                }
            }*/
        } else if (signUp.getStatus().equals(OK)) {
            Intent intent = new Intent();
            intent.putExtra(DATA, et_email.getText().toString().trim());
            setResult(999, intent);
            Toast.makeText(this, getString(R.string.notify_verify_email), Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
