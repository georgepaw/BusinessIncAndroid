package company.businessinc.bathtouch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
                if(mPasswordEditText.getText().length() < 6) {
                    mPasswordEditText.setError("Password must be longer than 6 characters");
                } else if(!mRePasswordEditText.getText().toString().equals(mPasswordEditText.getText().toString())) {
                    mRePasswordEditText.setError("Passwords do not match");
                } else
                    new UserResetPassword(ForgotPasswordActivity.this, mPasswordEditText.getText().toString(),
                        mTokenEditText.getText().toString()).execute();
            }
        });
    }

    @Override
    public void userResetPasswordCallback(Status data) {
        if(data != null) {
            if(data.getStatus()) {
                Log.d("Reset", "Password reset");
                Intent intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
            } else {
                Log.d("Reset", "Reset returned false");
                mTokenEditText.setError("Token not recognised");
            }
        } else {
            Log.d("Reset", "Error connecting and parsing");
            Toast.makeText(this, "Cannot connect", Toast.LENGTH_SHORT).show();
        }
    }
}
