package company.businessinc.bathtouch.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.rengwuxian.materialedittext.MaterialEditText;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Licence;
import company.businessinc.dataModels.Team;
import company.businessinc.dataModels.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by user on 22/11/14.
 */
public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DBObserver{

    private Context mContext;
    private List<Team> teamList;
    private static ViewHolderSettings viewHolderSettings;

    private static final String MALE = "Male";
    private static final String FEMALE = "Female";

    public class ViewHolderSettings extends RecyclerView.ViewHolder{

        private CheckBox mNotificationsCheckBox, mRefereeCheckBox;
        private MaterialEditText mNameEditText,mEmailEditText;
        private Spinner mGenderSpinner, mTeamSpinner;

        public ViewHolderSettings(View v) {
            super(v);
            mNotificationsCheckBox = (CheckBox) v.findViewById(R.id.settings_item_notification_checkbox);
            mRefereeCheckBox = (CheckBox) v.findViewById(R.id.settings_item_ref_checkbox);
            mNameEditText = (MaterialEditText)v.findViewById(R.id.settings_item_name_edit_text);
            mEmailEditText = (MaterialEditText)v.findViewById(R.id.settings_item_email_edit_text);
            mGenderSpinner = (Spinner)v.findViewById(R.id.settings_item_gender_spinner);
            mTeamSpinner = (Spinner)v.findViewById(R.id.settings_item_team_spinner);
        }
    }

    public SettingsAdapter(Fragment context) {
        mContext = context.getActivity();
    }


    @Override
    public int getItemViewType(int pos) {
        return pos == 0 ? 0 : 1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View vNoMatch = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.settings_item, parent, false);
        DataStore.getInstance(mContext).registerAllTeamsDBObservers(this);
        return new ViewHolderSettings(vNoMatch);
    }
    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        DataStore.getInstance(mContext).unregisterAllTeamsDBObservers(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        viewHolderSettings = (ViewHolderSettings) holder;

        viewHolderSettings.mNotificationsCheckBox.setChecked(DataStore.getInstance(mContext).getNotifications());
        viewHolderSettings.mRefereeCheckBox.setChecked(DataStore.getInstance(mContext).isReferee());
        viewHolderSettings.mEmailEditText.setText(DataStore.getInstance(mContext).getUserEmail());
        viewHolderSettings.mNameEditText.setText(DataStore.getInstance(mContext).getUserName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext.getApplicationContext(), R.layout.toolbar_spinner_item_dropdown,new String[]{MALE,FEMALE});
        viewHolderSettings.mGenderSpinner.setAdapter(adapter);
        viewHolderSettings.mGenderSpinner.setSelection(DataStore.getInstance(mContext).isUserMale() ? 0 : 1);

        //get the team list
        teamList = DataStore.getInstance(mContext).getAllTeams();
        if (teamList.size() == 0){
            teamList.add(new Team(DataStore.getInstance(mContext).getUserTeamID(),DataStore.getInstance(mContext).getUserTeam(),null,null,null,null));
        }
        final ArrayList<String> teamNames = new ArrayList<>();
        int yourTeamPosition = 0;
        for(int i = 0; i < teamList.size(); i++) {
            Team t = teamList.get(i);
            teamNames.add(t.getTeamName());
            if(t.getTeamName().contains(DataStore.getInstance(mContext).getUserTeam())){
                yourTeamPosition = i;
            }
        }
        ArrayAdapter<String> teamAdapter = new ArrayAdapter<String>(mContext.getApplicationContext(), R.layout.toolbar_spinner_item_dropdown, teamNames);
        viewHolderSettings.mTeamSpinner.setAdapter(teamAdapter);
        viewHolderSettings.mTeamSpinner.setSelection(yourTeamPosition);
    }

    @Override
    public void notify(String tableName, Object data) {
        if(tableName.contains(DBProviderContract.ALLTEAMS_TABLE_NAME)){
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public User getUser(){
        User user = new User();
        user.setEmail(viewHolderSettings.mEmailEditText.getText().toString());
        user.setName(viewHolderSettings.mNameEditText.getText().toString());
        user.setNotifications(viewHolderSettings.mNotificationsCheckBox.isChecked());
        user.setIsReferee(viewHolderSettings.mRefereeCheckBox.isChecked());
        user.setIsMale(viewHolderSettings.mGenderSpinner.getSelectedItemPosition() == 0);
        user.setTeamID(teamList.get(viewHolderSettings.mGenderSpinner.getSelectedItemPosition()).getTeamID());
        return user;
    }



}
