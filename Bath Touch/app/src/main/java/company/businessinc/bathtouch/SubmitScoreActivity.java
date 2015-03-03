package company.businessinc.bathtouch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import company.businessinc.bathtouch.R;
import company.businessinc.bathtouch.data.DataStore;
import company.businessinc.dataModels.CachedRequest;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.endpoints.ScoreSubmit;
import company.businessinc.endpoints.ScoreSubmitInterface;
import company.businessinc.networking.CheckNetworkConnection;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SubmitScoreActivity extends ActionBarActivity implements ScoreSubmitInterface {

    private String TAG = "SubmitScoreActivity";
    private Integer mMatchId;
    private TextView mTeamOneName, mTeamTwoName;
    private EditText mTeamOneEditText, mTeamTwoEditText;
    private CheckBox mTeamOneForfeit, mTeamTwoForfeit, mTeamOneCaptianApprove, mTeamTwoCaptianApprove;

    private String teamOneNameText = "NULL";
    private String teamTwoNameText = "NULL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_score);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            mMatchId = extras.getInt(Match.KEY_MATCHID);
            teamOneNameText = extras.getString(Match.KEY_TEAMONE);
            teamTwoNameText = extras.getString(Match.KEY_TEAMTWO);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_submit_score);
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mTeamOneName = (TextView) findViewById(R.id.submit_score_team1_name);
        mTeamTwoName = (TextView) findViewById(R.id.submit_score_team2_name);
        mTeamOneEditText = (EditText) findViewById(R.id.submit_score_team1_score);
        mTeamTwoEditText = (EditText) findViewById(R.id.submit_score_team2_score);
        mTeamOneForfeit = (CheckBox) findViewById(R.id.submit_score_team1_forfeit);
        mTeamTwoForfeit = (CheckBox) findViewById(R.id.submit_score_team2_forfeit);
        mTeamOneCaptianApprove = (CheckBox) findViewById(R.id.submit_score_team1_approve);
        mTeamTwoCaptianApprove = (CheckBox) findViewById(R.id.submit_score_team2_approve);

        mTeamOneName.setText(teamOneNameText);
        mTeamTwoName.setText(teamTwoNameText);
    }

    public void onSubmitScore(View v) {
        Integer mTeamOneScore = null;
        Integer mTeamTwoScore = null;
        if(!mTeamOneCaptianApprove.isChecked() || !mTeamTwoCaptianApprove.isChecked()){
            Toast.makeText(this, "Captains need to approve scores!", Toast.LENGTH_SHORT).show();
        } else if(mTeamOneForfeit.isChecked() && mTeamTwoForfeit.isChecked()) {
            Toast.makeText(this, "Both teams can't forfeit.", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, "Enter score for " + teamOneNameText, Toast.LENGTH_SHORT).show();
                    return;
                }

                try{
                    mTeamTwoScore = Integer.valueOf(mTeamTwoEditText.getText().toString());
                } catch(NumberFormatException e){
                    Toast.makeText(this, "Enter score for " + teamTwoNameText, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if(CheckNetworkConnection.check(this)) {
                new ScoreSubmit(this, mMatchId, mTeamOneScore, mTeamTwoScore, mIsForfeit).execute();
            } else {
                Toast.makeText(this, "No network connection, the score will be submitted automatically", Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("matchID", Integer.toString(mMatchId));
                    jsonObject.put("teamOneScore", Integer.toString(mTeamOneScore));
                    jsonObject.put("teamTwoScore", Integer.toString(mTeamTwoScore));
                    jsonObject.put("isForfeit", Boolean.toString(mIsForfeit));
                    DataStore.getInstance(this).cacheRequest(new CachedRequest(CachedRequest.RequestType.SUBMITSCORE, jsonObject));
                } catch (JSONException e){

                }
                finish();
            }
        }
    }

    @Override
    public void scoreSubmitCallback(ResponseStatus data) {
        if(data != null){
            if(data.getStatus()) {
                Toast.makeText(this, "Score submitted successfully", Toast.LENGTH_SHORT).show();
                DataStore.getInstance(getApplicationContext()).refreshData(); //update all scores
                finish();
            } else {
                Toast.makeText(this, "Score could not be submitted", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Score could not be submitted", Toast.LENGTH_LONG).show();
        }
    }
}
