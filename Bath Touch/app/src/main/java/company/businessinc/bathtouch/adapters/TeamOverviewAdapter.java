package company.businessinc.bathtouch.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.SubmitScoreActivity;
import company.businessinc.bathtouch.data.DBObserver;
import company.businessinc.bathtouch.data.DBProviderContract;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.CachedRequest;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.endpoints.ScoreSubmit;
import company.businessinc.endpoints.ScoreSubmitInterface;
import company.businessinc.networking.CheckNetworkConnection;

/**
 * Created by Louis on 20/03/2015.
 */
public class TeamOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements DBObserver {

    private final Context mContext;
    private final SwipeRefreshLayout mSwipeRefreshLayout;
    private final ViewPager mViewPager;
    private onMatchDetailsSelected mCallbacks;
    private int mTeamID;
    private int mLeagueID;
    private League league;

//    private Boolean

    public interface onMatchDetailsSelected {
        public void matchDetailsSelectedCallback(Bundle args);
    }

    public class ViewHolderTeam extends RecyclerView.ViewHolder {

        private TextView mNextMatchName, mNextMatchPlace, mNextMatchDate,
                mNextRefMatchName, mNextRefMatchPlace, mNextRefMatchDate, mPastMatchesHeader,
                mLeagueHeader, mLeaguePoints, mLeagueWins, mLeagueDraws, mLeagueLosses;
        private ImageView mNextMatchImage, mNextRefMatchImage;
        private List<ImageView> mPastMatchesImages;
        private LinearLayout mNextMatchCheckBoxContainer;
        private Button mNextMatchManage, mNextRefMatchSubmit;

        private CardView mNextMatchCard, mNextRefMatchCard, mPastMatchesCard, mLeagueCard;

        public ViewHolderTeam(View itemView) {
            super(itemView);
            mNextMatchCard = (CardView) itemView.findViewById(R.id.fragment_team_overview_nextmatch);
            mNextMatchName = (TextView) itemView.findViewById(R.id.fragment_team_overview_nextmatch_name);
            mNextMatchPlace = (TextView) itemView.findViewById(R.id.fragment_team_overview_nextmatch_place);
            mNextMatchDate = (TextView) itemView.findViewById(R.id.fragment_team_overview_nextmatch_date);

            mPastMatchesCard = (CardView)  itemView.findViewById(R.id.fragment_team_overview_pastmatches);
            mPastMatchesHeader = (TextView) itemView.findViewById(R.id.fragment_team_overview_pastmatches_header);

            mNextRefMatchCard = (CardView) itemView.findViewById(R.id.fragment_team_overview_nextrefmatch);

            mNextRefMatchName = (TextView) itemView.findViewById(R.id.fragment_team_overview_nextrefmatch_name);
            mNextRefMatchPlace = (TextView) itemView.findViewById(R.id.fragment_team_overview_nextrefmatch_place);
            mNextRefMatchDate = (TextView) itemView.findViewById(R.id.fragment_team_overview_nextrefmatch_date);
            mNextRefMatchSubmit = (Button) itemView.findViewById(R.id.fragment_team_overview_nextrefmatch_submit);
            mNextRefMatchImage = (ImageView) itemView.findViewById(R.id.fragment_team_overview_nextrefmatch_image);

            mNextMatchCheckBoxContainer = (LinearLayout) itemView.findViewById(R.id.fragment_team_overview_nextmatch_checkbox_container);
            mNextMatchManage = (Button) itemView.findViewById(R.id.fragment_team_overview_nextmatch_manage);

            mNextMatchImage = (ImageView) itemView.findViewById(R.id.fragment_team_overview_nextmatch_image);
            mPastMatchesImages = new ArrayList<>();
            mPastMatchesImages.add(0, (ImageView) itemView.findViewById(R.id.fragment_team_overview_pastmatches_image_1));
            mPastMatchesImages.add(1, (ImageView) itemView.findViewById(R.id.fragment_team_overview_pastmatches_image_2));
            mPastMatchesImages.add(2, (ImageView) itemView.findViewById(R.id.fragment_team_overview_pastmatches_image_3));
            mPastMatchesImages.add(3, (ImageView) itemView.findViewById(R.id.fragment_team_overview_pastmatches_image_4));
            mPastMatchesImages.add(4, (ImageView) itemView.findViewById(R.id.fragment_team_overview_pastmatches_image_5));

            mLeagueCard = (CardView) itemView.findViewById(R.id.fragment_team_overview_league);
            mLeagueHeader = (TextView) itemView.findViewById(R.id.fragment_team_overview_league_header);
            mLeaguePoints = (TextView) itemView.findViewById(R.id.fragment_team_overview_league_points);
            mLeagueWins = (TextView) itemView.findViewById(R.id.fragment_team_overview_league_wins);
            mLeagueDraws = (TextView) itemView.findViewById(R.id.fragment_team_overview_league_draws);
            mLeagueLosses = (TextView) itemView.findViewById(R.id.fragment_team_overview_league_losses);

        }
    }

