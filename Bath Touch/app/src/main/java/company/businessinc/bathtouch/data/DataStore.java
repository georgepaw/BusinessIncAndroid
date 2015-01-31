package company.businessinc.bathtouch.data;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Team;
import company.businessinc.dataModels.User;
import company.businessinc.endpoints.LeagueList;
import company.businessinc.endpoints.LeagueListInterface;
import company.businessinc.endpoints.LeagueSchedule;
import company.businessinc.endpoints.LeagueScheduleInterface;
import company.businessinc.endpoints.LeagueScores;
import company.businessinc.endpoints.LeagueScoresInterface;
import company.businessinc.endpoints.LeagueView;
import company.businessinc.endpoints.LeagueViewInterface;
import company.businessinc.endpoints.RefGames;
import company.businessinc.endpoints.RefGamesInterface;
import company.businessinc.endpoints.TeamLeagues;
import company.businessinc.endpoints.TeamLeaguesInterface;
import company.businessinc.endpoints.TeamList;
import company.businessinc.endpoints.TeamListInterface;
import company.businessinc.endpoints.TeamSchedule;
import company.businessinc.endpoints.TeamScheduleInterface;
import company.businessinc.endpoints.TeamScores;
import company.businessinc.endpoints.TeamScoresInterface;

/**
 * Created by Louis on 29/11/2014.
 */
