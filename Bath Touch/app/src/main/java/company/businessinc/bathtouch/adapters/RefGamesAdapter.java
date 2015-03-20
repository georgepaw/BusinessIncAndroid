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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;

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


    public class ViewHolderResults extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTeam1Name, mTeam2Name, mTeam1Score, mTeam2Score, mLocation, mDate, mLeague;
        public ImageView mImageView, mOppTeamImg;
        public RelativeLayout mMatchCardButton, mExpandable;
        public CardView mCard;

        public ViewHolderResults(View v) {
            super(v);
            mTeam1Name = (TextView) v.findViewById(R.id.match_result_item_match_team1_name);
            mTeam2Name = (TextView) v.findViewById(R.id.match_result_item_match_team2_name);
            mTeam1Score = (TextView) v.findViewById(R.id.match_result_item_match_team1_score);
            mTeam2Score = (TextView) v.findViewById(R.id.match_result_item_match_team2_score);
            mLeague = (TextView) v.findViewById(R.id.match_result_league);
            mDate = (TextView) v.findViewById(R.id.match_result_date);
            mLocation = (TextView) v.findViewById(R.id.match_result_location);
            mImageView = (ImageView) v.findViewById(R.id.match_result_item_result_image);
            mCard = (CardView) v.findViewById(R.id.match_result_item_container);
            mMatchCardButton = (RelativeLayout) v.findViewById(R.id.match_result_match_overview_button);
            mExpandable = (RelativeLayout) v.findViewById(R.id.match_result_expandable);


            mCard.setOnClickListener(this);

            //If clicked, callback to main fragment to start the match over view fragment
            mMatchCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int matchID;
                    boolean hasBeenPlayed;
                    if(getPosition() > refPastGames.size() - 1) {
                        matchID = refUpcomingGames.get(getPosition() - refPastGames.size()).getMatchID();
                        hasBeenPlayed = false;
                    } else {
                        matchID = refPastGames.get(getPosition()).getMatchID();
                        hasBeenPlayed = true;
                    }
                    mCallbacks.showMatchOverview(matchID, hasBeenPlayed);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == mCard.getId()) {
                ValueAnimator animator; //expand the player
                if (mExpandable.getVisibility() == View.GONE) {
                    mExpandable.setVisibility(View.VISIBLE); //expand the view
                    final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    mExpandable.measure(widthSpec, heightSpec);
                    animator = ValueAnimator.ofInt(0, mExpandable.getMeasuredHeight());
                } else {
                    animator = ValueAnimator.ofInt(mExpandable.getHeight(), 0);
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
                            mExpandable.setVisibility(View.GONE);
                        }
                    });
                }
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        //Update Height
                        int value = (Integer) valueAnimator.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = mExpandable.getLayoutParams();
                        layoutParams.height = value;
                        mExpandable.setLayoutParams(layoutParams);
                    }
                });
                animator.start();
            }
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
                .inflate(R.layout.match_result_item, parent, false);

        switch(viewType){
            default:
                return new ViewHolderResults(v);
        }
    }

    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        DataStore.getInstance(mContext).unregisterMyUpcomingRefereeDBObserver(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME:
                setData();
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

        Match match;
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
        v.mLocation.setText(match.getPlace().toString());
        v.mLeague.setText(DataStore.getInstance(mContext).getLeagueName(match.getLeagueID()));

        DateFormatter df = new DateFormatter();
        v.mDate.setText(df.format(match.getDateTime()));

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

        int teamColor;
        try{
            //set specific team colors if it has been loaded by db
            teamColor = Color.parseColor(oppTeam.getTeamColorPrimary());
        }
        catch (Exception e){
            teamColor = Color.GRAY;
        }
        v.mExpandable.setBackgroundColor(teamColor);
        v.mExpandable.getBackground().setAlpha(200);


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

