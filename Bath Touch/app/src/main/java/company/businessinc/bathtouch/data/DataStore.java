package company.businessinc.bathtouch.data;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;

import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.LeagueList;
import company.businessinc.endpoints.LeagueListInterface;
import company.businessinc.endpoints.TeamLeagues;
import company.businessinc.endpoints.TeamLeaguesInterface;
import company.businessinc.endpoints.TeamList;
import company.businessinc.endpoints.TeamListInterface;

/**
 * Created by Louis on 29/11/2014.
 */
public class DataStore implements TeamListInterface, TeamLeaguesInterface, LeagueListInterface{

    private static DataStore sInstance;
    private Context context;

    private static final String TAG = "DataStore";
    private User user;
    private Match nextRefMatch;

    public LinkedList<String> userTables;


    public static DataStore getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DataStore(context);
        }
        return sInstance;
    }

    private DataStore(Context context) {
        this.context = context;
        this.user = new User();
    }

    public static void newInstance(Context context) {
        sInstance = new DataStore(context);
    }

    public void setUser(User user){
        this.user = user;
    }

    public boolean isUserLoggedIn(){
        return user.isLoggedIn();
    }

    public String getUserName(){
        return user.getName();
    }

    public String getUserTeam(){
        return user.getTeamName();
    }

    public int getUserTeamID(){
        return user.getTeamID();
    }

    public String userToJSON(){
        return user.toString();
    }

    public void loadAllTeams(){
        //check if table exists
       if(isTableEmpty(DBProviderContract.ALLTEAMS_TABLE_NAME)) {
            //if it doesn't then call the API
            new TeamList(this).execute();
       }
    }

    public void loadLeaguesTeams(int leagueID){

    }

    public void teamListCallback(List<Team> data, TeamList.CallType callType){
        if(data != null){
            switch(callType){
                case GETALLLTEAMS:
                    LinkedList<ContentValues> cV = new LinkedList<>();
                    List<Integer> teamIdsAlreadyAdded = new LinkedList<>();
                    for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                        if(!teamIdsAlreadyAdded.contains(data.get(i).getTeamID())) {
                            //only add unique teams
                            //this is due to TeamList returning duplicates of teams that are in mote than one league
                            //when getting all the teams from the endpoitns
                            cV.add(data.get(i).toContentValues());
                            teamIdsAlreadyAdded.add(data.get(i).getTeamID());
                        }
                    }
                    ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
                    context.getContentResolver().bulkInsert(DBProviderContract.ALLTEAMS_TABLE_CONTENTURI,contentValues);
                    break;
                case GETLEAGUETEAMS:
                    break;
            }
        }
    }

    public void loadMyLeagues(){
        //check if table exists
        if(isTableEmpty(DBProviderContract.MYLEAGUES_TABLE_NAME)) {
            //if it doesn't then call the API
            new TeamLeagues(this, user.getTeamID()).execute();
        }
    }

    public void teamLeaguesCallback(List<League> data){
        if(data!=null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                cV.add(data.get(i).toContentValues());
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.MYLEAGUES_TABLE_CONTENTURI,contentValues);
        }
    }

    public void loadAllLeagues(){
        //check if table exists
        if(isTableEmpty(DBProviderContract.ALLLEAGUES_TABLE_NAME)) {
            //if it doesn't then call the API
            new LeagueList(this).execute();
        }
    }

    public void leagueListCallback(List<League> data){
        if(data!=null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                cV.add(data.get(i).toContentValues());
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.ALLLEAGUES_TABLE_CONTENTURI,contentValues);
        }
    }

    public void loadLeagueScores(int leagueID){

    }

    public void loadLeagueFutureFixtures(int leagueID){

    }

    public void loadLeagueStandings(int leagueID){

    }

    public void loadTeamsFutureFixtures(int teamID, int leagueID){

    }

    public void loadTeamsLeagueScore(int teamID, int leagueID){

    }

    public void setNextRefMatch(Match match) {
        nextRefMatch = match;
    }

    public Match getNextRefMatch() {
        return nextRefMatch;
    }

    private boolean isTableEmpty(String tableName){
        ContentProviderClient client =  context.getContentResolver().acquireContentProviderClient(DBProviderContract.AUTHORITY);
        return ((DBProvider)client.getLocalContentProvider()).isTableEmpty(tableName);
    }

    public void clearUserData() {
        nextRefMatch = null;
        user = new User();
        dropUserTables();
    }

    private void dropUserTables(){
        ContentProviderClient client =  context.getContentResolver().acquireContentProviderClient(DBProviderContract.AUTHORITY);
        ((DBProvider)client.getLocalContentProvider()).dropUserData(userTables);
    }

}
