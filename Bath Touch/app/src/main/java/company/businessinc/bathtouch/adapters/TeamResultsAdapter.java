package company.businessinc.bathtouch.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;

import java.util.ArrayList;
import java.util.List;

import company.businessinc.bathtouch.DateFormatter;
import company.businessinc.bathtouch.R;
import company.businessinc.dataModels.Match;

/**
 * Created by user on 21/11/14.
 */
public class TeamResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int expandedPosition = -1;
    private OnResultSelectedCallbacks mCallbacks;
    private Context mContext;
    private String mLeagueName = "";

    public interface OnResultSelectedCallbacks {
        public void showMatchOverview(int position);
    }


    public class ViewHolderResults extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTeam1Name, mTeam2Name, mTeam1Score, mTeam2Score, mLocation, mDate, mLeague;
        public ImageView mImageView, mCloseButton;
        public RelativeLayout mCard, mMatchCardButton;
        public LinearLayout mExpandable;


        public ViewHolderResults(View v) {
            super(v);
            mTeam1Name = (TextView) v.findViewById(R.id.match_result_item_match_team1_name);
            mTeam2Name = (TextView) v.findViewById(R.id.match_result_item_match_team2_name);
            mTeam1Score = (TextView) v.findViewById(R.id.match_result_item_match_team1_score);
            mTeam2Score = (TextView) v.findViewById(R.id.match_result_item_match_team2_score);
            mLeague = (TextView) v.findViewById(R.id.match_result_league);
            mDate = (TextView) v.findViewById(R.id.match_result_date);
            mLocation = (TextView) v.findViewById(R.id.match_result_location);
            mImageView = (ImageView) v.findViewById(R.id.match_result_item_result_image);
            mCloseButton = (ImageView) v.findViewById(R.id.match_result_close_button);
            mCard = (RelativeLayout) v.findViewById(R.id.match_result_item_container);
            mMatchCardButton = (RelativeLayout) v.findViewById(R.id.match_result_match_overview_button);
            mExpandable = (LinearLayout) v.findViewById(R.id.match_result_expandable);


            mCard.setOnClickListener(this);
            mCloseButton.setOnClickListener(this);

            //If clicked, callback to main fragment to start the match over view fragment
            mMatchCardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallbacks.showMatchOverview(getPosition());
                }
            });
        }

        @Override
        public void onClick(View v) {
            //Toggle whether the hidden element of the viewholder is displayed on a click
            changeVis(getPosition());
        }
    }

    private List<Match> leagueScores;
    private String teamName;

    public TeamResultsAdapter(Fragment context) {
        leagueScores = new ArrayList<>();
//        mContext = context;
        mCallbacks = (OnResultSelectedCallbacks) context;
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
                .inflate(R.layout.match_result_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        return new ViewHolderResults(v);


    }

    public void setData(List<Match> leagueScores, String teamName) {
        this.leagueScores = leagueScores;
        this.teamName = teamName;
        notifyDataSetChanged();
    }

    public void setLeagueName(String name){
        mLeagueName = name;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Match match = leagueScores.get(position);
        ViewHolderResults v = (ViewHolderResults) holder;
        v.mTeam1Name.setText(match.getTeamOne());
        v.mTeam2Name.setText(match.getTeamTwo());
        v.mTeam1Score.setText(match.getTeamOnePoints().toString());
        v.mTeam2Score.setText(match.getTeamTwoPoints().toString());
        v.mLocation.setText(match.getPlace().toString());

        v.mLeague.setText(mLeagueName);

        DateFormatter df = new DateFormatter();
        v.mDate.setText(df.format(match.getDateTime()));

        //get the context of the adapater, to use resources later on
        Context context = v.mImageView.getContext();
        TextDrawable win = TextDrawable.builder()
                .buildRound("W", context.getResources().getColor(R.color.darkgreen));
        TextDrawable draw = TextDrawable.builder()
                .buildRound("D", context.getResources().getColor(R.color.darkorange));
        TextDrawable lose = TextDrawable.builder()
                .buildRound("L", context.getResources().getColor(R.color.darkred));

        if (match.getTeamOne().equals(teamName)) {
            v.mTeam1Name.setTypeface(null, Typeface.BOLD);
            v.mTeam1Score.setTypeface(null, Typeface.BOLD);
            v.mTeam2Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Score.setTypeface(null, Typeface.NORMAL);
            if (match.getTeamOnePoints() > match.getTeamTwoPoints()) {
                //won, set green
                v.mImageView.setImageDrawable(win);
            } else {
                v.mImageView.setImageDrawable(lose);
            }
        } else {
            v.mTeam1Name.setTypeface(null, Typeface.NORMAL);
            v.mTeam1Score.setTypeface(null, Typeface.NORMAL);
            v.mTeam2Name.setTypeface(null, Typeface.BOLD);
            v.mTeam2Score.setTypeface(null, Typeface.BOLD);
            if (match.getTeamOnePoints() < match.getTeamTwoPoints()) {
                //won, set green
                v.mImageView.setImageDrawable(win);
            } else {
                v.mImageView.setImageDrawable(lose);
            }
        }

        if (match.getTeamOnePoints() == match.getTeamTwoPoints()) {
            v.mImageView.setImageDrawable(draw);
        }

        //        check whether to open close or leave a card alone
        if (position == expandedPosition) {
            v.mExpandable.setVisibility(View.VISIBLE);
        } else {
            v.mExpandable.setVisibility(View.GONE);
        }

    }

    public void changeVis(int loc) {

        if (expandedPosition == loc) { //if clicking an open view, close it
            expandedPosition = -1;
            notifyItemChanged(loc);
        } else {
            if (expandedPosition > -1) {
                int prev = expandedPosition;
                expandedPosition = -1;
                notifyItemChanged(prev);
            }
            expandedPosition = loc;
            notifyItemChanged(expandedPosition);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return leagueScores.size();
    }

}

