package company.businessinc.bathtouch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import company.businessinc.dataModels.Status;
import company.businessinc.endpoints.UserResetPassword;
import company.businessinc.endpoints.UserResetPasswordInterface;


public class ForgotPasswordActivity extends ActionBarActivity implements UserResetPasswordInterface{

    private String mUsername;
    private EditText mTokenEditText, mPasswordEditText, mRePasswordEditText;
    private Button mReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        mTokenEditText = (EditText) findViewById(R.id.activity_forgot_password_token_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.activity_forgot_password_password_edit_text);
        mRePasswordEditText = (EditText) findViewById(R.id.activity_forgot_password_repassword_edit_text);
        mReset = (Button) findViewById(R.id.activity_forgot_password_button_reset);

        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mPasswordEditText.getText().length() < 8)
                    Toast.makeText(ForgotPasswordActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                else
                    new UserResetPassword(ForgotPasswordActivity.this, mTokenEditText.getText().toString(),
                        mPasswordEditText.getText().toString()).execute();
            }
        });
    }

    @Override
    public void userResetPasswordCallback(Status data) {

    }
}
