package company.businessinc.bathtouch;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import company.businessinc.dataModels.User;
import company.businessinc.endpoints.UserLogin;
import company.businessinc.endpoints.UserLoginInterface;
import company.businessinc.networking.CheckNetworkConnection;


public class MainActivity extends ActionBarActivity implements UserLoginInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
    }

    public void login_as_user(View view) {
        if (CheckNetworkConnection.check(this)) {
            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.login_boxes);
            String username = ((EditText)linearLayout.getChildAt(0)).getText().toString();
            String password = ((EditText)linearLayout.getChildAt(1)).getText().toString();
            Log.d("Login", "Network is working, let's log in");
            new UserLogin(this,username,password).execute();
        } else {
            Toast.makeText(this, "No connection", Toast.LENGTH_SHORT);
            Log.d("Login", "Network is not working");
        }
    }

    public void login_as_anonymous(View view) {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
    }

    //After the login button is pressed
    public void userLoginCallback(User data) {
        if (data != null) {
            if (data.getStatus()) { //User has logged in
                data.getUserID(); //this needs to be stored somewhere
                Log.d("Login", "Logged in");
                Intent intent = new Intent(this, HomePageActivity.class);

                startActivity(intent);
            } else {
                Log.d("Login", "Invalid credentials");
                Toast toast = Toast.makeText(MainActivity.this, "Bad Details", Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            Log.d("Login", "Error connecting and parsing");
            Toast.makeText(MainActivity.this, "Cannot connect", Toast.LENGTH_SHORT);

        }
    }


}
