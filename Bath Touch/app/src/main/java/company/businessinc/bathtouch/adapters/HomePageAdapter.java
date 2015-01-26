package company.businessinc.bathtouch.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import company.businessinc.bathtouch.ApdaterData.HomeCardData;
import company.businessinc.bathtouch.DataStore;
import company.businessinc.bathtouch.R;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.LeagueView;
import company.businessinc.endpoints.LeagueViewInterface;
import company.businessinc.endpoints.RefGames;
import company.businessinc.endpoints.RefGamesInterface;

/**
 * Created by user on 20/11/14.
 */
public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LeagueViewInterface, RefGamesInterface {

    private String TAG = "HomePageAdapter";
    private ViewHolderTable mViewHolderTable;
    private ViewHolderNextMatch mViewHolderNextMatch;
    private int items = 4;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public class ViewHolderHome extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTextView;
        public ViewHolderHome(View v) {
            super(v);
            mCardView = (CardView) v.findViewById(R.id.home_page_card_home);
            mTextView = (TextView) v.findViewById(R.id.home_page_card_home_header);
        }
    }

    public class ViewHolderEmpty extends RecyclerView.ViewHolder {
        public ViewHolderEmpty(View v) {
            super(v);
        }
    }

    public class ViewHolderNextMatch extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView mTeam1Name, mTeam2Name, mDate, mParticipation, mVS;
        public Match match;

         public ViewHolderNextMatch(View v) {
             super(v);
             mCardView = (CardView) v.findViewById(R.id.home_page_card_table);
             mTeam1Name = (TextView) v.findViewById(R.id.home_card_next_match_team1_name);
             mTeam2Name = (TextView) v.findViewById(R.id.home_card_next_match_team2_name);
             mDate = (TextView) v.findViewById(R.id.home_card_next_match_pretty_time);
             mParticipation = (TextView) v.findViewById(R.id.home_card_next_match_participation);
             mVS = (TextView) v.findViewById(R.id.home_card_next_match_vs);
         }

    }

    public class ViewHolderMyTeam extends RecyclerView.ViewHolder {

        public TextView mTeamName, mTeam1Name, mTeam2Name;
        public TextView mTeamScore1, mTeamScore2, mTeam1Score, mTeam2Score;

        public ViewHolderMyTeam(View v) {
            super(v);

            mTeamName = (TextView) v.findViewById(R.id.home_card_team_name);
            mTeam1Name = (TextView) v.findViewById(R.id.home_card_team_opp1_name);
            mTeam2Name = (TextView) v.findViewById(R.id.home_card_team_opp2_name);
            mTeamScore1 = (TextView) v.findViewById(R.id.home_card_team_score1);
            mTeamScore2 = (TextView) v.findViewById(R.id.home_card_team_score2);
            mTeam1Score = (TextView) v.findViewById(R.id.home_card_team_opp1_score);
            mTeam2Score = (TextView) v.findViewById(R.id.home_card_team_opp2_score);

        }
    }

    public class ViewHolderTable extends RecyclerView.ViewHolder {
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


        }



    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public HomePageAdapter() {

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
        View vt = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_table, parent, false);
        View vnm = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_next_match, parent, false);
        View vmt = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_team, parent, false);
        View ve = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_empty, parent, false);
        switch (viewType){
            case 0:
                if(User.isLoggedIn()){
                    return new ViewHolderNextMatch(vnm);
                } else {
                    return new ViewHolderEmpty(ve);
                }
            case 1:
                return new ViewHolderTable(vt);
            case 2:
                return new ViewHolderMyTeam(vmt);
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
            new LeagueView(this, 1).execute(); //TODO remove hard coding here
        }
        else if(holder instanceof ViewHolderNextMatch) {

            mViewHolderNextMatch = (ViewHolderNextMatch) holder;
            if(User.isLoggedIn()){
                mViewHolderNextMatch.mCardView.setVisibility(View.VISIBLE);
                if(DataStore.getNextMatch() != null)
                    setNextMatchText(DataStore.getNextMatch());
                else
                    new RefGames(this).execute();
            }
        }
        else if(holder instanceof ViewHolderMyTeam){
            ViewHolderMyTeam vmt = (ViewHolderMyTeam) holder;

            vmt.mTeamName.setText("CompSci Vipers");
            vmt.mTeam1Name.setText("EE Tigers");
            vmt.mTeam2Name.setText("HP All Stars");
            vmt.mTeam1Score.setText("4");
            vmt.mTeam2Score.setText("0");
            vmt.mTeamScore1.setText("28");
            vmt.mTeamScore2.setText("11");
        }else if(holder instanceof  ViewHolderEmpty) {
            ViewHolderEmpty ve = (ViewHolderEmpty) holder;
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

    @Override
    public void leagueViewCallback(List<LeagueTeam> data) {
        if(data!= null) {
            Collections.sort(data, new Comparator<LeagueTeam>() {
                @Override
                public int compare(LeagueTeam T1, LeagueTeam T2) {
                    return T1.getPosition() - T2.getPosition();
                }
            });
            setVisibility("table", View.VISIBLE);
            mViewHolderTable.mProgressBar.setVisibility(View.GONE);
            mViewHolderTable.mHeaderTextView.setText("Bath Summer League 2015");
            mViewHolderTable.mTeam1name.setText(data.get(0).getTeamName());
            mViewHolderTable.mTeam2name.setText(data.get(1).getTeamName());
            mViewHolderTable.mTeam3name.setText(data.get(2).getTeamName());
            mViewHolderTable.mTeam1Number.setText(data.get(0).getPosition().toString());
            mViewHolderTable.mTeam2Number.setText(data.get(1).getPosition().toString());
            mViewHolderTable.mTeam3Number.setText(data.get(2).getPosition().toString());
            mViewHolderTable.mTeam1Won.setText(data.get(0).getWin().toString());
            mViewHolderTable.mTeam2Won.setText(data.get(1).getWin().toString());
            mViewHolderTable.mTeam3Won.setText(data.get(2).getWin().toString());
            mViewHolderTable.mTeam1Draw.setText(data.get(0).getDraw().toString());
            mViewHolderTable.mTeam2Draw.setText(data.get(1).getDraw().toString());
            mViewHolderTable.mTeam3Draw.setText(data.get(2).getDraw().toString());
            mViewHolderTable.mTeam1Lost.setText(data.get(0).getLose().toString());
            mViewHolderTable.mTeam2Lost.setText(data.get(1).getLose().toString());
            mViewHolderTable.mTeam3Lost.setText(data.get(2).getLose().toString());
            mViewHolderTable.mTeam1Pts.setText(data.get(0).getLeaguePoints().toString());
            mViewHolderTable.mTeam2Pts.setText(data.get(1).getLeaguePoints().toString());
            mViewHolderTable.mTeam3Pts.setText(data.get(2).getLeaguePoints().toString());
        }

    }

   @Override
   public void refGamesCallback(List<Match> data){
       if(data != null){
           //sort the games in ascending order
           Collections.sort(data, new Comparator<Match>() {
               public int compare(Match m1, Match m2) {
                   return m1.getDateTime().compareTo(m2.getDateTime());
               }
           });
           //find the next game
           Match nextMatch = null;
           //Should probably replace this with a better search
           GregorianCalendar gc = new GregorianCalendar();
           gc.add(GregorianCalendar.HOUR,-4); //minus 4 hours so that he can see the next game during and after it's being played
           for(Match m : data){
               if(m.getDateTime().compareTo(gc.getTime()) > -1){
                   nextMatch = m;
               }
           }
           if(nextMatch!=null){
               nextMatch = data.get(0);
               DataStore.setNextMatch(nextMatch);
               setNextMatchText(nextMatch);
           } else {
               //didn't find an upcoming game
                Log.e(TAG, "Didn't find any upcoming games");

           }
       }
   }

    public void setNextMatchText(Match nextMatch) {
        mViewHolderNextMatch.mParticipation.setText("Refereeing");
        mViewHolderNextMatch.mTeam1Name.setText(nextMatch.getTeamOne());
        mViewHolderNextMatch.mTeam2Name.setText(nextMatch.getTeamTwo());
        mViewHolderNextMatch.mVS.setText("VS");
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM, HH:mm");
        mViewHolderNextMatch.mDate.setText(sdf.format(nextMatch.getDateTime()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items;
    }
}
