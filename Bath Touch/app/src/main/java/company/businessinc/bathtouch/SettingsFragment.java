package company.businessinc.bathtouch;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.rengwuxian.materialedittext.MaterialEditText;
import company.businessinc.bathtouch.adapters.SettingsAdapter;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.UserEdit;
import company.businessinc.endpoints.UserEditInterface;


public class SettingsFragment extends Fragment implements UserEditInterface {

    private static final String TAG = "SettingsFragment";
    public static final String ARG_OBJECT = "object";

    private View mLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static final String ANON_PRIMARY = "#ff0000";
    private static final String ANON_SECONDARY = "#ffffff";

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public SettingsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.save_settings, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(ANON_PRIMARY)));
        actionBar.setTitle("Settings");
        actionBar.setElevation(0f);

        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_settings, container, false);

        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.fragment_settings_recycle);

//        // use this setting to improve performance if you know that changes
//        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //set up the settings page
        mAdapter = new SettingsAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        return mLayout;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.save_settings:
                User newSettings = ((SettingsAdapter)mAdapter).getUser();
                new UserEdit(this,newSettings.getEmail(),newSettings.getName(),newSettings.isReferee(),newSettings.isMale(),newSettings.getTeamID()).execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void userEditCallback(ResponseStatus data) {
        if(data.getStatus()){
            Toast.makeText(getActivity(),"The changes have been made. For the new settings to take place, please log out.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(),"The settings could not be save. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}
