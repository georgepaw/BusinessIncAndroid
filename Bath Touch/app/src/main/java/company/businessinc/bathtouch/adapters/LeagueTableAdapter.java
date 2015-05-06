package company.businessinc.bathtouch.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
    private int mTeamId;
    private int mTeamColor;
    private int leagueID;


    public class ViewHolderLeague extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTeamName, mTeamWin, mTeamLose, mTeamForfeits, mTeamDraw, mTeamPts, mPtsFor, mPtsAgn, mCaptainName, mTeamPos;
        public ImageView mImagePosition;
        public RelativeLayout mExpandable, mMain;
        public CardView mCard;

        public ViewHolderLeague(View v) {
            super(v);
            mCard = (CardView) v.findViewById(R.id.league_item_team1_container);
            mCard.setOnClickListener(this);
            mMain = (RelativeLayout) v.findViewById(R.id.league_item_team_main_background);
            mTeamName = (TextView) v.findViewById(R.id.league_item_team_name);
            mTeamWin = (TextView) v.findViewById(R.id.league_item_team_won);
            mTeamLose = (TextView) v.findViewById(R.id.league_item_team_lose);
            mTeamDraw = (TextView) v.findViewById(R.id.league_item_team_draw);
            mTeamForfeits = (TextView) v.findViewById(R.id.league_item_team_forfeit);
            mTeamPts = (TextView) v.findViewById(R.id.league_item_team_points);
            mCaptainName = (TextView) v.findViewById(R.id.league_item_captain_name);
            mImagePosition = (ImageView) v.findViewById(R.id.image_view);
            mExpandable = (RelativeLayout) v.findViewById(R.id.league_item_extra_text_container);
            mPtsFor = (TextView) v.findViewById(R.id.league_item_pts_for);
            mPtsAgn = (TextView) v.findViewById(R.id.league_item_pts_agn);
            mTeamPos = (TextView) v.findViewById(R.id.league_item_team_position);

        }

        @Override
        public void onClick(View v) {
            if(v.getId() == mCard.getId()){
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
                if(data == null || (int) data == leagueID){
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
        return new ViewHolderLeague(v);
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
        int circleColor;
        int textColor = mContext.getResources().getColor(R.color.body_text_2);
        int userTeamId = -1;
        if(DataStore.getInstance(mContext).isUserLoggedIn()) {
            userTeamId = DataStore.getInstance(mContext).getUserTeamID();
        }

        for (Team e : allTeams) {
            if (e.getTeamID() == team.getTeamID()) {
                fullTeam = e;
                break;
            }
        }



        //try and get other team details, may have not been loaded from db yet
        try {
            String captainName = fullTeam.getCaptainName();
            captainName = captainName.equals("") ? "No captain" : captainName;
            v.mCaptainName.setText(captainName);
        } catch (Exception e) {
            v.mCaptainName.setText("No Captain found");
            Log.d("LEAGUETABLEDAPATER", "No team found in db for leagueTeam");
        }
        try {
            circleColor = Color.parseColor(fullTeam.getTeamColorPrimary());
        } catch (Exception e) {
            circleColor = Color.LTGRAY;
            Log.d("LEAGUEADAPTER", "Team colors still null in db");

        }

        //if this is our team, set colours so that the entire item is team colored
        if(fullTeam.getTeamID() == userTeamId){
            textColor = Color.parseColor(fullTeam.getTeamColorPrimary());
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
        v.mPtsFor.setText(team.getPointsFor().toString());
        v.mPtsAgn.setText(team.getPointsAgainst().toString());

        String leaguePosition = team.getPosition().toString();

        v.mTeamPos.setText(leaguePosition);
        v.mTeamPos.setTextColor(circleColor);

    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return leagueTeams.size();
    }

}