    public TeamOverviewAdapter(Fragment context, int teamID, int leagueID, SwipeRefreshLayout swipeRefreshLayout, ViewPager viewPager) {
        mContext = context.getActivity();
        mTeamID = teamID;
        mLeagueID = leagueID;
        mSwipeRefreshLayout = swipeRefreshLayout;
        mViewPager = viewPager;
        mCallbacks = (onMatchDetailsSelected) mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_team_overview_items, parent, false);
        return new ViewHolderTeam(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolderTeam v = (ViewHolderTeam) holder;
        leagueChanged(v);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
        if(DataStore.getInstance(mContext).isUserLoggedIn()) {
            if(DataStore.getInstance(mContext).isReferee()) {
                DataStore.getInstance(mContext).registerMyUpcomingRefereeDBObserver(this);
            }
            DataStore.getInstance(mContext).registerMyUpcomingGamesDBObserver(this);
            DataStore.getInstance(mContext).registerTeamsScoresDBObserver(this);
            DataStore.getInstance(mContext).registerTeamsFixturesDBObserver(this);
            DataStore.getInstance(mContext).registerLeagueTeamsDBObserver(this);
        } else {
            DataStore.getInstance(mContext).registerLeagueScoreDBObserver(this);
        }
        DataStore.getInstance(mContext).registerLeaguesStandingsDBObserver(this);
        DataStore.getInstance(mContext).registerAllLeagueDBObserver(this);
    }

