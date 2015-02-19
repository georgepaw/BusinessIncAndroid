package company.businessinc.bathtouch.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import company.businessinc.dataModels.*;
import company.businessinc.endpoints.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Louis on 29/11/2014.
 */
public class DataStore implements TeamListInterface, TeamLeaguesInterface, LeagueListInterface, RefGamesInterface, LeagueViewInterface, LeagueScheduleInterface,
        LeagueScoresInterface, TeamScoresInterface, TeamScheduleInterface, UpAvailabilityInterface, TeamAvailabilityInterface {

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
    private ArrayList<Integer> loadedLeagueTeams = new ArrayList<>();
    private ArrayList<Integer> myMatchesAvailability = new ArrayList<>();
    private ArrayList<Integer> matchesAvailability = new ArrayList<>();

    //Data observers for every league
    private List<DBObserver> AllTeamsDBObservers = new ArrayList<>();
    private List<DBObserver> MyLeaguesDBObservers = new ArrayList<>();
    private List<DBObserver> AllLeagueDBObservers = new ArrayList<>();
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
        clearUserData();
    }

    public synchronized static void newInstance(Context context) {
        sInstance = new DataStore(context);
    }



    public synchronized void setUser(User user){
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

        List<Match> output = Match.cursorToList(cursor);
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

        List<Match> output = Match.cursorToList(cursor);
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

        List<LeagueTeam> output = LeagueTeam.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
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

        List<Match> output = Match.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Match> getTeamScores(int leagueID, int teamID){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.TEAMSSCORES_TABLE_NAME,
                null,
                DBProviderContract.SELECTION_LEAGUEIDANDTEAMID,
                new String[]{Integer.toString(leagueID), Integer.toString(teamID)},
                null,
                null,
                null,
                null);

        List<Match> output = Match.cursorToList(cursor);
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

        List<Match> output = Match.cursorToList(cursor);
        cursor.close();
        SQLiteManager.getInstance(context).closeDatabase();
        return output;
    }

    public List<Match> getMyUpcomingRefereeGames(){
        Cursor cursor = SQLiteManager.getInstance(context).query(context,
                DBProviderContract.MYUPCOMINGREFEREEGAMES_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null);

        List<Match> output = Match.cursorToList(cursor);
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
        if(!loadedMyLeagues){
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
            loadedAllLeagues = false;
        }
    }

    public void leagueListCallback(ResponseStatus responseStatus){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueList was successful, notify my DBObservers");
            notifyAllLeagueDBObservers(null);
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
            notifyMyUpcomingGamesDBObservers(null);
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
            notifyTeamsFixturesDBObservers(new Tuple<>(leagueID,teamID));
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
            notifyTeamsScoresDBObservers(new Tuple<>(leagueID,teamID));
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

    public void loadMyAvailability(int matchID){
        if(!myMatchesAvailability.contains(matchID)){
            new UpAvailability(this,context, matchID).execute();
            myMatchesAvailability.add(matchID);
        }
    }

    public void setMyAvailability(boolean isPlaying, int matchID){
        if(!myMatchesAvailability.contains(matchID)){
            myMatchesAvailability.add(matchID);
        }
        new UpAvailability(this, context, isPlaying ? 1 : 0, matchID).execute();
    }

    public void setPlayersAvailability(boolean isPlaying, int userID, int matchID){
        new UpAvailability(this, context, isPlaying ? 1 : 0, matchID, userID).execute();
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
                    notifyMyUpcomingGameAvailabilitysDBObservers(new Tuple<>(matchID,userID));
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

    public synchronized void clearUserData() {
        user = new User();
        dropAllTables();
        loadedAllTeams = false;
        loadedMyLeagues = false;
        loadedAllLeagues = false;
        loadedLeagueScores = new ArrayList<>();
        loadedLeagueFixtures = new ArrayList<>();
        loadedLeagueStandings = new ArrayList<>();
        loadedTeamsFixtures = new HashMap<>();
        loadedTeamsScore = new HashMap<>();
        loadedRefGames = false;
        myMatchesAvailability = new ArrayList<>();
        matchesAvailability = new ArrayList<>();
        loadedLeagueTeams = new ArrayList<>();
    }

    public synchronized void refreshData() {
        dropAllTables();
        loadedAllTeams = false;
        loadedMyLeagues = false;
        loadedAllLeagues = false;
        loadedLeagueScores = new ArrayList<>();
        loadedLeagueFixtures = new ArrayList<>();
        loadedLeagueStandings = new ArrayList<>();
        loadedTeamsFixtures = new HashMap<>();
        loadedTeamsScore = new HashMap<>();
        loadedRefGames = false;
        myMatchesAvailability = new ArrayList<>();
        matchesAvailability = new ArrayList<>();
        loadedLeagueTeams = new ArrayList<>();
        notifyAllTeamsDBObservers(null);
        notifyMyLeaguesDBObservers(null);
        notifyAllLeagueDBObservers(null);
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
    }

    public synchronized void refreshMatchAvailabilities(){
        dropAvailability();
    }

    private void dropAllTables(){
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

    private void dropAvailability(){
        SQLiteDatabase db = SQLiteManager.getInstance(context).openDatabase();
        db.execSQL(DBProviderContract.SQL_DROP_TABLE_IF_EXISTS + " " + DBProviderContract.MYTEAMPLAYERSAVAILABILITY_TABLE_NAME);
        db.execSQL(DBProviderContract.CREATE_MYTEAMPLAYERSAVAILABILITY_TABLE);
    }

    /**
     * DBObservers registration
     */

    public synchronized void registerAllTeamsDBObservers(DBObserver dbObserver) {
        AllTeamsDBObservers.add(dbObserver);
    }

    public synchronized void registerMyLeaguesDBObserver(DBObserver dbObserver) {
        MyLeaguesDBObservers.add(dbObserver);
    }

    public synchronized void registerAllLeagueDBObserver(DBObserver dbObserver) {
        AllLeagueDBObservers.add(dbObserver);
    }

    public synchronized void registerLeagueScoreDBObserver(DBObserver dbObserver) {
        LeagueScoreDBObservers.add(dbObserver);
    }

    public synchronized void registerLeaguesFixturesDBObserver(DBObserver dbObserver) {
        LeaguesFixturesDBObservers.add(dbObserver);
    }

    public synchronized void registerLeaguesStandingsDBObserver(DBObserver dbObserver) {
        LeaguesStandingsDBObservers.add(dbObserver);
    }

    public synchronized void registerTeamsFixturesDBObserver(DBObserver dbObserver) {
        TeamsFixturesDBObservers.add(dbObserver);
    }

    public synchronized void registerTeamsScoresDBObserver(DBObserver dbObserver) {
        TeamsScoresDBObservers.add(dbObserver);
    }

    public synchronized void registerMyUpcomingGamesDBObserver(DBObserver dbObserver) {
        MyUpcomingGamesDBObservers.add(dbObserver);
    }

    public synchronized void registerMyUpcomingRefereeDBObserver(DBObserver dbObserver) {
        MyUpcomingRefereeDBObservers.add(dbObserver);
    }

    public synchronized void registerLeagueTeamsDBObserver(DBObserver dbObserver) {
        LeagueTeamsDBObservers.add(dbObserver);
    }

    public synchronized void registerMyUpcomingGameAvailabilitysDBObserver(DBObserver dbObserver) {
        MyUpcomingGameAvailabilityDBObservers.add(dbObserver);
    }

    public synchronized void registerMyTeamsPlayerAvailabilitysDBObserver(DBObserver dbObserver) {
        MyTeamsPlayerAvailabilityDBObservers.add(dbObserver);
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

    /**
     * DBObserver notification
     */
    
    private void notifyDBObservers(List<DBObserver> dbObservers, final String tableName, final Object data){
        for (final DBObserver dbObserver : dbObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    dbObserver.notify(tableName, data);
                }
            }).start();
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
}
