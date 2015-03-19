package company.businessinc.bathtouch.adapters;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.amulyakhare.textdrawable.TextDrawable;
import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 22/11/14.
 */
public class TodaysGameAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DBObserver{

    private List<Match> matchList = new ArrayList<>();
    private List<Team> allTeams = new ArrayList<Team>();
    private Context mContext;
    private int leagueID;
    private MatchSelectedCallback mCallbacks;


    public class ViewHolderMatch extends RecyclerView.ViewHolder{
        public TextView mTeamNameOne, mTeamNameTwo, mLeagueName, mLocation, mDate;
        public ImageView mImageTeamOne, mImageTeamTwo;
        public Button mButton;

        public ViewHolderMatch(View v) {
            super(v);
            mTeamNameOne = (TextView) v.findViewById(R.id.fragment_todays_game_team_one_name);
            mTeamNameTwo = (TextView) v.findViewById(R.id.fragment_todays_game_team_two_name);
            mLeagueName = (TextView) v.findViewById(R.id.fragment_todays_game_league);
            mLocation = (TextView) v.findViewById(R.id.fragment_todays_game_location);
            mDate = (TextView) v.findViewById(R.id.fragment_todays_game_date);
            mImageTeamOne = (ImageView) v.findViewById(R.id.fragment_todays_game_team_one_image);
            mImageTeamTwo = (ImageView) v.findViewById(R.id.fragment_todays_game_team_two_image);
            mButton = (Button) v.findViewById(R.id.fragment_todays_game_button);
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Match match = matchList.get(getPosition()-1);
                    boolean hasBeenPlayed = (match.getTeamOnePoints() != null && match.getTeamTwoPoints() != null);
                    mCallbacks.showMatchOverview(match.getMatchID(), hasBeenPlayed);
                }
            });
        }




    }

    public class ViewHolderNoMatch extends RecyclerView.ViewHolder{
        RelativeLayout noMatch;
        public ViewHolderNoMatch(View v) {
            super(v);
            noMatch = (RelativeLayout) v.findViewById(R.id.fragment_no_match);
        }
    }

    public interface MatchSelectedCallback {
        public void showMatchOverview(int matchID, boolean hasBeenPlayed);
    }


    public TodaysGameAdapter(Fragment context, int leagueID) {
        mContext = context.getActivity();
        this.leagueID = leagueID;
        mCallbacks = (MatchSelectedCallback) context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        DataStore.getInstance(mContext).registerAllTeamsDBObservers(this);
        DataStore.getInstance(mContext).registerLeagueTodayDBObserver(this);
        this.allTeams = DataStore.getInstance(mContext).getAllTeams();
        this.matchList = DataStore.getInstance(mContext).getTodaysMatches(leagueID);
    }

    @Override
    public void notify(String tableName, Object data) {
        switch (tableName){
            case DBProviderContract.ALLTEAMS_TABLE_NAME:
                this.allTeams = DataStore.getInstance(mContext).getAllTeams();
                notifyDataSetChanged();
                break;
            case DBProviderContract.LEAGUETODAY_TABLE_NAME:
                if(data == null || (int) data == leagueID){
                    this.matchList = DataStore.getInstance(mContext).getTodaysMatches(leagueID);
                    notifyDataSetChanged();
                }
                break;
        }
    }


    @Override
    public int getItemViewType(int pos) {
        return pos == 0 ? 0 : 1;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        switch (viewType){
            case 1:
                View vMatch = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.todays_game_match, parent, false);
                return new ViewHolderMatch(vMatch);
            default:
                View vNoMatch = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.todays_game_no_match, parent, false);
                return new ViewHolderNoMatch(vNoMatch);
        }
    }

    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        DataStore.getInstance(mContext).unregisterAllTeamsDBObservers(this);
        DataStore.getInstance(mContext).unregisterLeagueTodayDBObserver(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }


    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderMatch){
            ViewHolderMatch v = (ViewHolderMatch) holder;
            Match match = matchList.get(position-1);
            v.mTeamNameOne.setText(match.getTeamOne());
            v.mTeamNameTwo.setText(match.getTeamTwo());
            v.mLocation.setText(match.getPlace());
            v.mDate.setText(new DateFormatter().format(match.getDateTime()));
            v.mLeagueName.setText(DataStore.getInstance(mContext).getLeagueName(leagueID));

            int colourTeamOne;
            int colourTeamTwo;
            Team teamOne = DataStore.getInstance(mContext).getTeam(leagueID,match.getTeamOneID());
            Team teamTwo = DataStore.getInstance(mContext).getTeam(leagueID,match.getTeamTwoID());
            if(teamOne != null){
                colourTeamOne = Color.parseColor(teamOne.getTeamColorPrimary());
            } else {
                colourTeamOne = Color.RED;
            }

            if(teamTwo != null){
                colourTeamTwo = Color.parseColor(teamTwo.getTeamColorPrimary());
            } else {
                colourTeamTwo = Color.RED;
            }

            TextDrawable teamOneDrawable = TextDrawable.builder()
                    .buildRound(match.getTeamOne().substring(0,1).toUpperCase(), colourTeamOne);

            TextDrawable teamTwoDrawable = TextDrawable.builder()
                    .buildRound(match.getTeamTwo().substring(0,1).toUpperCase(), colourTeamTwo);

            v.mImageTeamOne.setImageDrawable(teamOneDrawable);

            v.mImageTeamTwo.setImageDrawable(teamTwoDrawable);

        } else {
            ViewHolderNoMatch v  = (ViewHolderNoMatch) holder;
            if(matchList.isEmpty()){
                v.noMatch.setVisibility(View.VISIBLE);
            } else {
                v.noMatch.setVisibility(View.INVISIBLE);
            }

        }
    }


