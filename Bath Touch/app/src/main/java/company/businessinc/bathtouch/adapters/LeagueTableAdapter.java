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
public class LeagueTableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    private LeagueTableData mDataset;
    private List<LeagueTeam> leagueTeams = new ArrayList<LeagueTeam>();
    private Context mContext;
    private int expandedPosition = -1;


    public void changeVis(int loc) {
        Log.d("LEAGUEFRAGMENT", "clicked");

//        ViewHolderLeague holder = (ViewHolderLeague) v.getTag();
//        String theString = Integer.toString(holder.getPosition());

        // Check for an expanded view, collapse if you find one
        if (expandedPosition >= 0) {
            int prev = expandedPosition;
            notifyItemChanged(prev);
        }
        // Set the current position to "expanded"
        expandedPosition = loc;
        notifyItemChanged(expandedPosition);

        Toast.makeText(mContext, "Clicked: " + Integer.toString(loc), Toast.LENGTH_SHORT).show();
    }


    public class ViewHolderLeague extends RecyclerView.ViewHolder  implements  View.OnClickListener{
        public TextView mTeamName, mTeamPos, mTeamWin, mTeamLose, mTeamDraw, mTeamPts;
        public ImageView mImagePosition;
        public LinearLayout mExpandArea;
        public RelativeLayout mItem;

        public ViewHolderLeague(View v) {
            super(v);
            mTeamName = (TextView) v.findViewById(R.id.league_item_team_name);
//            mTeamPos = (TextView) v.findViewById(R.id.league_item_team_position);
            mTeamWin = (TextView) v.findViewById(R.id.league_item_team_won);
            mTeamLose = (TextView) v.findViewById(R.id.league_item_team_lose);
            mTeamDraw = (TextView) v.findViewById(R.id.league_item_team_draw);
            mTeamPts = (TextView) v.findViewById(R.id.league_item_team_points);
            mImagePosition = (ImageView) v.findViewById(R.id.image_view);
            mExpandArea = (LinearLayout) v.findViewById(R.id.llExpandArea);
            mItem = (RelativeLayout) v.findViewById(R.id.league_display_item_container);
            mItem.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            changeVis(getPosition());
        }
    }

    public LeagueTableAdapter(Activity context) {
        mContext = context.getApplicationContext();
    }

    public void setLeagueTeams(List<LeagueTeam> leagueTeams){
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

        String leaguePosition = team.getPosition().toString();
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .textColor(Color.WHITE)
                .toUpperCase()
                .endConfig()
                .buildRound(leaguePosition, mContext.getResources().getColor(R.color.primary));

        Log.d("LEAGUEFRAGMENT", "binded");


        v.mImagePosition.setImageDrawable(drawable);

        if (position == expandedPosition) {
            v.mExpandArea.setVisibility(View.VISIBLE);
        } else {
            v.mExpandArea.setVisibility(View.GONE);
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return leagueTeams.size();
    }


}
