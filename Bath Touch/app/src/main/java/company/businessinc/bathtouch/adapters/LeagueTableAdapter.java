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
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Team;

/**
 * Created by user on 22/11/14.
 */
public class LeagueTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DBObserver{

    private List<LeagueTeam> leagueTeams = new ArrayList<LeagueTeam>();
    private List<Team> allTeams = new ArrayList<Team>();
    private Context mContext;
    private int previousExpandedLoc = -1;
    private View previousExpandedView = null;
    private int mTeamId;
    private int mTeamColor;
    private int leagueID;


    public void animateChange(View v, int pos) {
        if(previousExpandedLoc == -1){

            previousExpandedLoc = pos;
            previousExpandedView = v;
        }
        //same item clicked again, close it
        else if(previousExpandedLoc == pos){
            previousExpandedLoc = pos;
            previousExpandedView = v;
        }
        //another item clicked, close previosuly open one
        else{
            ExpandAnimation animation = new ExpandAnimation(previousExpandedView, 500);

            notifyItemChanged(pos);

            previousExpandedView.startAnimation(animation);

        }

        ExpandAnimation animation = new ExpandAnimation(v, 500);

        notifyItemChanged(pos);

        v.startAnimation(animation);

        previousExpandedView = v;
        previousExpandedLoc = pos;
    }


    public class ViewHolderLeague extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTeamName, mTeamWin, mTeamLose, mTeamForfeits, mTeamDraw, mTeamPts, mPtsFor, mPtsAgn, mCaptainName, mTeamPos;
        public ImageView mImagePosition;
        public RelativeLayout mDisplayItem, mTextArea, mExtraText;

        public boolean expanded = false;

        public ViewHolderLeague(View v) {
            super(v);
            mTeamName = (TextView) v.findViewById(R.id.league_item_team_name);
//            mTeamPos = (TextView) v.findViewById(R.id.league_item_team_position);
            mTeamWin = (TextView) v.findViewById(R.id.league_item_team_won);
            mTeamLose = (TextView) v.findViewById(R.id.league_item_team_lose);
            mTeamDraw = (TextView) v.findViewById(R.id.league_item_team_draw);
            mTeamForfeits = (TextView) v.findViewById(R.id.league_item_team_forfeit);
            mTeamPts = (TextView) v.findViewById(R.id.league_item_team_points);
//            mPtsFor = (TextView) v.findViewById(R.id.league_item_ptsfor);
//            mPtsAgn = (TextView) v.findViewById(R.id.league_item_ptsagn);
            mCaptainName = (TextView) v.findViewById(R.id.league_item_captain_name);
            mImagePosition = (ImageView) v.findViewById(R.id.image_view);
//            mImageFor = (ImageView) v.findViewById(R.id.league_display_pointsfor_image);
//            mImageAgn = (ImageView) v.findViewById(R.id.league_display_pointsagn_image);
//            mCloseButton = (ImageView) v.findViewById(R.id.league_item_close_button);
//            mExpandArea = (RelativeLayout) v.findViewById(R.id.llExpandArea);
            mDisplayItem = (RelativeLayout) v.findViewById(R.id.league_display_item_container);
            mTextArea = (RelativeLayout) v.findViewById(R.id.league_item_text_container);
            mExtraText = (RelativeLayout) v.findViewById(R.id.league_item_extra_text_container);
            mPtsFor = (TextView) v.findViewById(R.id.league_item_pts_for);
            mPtsAgn = (TextView) v.findViewById(R.id.league_item_pts_agn);
            mTeamPos = (TextView) v.findViewById(R.id.league_item_team_position);
            //mDisplayItem.setOnClickListener(this);

//            mCloseButton.setOnClickListener(this);
//            mTeamOverviewButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext, "No Team Overview Fragment", Toast.LENGTH_SHORT).show();
//                }
//            });

        }

