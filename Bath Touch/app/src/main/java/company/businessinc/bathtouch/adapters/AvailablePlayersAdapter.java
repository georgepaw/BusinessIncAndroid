package company.businessinc.bathtouch.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.LinearInterpolator;
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
import company.businessinc.endpoints.UpAvailability;
import company.businessinc.endpoints.UpAvailabilityInterface;

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


    public class ViewHolderPlayer extends RecyclerView.ViewHolder implements View.OnClickListener, UpAvailabilityInterface {
        ImageView mPlayerReal, mIsGhostPlayer;
        TextView mPlayerName;
        CheckBox mCheckBox;
        TextView mPlayerNumber;
        RelativeLayout mRelativeExtras;
        CardView mCardTop, mCardPhone, mCardEmail;

        public ViewHolderPlayer(View itemView) {
            super(itemView);

            mPlayerReal = (ImageView) itemView.findViewById(R.id.team_roster_player_status_icon);
            mIsGhostPlayer = (ImageView) itemView.findViewById(R.id.team_roster_expanded_isGhost_image);
            mPlayerName = (TextView) itemView.findViewById(R.id.team_roster_player_name);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.team_roster_player_checkbox);
            mPlayerNumber = (TextView) itemView.findViewById(R.id.team_roster_player_number);
            mCardTop = (CardView) itemView.findViewById(R.id.team_roster_card);
            mRelativeExtras = (RelativeLayout) itemView.findViewById(R.id.team_roster_expanded);
            mCardPhone = (CardView) itemView.findViewById(R.id.team_roster_expanded_phone);
            mCardEmail = (CardView) itemView.findViewById(R.id.team_roster_expanded_email);
            mCheckBox.setOnClickListener(this);
            mCardTop.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (v.getId() == mCheckBox.getId()) {
                int position = getPosition();
                int userID;
                if (position > selectedPlayers.size() + 1) { //Magic constants by james
                    position = position - 2;
                    userID = unselectedPlayers.get(position - selectedPlayers.size()).getUserID();
                } else {
                    position = position - 1;
                    userID = selectedPlayers.get(position).getUserID();
                }
                mCheckBox.setEnabled(false);
                new UpAvailability(this, context, mCheckBox.isChecked() ? 1 : 0, matchID, userID).execute();
            } else if (v.getId() == mCardTop.getId()) {
                ValueAnimator animator; //expand the player
                if (mRelativeExtras.getVisibility() == View.GONE) {
                    mRelativeExtras.setVisibility(View.VISIBLE); //expand the view
                    final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    mRelativeExtras.measure(widthSpec, heightSpec);
                    animator = ValueAnimator.ofInt(0, mRelativeExtras.getMeasuredHeight());
                } else {
                    animator = ValueAnimator.ofInt(mRelativeExtras.getHeight(), 0);
                    animator.addListener(new Animator.AnimatorListener() { //listen to the end of animation and then get rid off the view
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            mRelativeExtras.setVisibility(View.GONE);
                        }
                    });
                }
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //Update Height
                        int value = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = mRelativeExtras.getLayoutParams();
                        layoutParams.height = value;
                        mRelativeExtras.setLayoutParams(layoutParams);
                    }
                });
                animator.start();
            }
        }

        @Override
        public void upAvailabilityCallback(ResponseStatus responseStatus, boolean isPlaying, UpAvailability.CallType callType, int matchID, int userID) {
            if(responseStatus.getStatus()){
                List<Player> listToSearch = isPlaying ? unselectedPlayers : selectedPlayers;
                for(Player player : listToSearch){
                    if(player.getUserID() == userID){
                        selectPlayer(player, isPlaying);
                        break;
                    }
                }
            } else {
                Toast.makeText(context, "Players availability couldn't be changed, try again later.", Toast.LENGTH_LONG).show();
            }
            mCheckBox.setEnabled(true);
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

        final Player player;

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

            //set whether they are ghost or not
            if(player.getIsGhostPlayer()){
                v.mIsGhostPlayer.setImageResource(R.drawable.ic_check);
                v.mCardEmail.setVisibility(View.GONE);
            } else {
                v.mIsGhostPlayer.setImageResource(R.drawable.ic_close);
                v.mCardEmail.setVisibility(View.VISIBLE);
                v.mCardEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //start a email intent with the players email
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{player.getEmail()});
                        try {
                            context.startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            final String number = getContactNumberByName(player.getName());
            if(!number.equals("N/A")){
                v.mCardPhone.setVisibility(View.VISIBLE);
                v.mCardPhone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String num = "tel:" + number;
                        Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(num));
                        context.startActivity(callIntent);
                    }
                });
            } else {
                v.mCardPhone.setVisibility(View.GONE);
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

    public String getContactNumberByName(String name) {
        String number = "N/A";
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (cursor.moveToFirst() && cursor.getCount() > 0) {
            number = cursor.getString(0);
        }
        cursor.close();
        return number;
    }

    //Toggle the availablity of a player selected from
    public void selectPlayer(Player player, boolean isPlaying) {
        List<Player> oldList = isPlaying ? unselectedPlayers:selectedPlayers;
        List<Player> newList = isPlaying ? selectedPlayers:unselectedPlayers;
        int oldPos = oldList.indexOf(player) + (isPlaying ? selectedPlayers.size() + 2 : 1);//add the magic constants
        oldList.remove(player);
        int newPos = insertToCorrectPlace(newList,player) + (isPlaying ? 1 : selectedPlayers.size() + 2);
        notifyItemMoved(oldPos, newPos);
        notifyItemChanged(0); //update the headers
        notifyItemChanged(selectedPlayers.size()+1);

    }

    private int insertToCorrectPlace(List<Player> list, Player player){
        //the list should be sorted by players names
        if(list.isEmpty()){ //in case list is empty we have to do this
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

