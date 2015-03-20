package company.businessinc.bathtouch.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.CachedRequest;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.endpoints.ScoreSubmit;
import company.businessinc.endpoints.ScoreSubmitInterface;
import company.businessinc.networking.CheckNetworkConnection;

/**
 * Created by user on 21/11/14.
 */
public class RefGamesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DBObserver {

    private OnRefGameSelectedCallbacks mCallbacks;
    private Context mContext;
    private int teamID;
    private List<Match> refUpcomingGames, refPastGames;
    private List<Team> allTeams = new ArrayList<Team>();
    public interface OnRefGameSelectedCallbacks {
        public void showMatchOverview(int matchID, boolean hasBeenPlayed);
    }


    public class ViewHolderResults extends RecyclerView.ViewHolder {
        public TextView mTeam1Name, mTeam2Name, mTeam1Score, mTeam2Score, mLocation, mDate, mLeague;
        public ImageView mImageView, mOppTeamImg;
        public CardView mCard;

        public ViewHolderResults(View v) {
            super(v);
            mTeam1Name = (TextView) v.findViewById(R.id.ref_match_result_item_match_team1_name);
            mTeam2Name = (TextView) v.findViewById(R.id.ref_match_result_item_match_team2_name);
            mTeam1Score = (TextView) v.findViewById(R.id.ref_match_result_item_match_team1_score);
            mTeam2Score = (TextView) v.findViewById(R.id.ref_match_result_item_match_team2_score);
            mImageView = (ImageView) v.findViewById(R.id.ref_match_result_item_result_image);
            mCard = (CardView) v.findViewById(R.id.ref_match_result_item_container);


        }
    }

