package company.businessinc.bathtouch;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import company.businessinc.dataModels.Status;
import company.businessinc.endpoints.ScoreSubmit;
import company.businessinc.endpoints.ScoreSubmitInterface;


public class SubmitScoreActivity extends ActionBarActivity implements ScoreSubmitInterface {

    private Integer mMatchId, mTeamOneScore, mTeamTwoScore;
    private Boolean mIsForfeit;
    private EditText mTeamOneEditText, mTeamTwoEditText;
    private CheckBox mTeamOneForfeit, mTeamTwoForfeit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_score);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_submit_score);
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));

        mTeamOneEditText = (EditText) findViewById(R.id.submit_score_team1_score);
        mTeamTwoEditText = (EditText) findViewById(R.id.submit_score_team2_score);
        mTeamOneForfeit = (CheckBox) findViewById(R.id.submit_score_team1_forfeit);
        mTeamTwoForfeit = (CheckBox) findViewById(R.id.submit_score_team2_forfeit);

        mMatchId = getMatchId();

    }

    public void onSubmitScore(View v) {
        mTeamOneScore = Integer.valueOf(mTeamOneEditText.getText().toString());
        mTeamTwoScore = Integer.valueOf(mTeamTwoEditText.getText().toString());
        if(mTeamOneForfeit.isChecked() || mTeamTwoForfeit.isChecked())
            mIsForfeit = true;
        else mIsForfeit = false;

        new ScoreSubmit(this, mMatchId, mTeamOneScore, mTeamTwoScore, mIsForfeit).execute();
    }

    private Integer getMatchId() {
        //TODO FETCH THE REAL MATCH ID
        return 0;
    }

    @Override
    public void scoreSubmitCallback(Status data) {

        if(data.getStatus()) {
            Toast.makeText(this, "Score submitted successfully", Toast.LENGTH_SHORT).show();
            recreate();
        } else
            Toast.makeText(this, "Score could not be submitted", Toast.LENGTH_LONG).show();
    }
}
