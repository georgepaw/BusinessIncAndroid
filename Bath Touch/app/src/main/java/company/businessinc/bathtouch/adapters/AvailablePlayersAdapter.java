package company.businessinc.bathtouch.adapters;

import android.app.Activity;
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
import company.businessinc.dataModels.Player;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersAdapter extends RecyclerView.Adapter {

    private boolean is_available;
    private AvailablePlayerCallbacks mCallbacks;
    private List<Player> playerList = new ArrayList<Player>();


    public static interface AvailablePlayerCallbacks{
        void onPlayerAvailableChecked(boolean available, int playerID);
        void onPlayerSelected(int playerID);
    }

    public AvailablePlayersAdapter(boolean available, Activity activity){
        is_available = available;
        mCallbacks = (AvailablePlayerCallbacks) activity;
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
                mCallbacks.onPlayerSelected(getPosition());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

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

    public void removeAt(int position){
        playerList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, playerList.size());

        //fire call back to add the card to the other page
        mCallbacks.onPlayerAvailableChecked(is_available, position);
    }

    @Override
    public int getItemCount() {
        return playerList.size();
    }

    public void addToPlayerList(Player player){
        boolean found = false;
        for(int i = 0; i < playerList.size(); i++){
            if(playerList.get(i).getUserID() == player.getUserID()){
                playerList.set(i, player);
                found = true;
                break;
            }
        }
        if(!found) {
            playerList.add(player);
        }
        notifyDataSetChanged();
    }
}

