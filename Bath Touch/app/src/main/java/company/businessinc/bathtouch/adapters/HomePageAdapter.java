package company.businessinc.bathtouch.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.bathtouch.R;
import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.endpoints.LeagueView;
import company.businessinc.endpoints.LeagueViewInterface;

/**
 * Created by user on 20/11/14.
 */
public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private String TAG = "HomePageAdapter";
    private ViewHolderTable mViewHolderTable;
    private ViewHolderNextMatch mViewHolderNextRefMatch;
    private ViewHolderNextMatch mViewHolderNextMatch;
    private int items = 6;
    private homePageAdapterCallbacks mCallbacks;
    private Match nextMatch = null;
    private Match nextRefMatch = null;
    private List<LeagueTeam> leagueTeam = null;
    private League league = null;

    //card positions
    public static final int GREETINGCARD = 0;
    public static final int NEXTREFGAME = 1;
    public static final int NEXTGAME = 2;
    public static final int TABLE = 3;
    public static final int TEAMRESULTS = 4;
    public static final int TEAMOVERVIEW = 5;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static interface homePageAdapterCallbacks {
        void onNextMatchCardSelected();
        void onTeamResultsCardSelected();
        void onLeagueCardSelected();
        void onNextRefMatchCardSelected();

    }

    public class ViewHolderHome extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public ViewHolderHome(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.home_page_card_home);
            mTextView = (TextView) v.findViewById(R.id.home_page_card_home_header);
        }
    }

    public class ViewHolderGreeting extends RecyclerView.ViewHolder {
        public TextView mUserName;
        public TextView mTeamName;

        public ViewHolderGreeting(View v) {
            super(v);
            mUserName = (TextView) v.findViewById(R.id.alt_home_page_username);
            mTeamName = (TextView) v.findViewById(R.id.alt_home_page_user_team);
        }

    }

    public class ViewHolderTeamOverview extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ViewHolderTeamOverview(View v){
            super(v);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            Log.d(TAG, "woo");
            Toast.makeText(v.getContext(), "Clicked box", Toast.LENGTH_LONG).show();
        }
    }

    public class ViewHolderEmpty extends RecyclerView.ViewHolder {
        public ViewHolderEmpty(View v) {
            super(v);
        }
    }

    public class ViewHolderNextMatch extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CardView mCardView;
        public TextView mTeam1Name, mTeam2Name, mDate, mParticipation, mVS;
        public Match match;
        public boolean isRef = false;

         public ViewHolderNextMatch(View v, boolean isRef) {
             super(v);
             this.isRef = isRef;
             mCardView = (CardView) v.findViewById(R.id.home_page_card_next_match_container);
             mTeam1Name = (TextView) v.findViewById(R.id.home_card_next_match_team1_name);
             mTeam2Name = (TextView) v.findViewById(R.id.home_card_next_match_team2_name);
             mDate = (TextView) v.findViewById(R.id.home_card_next_match_pretty_time);
             mParticipation = (TextView) v.findViewById(R.id.home_card_next_match_participation);
             mVS = (TextView) v.findViewById(R.id.home_card_next_match_vs);
             mCardView.setOnClickListener(this);
         }

        @Override
        public void onClick(View v){
            if(mCallbacks != null){
                if (v.getId() == mCardView.getId()){
                    if(isRef) {
                        mCallbacks.onNextRefMatchCardSelected();
                    } else {
                        mCallbacks.onNextMatchCardSelected();
                    }
                }
            }
        }
    }

    public class ViewHolderMyTeam extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mTeamName, mTeam1Name, mTeam2Name;
        public TextView mTeamScore1, mTeamScore2, mTeam1Score, mTeam2Score;
        public CardView mCardView;

        public ViewHolderMyTeam(View v) {
            super(v);

            mTeamName = (TextView) v.findViewById(R.id.home_card_team_name);
            mTeam1Name = (TextView) v.findViewById(R.id.home_card_team_opp1_name);
            mTeam2Name = (TextView) v.findViewById(R.id.home_card_team_opp2_name);
            mTeamScore1 = (TextView) v.findViewById(R.id.home_card_team_score1);
            mTeamScore2 = (TextView) v.findViewById(R.id.home_card_team_score2);
            mTeam1Score = (TextView) v.findViewById(R.id.home_card_team_opp1_score);
            mTeam2Score = (TextView) v.findViewById(R.id.home_card_team_opp2_score);
            mCardView = (CardView) v.findViewById(R.id.home_page_card_team_container);
            mCardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mCallbacks.onTeamResultsCardSelected();
        }
    }

    public class ViewHolderTable extends RecyclerView.ViewHolder implements View.OnClickListener{
        // each data item is just a string in this case
        public CardView mCardView;
        public ProgressBar mProgressBar;
        public TextView mHeaderTextView;
        public TextView mSubHeaderTextView;
        public TextView mTeam1name, mTeam2name, mTeam3name;
        public TextView mTeam1Number, mTeam2Number, mTeam3Number;
        public TextView mTeam1Won, mTeam2Won, mTeam3Won;
        public TextView mTeam1Draw, mTeam2Draw, mTeam3Draw;
        public TextView mTeam1Lost, mTeam2Lost, mTeam3Lost;
        public TextView mTeam1Pts, mTeam2Pts, mTeam3Pts;

        public ViewHolderTable(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.home_page_card_table);
            mProgressBar = (ProgressBar) v.findViewById(R.id.home_page_card_table_progressbar);
            mHeaderTextView = (TextView) v.findViewById(R.id.home_page_card_table_header);
            mSubHeaderTextView = (TextView) v.findViewById(R.id.home_page_card_table_subHeader);
            mTeam1name = (TextView) v.findViewById(R.id.team_name1);
            mTeam2name = (TextView) v.findViewById(R.id.team_name2);
            mTeam3name = (TextView) v.findViewById(R.id.team_name3);
            mTeam1Number = (TextView) v.findViewById(R.id.team_number1);
            mTeam2Number = (TextView) v.findViewById(R.id.team_number2);
            mTeam3Number = (TextView) v.findViewById(R.id.team_number3);
            mTeam1Won = (TextView) v.findViewById(R.id.team_won1);
            mTeam2Won = (TextView) v.findViewById(R.id.team_won2);
            mTeam3Won = (TextView) v.findViewById(R.id.team_won3);
            mTeam1Draw = (TextView) v.findViewById(R.id.team_draw1);
            mTeam2Draw = (TextView) v.findViewById(R.id.team_draw2);
            mTeam3Draw = (TextView) v.findViewById(R.id.team_draw3);
            mTeam1Lost = (TextView) v.findViewById(R.id.team_lose1);
            mTeam2Lost = (TextView) v.findViewById(R.id.team_lose2);
            mTeam3Lost = (TextView) v.findViewById(R.id.team_lose3);
            mTeam1Pts = (TextView) v.findViewById(R.id.team_points1);
            mTeam2Pts = (TextView) v.findViewById(R.id.team_points2);
            mTeam3Pts = (TextView) v.findViewById(R.id.team_points3);
            mCardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mCallbacks.onLeagueCardSelected();
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomePageAdapter(FragmentActivity activity) {
        //add the callbacks of the fragment
        mCallbacks = (homePageAdapterCallbacks) activity;
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
                .inflate(R.layout.home_cards_content, parent, false);
                // set the view's size, margins, paddings and layout parameters
        View ve = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_empty, parent, false);

        switch (viewType){
            case GREETINGCARD:
                View vg = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_greeting, parent, false);
                return new ViewHolderGreeting(vg);
            case NEXTREFGAME:
                if(DataStore.getInstance(context).isUserLoggedIn() && DataStore.getInstance(context).isReferee()){
                    View vnrm = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_next_match, parent, false);
                    return new ViewHolderNextMatch(vnrm, true);
                } else {
                    return new ViewHolderEmpty(ve);
                }
            case NEXTGAME:
                if(DataStore.getInstance(context).isUserLoggedIn()){
                    View vnm = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_next_match, parent, false);
                    return new ViewHolderNextMatch(vnm, false);
                } else {
                    return new ViewHolderEmpty(ve);
                }
            case TABLE:
                View vt = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_table, parent, false);
                return new ViewHolderTable(vt);
            case TEAMRESULTS:
                View vmt = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_team, parent, false);
                return new ViewHolderMyTeam(vmt);
            case TEAMOVERVIEW:
                View vto = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_team_overview, parent, false);
                return new ViewHolderTeamOverview(vto);
            default:
                return new ViewHolderEmpty(ve);
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(holder instanceof ViewHolderHome){
            ViewHolderHome vh = (ViewHolderHome) holder;
            vh.mTextView.setText("HOME PAGE");
        }
        else if (holder instanceof ViewHolderTable) {
            mViewHolderTable = (ViewHolderTable) holder;
            mViewHolderTable.mSubHeaderTextView.setText("Standings of Top 3 Teams");
           setVisibility("table", View.INVISIBLE);
            mViewHolderTable.mProgressBar.setVisibility(View.VISIBLE);
            loadData();
        }
        else if(holder instanceof ViewHolderNextMatch) {
            switch(position){
                case NEXTREFGAME:
                    mViewHolderNextRefMatch = (ViewHolderNextMatch) holder;
                    if(DataStore.getInstance(context).isUserLoggedIn()){
                        if(nextRefMatch!=null) {
                            setNextRefMatch(nextRefMatch);
                        }
                    }
                    break;
                case NEXTGAME:
                    mViewHolderNextMatch = (ViewHolderNextMatch) holder;
                    if(DataStore.getInstance(context).isUserLoggedIn()){
                        if(nextRefMatch!=null) {
                            setNextMatch(nextMatch);
                        }
                    }
                    break;
            }
        }
        else if(holder instanceof ViewHolderMyTeam){ //TODO remove hardcoding
            ViewHolderMyTeam vmt = (ViewHolderMyTeam) holder;

            vmt.mTeamName.setText("ALL HARDCODED");
            vmt.mTeam1Name.setText("EE Tigers");
            vmt.mTeam2Name.setText("HP All Stars");
            vmt.mTeam1Score.setText("4");
            vmt.mTeam2Score.setText("0");
            vmt.mTeamScore1.setText("28");
            vmt.mTeamScore2.setText("11");

        }
        else if(holder instanceof  ViewHolderEmpty) {
            ViewHolderEmpty ve = (ViewHolderEmpty) holder;
        }
        else if(holder instanceof ViewHolderGreeting){
            ViewHolderGreeting vg = (ViewHolderGreeting) holder;
            if(DataStore.getInstance(context).isUserLoggedIn()){
                vg.mUserName.setText(DataStore.getInstance(context).getUserName());
                vg.mTeamName.setText(DataStore.getInstance(context).getUserTeam());
            }
            else{
                vg.mUserName.setText("Bath Touch Leagues");
                vg.mTeamName.setText("Log in to view your teams");
            }

        }
        else if(holder instanceof ViewHolderTeamOverview){
            ViewHolderTeamOverview vto = (ViewHolderTeamOverview) holder;
        }
        else{
            ViewHolderHome vho = (ViewHolderHome) holder;
            vho.mTextView.setText("HOME PAGE");
        }


    }

    private void setVisibility(String viewholdername, int visibility) {

        mViewHolderTable.mSubHeaderTextView.setVisibility(visibility);
        mViewHolderTable.mProgressBar.setVisibility(visibility);
        mViewHolderTable.mHeaderTextView.setVisibility(visibility);
        mViewHolderTable.mTeam1name.setVisibility(visibility);
        mViewHolderTable.mTeam2name.setVisibility(visibility);
        mViewHolderTable.mTeam3name.setVisibility(visibility);
        mViewHolderTable.mTeam1Number.setVisibility(visibility);
        mViewHolderTable.mTeam2Number.setVisibility(visibility);
        mViewHolderTable.mTeam3Number.setVisibility(visibility);
        mViewHolderTable.mTeam1Won.setVisibility(visibility);
        mViewHolderTable.mTeam2Won.setVisibility(visibility);
        mViewHolderTable.mTeam3Won.setVisibility(visibility);
        mViewHolderTable.mTeam1Draw.setVisibility(visibility);
        mViewHolderTable.mTeam2Draw.setVisibility(visibility);
        mViewHolderTable.mTeam3Draw.setVisibility(visibility);
        mViewHolderTable.mTeam1Lost.setVisibility(visibility);
        mViewHolderTable.mTeam2Lost.setVisibility(visibility);
        mViewHolderTable.mTeam3Lost.setVisibility(visibility);
        mViewHolderTable.mTeam1Pts.setVisibility(visibility);
        mViewHolderTable.mTeam2Pts.setVisibility(visibility);
        mViewHolderTable.mTeam3Pts.setVisibility(visibility);
    }
    
    public void setLeague(List<LeagueTeam> data, League league){
        this.leagueTeam = data;
        this.league = league;
        notifyDataSetChanged();
    }

    private void loadData() {
        if(leagueTeam!= null) {
            Collections.sort(leagueTeam, new Comparator<LeagueTeam>() {
                @Override
                public int compare(LeagueTeam T1, LeagueTeam T2) {
                    return T1.getPosition() - T2.getPosition();
                }
            });
            setVisibility("table", View.VISIBLE);
            mViewHolderTable.mProgressBar.setVisibility(View.GONE);
            mViewHolderTable.mHeaderTextView.setText(league.getLeagueName());
            mViewHolderTable.mTeam1name.setText(leagueTeam.get(0).getTeamName());
            mViewHolderTable.mTeam2name.setText(leagueTeam.get(1).getTeamName());
            mViewHolderTable.mTeam3name.setText(leagueTeam.get(2).getTeamName());
            mViewHolderTable.mTeam1Number.setText(leagueTeam.get(0).getPosition().toString());
            mViewHolderTable.mTeam2Number.setText(leagueTeam.get(1).getPosition().toString());
            mViewHolderTable.mTeam3Number.setText(leagueTeam.get(2).getPosition().toString());
            mViewHolderTable.mTeam1Won.setText(leagueTeam.get(0).getWin().toString());
            mViewHolderTable.mTeam2Won.setText(leagueTeam.get(1).getWin().toString());
            mViewHolderTable.mTeam3Won.setText(leagueTeam.get(2).getWin().toString());
            mViewHolderTable.mTeam1Draw.setText(leagueTeam.get(0).getDraw().toString());
            mViewHolderTable.mTeam2Draw.setText(leagueTeam.get(1).getDraw().toString());
            mViewHolderTable.mTeam3Draw.setText(leagueTeam.get(2).getDraw().toString());
            mViewHolderTable.mTeam1Lost.setText(leagueTeam.get(0).getLose().toString());
            mViewHolderTable.mTeam2Lost.setText(leagueTeam.get(1).getLose().toString());
            mViewHolderTable.mTeam3Lost.setText(leagueTeam.get(2).getLose().toString());
            mViewHolderTable.mTeam1Pts.setText(leagueTeam.get(0).getLeaguePoints().toString());
            mViewHolderTable.mTeam2Pts.setText(leagueTeam.get(1).getLeaguePoints().toString());
            mViewHolderTable.mTeam3Pts.setText(leagueTeam.get(2).getLeaguePoints().toString());
        }

    }

    public void setNextRefMatch(Match nextRefMatch){
        this.nextRefMatch = nextRefMatch;
        mViewHolderNextRefMatch.mParticipation.setText("Refereeing");
        mViewHolderNextRefMatch.mTeam1Name.setText(nextRefMatch.getTeamOne());
        mViewHolderNextRefMatch.mTeam2Name.setText(nextRefMatch.getTeamTwo());
        mViewHolderNextRefMatch.mVS.setText("VS");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, HH:mm");
        mViewHolderNextRefMatch.mDate.setText(sdf.format(nextRefMatch.getDateTime()));
        mViewHolderNextRefMatch.mCardView.setVisibility(View.VISIBLE);
    }

    public void setNextMatch(Match nextMatch){
        this.nextMatch = nextMatch;
        mViewHolderNextMatch.mParticipation.setText("Playing");
        mViewHolderNextMatch.mTeam1Name.setText(nextMatch.getTeamOne());
        mViewHolderNextMatch.mTeam2Name.setText(nextMatch.getTeamTwo());
        mViewHolderNextMatch.mVS.setText("VS");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, HH:mm");
        mViewHolderNextMatch.mDate.setText(sdf.format(nextMatch.getDateTime()));
        mViewHolderNextMatch.mCardView.setVisibility(View.VISIBLE);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items;
    }
}
