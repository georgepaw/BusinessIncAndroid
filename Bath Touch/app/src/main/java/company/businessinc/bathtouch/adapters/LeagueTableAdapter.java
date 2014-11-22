package company.businessinc.bathtouch.adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import company.businessinc.bathtouch.ApdaterData.LeagueTableData;
import company.businessinc.bathtouch.R;
import company.businessinc.dataModels.LeagueTeam;

/**
 * Created by user on 22/11/14.
 */
public class LeagueTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LeagueTableData mDataset;

    public class ViewHolderLeague extends RecyclerView.ViewHolder {
        public TextView mTeamName, mTeamPos, mTeamWin, mTeamLose, mTeamDraw, mTeamPts;

        public ViewHolderLeague(View v) {
            super(v);
            mTeamName = (TextView) v.findViewById(R.id.league_item_team_name);
            mTeamPos = (TextView) v.findViewById(R.id.league_item_team_position);
            mTeamWin = (TextView) v.findViewById(R.id.league_item_team_won);
            mTeamLose = (TextView) v.findViewById(R.id.league_item_team_lose);
            mTeamDraw = (TextView) v.findViewById(R.id.league_item_team_draw);
            mTeamPts = (TextView) v.findViewById(R.id.league_item_team_points);

        }
    }

    public LeagueTableAdapter(LeagueTableData mDataSet) {
        mDataset = mDataSet;
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
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolderLeague(v);


    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){

        LeagueTeam team = mDataset.getTeam(position);
        ViewHolderLeague v = (ViewHolderLeague) holder;

        v.mTeamName.setText(team.getTeamName());
        v.mTeamPos.setText(team.getPosition().toString());
        v.mTeamDraw.setText(team.getDraw().toString());
        v.mTeamLose.setText(team.getLose().toString());
        v.mTeamWin.setText(team.getWin().toString());
        v.mTeamPts.setText(team.getLeaguePoints().toString());
        v.mTeamPts.setTypeface(null, Typeface.BOLD);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}