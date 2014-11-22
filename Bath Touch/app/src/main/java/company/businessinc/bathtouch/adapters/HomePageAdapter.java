package company.businessinc.bathtouch.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.businessinc.bathtouch.ApdaterData.HomeCardData;
import company.businessinc.bathtouch.R;

/**
 * Created by user on 20/11/14.
 */
public class HomePageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HomeCardData mDataset;

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

    public class ViewHolderNextMatch extends RecyclerView.ViewHolder {

        public TextView mTeam1Name, mTeam2Name;

         public ViewHolderNextMatch(View v) {
             super(v);
             mTeam1Name = (TextView) v.findViewById(R.id.home_card_next_match_team1_name);
             mTeam2Name = (TextView) v.findViewById(R.id.home_card_next_match_team2_name);
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
    public HomePageAdapter(HomeCardData myDataset) {
        mDataset = myDataset;
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

        switch (viewType){
            case 0:
                return new ViewHolderNextMatch(vnm);
            case 1:
                return new ViewHolderTable(vt);
            case 2:
                return new ViewHolderMyTeam(vmt);
            default:
                return new ViewHolderHome(v);
        }


    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        if(holder instanceof ViewHolderHome){
            ViewHolderHome vh = (ViewHolderHome) holder;
            vh.mTextView.setText("HOME PAGE MUTHAFUCKA");
        }
        else if (holder instanceof ViewHolderTable) {
            ViewHolderTable vht = (ViewHolderTable) holder;

            vht.mHeaderTextView.setText("Bath Summer League 2015");
            vht.mSubHeaderTextView.setText("Standings of Top 3 Teams");
            vht.mTeam1name.setText(mDataset.teams.get(0).getTeamName());
            vht.mTeam2name.setText(mDataset.teams.get(1).getTeamName());
            vht.mTeam3name.setText(mDataset.teams.get(2).getTeamName());
            vht.mTeam1Number.setText(mDataset.teams.get(0).getPosition().toString());
            vht.mTeam2Number.setText(mDataset.teams.get(1).getPosition().toString());
            vht.mTeam3Number.setText(mDataset.teams.get(2).getPosition().toString());
            vht.mTeam1Won.setText(mDataset.teams.get(0).getWin().toString());
            vht.mTeam2Won.setText(mDataset.teams.get(1).getWin().toString());
            vht.mTeam3Won.setText(mDataset.teams.get(2).getWin().toString());
            vht.mTeam1Draw.setText(mDataset.teams.get(0).getDraw().toString());
            vht.mTeam2Draw.setText(mDataset.teams.get(1).getDraw().toString());
            vht.mTeam3Draw.setText(mDataset.teams.get(2).getDraw().toString());
            vht.mTeam1Lost.setText(mDataset.teams.get(0).getLose().toString());
            vht.mTeam2Lost.setText(mDataset.teams.get(1).getLose().toString());
            vht.mTeam2Lost.setText(mDataset.teams.get(2).getLose().toString());
            vht.mTeam1Pts.setText(mDataset.teams.get(0).getPointsFor().toString());
            vht.mTeam2Pts.setText(mDataset.teams.get(1).getPointsFor().toString());
            vht.mTeam3Pts.setText(mDataset.teams.get(2).getPointsFor().toString());


        }
        else if(holder instanceof ViewHolderNextMatch) {
            ViewHolderNextMatch vnm = (ViewHolderNextMatch) holder;

            vnm.mTeam1Name.setText(mDataset.teams.get(0).getTeamName());
            vnm.mTeam2Name.setText(mDataset.teams.get(1).getTeamName());
        }
        else if(holder instanceof ViewHolderMyTeam){
            ViewHolderMyTeam vmt = (ViewHolderMyTeam) holder;

            vmt.mTeamName.setText("Business Inc United");
            vmt.mTeam1Name.setText("Autistics Athletic Club");
            vmt.mTeam2Name.setText("Dyslises Tmea");
            vmt.mTeam1Score.setText("8");
            vmt.mTeam2Score.setText("11");
            vmt.mTeamScore1.setText("16");
            vmt.mTeamScore2.setText("5");
        }
        else{
            ViewHolderHome vho = (ViewHolderHome) holder;
            vho.mTextView.setText("HOME PAGE MUTHAFUCKA");
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}