    public RefGamesAdapter(Fragment context) {
        refUpcomingGames = new ArrayList<>();
        refPastGames = new ArrayList<>();
        mCallbacks = (OnRefGameSelectedCallbacks) context;
        mContext = context.getActivity();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){

        DataStore.getInstance(mContext).registerMyUpcomingRefereeDBObserver(this);
        DataStore.getInstance(mContext).registerAllTeamsDBObservers(this);
        setData();
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
                .inflate(R.layout.ref_match_result_item, parent, false);

        switch(viewType){
            default:
                return new ViewHolderResults(v);
        }
    }

    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        DataStore.getInstance(mContext).unregisterMyUpcomingRefereeDBObserver(this);
        DataStore.getInstance(mContext).unregisterAllTeamsDBObservers(this);

        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME:
                setData();
            case DBProviderContract.ALLTEAMS_TABLE_NAME:
                notifyDataSetChanged();
                break;
        }
    }

    private void setData() {
        this.refUpcomingGames = DataStore.getInstance(mContext).getMyUpcomingRefereeGames();
        this.refPastGames = DataStore.getInstance(mContext).getMyPastRefereeGames();
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        bindResultItem((ViewHolderResults)holder, position );


//        if(position == 0){
//            bindHeaderItem((ViewHolderHeader) holder, position);
//        }
//        else{
//            bindResultItem((ViewHolderResults)holder, position );
//        }


    }

    public void bindResultItem(ViewHolderResults v, int position){

        final Match match;
        Boolean played;
        if (position >= refPastGames.size()) {
            match = refUpcomingGames.get(position - refPastGames.size());
            played = false;
        } else {
            match = refPastGames.get(position);
            played = true;
        }

        Team oppTeam = null;
        int oppTeamId;

        if(match.getTeamOneID().equals(teamID)){
            oppTeamId = match.getTeamTwoID();
        }else{
            oppTeamId = match.getTeamOneID();
        }

        for(Team e : allTeams){
            if(e.getTeamID().equals(oppTeamId)){
                oppTeam = e;
            }
        }


        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        v.mTeam1Name.setText(match.getTeamOne());
        v.mTeam2Name.setText(match.getTeamTwo());
        v.mTeam1Score.setText(match.getTeamOnePoints().toString());
        v.mTeam2Score.setText(match.getTeamTwoPoints().toString());

        //get the context of the adapater, to use resources later on
        Context context = v.mImageView.getContext();
        TextDrawable notPlayed = TextDrawable.builder()
                .buildRound("N", context.getResources().getColor(R.color.dark_divider));

        if (match.getTeamOneID().equals(teamID)) {
            v.mTeam1Name.setTypeface(null, Typeface.BOLD);
            v.mTeam1Score.setTypeface(null, Typeface.BOLD);
            v.mTeam2Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Score.setTypeface(null, Typeface.NORMAL);
            if(!played) {
                v.mImageView.setImageDrawable(notPlayed);
            }
        } else if(match.getTeamTwoID().equals(teamID)) {
            v.mTeam1Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam1Score.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Name.setTypeface(null, Typeface.BOLD);
            v.mTeam2Score.setTypeface(null, Typeface.BOLD);
            if(!played) {
                v.mImageView.setImageDrawable(notPlayed);
            }
        } else {
            v.mTeam1Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam1Score.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Score.setTypeface(null, Typeface.NORMAL);
            if(!played) {
                v.mImageView.setImageDrawable(notPlayed);
            }
        }

        v.mCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSubmitScoreDialog(match);
            }
        });
    }

    private void showSubmitScoreDialog(final Match match) {
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title("Submit Scores")
                .customView(R.layout.dialog_submit_score, true)
                .positiveText("Submit")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Integer mTeamOneScore = null;
                        Integer mTeamTwoScore = null;
                        CheckBox mTeamCaptainConfirm = (CheckBox) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_confirm);
                        CheckBox mTeamOneForfeit = (CheckBox) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_forfeit);
                        CheckBox mTeamTwoForfeit = (CheckBox) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_forfeit);
                        MaterialEditText mTeamOneEditText = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_name);
                        MaterialEditText mTeamTwoEditText = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_name);

                        if(!mTeamCaptainConfirm.isChecked()){
                            Toast.makeText(mContext, "Captains need to approve scores", Toast.LENGTH_SHORT).show();
                        } else if(mTeamOneForfeit.isChecked() && mTeamTwoForfeit.isChecked()) {
                            Toast.makeText(mContext, "Both teams can't forfeit", Toast.LENGTH_SHORT).show();
                        } else {
                            boolean mIsForfeit = false;
                            if(mTeamOneForfeit.isChecked()){
                                mTeamOneScore = 0;
                                mTeamTwoScore = 10;
                                mIsForfeit = true;
                            } else if(mTeamTwoForfeit.isChecked()){
                                mTeamOneScore = 10;
                                mTeamTwoScore = 0;
                                mIsForfeit = true;
                            } else {
                                try{
                                    mTeamOneScore = Integer.valueOf(mTeamOneEditText.getText().toString());
                                } catch(NumberFormatException e){
                                    Toast.makeText(mContext, "Enter score for " + match.getTeamOne(), Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                try{
                                    mTeamTwoScore = Integer.valueOf(mTeamTwoEditText.getText().toString());
                                } catch(NumberFormatException e){
                                    Toast.makeText(mContext, "Enter score for " + match.getTeamTwo(), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            if(CheckNetworkConnection.check(mContext)) {
                                ScoreSubmitInterface activity = null;
                                try {
                                    activity = (ScoreSubmitInterface) mContext;
                                } catch (ClassCastException e) {
                                    throw new ClassCastException(activity.toString()
                                            + " must implement ScoreSubmitInteface");
                                }
                                new ScoreSubmit((ScoreSubmitInterface) mContext, mContext, match.getMatchID(), mTeamOneScore, mTeamTwoScore, mIsForfeit).execute();
                            } else {
                                Toast.makeText(mContext, "No network connection, the score will be submitted automatically", Toast.LENGTH_LONG).show();
                                try {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("matchID", Integer.toString(match.getMatchID()));
                                    jsonObject.put("teamOneScore", Integer.toString(mTeamOneScore));
                                    jsonObject.put("teamTwoScore", Integer.toString(mTeamTwoScore));
                                    jsonObject.put("isForfeit", Boolean.toString(mIsForfeit));
                                    DataStore.getInstance(mContext).cacheRequest(new CachedRequest(CachedRequest.RequestType.SUBMITSCORE, jsonObject));
                                } catch (JSONException e){

                                }
                            }
                        }
                    }
                })
                .build();

        Team teamOne = DataStore.getInstance(mContext).getTeamFromAllTeams(match.getTeamOneID());
        Team teamTwo = DataStore.getInstance(mContext).getTeamFromAllTeams(match.getTeamTwoID());

        TextDrawable teamOneAvatar = TextDrawable.builder()
                .buildRound(match.getTeamOne().substring(0,1),
                        Color.parseColor(teamOne.getTeamColorPrimary()));
        TextDrawable teamTwoAvatar = TextDrawable.builder()
                .buildRound(match.getTeamTwo().substring(0,1),
                        Color.parseColor(teamTwo.getTeamColorPrimary()));
        ImageView teamOneImage = (ImageView) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_image);
        ImageView teamTwoImage = (ImageView) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_image);
        teamOneImage.setImageDrawable(teamOneAvatar);
        teamTwoImage.setImageDrawable(teamTwoAvatar);

        MaterialEditText teamOneInput = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_one_name);
        MaterialEditText teamTwoInput = (MaterialEditText) dialog.getCustomView().findViewById(R.id.dialog_submit_score_team_two_name);
        teamOneInput.setHint(match.getTeamOne() + "'s score");
        teamOneInput.setFloatingLabelText("Input " + match.getTeamOne() + "'s score");
        teamTwoInput.setHint(match.getTeamTwo() + "'s score");
        teamTwoInput.setFloatingLabelText("Input " + match.getTeamTwo() + "'s score");
        dialog.show();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        //plus one because of header fragment
        return refUpcomingGames.size() + refPastGames.size();
    }

    public int getPastCount() {
        return refPastGames.size();
    }
}