        @Override
        public void onClick(View v) {
            animateChange(v, getPosition());
        }




    }

    
    public LeagueTableAdapter(Activity context, int leagueID, int teamId) {
        mContext = context.getApplicationContext();
        mTeamId = teamId;
        if(DataStore.getInstance(context).isUserLoggedIn()) {
            mTeamColor = DataStore.getInstance(mContext).getUserTeamColorPrimary();
        }

        this.leagueID = leagueID;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        DataStore.getInstance(mContext).registerAllTeamsDBObservers(this);
        DataStore.getInstance(mContext).registerLeaguesStandingsDBObserver(this);
        this.allTeams = DataStore.getInstance(mContext).getAllTeams();
        this.leagueTeams = DataStore.getInstance(mContext).getLeagueStandings(leagueID);
    }

    @Override
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.ALLTEAMS_TABLE_NAME:
                this.allTeams = DataStore.getInstance(mContext).getAllTeams();
                notifyDataSetChanged();
                break;
            case DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME:
                if(data == null || data == leagueID){
                    this.leagueTeams = DataStore.getInstance(mContext).getLeagueStandings(leagueID);
                    notifyDataSetChanged();
                }
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
                .inflate(R.layout.league_item, parent, false);
        ViewHolderLeague vh = new ViewHolderLeague(v);
        // set the view's size, margins, paddings and layout parameters



        return vh;


    }

    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        DataStore.getInstance(mContext).unregisterAllTeamsDBObservers(this);
        DataStore.getInstance(mContext).unregisterLeaguesStandingsDBObserver(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        bindLeagueItem((ViewHolderLeague) holder, position);
    }


    public void bindLeagueItem(ViewHolderLeague v, int position) {

        LeagueTeam team = leagueTeams.get(position);
        Team fullTeam = null;
        int circleColor = Color.GRAY;
        int textColor = mContext.getResources().getColor(R.color.body_text_2);
        int circleTextColor = Color.WHITE;

        for (Team e : allTeams) {
            if (e.getTeamID() == team.getTeamID()) {
                fullTeam = e;
                break;
            }
        }

        //try and get other team details, may have not been loaded from db yet
        try {
            v.mCaptainName.setText(fullTeam.getCaptainName());
        } catch (Exception e) {
            v.mCaptainName.setText("No Captain found");
            Log.d("LEAGUETABLEDAPATER", "No team found in db for leagueTeam");
        }
        try {
            if (mTeamId == team.getTeamID()) {
                v.mDisplayItem.setBackgroundColor(mTeamColor);
                textColor = Color.WHITE;
                circleTextColor = mTeamColor;
                circleColor = Color.WHITE;
            } else {
                circleColor = Color.parseColor(fullTeam.getTeamColorPrimary());
            }
        } catch (Exception e) {
            circleColor = Color.LTGRAY;
            Log.d("LEAGUEADAPTER", "Team colors still null in db");
        }


        //set text for teams league standings
        v.mTeamName.setText(team.getTeamName());
        v.mTeamName.setTextColor(textColor);
//        v.mTeamPos.setText(team.getPosition().toString());
        v.mTeamForfeits.setText(team.getForfeit().toString());
        v.mTeamForfeits.setTextColor(textColor);
        v.mTeamDraw.setText(team.getDraw().toString());
        v.mTeamDraw.setTextColor(textColor);
        v.mTeamLose.setText(team.getLose().toString());
        v.mTeamLose.setTextColor(textColor);
        v.mTeamWin.setText(team.getWin().toString());
        v.mTeamWin.setTextColor(textColor);
        v.mTeamPts.setText(team.getLeaguePoints().toString());
        v.mTeamPts.setTypeface(null, Typeface.BOLD);
        v.mTeamPts.setTextColor(textColor);
        v.mPtsFor.setText("\u25B2  " + team.getPointsFor().toString());
        v.mPtsAgn.setText("\u25BC  " + team.getPointsAgainst().toString());

        //Set circle icons for positions, points for and against
        String leaguePosition = team.getPosition().toString();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(circleTextColor)
                .toUpperCase()
                .endConfig()
                .buildRound(leaguePosition, circleColor);

//        v.mImagePosition.setImageDrawable(drawable);
        v.mTeamPos.setText(leaguePosition);
        v.mTeamPos.setTextColor(circleColor);

//        String downArrow = "\u25BC";
//        String upArrow = "\u25B2";
//        drawable = TextDrawable.builder()
//                .beginConfig()
//                .textColor(Color.WHITE)
//                .toUpperCase()
//                .endConfig()
//                .buildRound(upArrow, mContext.getResources().getColor(R.color.green));
//        v.mImageFor.setImageDrawable(drawable);
//
//        drawable = TextDrawable.builder()
//                .beginConfig()
//                .textColor(Color.WHITE)
//                .toUpperCase()
//                .endConfig()
//                .buildRound(downArrow, mContext.getResources().getColor(R.color.red_monza)); //unicode uparrow
//
//
//        v.mImageAgn.setImageDrawable(drawable);

//        check whether to open close or leave a card alone
//        if (position == expandedPosition) {
//            v.mExpandArea.setVisibility(View.VISIBLE);
//        } else {
//            v.mExpandArea.setVisibility(View.GONE);
//        }

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return leagueTeams.size();
    }


    public class ExpandAnimation extends Animation {
        private View mAnimatedView;
        private RelativeLayout.LayoutParams mViewLayoutParams;
        private int mMarginStart, mMarginEnd;
        private boolean mIsVisibleAfter = false;
        private boolean mWasEndedAlready = false;

        /**
         * Initialize the animation
         * @param view The layout we want to animate
         * @param duration The duration of the animation, in ms
         */
        public ExpandAnimation(View view, int duration) {

            setDuration(duration);
            mAnimatedView = view;
            mViewLayoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();

            // decide to show or hide the view
            mIsVisibleAfter = (view.getVisibility() == View.VISIBLE);

            mMarginStart = mViewLayoutParams.bottomMargin;
            mMarginEnd = (mMarginStart != 0 ? (0- view.getHeight()) : 0);

            view.setVisibility(View.VISIBLE);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);

            if (interpolatedTime < 1.0f) {

                // Calculating the new bottom margin, and setting it
                mViewLayoutParams.bottomMargin = mMarginStart
                        + (int) ((mMarginEnd - mMarginStart) * interpolatedTime);

                // Invalidating the layout, making us seeing the changes we made
                mAnimatedView.requestLayout();

                // Making sure we didn't run the ending before (it happens!)
            } else if (!mWasEndedAlready) {
                mViewLayoutParams.bottomMargin = mMarginEnd;
                mAnimatedView.requestLayout();

                if (mIsVisibleAfter) {
                    mAnimatedView.setVisibility(View.GONE);
                }
                mWasEndedAlready = true;
            }
        }
    }

}
