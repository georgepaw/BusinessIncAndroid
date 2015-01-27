package company.businessinc.bathtouch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import company.businessinc.endpoints.UserLogin;
import company.businessinc.networking.CheckNetworkConnection;


public class CreateAccountActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
    }

    public void create_account(View view) {
        if (CheckNetworkConnection.check(this)) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.create_account_boxes);
            String name = ((EditText)linearLayout.getChildAt(0)).getText().toString();
            String email = ((EditText)linearLayout.getChildAt(1)).getText().toString();
            String username = ((EditText)linearLayout.getChildAt(2)).getText().toString();
            String password = ((EditText)linearLayout.getChildAt(3)).getText().toString();
            Log.d("Login", "Network is working, let's log in");
//            new UserLogin(this,username,password).execute();
            Toast.makeText(this, name + ", " + email + ", " + username + ", " + password, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT).show();
            Log.d("Login", "Network is not working");
        }
    }
}