public class DataStore implements TeamListInterface, TeamLeaguesInterface, LeagueListInterface, RefGamesInterface, LeagueViewInterface, LeagueScheduleInterface,
        LeagueScoresInterface, TeamScoresInterface, TeamScheduleInterface{

    private static DataStore sInstance;
    private Context context;

    private static final String TAG = "DataStore";
    private User user;

    //Flags that store which API calls have already been called
    private boolean loadedAllTeams = false;
    private boolean loadedMyLeagues = false;
    private boolean loadedAllLeagues = false;
    private ArrayList<Integer> loadedLeagueScores = new ArrayList<>();
    private ArrayList<Integer> loadedLeagueFixtures = new ArrayList<>();
    private ArrayList<Integer> loadedLeagueStandings = new ArrayList<>();
    private HashMap<Integer, ArrayList<Integer>> loadedTeamsFixtures = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> loadedTeamsScore = new HashMap<>();
    private boolean loadedRefGames = false;

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

    public boolean isUserCaptain(){
        return true;//TODO hardcoded, need to get WEB team to pass us this when user logs in
    }

    public boolean isReferee(){
        return true;//TODO hardcoded, need to get WEB team to pass us this when user logs in
    }

    public String userToJSON(){
        return user.toString();
    }

    public void loadAllTeams(){
        if(!loadedAllTeams) {
            new TeamList(this).execute();
            loadedAllTeams = true;
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
        if(!loadedMyLeagues){
            new TeamLeagues(this, user.getTeamID()).execute();
            loadedMyLeagues = true;
        }
    }

    public void teamLeaguesCallback(List<League> data){
        if(data!=null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                cV.add(data.get(i).toContentValues());
                //for every league, get my scores and fixtures
                context.getContentResolver().query(DBProviderContract.TEAMSFIXTURES_TABLE_CONTENTURI,null, DBProviderContract.SELECTION_LEAGUEIDANDTEAMID, new String[]{Integer.toString(data.get(i).getLeagueID()),Integer.toString(user.getTeamID())},null).close();
                context.getContentResolver().query(DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI, null, DBProviderContract.SELECTION_LEAGUEIDANDTEAMID, new String[]{Integer.toString(data.get(i).getLeagueID()), Integer.toString(user.getTeamID())}, null).close();
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.MYLEAGUES_TABLE_CONTENTURI,contentValues);
        }
    }

    public void loadAllLeagues(){
        if(!loadedAllLeagues){
            new LeagueList(this).execute();
            loadedAllLeagues = false;
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
        if(!loadedLeagueScores.contains(leagueID)) {
            new LeagueScores(this, leagueID).execute();
            loadedLeagueScores.add(leagueID);
        }
    }

    @Override
    public void leagueScoresCallback(List<Match> data, int leagueID){
        if(data != null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                ContentValues dis = data.get(i).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                cV.add(dis);
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.LEAGUESSCORE_TABLE_CONTENTURI, contentValues);
        }
    }

    public void loadLeagueFixtures(int leagueID){
        if(!loadedLeagueFixtures.contains(leagueID)) {
            new LeagueSchedule(this, leagueID).execute();
            loadedLeagueFixtures.add(leagueID);
        }
    }

    @Override
    public void leagueScheduleCallback(List<Match> data, int leagueID){
        if(data != null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                ContentValues dis = data.get(i).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                cV.add(dis);
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.LEAGUESFIXTURES_TABLE_CONTENTURI, contentValues);
        }
    }

    public void loadLeagueStandings(int leagueID){
        if(!loadedLeagueStandings.contains(leagueID)) {
            new LeagueView(this, leagueID).execute();
            loadedLeagueStandings.add(leagueID);
        }
    }
    @Override
    public void leagueViewCallback(List<LeagueTeam> data, int leagueID) {
        if(data != null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                ContentValues dis = data.get(i).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                cV.add(dis);
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.LEAGUESSTANDINGS_TABLE_CONTENTURI, contentValues);
        }
    }

    public void loadTeamsFixtures(int leagueID, int teamID){
        ArrayList<Integer> integerArrayList = loadedTeamsFixtures.get(leagueID);
        if(integerArrayList == null){
            integerArrayList = new ArrayList<>();
            loadedTeamsFixtures.put(leagueID, integerArrayList);
        }
        if(!loadedTeamsFixtures.get(leagueID).contains(teamID)){
            new TeamSchedule(this,leagueID,teamID).execute();
            loadedTeamsFixtures.get(leagueID).add(teamID);
        }
    }

    @Override
    public void teamScheduleCallback(List<Match> data, int leagueID, int teamID){
        if(data != null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                ContentValues dis = data.get(i).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                //check is this is a game that current user would play in, if yes add it to upcoming games
                if(data.get(i).getTeamOneID() == user.getTeamID() || data.get(i).getTeamTwoID() == user.getTeamID()){
                    context.getContentResolver().insert(DBProviderContract.MYUPCOMINGGAMES_TABLE_CONTENTURI, dis);
                }
                dis.put(Team.KEY_TEAMID, teamID);
                cV.add(dis);
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.TEAMSFIXTURES_TABLE_CONTENTURI, contentValues);
        }
    }

    public void loadTeamsLeagueScore(int leagueID, int teamID){
        ArrayList<Integer> integerArrayList = loadedTeamsScore.get(leagueID);
        if(integerArrayList == null){
            integerArrayList = new ArrayList<>();
            loadedTeamsScore.put(leagueID, integerArrayList);
        }
        if(!loadedTeamsScore.get(leagueID).contains(teamID)){
            new TeamScores(this,leagueID,teamID).execute();
            loadedTeamsScore.get(leagueID).add(teamID);
        }
    }

    @Override
    public void teamScoresCallback(List<Match> data, int leagueID, int teamID){
        if(data != null){
            LinkedList<ContentValues> cV = new LinkedList<>();
            for (int i = 0; i < data.size() ; i++){ //insert all of them into the table
                ContentValues dis = data.get(i).toContentValues();
                dis.put(League.KEY_LEAGUEID, leagueID);
                dis.put(Team.KEY_TEAMID, teamID);
                cV.add(dis);
            }
            ContentValues[] contentValues = cV.toArray(new ContentValues[cV.size()]);
            context.getContentResolver().bulkInsert(DBProviderContract.TEAMSSCORES_TABLE_CONTENTURI, contentValues);
        }
    }

    public void loadMyUpcomingGames(){

    }

    public void loadMyUpcomingRefGames(){
        if(!loadedRefGames) {
            new RefGames(this).execute();
            loadedRefGames = true;
        }
    }

    @Override
    public void refGamesCallback(List<Match> data){
        if(data != null){
            //sort the games in ascending order
            data = Match.sortList(data, Match.SortType.ASCENDING);
            //find the next game
            Match nextMatch = null;
            //Should probably replace this with a better search
            GregorianCalendar gc = new GregorianCalendar();
            gc.add(GregorianCalendar.HOUR,-4); //minus 4 hours so that he can see the next game during and after it's being played
            for(Match m : data){
                if(m.getDateTime().compareTo(gc.getTime()) > 0){
                    context.getContentResolver().insert(DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_CONTENTURI, m.toContentValues());
                }
            }
        }
    }

    private boolean isTableEmpty(String tableName){
        ContentProviderClient client =  context.getContentResolver().acquireContentProviderClient(DBProviderContract.AUTHORITY);
        return ((DBProvider)client.getLocalContentProvider()).isTableEmpty(tableName);
    }

    public void clearUserData() {
        user = new User();
        dropUserTables();
        loadedMyLeagues = false;
        loadedRefGames = false;
    }

    private void dropUserTables(){
        ContentProviderClient client =  context.getContentResolver().acquireContentProviderClient(DBProviderContract.AUTHORITY);
        ((DBProvider)client.getLocalContentProvider()).dropUserData();
    }

}
