package company.businessinc.bathtouch.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Player;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.endpoints.RequestPlayers;
import company.businessinc.endpoints.RequestPlayersInterface;

/**
 * Created by user on 30/01/15.
 */
public class AvailablePlayersAdapter extends RecyclerView.Adapter implements DBObserver {

    private List<Player> selectedPlayers = new ArrayList<Player>();
    private List<Player> unselectedPlayers = new ArrayList<Player>();

    private Context context;
    private int matchID;
    private boolean hasBeenPlayed;

    private int NUMHEADERS = 2;
    private final int REQUESTPLAYERS;

    public static interface AvailablePlayerCallbacks {
        void onPlayerAvailableChecked(boolean available, int playerID);

        void onPlayerSelected(Player player);
    }

    public AvailablePlayersAdapter(Context context, int matchID, boolean hasBeenPlayed) {
        this.context = context;
        this.matchID = matchID;
        this.hasBeenPlayed = hasBeenPlayed;

        REQUESTPLAYERS = DataStore.getInstance(context).isUserCaptain() ? 1 : 0;

    }


    public class ViewHolderPlayer extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mPlayerReal;
        TextView mPlayerName;
        CheckBox mCheckBox;
        TextView mPlayerNumber;
        CardView mCard;

        public ViewHolderPlayer(View itemView) {
            super(itemView);

            mPlayerReal = (ImageView) itemView.findViewById(R.id.team_roster_player_status_icon);
//            mPlayerAvail = (ImageView) itemView.findViewById(R.id.team_roster_player_avail_icon);
            mPlayerName = (TextView) itemView.findViewById(R.id.team_roster_player_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.team_roster_player_checkbox);
            mPlayerNumber = (TextView) itemView.findViewById(R.id.team_roster_player_number);
            mCard = (CardView) itemView.findViewById(R.id.team_roster_card);
            mCheckBox.setOnClickListener(this);
            mCard.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == mCheckBox.getId()) {
                selectPlayer(getPosition());
            } else if(v.getId() == mCard.getId()) {
                Log.d("a","");
            }
        }
    }

    public class ViewHolderHeader extends RecyclerView.ViewHolder {
        private TextView mTextView, mPlayerCount, mSubsCount;
        private View mDivider;
        private ImageView mCheck;
        private RelativeLayout mPlayerStats;

        public ViewHolderHeader(View itemView) {
            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.team_roster_header_item_text);
            mDivider = (View) itemView.findViewById(R.id.team_roster_header_item_divider);
            mCheck = (ImageView) itemView.findViewById(R.id.team_roster_header_check_image);
            mPlayerCount = (TextView) itemView.findViewById(R.id.team_roster_header_player_count);
            mSubsCount = (TextView) itemView.findViewById(R.id.team_roster_header_subs_count);
            mPlayerStats = (RelativeLayout) itemView.findViewById(R.id.team_roster_header_team_stats);
        }
    }

    public class ViewHolderRequest extends RecyclerView.ViewHolder implements View.OnClickListener, RequestPlayersInterface {
        CardView mMales, mFemales;

        public ViewHolderRequest(View itemView) {
            super(itemView);
            mFemales = (CardView) itemView.findViewById(R.id.team_roster_request_females_layout);
            mMales = (CardView) itemView.findViewById(R.id.team_roster_request_males_layout);

            mMales.setOnClickListener(this);
            mFemales.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            boolean isMale = v.getId() == mMales.getId();
            new RequestPlayers(this, matchID, isMale).execute();
        }

        @Override
        public void requestPlayersCallback(ResponseStatus data) {
            if(data.getStatus()){
                Toast.makeText(context, "Player request was successful, a notification has been sent to all players", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Player request was not successful, please try again later", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public int getItemViewType(int pos) {
        if (pos == 0 || pos == selectedPlayers.size() + 1) {
            return 0; //header
        } else if((pos > 0 && pos < selectedPlayers.size() + 1) || ((pos > (selectedPlayers.size() + 1) && pos < (selectedPlayers.size() + 2 + unselectedPlayers.size())))){
            return 1;
        } else if(pos >= (selectedPlayers.size() + unselectedPlayers.size() + 2)){
            return 2;
        } else {
            return 3;
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        DataStore.getInstance(context).registerMyTeamsPlayerAvailabilitysDBObserver(this);
        selectedPlayers = DataStore.getInstance(context).getPlayersAvailability(matchID, true);
        unselectedPlayers = DataStore.getInstance(context).getPlayersAvailability(matchID, false);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        selectedPlayers = DataStore.getInstance(context).getPlayersAvailability(matchID, true);
        unselectedPlayers = DataStore.getInstance(context).getPlayersAvailability(matchID, false);
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_roster_header_item, parent, false);
            return new ViewHolderHeader(v);
        } else if(viewType == 1) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_roster_available_player_item, parent, false);
            return new ViewHolderPlayer(v);
        } else if(viewType==2){
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.team_roster_request_players, parent, false);
            return new ViewHolderRequest(v);
        } else {
            return null;
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
            v.mCheck.setVisibility(View.GONE);
            v.mPlayerStats.setVisibility(View.GONE);

            int selected = selectedPlayers.size();
            int unselected = unselectedPlayers.size();



            Drawable checkIcon = context.getResources().getDrawable(R.mipmap.ic_check_black);
            int green = context.getResources().getColor(R.color.green);
            ColorFilter filter = new LightingColorFilter( green, green);
            checkIcon.setColorFilter(filter);

            //update contents of header based on player number
            if (position == 0) {
                v.mPlayerStats.setVisibility(View.VISIBLE);

                v.mTextView.setText(String.format("Playing"));
                int overflow = 0;

                //if the team has the required number of players, not included gender ratios yet
                //Check at least 2 femals and 1 male available
                if(selected >= 6 && Player.getGenderCount(selectedPlayers, false) >= 2 && Player.getGenderCount(selectedPlayers, true) >= 1){
                    overflow = selected - 6;
                    selected = 6;
                    v.mCheck.setImageDrawable(checkIcon);
                    v.mCheck.setVisibility(View.VISIBLE);
                }
                v.mPlayerCount.setText(String.format("%d/%d", Player.getGenderCount(selectedPlayers, true), Player.getGenderCount(selectedPlayers, false)));
                v.mSubsCount.setText(String.format("%d", overflow));

            } else {
                //2nd header only shows it's remaining players
                v.mPlayerStats.setVisibility(View.GONE);
                v.mTextView.setText(String.format("Not Playing (%d)", unselected));
            }




        } else if(holder instanceof ViewHolderPlayer){
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


            v.mPlayerName.setText(player.getName());
            if(DataStore.getInstance(context).getUserName().contains(player.getName())){
                v.mPlayerName.setTypeface(null, Typeface.BOLD_ITALIC);
            }
            if (player.getIsGhostPlayer()) {
                v.mPlayerReal.setVisibility(View.INVISIBLE);
            }
            else{
                v.mPlayerReal.setVisibility(View.VISIBLE);
            }

            if (player.getIsPlaying()) {
                v.mCheckBox.setChecked(true);
            } else {
                v.mCheckBox.setChecked(false);
            }

            //Make sure only captain, can change players availability, or player can change his own availability
            if(!hasBeenPlayed && (DataStore.getInstance(context).isUserCaptain() || DataStore.getInstance(context).getUserName().contains(player.getName()))){
                v.mCheckBox.setEnabled(true);
            } else {
                v.mCheckBox.setEnabled(false);
            }

        } else if(holder instanceof ViewHolderRequest){
            ViewHolderRequest v = (ViewHolderRequest)holder;
            if(selectedPlayers.size() < 6 || Player.getGenderCount(selectedPlayers, true) < 1){
                v.mMales.setVisibility(View.VISIBLE);
            } else {
                v.mMales.setVisibility(View.GONE);
            }
            if(selectedPlayers.size() < 6 || Player.getGenderCount(selectedPlayers, false) < 2){
                v.mFemales.setVisibility(View.VISIBLE);
            } else {
                v.mFemales.setVisibility(View.GONE);
            }
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
            int pos = insertToCorrectPlace(selectedPlayers,player);

            notifyItemMoved(position + 2, pos+1);
            DataStore.getInstance(context).setPlayersAvailability(true,player.getUserID(),matchID);

        } else {
            Log.d("CLICK", "Removed from selected");
            position = position - 1;
            player = selectedPlayers.get(position);
            Log.d("SELECTED", player.getName());
            selectedPlayers.remove(position);
            int pos = insertToCorrectPlace(unselectedPlayers,player);

            notifyItemMoved(position + 1, selectedPlayers.size() + pos + 2); //BOW TO THE MAGIC CONSTANTS
            DataStore.getInstance(context).setPlayersAvailability(false, player.getUserID(), matchID);
        }
    }

    private int insertToCorrectPlace(List<Player> list, Player player){
        //the list should be sorted by players names
        if( list.size() ==0){
            list.add(player);
            return 0;
        }
        int i;
        for(i = 0; i < list.size(); i++){
            if(list.get(i).getName().compareToIgnoreCase(player.getName()) > 0){
                break;
            }
        }
        list.add(i, player);
        return i;
    }

        //update the availability in the database
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView){
        DataStore.getInstance(context).unregisterMyTeamsPlayerAvailabilitysDBObserver(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }


    @Override
    public int getItemCount() {
        return selectedPlayers.size() + unselectedPlayers.size() + NUMHEADERS + REQUESTPLAYERS;
    }

    @Override
    public void notify(String tableName, Object data) {
        switch(tableName){
            case DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME:
                if(data == null || (int) data == matchID) {
                    selectedPlayers = DataStore.getInstance(context).getPlayersAvailability(matchID, true);
                    unselectedPlayers = DataStore.getInstance(context).getPlayersAvailability(matchID, false);
                    notifyDataSetChanged();
                }
                break;
        }
    }
}

