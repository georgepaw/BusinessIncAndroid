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
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.ApdaterData.LeagueTableData;
import company.businessinc.bathtouch.R;
import company.businessinc.dataModels.LeagueTeam;

/**
 * Created by user on 22/11/14.
 */
public class LeagueTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private LeagueTableData mDataset;
    private List<LeagueTeam> leagueTeams = new ArrayList<LeagueTeam>();
    private Context mContext;
    private int expandedPosition = -1;


    public void changeVis(int loc) {
        Log.d("LEAGUEFRAGMENT", "clicked");

//        ViewHolderLeague holder = (ViewHolderLeague) v.getTag();
//        String theString = Integer.toString(holder.getPosition());

        // Check for an expanded view, collapse if you find one
//        if (expandedPosition >= 0) {
//            int prev = expandedPosition;//
//
//            notifyItemChanged(prev);
//        }

        if (expandedPosition == loc) { //if clicking an open view, close it
            expandedPosition = -1;
            notifyItemChanged(loc);
        } else {
            if(expandedPosition > -1){
                int prev = expandedPosition;
                expandedPosition = -1;
                notifyItemChanged(prev);
            }
            expandedPosition = loc;
            notifyItemChanged(expandedPosition);
        }



    }


    public class ViewHolderLeague extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTeamName, mTeamPos, mTeamWin, mTeamLose, mTeamDraw, mTeamPts, mPtsFor, mPtsAgn;
        public ImageView mImagePosition, mImageFor, mImageAgn, mCloseButton;
        public RelativeLayout mExpandArea;
        public RelativeLayout mItem, mTeamOverviewButton;

        public ViewHolderLeague(View v) {
            super(v);
            mTeamName = (TextView) v.findViewById(R.id.league_item_team_name);
//            mTeamPos = (TextView) v.findViewById(R.id.league_item_team_position);
            mTeamWin = (TextView) v.findViewById(R.id.league_item_team_won);
            mTeamLose = (TextView) v.findViewById(R.id.league_item_team_lose);
            mTeamDraw = (TextView) v.findViewById(R.id.league_item_team_draw);
            mTeamPts = (TextView) v.findViewById(R.id.league_item_team_points);
            mPtsFor = (TextView) v.findViewById(R.id.league_item_ptsfor);
            mPtsAgn = (TextView) v.findViewById(R.id.league_item_ptsagn);
            mImagePosition = (ImageView) v.findViewById(R.id.image_view);
            mImageFor = (ImageView) v.findViewById(R.id.league_display_pointsfor_image);
            mImageAgn = (ImageView) v.findViewById(R.id.league_display_pointsagn_image);
            mCloseButton = (ImageView) v.findViewById(R.id.league_item_close_button);
            mExpandArea = (RelativeLayout) v.findViewById(R.id.llExpandArea);
            mItem = (RelativeLayout) v.findViewById(R.id.league_display_item_container);
            mTeamOverviewButton = (RelativeLayout) v.findViewById(R.id.league_item_team_overview_button);

            mItem.setOnClickListener(this);
            mCloseButton.setOnClickListener(this);
            mTeamOverviewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "No Team Pverview Fragment", Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public void onClick(View v) {
            changeVis(getPosition());
        }
    }

    public LeagueTableAdapter(Activity context) {
        mContext = context.getApplicationContext();
    }

    public void setLeagueTeams(List<LeagueTeam> leagueTeams) {
        this.leagueTeams = leagueTeams;
        notifyDataSetChanged();
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

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        LeagueTeam team = leagueTeams.get(position);
        ViewHolderLeague v = (ViewHolderLeague) holder;

        v.mTeamName.setText(team.getTeamName());
//        v.mTeamPos.setText(team.getPosition().toString());
        v.mTeamDraw.setText(team.getDraw().toString());
        v.mTeamLose.setText(team.getLose().toString());
        v.mTeamWin.setText(team.getWin().toString());
        v.mTeamPts.setText(team.getLeaguePoints().toString());
        v.mTeamPts.setTypeface(null, Typeface.BOLD);
        v.mPtsFor.setText(team.getPointsFor().toString());
        v.mPtsAgn.setText(team.getPointsAgainst().toString());

        //Set circle icons for positions, points for and against
        String leaguePosition = team.getPosition().toString();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .toUpperCase()
                .endConfig()
                .buildRound(leaguePosition, mContext.getResources().getColor(R.color.primary));

        v.mImagePosition.setImageDrawable(drawable);

        String downArrow = "\u25BC";
        String upArrow = "\u25B2";
        drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .toUpperCase()
                .endConfig()
                .buildRound(upArrow, mContext.getResources().getColor(R.color.green));
        v.mImageFor.setImageDrawable(drawable);
        drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .toUpperCase()
                .endConfig()
                .buildRound(downArrow, mContext.getResources().getColor(R.color.red_monza)); //unicode uparrow
        v.mImageAgn.setImageDrawable(drawable);

        int duration = 400;

//        check whether to open close or leave a card alone
        if(position == expandedPosition){
            Log.d("EXPAND", Integer.toString(expandedPosition));//
            v.mExpandArea.setVisibility(View.VISIBLE);
        }
        else{
            v.mExpandArea.setVisibility(View.GONE);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return leagueTeams.size();
    }


}
