package company.businessinc.bathtouch.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import company.businessinc.bathtouch.SubmitScoreActivity;
import company.businessinc.dataModels.*;
import company.businessinc.endpoints.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Louis on 29/11/2014.
 */
public class DataStore implements TeamListInterface, TeamLeaguesInterface, LeagueListInterface, RefGamesInterface, LeagueViewInterface, LeagueScheduleInterface,
        LeagueScoresInterface, TeamScoresInterface, TeamScheduleInterface, UpAvailabilityInterface, TeamAvailabilityInterface, ScoreSubmitInterface,
        RequestMessagesInterface, LeagueTodayInterface, LeagueHistoricInterface, TeamHistoricInterface{

    private static DataStore sInstance;
    private Context context;

    private static final String TAG = "DataStore";
    private User user;

    //Flags that store which API calls have already been called
    private boolean loadedAllTeams = false;
    private boolean loadedMyLeagues = false;
    private boolean loadedAllLeagues = false;
    private boolean loadedLiveLeagues = false;
    private boolean loadedMessages = false;
    private ArrayList<Integer> loadedLeagueScores = new ArrayList<>();
    private ArrayList<Integer> loadedLeagueFixtures = new ArrayList<>();
    private ArrayList<Integer> loadedLeagueStandings = new ArrayList<>();
    private ArrayList<Integer> loadedLeagueHistoric = new ArrayList<>();
    private HashMap<Integer, ArrayList<Integer>> loadedTeamsFixtures = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> loadedTeamsHistoric = new HashMap<>();
    private HashMap<Integer, ArrayList<Integer>> loadedTeamsScore = new HashMap<>();
    private boolean loadedRefGames = false;
    private ArrayList<Integer> loadedLeagueTeams = new ArrayList<>();
    private ArrayList<Integer> myMatchesAvailability = new ArrayList<>();
    private ArrayList<Integer> matchesAvailability = new ArrayList<>();
    private ArrayList<Integer> loadedLeagueToday = new ArrayList<>();

    //Data observers for every league
    private List<DBObserver> AllTeamsDBObservers = new ArrayList<>();
    private List<DBObserver> MessagesDBObservers = new ArrayList<>();
    private List<DBObserver> MyLeaguesDBObservers = new ArrayList<>();
    private List<DBObserver> AllLeagueDBObservers = new ArrayList<>();
    private List<DBObserver> LiveLeagueDBObservers = new ArrayList<>();
    private List<DBObserver> LeagueScoreDBObservers = new ArrayList<>();
    private List<DBObserver> LeaguesFixturesDBObservers = new ArrayList<>();
    private List<DBObserver> LeaguesStandingsDBObservers = new ArrayList<>();
    private List<DBObserver> TeamsFixturesDBObservers = new ArrayList<>();
    private List<DBObserver> TeamsScoresDBObservers = new ArrayList<>();
    private List<DBObserver> MyUpcomingGamesDBObservers = new ArrayList<>();
    private List<DBObserver> MyUpcomingRefereeDBObservers = new ArrayList<>();
    private List<DBObserver> LeagueTeamsDBObservers = new ArrayList<>();
    private List<DBObserver> MyUpcomingGameAvailabilityDBObservers = new ArrayList<>();
    private List<DBObserver> MyTeamsPlayerAvailabilityDBObservers = new ArrayList<>();
    private List<DBObserver> LeagueTodayDBObservers = new ArrayList<>();
    private List<DBObserver> LeagueHistoricDBObservers = new ArrayList<>();
    private List<DBObserver> TeamHistoricDBObservers = new ArrayList<>();

    public static synchronized DataStore getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new DataStore(context);
        }
        return sInstance;
    }

    protected DataStore(Context context) {
        this.context = context;
        AllTeamsDBObservers = new ArrayList<>();
        MyLeaguesDBObservers = new ArrayList<>();
        AllLeagueDBObservers = new ArrayList<>();
        LeagueScoreDBObservers = new ArrayList<>();
        LeaguesFixturesDBObservers = new ArrayList<>();
        LeaguesStandingsDBObservers = new ArrayList<>();
        TeamsFixturesDBObservers = new ArrayList<>();
        TeamsScoresDBObservers = new ArrayList<>();
        MyUpcomingGamesDBObservers = new ArrayList<>();
        MyUpcomingRefereeDBObservers = new ArrayList<>();
        LeagueTeamsDBObservers = new ArrayList<>();
        MyUpcomingGameAvailabilityDBObservers = new ArrayList<>();
        MyTeamsPlayerAvailabilityDBObservers = new ArrayList<>();
        LeagueTodayDBObservers = new ArrayList<>();
        clearUserData();
    }

    public synchronized static void newInstance(Context context) {
        sInstance = new DataStore(context);
    }



    public synchronized void setUser(User user){
        this.user = user;
        loadAllLeagues();
        if(isUserLoggedIn()){
           loadMyLeagues();
        }
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

    public String getUserEmail(){
        return user.getEmail();
    }

    public Boolean isUserMale(){
        return user.isMale();
    }

    public int getUserTeamCaptainID() {
        return user.getCaptainID();
    }

    public String getUserTeamCaptainName() {
        return user.getCaptainName();
    }

    public int getUserTeamColorPrimary() {
        return Color.parseColor(user.getTeamColorPrimary());
    }

    public int getUserTeamColorSecondary() {
        return Color.parseColor(user.getTeamColorSecondary());
    }

    public boolean isUserCaptain(){
        return user.isCaptain();
    }

    public boolean isReferee(){
        return user.isReferee();
    }

    public String userToJSON(){
        return user.toString();
    }

    public Boolean getNotifications() {
        return user.getNotifications();
    }

    public List<Team> getAllTeams(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.ALLTEAMS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Team> output = Team.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Team> getLeagueTeams(int leagueID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUETEAMS_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEID,
                new String[]{Integer.toString(leagueID)},
                null,
                null,
                null,
                null);

        List<Team> output = Team.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public Team getTeam(int leagueID, int teamID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUETEAMS_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                new String[]{Integer.toString(leagueID), Integer.toString(teamID)},
                null,
                null,
                null,
                null);

        List<Team> output = Team.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.size() > 0 ? output.get(0) : null;
    }

    /**
     * Get team of the current logged in user
     */
    public Team getTeam(int leagueID){
        return getTeam(leagueID, DataStore.getInstance(context).getUserTeamID());
    }

    public Team getTeamFromAllTeams(int teamID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.ALLTEAMS_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_TEAMID,
                new String[]{Integer.toString(teamID)},
                null,
                null,
                null,
                null);

        List<Team> output = Team.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.size()>0? output.get(0) : null;
    }

    public List<League> getMyLeagues(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYLEAGUES_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        List<League> output = League.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public League getLeague(int leagueID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.ALLLEAGUES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEID,
                new String[]{Integer.toString(leagueID)},
                null,
                null,
                null,
                null);

        List<League> output = League.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.size() > 0 ? output.get(0) : null;
    }

    public String getLeagueName(int leagueID){
        League league = getLeague(leagueID);
        return league != null ? league.getLeagueName() : null;
    }

    public List<League> getAllLeagues(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.ALLLEAGUES_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        List<League> output = League.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }



    public List<League> getAllLiveLeagues(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LIVELEAGUE_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        List<League> output = League.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }
    public League getLiveLeague(int leagueID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LIVELEAGUE_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEID,
                new String[]{Integer.toString(leagueID)},
                null,
                null,
                null,
                null);

        List<League> output = League.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.size() > 0 ? output.get(0) : null;
    }

    public String getLiveLeagueName(int leagueID){
        League league = getLiveLeague(leagueID);
        return league != null ? league.getLeagueName() : null;
    }

    public List<Match> getLeagueScores(int leagueID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUESSCORE_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEID,
                new String[]{Integer.toString(leagueID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), Match.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Match> getLeagueFixtures(int leagueID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUESFIXTURES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEID,
                new String[]{Integer.toString(leagueID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), Match.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<LeagueTeam> getLeagueStandings(int leagueID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEID,
                new String[]{Integer.toString(leagueID)},
                null,
                null,
                null,
                null);

        List<LeagueTeam> output = LeagueTeam.sortByPosition(LeagueTeam.cursorToList(cursor));
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public LeagueTeam getLeagueTeam(int leagueID, int matchID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                new String[]{Integer.toString(leagueID), Integer.toString(matchID)},
                null,
                null,
                null,
                null);

        List<LeagueTeam> output = LeagueTeam.sortByPosition(LeagueTeam.cursorToList(cursor));
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.size() > 0 ? output.get(0) : null;
    }

    public LeagueTeam getLeagueTeam(int leagueID){
        return getLeagueTeam(leagueID, getUserTeamID());
    }

    public List<Match> getTeamFixtures(int leagueID, int teamID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.TEAMSFIXTURES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                new String[]{Integer.toString(leagueID), Integer.toString(teamID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), Match.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Match> getTeamScores(int leagueID, int teamID, Match.SortType sortType){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.TEAMSSCORES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                new String[]{Integer.toString(leagueID), Integer.toString(teamID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), sortType);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Match> getMyUpcomingGames(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), Match.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public Match getNextGame(){
        List<Match> matchList = getMyUpcomingGames();
        return matchList.size() > 0 ? matchList.get(0) : null;
    }

    public List<Match> getMyUpcomingRefereeGames(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_SCORES_ARE_NULL,
                null,
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), Match.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Match> getMyPastRefereeGames(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_SCORES_ARE_NOT_NULL,
                null,
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), Match.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public Match getNextRefGame(){
        List<Match> matchList = Match.sortList(getMyUpcomingRefereeGames(), Match.SortType.ASCENDING);
        return matchList.size() > 0 ? matchList.get(0) : null;
    }

    public boolean amIRefing(int matchID) {
        boolean output = false;
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_MATCHID,
                new String[]{Integer.toString(matchID)},
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            output = cursor.getCount() > 0;
        }
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public boolean amIPlaying(int matchID){
        boolean output = false;
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_MATCHID,
                new String[]{Integer.toString(matchID)},
                null,
                null,
                null,
                null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                output = cursor.getInt(1) == 1;
                cursor.moveToNext();
            }
        }
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Player> getPlayersAvailability(int matchID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_MATCHID,
                new String[]{Integer.toString(matchID)},
                null,
                null,
                null,
                null);

        List<Player> output = Player.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Player> getPlayersAvailability(int matchID, boolean arePlaying){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_MATCHIDANDISPLAYING,
                new String[]{Integer.toString(matchID), arePlaying ? "1" : "0"},
                null,
                null,
                null,
                null);

        List<Player> output = Player.sortByName(Player.cursorToList(cursor));
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public Match getPastLeagueMatch(int matchID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUESSCORE_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_MATCHID,
                new String[]{Integer.toString(matchID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.size() > 0? output.get(0):null;
    }

    public Match getFutureLeagueMatch(int matchID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUESFIXTURES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_MATCHID,
                new String[]{Integer.toString(matchID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.size() > 0? output.get(0):null;
    }

    public List<Message> getPlayerRequests(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MESSAGES_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Message> output = Message.sortList(Message.cursorToList(cursor), Message.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Match> getTodaysMatches(int leagueID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUETODAY_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEID,
                new String[]{Integer.toString(leagueID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.sortList(Match.cursorToList(cursor), Match.SortType.ASCENDING);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public Match getTodaysMatch(int leagueID, int matchID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.LEAGUETODAY_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEIDANDMATCHID,
                new String[]{Integer.toString(leagueID), Integer.toString(matchID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output.isEmpty() ? null : output.get(0);
    }

    /**
     * API Calls and thier callbacks
     */

    public void loadAllTeams(){
        if(!loadedAllTeams) {
            new TeamList(this, context).execute();
            loadedAllTeams = true;
        }
    }


    public void loadLeaguesTeams(int leagueID){
        if(!loadedLeagueTeams.contains(leagueID)){
            new TeamList(this, leagueID, context).execute();
            loadedLeagueTeams.add(leagueID);
        }
    }

    public void teamListCallback(ResponseStatus successful, TeamList.CallType callType, int leagueID){
        if(successful.getStatus()){
            Log.d(TAG, "The call TeamList was successful");
            switch (callType){
                case GETLEAGUETEAMS:
                    notifyLeagueTeamsDBObservers(leagueID);
                    break;
                case GETALLTEAMS:
                    notifyAllTeamsDBObservers(null);
                    break;
            }
        } else {
            Log.d(TAG, "The call TeamList was not successful");
        }
    }

    public void loadMyLeagues(){
        if(!loadedMyLeagues && user.getTeamID()!=null){ //check that user has a team
            new TeamLeagues(this, user.getTeamID(), context).execute();
            loadedMyLeagues = true;
        }
    }

    public void teamLeaguesCallback(ResponseStatus responseStatus){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call TeamLeagues was successful, notify my DBObservers");
            notifyMyLeaguesDBObservers(null);
        } else{
            Log.d(TAG, "The call TeamLeagues was not successful");
        }
    }

    public void loadAllLeagues(){
        if(!loadedAllLeagues){
            new LeagueList(this, context).execute();
            loadedAllLeagues = true;
        }
    }

    public void loadLiveLeagues(){
        if(!loadedLiveLeagues){
            new LeagueList(this, context, true).execute();
            loadedLiveLeagues = true;
        }
    }

    public void leagueListCallback(ResponseStatus responseStatus, boolean liveCall){
        if(responseStatus.getStatus()){
            if(liveCall){
                Log.d(TAG, "The call LeagueList Live was successful, notify my DBObservers");
                notifyLiveLeagueDBObservers(null);
            } else {
                Log.d(TAG, "The call LeagueList was successful, notify my DBObservers");
                notifyAllLeagueDBObservers(null);
            }
        } else{
            Log.d(TAG, "The call LeagueList was not successful");
        }
    }

    public void loadLeagueScores(int leagueID){
        if(!loadedLeagueScores.contains(leagueID)) {
            new LeagueScores(this, context, leagueID).execute();
            loadedLeagueScores.add(leagueID);
        }
    }

    public void leagueScoresCallback(ResponseStatus responseStatus, int leagueID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was successful, notify my DBObservers");
            notifyLeagueScoreDBObservers(leagueID);
        } else{
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was not successful");
        }
    }

    public void loadLeagueFixtures(int leagueID){
        if(!loadedLeagueFixtures.contains(leagueID)) {
            new LeagueSchedule(this, context, leagueID).execute();
            loadedLeagueFixtures.add(leagueID);
        }
    }

    public void leagueScheduleCallback(ResponseStatus responseStatus, int leagueID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was successful, notify my DBObservers");
            notifyLeaguesFixturesDBObservers(leagueID);
        } else{
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was not successful");
        }
    }

    public void loadLeagueStandings(int leagueID){
        if(!loadedLeagueStandings.contains(leagueID)) {
            new LeagueView(this, context, leagueID).execute();
            loadedLeagueStandings.add(leagueID);
        }
    }

    public void leagueViewCallback(ResponseStatus responseStatus, int leagueID) {
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueStandings for leagueID " + leagueID + " was successful, notify my DBObservers");
            notifyLeaguesStandingsDBObservers(leagueID);
        } else{
            Log.d(TAG, "The call LeagueStandings for leagueID " + leagueID + " was not successful");
        }
    }

    public void loadTeamsFixtures(int leagueID, int teamID){
        ArrayList<Integer> integerArrayList = loadedTeamsFixtures.get(leagueID);
        if(integerArrayList == null){
            integerArrayList = new ArrayList<>();
            loadedTeamsFixtures.put(leagueID, integerArrayList);
        }
        if(!loadedTeamsFixtures.get(leagueID).contains(teamID)){
            new TeamSchedule(this,context,leagueID,teamID).execute();
            loadedTeamsFixtures.get(leagueID).add(teamID);
        }
    }

    public void teamScheduleCallback(ResponseStatus responseStatus, int leagueID, int teamID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call TeamSchedule for leagueID " + leagueID + " for teamID "+ teamID+" was successful, notify my DBObservers");
            notifyTeamsFixturesDBObservers(new Tuple<>(leagueID, teamID));
            notifyMyUpcomingGamesDBObservers(null);
        } else{
            Log.d(TAG, "The call TeamSchedule for leagueID " + leagueID + " for teamID "+ teamID+" was not successful");
        }
    }

    public void loadTeamsLeagueScore(int leagueID, int teamID){
        ArrayList<Integer> integerArrayList = loadedTeamsScore.get(leagueID);
        if(integerArrayList == null){
            integerArrayList = new ArrayList<>();
            loadedTeamsScore.put(leagueID, integerArrayList);
        }
        if(!loadedTeamsScore.get(leagueID).contains(teamID)){
            new TeamScores(this,context,leagueID,teamID).execute();
            loadedTeamsScore.get(leagueID).add(teamID);
        }
    }

    public void teamScoresCallback(ResponseStatus responseStatus, int leagueID, int teamID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call TeamScores for leagueID " + leagueID + " for teamID "+ teamID+" was successful, notify my DBObservers");
            notifyTeamsScoresDBObservers(new Tuple<>(leagueID, teamID));
            notifyLeagueScoreDBObservers(leagueID);
        } else{
            Log.d(TAG, "The call TeamScores for leagueID " + leagueID + " for teamID "+ teamID+" was not successful");
        }
    }

    public void loadMyUpcomingGames(){

    }

    public void loadMyUpcomingRefGames(){
        if(!loadedRefGames) {
            new RefGames(this, context).execute();
            loadedRefGames = true;
        }
    }

    public void refGamesCallback(ResponseStatus responseStatus){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call RefGames was successful, notify my DBObservers");
            notifyMyUpcomingRefereeDBObservers(null);
        } else{
            Log.d(TAG, "The call RefGames was not successful");
        }
    }

    public void loadMessages(){
        if(!loadedMessages) {
            new RequestMessages(this, context).execute();
            loadedMessages = true;
        }
    }

    public void requestMessagesCallback(ResponseStatus responseStatus){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call RefGames was successful, notify my DBObservers");
            notifyMessagesDBObservers(null);
        } else{
            Log.d(TAG, "The call RefGames was not successful");
        }
    }

    public void loadMyAvailability(int matchID){
        if(!myMatchesAvailability.contains(matchID)){
            new UpAvailability(this,context, matchID).execute();
            myMatchesAvailability.add(matchID);
        }
    }

    public void setMyAvailability(boolean isPlaying, int matchID, int userID){
        if(!myMatchesAvailability.contains(matchID)){
            myMatchesAvailability.add(matchID);
        }
        new UpAvailability(this, context, isPlaying ? 1 : 0, matchID, userID, UpAvailability.CallType.SETMYAVAILABILITY).execute();
    }

    public void setPlayersAvailability(boolean isPlaying, int userID, int matchID){
        new UpAvailability(this, context, isPlaying ? 1 : 0, matchID, userID, UpAvailability.CallType.SETPLAYERSAVAILABILITY).execute();
    }

    public void upAvailabilityCallback(ResponseStatus responseStatus, boolean isPlaying, UpAvailability.CallType callType, int matchID, int userID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call UpAvailability was successful, notify my DBObservers");
            switch(callType){
                case GETMYAVAILABILITY:
                    notifyMyUpcomingGameAvailabilitysDBObservers(matchID);
                    break;
                case SETMYAVAILABILITY:
                    notifyMyUpcomingGameAvailabilitysDBObservers(matchID);
                    break;
                case SETPLAYERSAVAILABILITY:
                    notifyMyTeamsPlayerAvailabilitysDBObservers(matchID);
                    break;
            }
        } else{
            Log.d(TAG, "The call UpAvailability was not successful");
        }
    }

    public void loadMatchPlayersAvailability(int matchID){
        if(!matchesAvailability.contains(matchID)){
            new TeamAvailability(this, context, matchID).execute();
            matchesAvailability.add(matchID);
        }
    }

    public void teamAvailabilityCallback(ResponseStatus responseStatus, int matchID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call teamAvailability for matchID " + matchID + " was successful, notify my DBObservers");
            notifyMyTeamsPlayerAvailabilitysDBObservers(matchID);
        } else{
            Log.d(TAG, "The call teamAvailability for matchID " + matchID + " was not successful");
        }
    }

    public void loadLeagueToday(int leagueID){
        if(!loadedLeagueToday.contains(leagueID)) {
            new LeagueToday(this, context, leagueID).execute();
            loadedLeagueToday.add(leagueID);
        }
    }

    public void leagueTodayCallback(ResponseStatus responseStatus, int leagueID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueToday for leagueID " + leagueID + " was successful, notify my DBObservers");
            notifyLeagueTodayDBObservers(leagueID);
        } else{
            Log.d(TAG, "The call LeagueToday for leagueID " + leagueID + " was not successful");
        }
    }

    public void loadLeagueHistoric(int leagueID){
        if(!loadedLeagueHistoric.contains(leagueID)){
            new LeagueHistoric(this, context, leagueID).execute();
            loadedLeagueHistoric.add(leagueID);
        }
    }

    @Override
    public void leagueHistoricCallback(ResponseStatus responseStatus, int leagueID) {
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call leagueHistoric for leagueID " + leagueID + " was successful, notify my DBObservers");
            notifyLeagueHistoricDBObservers(leagueID);
        } else{
            Log.d(TAG, "The call leagueHistoric for leagueID " + leagueID + " was not successful");
        }
    }

    public void loadTeamHistoric(int leagueID, int teamID){
        ArrayList<Integer> integerArrayList = loadedTeamsHistoric.get(leagueID);
        if(integerArrayList == null){
            integerArrayList = new ArrayList<>();
            loadedTeamsHistoric.put(leagueID, integerArrayList);
        }
        if(!loadedTeamsHistoric.get(leagueID).contains(teamID)){
            new TeamHistoric(this, context, leagueID, teamID).execute();
            loadedTeamsHistoric.get(leagueID).add(teamID);
        }
    }

    @Override
    public void teamHistoricCallback(ResponseStatus responseStatus, int leagueID, int teamID) {
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call TeamHistoric for leagueID " + leagueID + " for teamID "+ teamID+" was successful, notify my DBObservers");
            notifyTeamHistoricDBObservers(new Tuple<>(leagueID, teamID));
        } else{
            Log.d(TAG, "The call TeamHistoric for leagueID " + leagueID + " for teamID "+ teamID+" was not successful");
        }
    }

    public synchronized void clearUserData() {
        setUser(new User());
        dropUserTables();
        loadedAllTeams = false;
        loadedMyLeagues = false;
        loadedAllLeagues = false;
        loadedLiveLeagues = false;
        loadedMessages = false;
        loadedLeagueScores = new ArrayList<>();
        loadedLeagueFixtures = new ArrayList<>();
        loadedLeagueStandings = new ArrayList<>();
        loadedLeagueHistoric = new ArrayList<>();
        loadedTeamsFixtures = new HashMap<>();
        loadedTeamsScore = new HashMap<>();
        loadedTeamsHistoric = new HashMap<>();
        loadedRefGames = false;
        myMatchesAvailability = new ArrayList<>();
        matchesAvailability = new ArrayList<>();
        loadedLeagueTeams = new ArrayList<>();
        loadedLeagueToday = new ArrayList<>();
    }

    public synchronized void dropUserTables(){
        String[] userTables = new String[]{DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME, DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME, DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME, DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME, DBProviderContract.MESSAGES_TABLE_NAME, DBProviderContract.LEAGUETODAY_TABLE_NAME, DBProviderContract.TEAMHISTORIC_TABLE_NAME};
        String[] createUserTables = new String[]{DBProviderContract.CREATE_MYUPCOMINGGAMES_TABLE, DBProviderContract.CREATE_MYUPCOMINGREFEREEGAMES_TABLE, DBProviderContract.CREATE_MYTEAMPLAYERSAVAILABILITY_TABLE, DBProviderContract.CREATE_MYUPCOMINGGAMESAVAILABILITY_TABLE, DBProviderContract.CREATE_MESSAGES_TABLE, DBProviderContract.CREATE_LEAGUE_TODAY, DBProviderContract.CREATE_TEAMHISTORIC_TABLE};
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        for(String t : userTables){
            db.execSQL(DBProviderContract.SQL_DROP_TABLE_IF_EXISTS + " " + t);
        }
        //Recreate them
        for(String t : createUserTables){
            db.execSQL(t);
        }
        SQLiteManager.getInstance(context).closeDatabase();
    }

    public synchronized void refreshData() {
        dropUserTables();
        loadedAllTeams = false;
        loadedMyLeagues = false;
        loadedAllLeagues = false;
        loadedLiveLeagues = false;
        loadedMessages = false;
        loadedLeagueScores = new ArrayList<>();
        loadedLeagueFixtures = new ArrayList<>();
        loadedLeagueStandings = new ArrayList<>();
        loadedLeagueHistoric = new ArrayList<>();
        loadedTeamsFixtures = new HashMap<>();
        loadedTeamsScore = new HashMap<>();
        loadedTeamsHistoric = new HashMap<>();
        loadedRefGames = false;
        myMatchesAvailability = new ArrayList<>();
        matchesAvailability = new ArrayList<>();
        loadedLeagueTeams = new ArrayList<>();
        loadedLeagueToday = new ArrayList<>();
        loadAllLeagues();
        if(isUserLoggedIn()){
            loadMyLeagues();
        }
        notifyAllTeamsDBObservers(null);
        notifyMyLeaguesDBObservers(null);
        notifyAllLeagueDBObservers(null);
        notifyLiveLeagueDBObservers(null);
        notifyLeagueScoreDBObservers(null);
        notifyLeaguesFixturesDBObservers(null);
        notifyLeaguesStandingsDBObservers(null);
        notifyTeamsFixturesDBObservers(null);
        notifyTeamsScoresDBObservers(null);
        notifyMyUpcomingGamesDBObservers(null);
        notifyMyUpcomingRefereeDBObservers(null);
        notifyLeagueTeamsDBObservers(null);
        notifyMyUpcomingGameAvailabilitysDBObservers(null);
        notifyMyTeamsPlayerAvailabilitysDBObservers(null);
        notifyMessagesDBObservers(null);
        notifyLeagueTodayDBObservers(null);
    }

    public synchronized void refreshMatchAvailabilities(){
        dropAvailability();
    }

    public void dropAllTables(){
        //first drop the tables that always exist
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        for(String t : DBProviderContract.TABLES){
            db.execSQL(DBProviderContract.SQL_DROP_TABLE_IF_EXISTS + " " + t);
        }
        //Recreate them
        for(String t : DBProviderContract.CREATE_TABLES){
            db.execSQL(t);
        }
        SQLiteManager.getInstance(context).closeDatabase();
    }

    public void refreshRequests(){
        loadedMessages = false;
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        db.execSQL(DBProviderContract.SQL_DROP_TABLE_IF_EXISTS + " " + DBProviderContract.MESSAGES_TABLE_NAME);
        db.execSQL(DBProviderContract.CREATE_MESSAGES_TABLE);
        SQLiteManager.getInstance(context).closeDatabase();
        notifyMessagesDBObservers(null);
    }

    public void refreshRefGames(){
        loadedRefGames = false;
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        db.execSQL(DBProviderContract.SQL_DROP_TABLE_IF_EXISTS + " " + DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME);
        db.execSQL(DBProviderContract.CREATE_MYUPCOMINGREFEREEGAMES_TABLE);
        SQLiteManager.getInstance(context).closeDatabase();
        notifyMyUpcomingRefereeDBObservers(null);
    }

    private void dropAvailability(){
        matchesAvailability = new ArrayList<>();
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        db.execSQL(DBProviderContract.SQL_DROP_TABLE_IF_EXISTS + " " + DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME);
        db.execSQL(DBProviderContract.CREATE_MYTEAMPLAYERSAVAILABILITY_TABLE);
        SQLiteManager.getInstance(context).closeDatabase();
        notifyMyTeamsPlayerAvailabilitysDBObservers(null);
    }

    public void internetIsBack(){
        Log.d(TAG, "Internet is back on, check if there are any cached requests");
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.CACHEDREQUESTS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        List<CachedRequest> cachedRequests = CachedRequest.cursorToList(cursor);
        db.execSQL(DBProviderContract.SQL_DROP_TABLE_IF_EXISTS + " " + DBProviderContract.CACHEDREQUESTS_TABLE_NAME);
        db.execSQL(DBProviderContract.CREATE_CACHEDREQUEST_TABLE);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        for(CachedRequest cachedRequest : cachedRequests){
            if(cachedRequest.getRequestType() == CachedRequest.RequestType.SUBMITSCORE){
                JSONObject jsonObject = cachedRequest.getParameters();
                try {
                    new ScoreSubmit(this, context, jsonObject.getInt("matchID"), jsonObject.getInt("teamOneScore"), jsonObject.getInt("teamTwoScore"), jsonObject.getBoolean("isForfeit")).execute();
                } catch (JSONException e){

                }
            }
        }
    }

    @Override
    public void scoreSubmitCallback(ResponseStatus data) {
        if(data.getStatus()) {
            Toast.makeText(context, "Cached score has been submitted", Toast.LENGTH_SHORT).show();
            refreshData();
        }

    }

    public void cacheRequest(CachedRequest cachedRequest){
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        db.insert(DBProviderContract.CACHEDREQUESTS_TABLE_NAME,null,cachedRequest.toContentValues());
        SQLiteManager.getInstance(context).closeDatabase();
    }

    /**
     * DBObservers registration
     */

    public synchronized void registerAllTeamsDBObservers(DBObserver dbObserver) {
        if(!AllTeamsDBObservers.contains(dbObserver)) {
            AllTeamsDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerMyLeaguesDBObserver(DBObserver dbObserver) {
        if(!MyLeaguesDBObservers.contains(dbObserver)) {
            MyLeaguesDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerAllLeagueDBObserver(DBObserver dbObserver) {
        if(!AllLeagueDBObservers.contains(dbObserver)) {
            AllLeagueDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerLiveLeagueDBObserver(DBObserver dbObserver) {
        if(!LiveLeagueDBObservers.contains(dbObserver)) {
            LiveLeagueDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerLeagueScoreDBObserver(DBObserver dbObserver) {
        if(!LeagueScoreDBObservers.contains(dbObserver)) {
            LeagueScoreDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerLeaguesFixturesDBObserver(DBObserver dbObserver) {
        if(!LeaguesFixturesDBObservers.contains(dbObserver)) {
            LeaguesFixturesDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerLeaguesStandingsDBObserver(DBObserver dbObserver) {
        if(!LeaguesStandingsDBObservers.contains(dbObserver)) {
            LeaguesStandingsDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerTeamsFixturesDBObserver(DBObserver dbObserver) {
        if(!TeamsFixturesDBObservers.contains(dbObserver)) {
            TeamsFixturesDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerTeamsScoresDBObserver(DBObserver dbObserver) {
        if(!TeamsScoresDBObservers.contains(dbObserver)) {
            TeamsScoresDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerMyUpcomingGamesDBObserver(DBObserver dbObserver) {
        if(!MyUpcomingGamesDBObservers.contains(dbObserver)) {
            MyUpcomingGamesDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerMyUpcomingRefereeDBObserver(DBObserver dbObserver) {
        if(!MyUpcomingRefereeDBObservers.contains(dbObserver)) {
            MyUpcomingRefereeDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerLeagueTeamsDBObserver(DBObserver dbObserver) {
        if(!LeagueTeamsDBObservers.contains(dbObserver)) {
            LeagueTeamsDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerMyUpcomingGameAvailabilitysDBObserver(DBObserver dbObserver) {
        if(!MyUpcomingGameAvailabilityDBObservers.contains(dbObserver)) {
            MyUpcomingGameAvailabilityDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerMyTeamsPlayerAvailabilitysDBObserver(DBObserver dbObserver) {
        if(!MyTeamsPlayerAvailabilityDBObservers.contains(dbObserver)) {
            MyTeamsPlayerAvailabilityDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerMessagesDBObserver(DBObserver dbObserver) {
        if(!MessagesDBObservers.contains(dbObserver)) {
            MessagesDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerLeagueTodayDBObserver(DBObserver dbObserver) {
        if(!LeagueTodayDBObservers.contains(dbObserver)) {
            LeagueTodayDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerLeagueHistoricDBObserver(DBObserver dbObserver) {
        if(!LeagueHistoricDBObservers.contains(dbObserver)) {
            LeagueHistoricDBObservers.add(dbObserver);
        }
    }

    public synchronized void registerTeamHistoricDBObserver(DBObserver dbObserver) {
        if(!TeamHistoricDBObservers.contains(dbObserver)) {
            TeamHistoricDBObservers.add(dbObserver);
        }
    }

    /**
     * DBObservers unregistration
     */

    public synchronized void unregisterAllTeamsDBObservers(DBObserver dbObserver) {

        AllTeamsDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterMyLeaguesDBObserver(DBObserver dbObserver) {
        MyLeaguesDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterAllLeagueDBObserver(DBObserver dbObserver) {
        AllLeagueDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterLiveLeagueDBObserver(DBObserver dbObserver) {
        LiveLeagueDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterLeagueScoreDBObserver(DBObserver dbObserver) {
        LeagueScoreDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterLeaguesFixturesDBObserver(DBObserver dbObserver) {
        LeaguesFixturesDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterLeaguesStandingsDBObserver(DBObserver dbObserver) {
        LeaguesStandingsDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterTeamsFixturesDBObserver(DBObserver dbObserver) {
        TeamsFixturesDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterTeamsScoresDBObserver(DBObserver dbObserver) {
        TeamsScoresDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterMyUpcomingGamesDBObserver(DBObserver dbObserver) {
        MyUpcomingGamesDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterMyUpcomingRefereeDBObserver(DBObserver dbObserver) {
        MyUpcomingRefereeDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterLeagueTeamsDBObserver(DBObserver dbObserver) {
        LeagueTeamsDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterMyUpcomingGameAvailabilitysDBObserver(DBObserver dbObserver) {
        MyUpcomingGameAvailabilityDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterMyTeamsPlayerAvailabilitysDBObserver(DBObserver dbObserver) {
        MyTeamsPlayerAvailabilityDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterMessagesDBObserver(DBObserver dbObserver) {
        MessagesDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterLeagueTodayDBObserver(DBObserver dbObserver) {
        LeagueTodayDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterLeagueHistoricDBObserver(DBObserver dbObserver) {
        LeagueHistoricDBObservers.remove(dbObserver);
    }

    public synchronized void unregisterTeamHistoricDBObserver(DBObserver dbObserver) {
        TeamHistoricDBObservers.remove(dbObserver);
    }
    /**
     * DBObserver notification
     */
    
    private void notifyDBObservers(List<DBObserver> dbObservers, final String tableName, final Object data){
        for (final DBObserver dbObserver : dbObservers) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                public void run() {
                    dbObserver.notify(tableName, data);
                }
            });
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                }
//            }).start();
        }
    }

    private void notifyAllTeamsDBObservers(final Object data) {
        notifyDBObservers(AllTeamsDBObservers, DBProviderContract.ALLTEAMS_TABLE_NAME, data);
    }

    private void notifyMyLeaguesDBObservers(final Object data) {
        notifyDBObservers(MyLeaguesDBObservers, DBProviderContract.MYLEAGUES_TABLE_NAME, data);
    }

    private void notifyAllLeagueDBObservers(final Object data) {
        notifyDBObservers(AllLeagueDBObservers, DBProviderContract.ALLLEAGUES_TABLE_NAME, data);
    }

    private void notifyLiveLeagueDBObservers(final Object data) {
        notifyDBObservers(LiveLeagueDBObservers, DBProviderContract.LIVELEAGUE_TABLE_NAME, data);
    }

    private void notifyLeagueScoreDBObservers(final Object data) {
        notifyDBObservers(LeagueScoreDBObservers, DBProviderContract.LEAGUESSCORE_TABLE_NAME, data);
    }

    private void notifyLeaguesFixturesDBObservers(final Object data) {
        notifyDBObservers(LeaguesFixturesDBObservers, DBProviderContract.LEAGUESFIXTURES_TABLE_NAME, data);
    }

    private void notifyLeaguesStandingsDBObservers(final Object data) {
        notifyDBObservers(LeaguesStandingsDBObservers, DBProviderContract.LEAGUESSTANDINGS_TABLE_NAME, data);
    }

    private void notifyTeamsFixturesDBObservers(final Object data) {
        notifyDBObservers(TeamsFixturesDBObservers, DBProviderContract.TEAMSFIXTURES_TABLE_NAME, data);
    }

    private void notifyTeamsScoresDBObservers(final Object data) {
        notifyDBObservers(TeamsScoresDBObservers, DBProviderContract.TEAMSSCORES_TABLE_NAME, data);
    }

    private void notifyMyUpcomingGamesDBObservers(final Object data) {
        notifyDBObservers(MyUpcomingGamesDBObservers, DBProviderContract.MYUPCOMINGGAMES_TABLE_NAME, data);
    }

    private void notifyMyUpcomingRefereeDBObservers(final Object data) {
        notifyDBObservers(MyUpcomingRefereeDBObservers, DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME, data);
    }

    private void notifyLeagueTeamsDBObservers(final Object data) {
        notifyDBObservers(LeagueTeamsDBObservers, DBProviderContract.LEAGUETEAMS_TABLE_NAME, data);
    }

    private void notifyMyUpcomingGameAvailabilitysDBObservers(final Object data) {
        notifyDBObservers(MyUpcomingGameAvailabilityDBObservers, DBProviderContract.MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME, data);
    }

    private void notifyMyTeamsPlayerAvailabilitysDBObservers(final Object data) {
        notifyDBObservers(MyTeamsPlayerAvailabilityDBObservers, DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME, data);
    }

    private void notifyMessagesDBObservers(final Object data) {
        notifyDBObservers(MessagesDBObservers, DBProviderContract.MESSAGES_TABLE_NAME, data);
    }

    private void notifyLeagueTodayDBObservers(final Object data) {
        notifyDBObservers(LeagueTodayDBObservers, DBProviderContract.LEAGUETODAY_TABLE_NAME, data);
    }

    private void notifyLeagueHistoricDBObservers(final Object data) {
        notifyDBObservers(LeagueHistoricDBObservers, DBProviderContract.LEAGUEHISTORIC_TABLE_NAME, data);
    }

    private void notifyTeamHistoricDBObservers(final Object data) {
        notifyDBObservers(TeamHistoricDBObservers, DBProviderContract.TEAMHISTORIC_TABLE_NAME, data);
    }
}