//    public void bindLeagueItem(ViewHolderLeague v, int position) {
//
//        LeagueTeam team = leagueTeams.get(position);
//        Team fullTeam = null;
//        int circleColor;
//        int textColor = mContext.getResources().getColor(R.color.body_text_2);
//
//        for (Team e : allTeams) {
//            if (e.getTeamID() == team.getTeamID()) {
//                fullTeam = e;
//                break;
//            }
//        }
//
//        //try and get other team details, may have not been loaded from db yet
//        try {
//            String captainName = fullTeam.getCaptainName();
//            captainName = captainName.equals("") ? "No captain" : captainName;
//            v.mCaptainName.setText(captainName);
//        } catch (Exception e) {
//            v.mCaptainName.setText("No Captain found");
//            Log.d("LEAGUETABLEDAPATER", "No team found in db for leagueTeam");
//        }
//        try {
//            circleColor = Color.parseColor(fullTeam.getTeamColorPrimary());
//        } catch (Exception e) {
//            circleColor = Color.LTGRAY;
//            Log.d("LEAGUEADAPTER", "Team colors still null in db");
//        }
//
//
//        //set text for teams league standings
//        v.mTeamName.setText(team.getTeamName());
//        v.mTeamName.setTextColor(textColor);
////        v.mTeamPos.setText(team.getPosition().toString());
//        v.mTeamForfeits.setText(team.getForfeit().toString());
//        v.mTeamForfeits.setTextColor(textColor);
//        v.mTeamDraw.setText(team.getDraw().toString());
//        v.mTeamDraw.setTextColor(textColor);
//        v.mTeamLose.setText(team.getLose().toString());
//        v.mTeamLose.setTextColor(textColor);
//        v.mTeamWin.setText(team.getWin().toString());
//        v.mTeamWin.setTextColor(textColor);
//        v.mTeamPts.setText(team.getLeaguePoints().toString());
//        v.mTeamPts.setTypeface(null, Typeface.BOLD);
//        v.mTeamPts.setTextColor(textColor);
//        v.mPtsFor.setText(team.getPointsFor().toString());
//        v.mPtsAgn.setText(team.getPointsAgainst().toString());
//
//        //Set circle icons for positions, points for and against
//        String leaguePosition = team.getPosition().toString();
//
////        v.mImagePosition.setImageDrawable(drawable);
//        v.mTeamPos.setText(leaguePosition);
//        v.mTeamPos.setTextColor(circleColor);
//
//    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return 1 + matchList.size();
    }

}
