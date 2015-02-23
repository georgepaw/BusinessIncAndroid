package company.businessinc.bathtouch.adapters;

import android.content.Context;
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
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Player;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersAdapter extends RecyclerView.Adapter {

    private boolean is_available;
    private AvailablePlayerCallbacks mCallbacks;
    private List<Player> playerList = new ArrayList<Player>();
    private List<Player> selectedPlayers = new ArrayList<Player>();
    private List<Player> unselectedPlayers = new ArrayList<Player>();

    private Context context;
    private int matchID;

    private int NUMHEADERS = 2;


    public static interface AvailablePlayerCallbacks {
        void onPlayerAvailableChecked(boolean available, int playerID);

        void onPlayerSelected(Player player);
    }

    public AvailablePlayersAdapter(boolean available, Context context, int matchID) {
        is_available = available;
//        mCallbacks = (AvailablePlayerCallbacks) context;
        this.context = context;
        this.matchID = matchID;
    }


    public class ViewHolderPlayer extends RecyclerView.ViewHolder implements View.OnClickListener {
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
            if (v.getId() == mCheckBox.getId()) {
                selectPlayer(getPosition());
            } else if (v.getId() == mCard.getId()) {
//                mCallbacks.onPlayerSelected(playerList.get(getPosition()));
            }
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        private TextView mTextView;
        private View mDivider;

        public ViewHolderHeader(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.team_roster_header_item_text);
            mDivider = (View) itemView.findViewById(R.id.team_roster_header_item_divider);
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if (pos == 0 || pos == selectedPlayers.size() + 1) {
            return 0; //header
        }
        return 1; //content
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_roster_header_item, parent, false);
            return new ViewHolderHeader(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_roster_available_player_item, parent, false);
            return new ViewHolderPlayer(v);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Player player;

        //first item is a header for the list
        if (holder instanceof ViewHolderHeader) {

            int color = DataStore.getInstance(context).getUserTeamColorPrimary();
            ViewHolderHeader v = (ViewHolderHeader) holder;
            v.mTextView.setTextColor(color);
            v.mDivider.setBackgroundColor(color);

            if (position == 0) {
                v.mTextView.setText("Playing");
            } else {
                v.mTextView.setText("Not Playing");
            }

        } else {
            //is a player item, so set that up

            ViewHolderPlayer v = (ViewHolderPlayer) holder;

            //check which list to select the player from, selected or unselected
            if (position >= selectedPlayers.size() + 2) {

                //take away position of both headers
                position = position - 2;

                //load an unselected player
                position = position - selectedPlayers.size();
                player = unselectedPlayers.get(position);

            } else {

                //take away position of top header
                position = position - 1;
                player = selectedPlayers.get(position);
            }


            int id = player.getUserID();

            v.mPlayerNumber.setText(Integer.toString(position));
            v.mPlayerName.setText(player.getName());
            v.mPlayerAvail.setVisibility(View.INVISIBLE);
            if (!player.getIsGhostPlayer()) {
                v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_full_green);
            } else {
                v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_outline);
            }
            if (player.getIsPlaying()) {
                v.mCheckBox.setChecked(true);

//                v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_full_green);
            } else {
                v.mCheckBox.setChecked(false);
//                v.mPlayerAvail.setImageResource(R.drawable.ic_checkbox_outline);
            }

            v.mPlayerNumber.setText(Integer.toString(id));
        }

    }

    //Toggle the availablity of a player selected from
    public void selectPlayer(int position) {
        Player player;
        int relPosition;
        //check which list to select the player from, selected or unselected

        if (position > selectedPlayers.size() + 1) {
            Log.d("CLICK", "Removed from unselected");
            position = position - 2;
            relPosition = position - selectedPlayers.size();
            player = unselectedPlayers.get(relPosition);
            unselectedPlayers.remove(relPosition);
            selectedPlayers.add(player);

            notifyItemMoved(position + 2, selectedPlayers.size());

        } else {
            Log.d("CLICK", "Removed from selected");
            position = position - 1;
            player = selectedPlayers.get(position);
            Log.d("SELECTED", player.getName());
            selectedPlayers.remove(position);
            unselectedPlayers.add(0, player);

            notifyItemMoved(position + 1, selectedPlayers.size() + 2); //BOW TO THE MAGIC CONSTANTS
        }

        //update the availability in the database
        DataStore.getInstance(context).setPlayersAvailability(!player.getIsPlaying(), player.getUserID(), matchID);

    }


    @Override
    public int getItemCount() {
        return selectedPlayers.size() + unselectedPlayers.size() + NUMHEADERS;
    }

    public void addToPlayerList(Player player) {
        boolean found = false;

        //add to the correct list
        if (player.getIsPlaying()) {
            //update an existing player
            for (int i = 0; i < selectedPlayers.size(); i++) {
                if (selectedPlayers.get(i).getUserID() == player.getUserID()) {
                    selectedPlayers.set(i, player);
                    found = true;
                    break;
                }

            }
            if (!found) {
                selectedPlayers.add(player);
            }
        } else {
            for (int i = 0; i < unselectedPlayers.size(); i++) {
                if (unselectedPlayers.get(i).getUserID() == player.getUserID()) {
                    unselectedPlayers.set(i, player);
                    found = true;
                    break;
                }
            }
            if (!found) {
                unselectedPlayers.add(player);
            }
        }


        notifyDataSetChanged();
    }
}

