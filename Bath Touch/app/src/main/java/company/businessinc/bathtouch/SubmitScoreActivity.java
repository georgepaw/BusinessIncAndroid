package company.businessinc.bathtouch;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.ResponseStatus;
import company.businessinc.endpoints.ScoreSubmit;
import company.businessinc.endpoints.ScoreSubmitInterface;


public class SubmitScoreActivity extends ActionBarActivity implements ScoreSubmitInterface {

    private String TAG = "SubmitScoreActivity";
    private Integer mMatchId;
    private TextView mTeamOneName, mTeamTwoName;
    private EditText mTeamOneEditText, mTeamTwoEditText;
    private CheckBox mTeamOneForfeit, mTeamTwoForfeit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_score);
        String teamOneNameText = "NULL";
        String teamTwoNameText = "NULL";

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

        mTeamOneName.setText(teamOneNameText);
        mTeamTwoName.setText(teamTwoNameText);
    }

    public void onSubmitScore(View v) {
        Integer mTeamOneScore = Integer.valueOf(mTeamOneEditText.getText().toString());
        Integer mTeamTwoScore = Integer.valueOf(mTeamTwoEditText.getText().toString());
        Boolean mIsForfeit;
        if(mTeamOneForfeit.isChecked() || mTeamTwoForfeit.isChecked())
            mIsForfeit = true;
        else mIsForfeit = false;

        new ScoreSubmit(this, mMatchId, mTeamOneScore, mTeamTwoScore, mIsForfeit).execute();
    }

    @Override
    public void scoreSubmitCallback(ResponseStatus data) {
        if(data != null){
            if(data.getStatus()) {
                Toast.makeText(this, "Score submitted successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Score could not be submitted", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Score could not be submitted", Toast.LENGTH_LONG).show();
        }
    }
}
