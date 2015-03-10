package company.businessinc.bathtouch.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.*;
import com.amulyakhare.textdrawable.TextDrawable;
import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Message;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.dataModels.Team;
import company.businessinc.endpoints.UpAvailability;
import company.businessinc.endpoints.UpAvailabilityInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22/11/14.
 */
public class PlayerRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DBObserver{

    private List<Message> messages = new ArrayList<Message>();
    private Context mContext;

    public class ViewHolderMessage extends RecyclerView.ViewHolder implements View.OnClickListener, UpAvailabilityInterface {
        ImageView mMessageView;
        TextView mTeamText, mMatchTime;
        Button mButton;

        public ViewHolderMessage(View v) {
            super(v);
            mMessageView = (ImageView) v.findViewById(R.id.message_view);
            mTeamText = (TextView) v.findViewById(R.id.message_team_name_text);
            mMatchTime = (TextView) v.findViewById(R.id.message_game_time);
            mButton = (Button) v.findViewById(R.id.message_button);
            mButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Message message = messages.get(getPosition());
            new UpAvailability(this, mContext, message.getMatchID(), message.getTeamID(), true).execute();
            mButton.setEnabled(false);
        }

        @Override
        public void upAvailabilityCallback(ResponseStatus responseStatus, boolean isPlaying, UpAvailability.CallType callType, int matchID, int userID) {
            if(responseStatus.getStatus()){
                Toast.makeText(mContext,"You have been added to the team!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext,"There was a problem adding you to the team. Try later", Toast.LENGTH_LONG).show();
            }
            mButton.setEnabled(true);
        }
    }


    public PlayerRequestAdapter(Activity context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        DataStore.getInstance(mContext).registerMessagesDBObserver(this);
        DataStore.getInstance(mContext).registerAllTeamsDBObservers(this);
        this.messages = DataStore.getInstance(mContext).getPlayerRequests();
    }

    @Override
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.MESSAGES_TABLE_NAME:
                messages = DataStore.getInstance(mContext).getPlayerRequests();
            case DBProviderContract.ALLTEAMS_TABLE_NAME:
                notifyDataSetChanged();
                break;
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new ViewHolderMessage(v);
    }

    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        DataStore.getInstance(mContext).unregisterMessagesDBObserver(this);
        DataStore.getInstance(mContext).unregisterAllTeamsDBObservers(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderMessage v = (ViewHolderMessage) holder;
        Message message = messages.get(position);
        v.mTeamText.setText(message.getTeamName() +" are looking for " +
                (message.getIsMale() ? "male" : "female")
                +" players, can you help?");
        v.mMatchTime.setText("Time:\t"+new DateFormatter().format(message.getDateTime()));
        int colourPrimary;
        int colourSecondary;
        Team thisTeam = DataStore.getInstance(mContext).getTeamFromAllTeams(message.getTeamID());
        if(thisTeam!=null){
            colourPrimary = Color.parseColor(thisTeam.getTeamColorPrimary());
            colourSecondary = Color.parseColor(thisTeam.getTeamColorSecondary());
        } else {
            colourPrimary = Color.BLACK;
            colourSecondary = Color.WHITE;
        }
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(colourPrimary)
                .toUpperCase()
                .endConfig()
                .buildRound(message.getTeamName().substring(0,1), colourSecondary);
        v.mMessageView.setImageDrawable(drawable);
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return messages.size();
    }
}
