package company.businessinc.bathtouch.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Player;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersAdapter extends RecyclerView.Adapter implements DBObserver {

    private boolean is_available;
    private AvailablePlayerCallbacks mCallbacks;
    private List<Player> playerList = new ArrayList<Player>();
    private Context context;
    private int matchID;


    public static interface AvailablePlayerCallbacks{
        void onPlayerAvailableChecked(boolean available, int playerID);
        void onPlayerSelected(Player player);
    }

    public AvailablePlayersAdapter(boolean available, Context context, int matchID){
        is_available = available;
        mCallbacks = (AvailablePlayerCallbacks) context;
        this.context = context;
        this.matchID = matchID;
        playerList = DataStore.getInstance(context).getPlayersAvailability(matchID, is_available);

    }


    public class ViewHolderPlayer extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        ImageView mPlayerReal;
        ImageView mPlayerAvail;
        TextView mPlayerName;
        CheckBox mCheckBox;
        TextView mPlayerNumber;
        RelativeLayout mCard;

        public ViewHolderPlayer(View itemView) {
            super(itemView);

            mPlayerReal = (ImageView) itemView.findViewById(R.id.team_roster_player_status_icon);
            mPlayerAvail = (ImageView) itemView.findViewById(R.id.team_roster_player_avail_icon);
            mPlayerName = (TextView) itemView.findViewById(R.id.team_roster_player_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.team_roster_player_checkbox);
            mPlayerNumber = (TextView) itemView.findViewById(R.id.team_roster_player_number);
            mCard = (RelativeLayout) itemView.findViewById(R.id.team_roster_card);
            mCheckBox.setOnClickListener(this);
            mCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == mCheckBox.getId()){
                removeAt(getPosition());
            }
            else if(v.getId() == mCard.getId()){
                mCallbacks.onPlayerSelected(playerList.get(getPosition()));
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        DataStore.getInstance(context).registerMyTeamsPlayerAvailabilitysDBObserver(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        playerList = DataStore.getInstance(context).getPlayersAvailability(matchID, is_available);
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.team_roster_available_player_item, parent, false);
        return new ViewHolderPlayer(v);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ViewHolderPlayer v = (ViewHolderPlayer) holder;
        Player player = playerList.get(position);
        int id = player.getUserID();

        v.mPlayerNumber.setText(Integer.toString(position));
        v.mPlayerName.setText(player.getName());
        v.mPlayerAvail.setVisibility(View.INVISIBLE);
        if(!player.getIsGhostPlayer()){
            v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_full_green);
        } else {
                v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_outline);
        }
        if(is_available){
            v.mCheckBox.setChecked(true);

//                v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_full_green);
        }
        else{
            v.mCheckBox.setChecked(false);
//                v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_outline);
        }

        v.mPlayerNumber.setText(Integer.toString(id));
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView){
        DataStore.getInstance(context).unregisterMyTeamsPlayerAvailabilitysDBObserver(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    public void removeAt(int position){
        Player player = playerList.get(position);
        playerList.remove(position);
        notifyItemRemoved(position);
        DataStore.getInstance(context).setPlayersAvailability(!player.getIsPlaying(), player.getUserID(), matchID);
        notifyItemRangeChanged(position, playerList.size());
        //fire call back to add the card to the other page
        //mCallbacks.onPlayerAvailableChecked(is_available, position);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    @Override
    public void notify(String tableName, Object data) {
        switch(tableName){
            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME:
                if(data == null || data == matchID) {
                    playerList = DataStore.getInstance(context).getPlayersAvailability(matchID, is_available);
                    notifyDataSetChanged();
                }
                break;
        }
    }
}