    @Override
    public void onDetachedFromRecyclerView (RecyclerView recyclerView){
        //prevent mem leaks, unregister
        if(DataStore.getInstance(mContext).isUserLoggedIn()) {
            if(DataStore.getInstance(mContext).isReferee()) {
                DataStore.getInstance(mContext).unregisterMyUpcomingRefereeDBObserver(this);
            }
            DataStore.getInstance(mContext).unregisterMyUpcomingGamesDBObserver(this);
            DataStore.getInstance(mContext).unregisterTeamsScoresDBObserver(this);
            DataStore.getInstance(mContext).unregisterTeamsFixturesDBObserver(this);
            DataStore.getInstance(mContext).unregisterLeagueTeamsDBObserver(this);
        } else {
            DataStore.getInstance(mContext).unregisterLeagueScoreDBObserver(this);
        }
        DataStore.getInstance(mContext).unregisterLeaguesStandingsDBObserver(this);
        DataStore.getInstance(mContext).unregisterAllLeagueDBObserver(this);
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public void notify(String tableName, Object data) {
        switch(tableName) {
            case DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME:
                if(DataStore.getInstance(mContext).getNextRefGame() != null) {
                    notifyDataSetChanged();
                }
                break;
            case DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME:
                if(DataStore.getInstance(mContext).getNextGame()!=null){
                    notifyDataSetChanged();
                }
                break;
            case DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME:
            case DBProviderContract.LEAGUETEAMS_TABLE_NAME:
            case DBProviderContract.LEAGUESSCORE_TABLE_NAME:
            case DBProviderContract.ALLLEAGUES_TABLE_NAME:
//                notifyDataSetChanged();
                break;
        }
    }

    private void leagueChanged(ViewHolderTeam v){
        league = DataStore.getInstance(mContext).getLeague(mLeagueID);
        if (league != null) {
            setLeague(v);
            setLeagueScores(v);
            setNextMatch(false, v);
        }
        setNextMatch(true, v);
    }

    public void setLeague(ViewHolderTeam v){
//        mLeagueContainer.setVisibility(View.VISIBLE);

        v.mLeagueCard.setVisibility(View.VISIBLE);
        String header = league.getLeagueName();
        List<LeagueTeam> leagueTeams = DataStore.getInstance(mContext).getLeagueStandings(league.getLeagueID());
        for(LeagueTeam team : leagueTeams) {
            if (team.getTeamID() == mTeamID) {
                int position = team.getPosition();
                switch (position % 10) {
                    case 1:
                        v.mLeagueHeader.setText(position + "st in " + header);
                        break;
                    case 2:
                        v.mLeagueHeader.setText(position + "nd in " + header);
                        break;
                    case 3:
                        v.mLeagueHeader.setText(position + "rd in " + header);
                        break;
                    default:
                        v.mLeagueHeader.setText(position + "th in " + header);
                        break;
                }
                v.mLeaguePoints.setText(team.getLeaguePoints().toString());
                int color = DataStore.getInstance(mContext).getUserTeamColorPrimary();
                v.mLeaguePoints.setTextColor(color);
                v.mLeagueWins.setText(team.getWin().toString());
                v.mLeagueWins.setTextColor(color);
                v.mLeagueDraws.setText(team.getDraw().toString());
                v.mLeagueDraws.setTextColor(color);
                v.mLeagueLosses.setText(team.getLose().toString());
                v.mLeagueLosses.setTextColor(color);
            }
        }
    }

    public void setNextMatch(boolean isRefMatch, ViewHolderTeam v){
        if(isRefMatch && DataStore.getInstance(mContext).getNextRefGame() != null) {
            //            mNextRefMatchContainer.setVisibility(View.VISIBLE);
            v.mNextRefMatchCard.setVisibility(View.VISIBLE);

            final Match nextMatch = DataStore.getInstance(mContext).getNextRefGame();

            v.mNextRefMatchName.setText(nextMatch.getTeamOne() + " vs " + nextMatch.getTeamTwo());
            v.mNextRefMatchPlace.setText(nextMatch.getPlace());
            DateFormatter sdf = new DateFormatter();
            v.mNextRefMatchDate.setText(sdf.format(nextMatch.getDateTime()));
            v.mNextRefMatchSubmit.setVisibility(View.VISIBLE);
            v.mNextRefMatchSubmit.setTextColor(DataStore.getInstance(mContext).getUserTeamColorPrimary());
            v.mNextRefMatchSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, SubmitScoreActivity.class);
                    intent.putExtra(Match.KEY_MATCHID, nextMatch.getMatchID());
                    intent.putExtra(Match.KEY_TEAMONE, nextMatch.getTeamOne());
                    intent.putExtra(Match.KEY_TEAMTWO, nextMatch.getTeamTwo());
                    showSubmitScoreDialog(nextMatch);
//                    startActivity(intent);
                }
            });
            Drawable drawable = TextDrawable.builder()
                    .buildRound("N", mContext
                            .getResources().getColor(R.color.dark_divider));
            v.mNextRefMatchImage.setImageDrawable(drawable);
        } else if(!isRefMatch && DataStore.getInstance(mContext).getNextGame() != null) {
            final Match nextMatch = DataStore.getInstance(mContext).getNextGame();
            //            mNextMatchContainer.setVisibility(View.VISIBLE);
            v.mNextMatchCard.setVisibility(View.VISIBLE);

//            mNextMatchDivider.setVisibility(View.VISIBLE);
            int colourTeamOne;
            int colourTeamTwo;

            Team teamOne = DataStore.getInstance(mContext).getTeam(mLeagueID,nextMatch.getTeamOneID());
            Team teamTwo = DataStore.getInstance(mContext).getTeam(mLeagueID,nextMatch.getTeamTwoID());

            if(teamOne != null){
                colourTeamOne = Color.parseColor(teamOne.getTeamColorPrimary());
            } else {
                colourTeamOne = Color.GRAY;
            }
            if(teamTwo != null){
                colourTeamTwo = Color.parseColor(teamTwo.getTeamColorPrimary());
            } else {
                colourTeamTwo = mContext
                        .getResources().getColor(R.color.dark_divider);
            }
            if (nextMatch.getTeamOneID() == DataStore.getInstance(mContext).getUserTeamID()) {
                v.mNextMatchName.setText(nextMatch.getTeamTwo());
                TextDrawable avatar = TextDrawable.builder()
                        .buildRound(nextMatch.getTeamTwo().substring(0, 1),
                                colourTeamTwo);
                v.mNextMatchImage.setImageDrawable(avatar);
            } else {
                v.mNextMatchName.setText(nextMatch.getTeamOne());
                TextDrawable avatar = TextDrawable.builder()
                        .buildRound(nextMatch.getTeamOne().substring(0, 1),
                                colourTeamOne);
                v.mNextMatchImage.setImageDrawable(avatar);
            }
            v.mNextMatchPlace.setText(nextMatch.getPlace());
            DateFormatter sdf = new DateFormatter();
            v.mNextMatchDate.setText(sdf.format(nextMatch.getDateTime()));
            v.mNextMatchCheckBoxContainer.setVisibility(View.GONE);
            v.mNextMatchManage.setVisibility(View.VISIBLE);
            v.mNextMatchManage.setText("Match Details");
            v.mNextMatchManage.setTextColor(DataStore.getInstance(mContext).getUserTeamColorPrimary());
            v.mNextMatchManage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putString(Match.KEY_TEAMONE, nextMatch.getTeamOne());
                    args.putString(Match.KEY_TEAMTWO, nextMatch.getTeamTwo());
                    args.putInt(Match.KEY_TEAMONEID, nextMatch.getTeamOneID());
                    args.putInt(Match.KEY_TEAMTWOID, nextMatch.getTeamTwoID());
                    args.putInt(Match.KEY_TEAMONEPOINTS, nextMatch.getTeamOnePoints());
                    args.putInt(Match.KEY_TEAMTWOPOINTS, nextMatch.getTeamTwoPoints());
                    args.putInt("leagueID", mLeagueID);
                    args.putInt(Match.KEY_MATCHID, nextMatch.getMatchID());
                    args.putString(Match.KEY_PLACE, nextMatch.getPlace());
                    args.putBoolean("hasBeenPlayed", false);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    args.putString(Match.KEY_DATETIME, sdf.format(nextMatch.getDateTime()));
                    args.putFloat(Match.KEY_LONGITUDE, nextMatch.getLongitude());
                    args.putFloat(Match.KEY_LATITUDE, nextMatch.getLatitude());
                    args.putString(Match.KEY_POSTCODE, nextMatch.getPostCode());
                    args.putString(Match.KEY_ADDRESS, nextMatch.getAddress());

                    mCallbacks.matchDetailsSelectedCallback(args);

                }
            });
        }
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

        Team teamOne = DataStore.getInstance(mContext).getTeam(mLeagueID,match.getTeamOneID());
        Team teamTwo = DataStore.getInstance(mContext).getTeam(mLeagueID,match.getTeamTwoID());

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

    public void setLeagueScores(ViewHolderTeam v){
        List<Match> pastMatches = DataStore.getInstance(mContext).getTeamScores(league.getLeagueID(),mTeamID, Match.SortType.DESCENDING);
        if(pastMatches.size() == 0){
            return;
        }
//        mPastMatchesContainer.setVisibility(View.VISIBLE);
        v.mPastMatchesCard.setVisibility(View.VISIBLE);
//        mPastMatchesDivider.setVisibility(View.VISIBLE);
        TextDrawable win = TextDrawable.builder()
                .buildRound("W", mContext.getResources().getColor(R.color.darkgreen));
        TextDrawable draw = TextDrawable.builder()
                .buildRound("D", mContext.getResources().getColor(R.color.darkorange));
        TextDrawable lose = TextDrawable.builder()
                .buildRound("L", mContext.getResources().getColor(R.color.darkred));
        int max = Math.min(pastMatches.size(), 5);
        v.mPastMatchesHeader.setText("Last " + max + " matches");
        for (int i = 0; i < max; i++) {
            final Match match = pastMatches.get(i);
            if (match.getTeamOneID() == mTeamID) {
                if (match.getTeamOnePoints() > match.getTeamTwoPoints()) {
                    v.mPastMatchesImages.get(i).setImageDrawable(win);
                } else {
                    v.mPastMatchesImages.get(i).setImageDrawable(lose);
                }
            } else {
                if (match.getTeamOnePoints() < match.getTeamTwoPoints()) {
                    v.mPastMatchesImages.get(i).setImageDrawable(win);
                } else {
                    v.mPastMatchesImages.get(i).setImageDrawable(lose);
                }
            }
            if (match.getTeamOnePoints() == match.getTeamTwoPoints()) {
                v.mPastMatchesImages.get(i).setImageDrawable(draw);
            }
            final int lgId = mLeagueID;
            v.mPastMatchesImages.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle args = new Bundle();
                    args.putString(Match.KEY_TEAMONE, match.getTeamOne());
                    args.putString(Match.KEY_TEAMTWO, match.getTeamTwo());
                    args.putInt(Match.KEY_TEAMONEID, match.getTeamOneID());
                    args.putInt(Match.KEY_TEAMTWOID, match.getTeamTwoID());
                    args.putInt(Match.KEY_TEAMONEPOINTS, match.getTeamOnePoints());
                    args.putInt(Match.KEY_TEAMTWOPOINTS, match.getTeamTwoPoints());
                    args.putInt("leagueID", mLeagueID);
                    args.putInt(Match.KEY_MATCHID, match.getMatchID());
                    args.putString(Match.KEY_PLACE, match.getPlace());
                    args.putBoolean("hasBeenPlayed", true);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
                    args.putString(Match.KEY_DATETIME, sdf.format(match.getDateTime()));
                    args.putFloat(Match.KEY_LONGITUDE, match.getLongitude());
                    args.putFloat(Match.KEY_LATITUDE, match.getLatitude());
                    args.putString(Match.KEY_POSTCODE, match.getPostCode());
                    args.putString(Match.KEY_ADDRESS, match.getAddress());
                    mCallbacks.matchDetailsSelectedCallback(args);
                }
            });
        }
    }
}
