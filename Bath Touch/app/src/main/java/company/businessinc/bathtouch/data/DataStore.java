package company.businessinc.bathtouch.data;

import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.util.*;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.LeagueTeam;
import company.businessinc.dataModels.Match;
import company.businessinc.dataModels.Player;
import company.businessinc.dataModels.ResponseStatus;
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
import company.businessinc.endpoints.TeamAvailability;
import company.businessinc.endpoints.TeamAvailabilityInterface;
import company.businessinc.endpoints.TeamLeagues;
import company.businessinc.endpoints.TeamLeaguesInterface;
import company.businessinc.endpoints.TeamList;
import company.businessinc.endpoints.TeamListInterface;
import company.businessinc.endpoints.TeamSchedule;
import company.businessinc.endpoints.TeamScheduleInterface;
import company.businessinc.endpoints.TeamScores;
import company.businessinc.endpoints.TeamScoresInterface;
import company.businessinc.endpoints.UpAvailability;
import company.businessinc.endpoints.UpAvailabilityInterface;

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
    private List<Observer> AllTeamsObservers = new ArrayList<>();
    private List<Observer> MyLeaguesObservers = new ArrayList<>();
    private List<Observer> AllLeagueObservers = new ArrayList<>();
    private List<Observer> LeagueScoreObservers = new ArrayList<>();
    private List<Observer> LeaguesFixturesObservers = new ArrayList<>();
    private List<Observer> LeaguesStandingsObservers = new ArrayList<>();
    private List<Observer> TeamsFixturesObservers = new ArrayList<>();
    private List<Observer> TeamsScoresObservers = new ArrayList<>();
    private List<Observer> MyUpcomingGamesObservers = new ArrayList<>();
    private List<Observer> MyUpcomingRefereeObservers = new ArrayList<>();
    private List<Observer> LeagueTeamsObservers = new ArrayList<>();
    private List<Observer> MyUpcomingGameAvailabilitysObservers = new ArrayList<>();
    private List<Observer> MyTeamsPlayerAvailabilitysObservers = new ArrayList<>();

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
        AllTeamsObservers = new ArrayList<>();
        MyLeaguesObservers = new ArrayList<>();
        AllLeagueObservers = new ArrayList<>();
        LeagueScoreObservers = new ArrayList<>();
        LeaguesFixturesObservers = new ArrayList<>();
        LeaguesStandingsObservers = new ArrayList<>();
        TeamsFixturesObservers = new ArrayList<>();
        TeamsScoresObservers = new ArrayList<>();
        MyUpcomingGamesObservers = new ArrayList<>();
        MyUpcomingRefereeObservers = new ArrayList<>();
        LeagueTeamsObservers = new ArrayList<>();
        MyUpcomingGameAvailabilitysObservers = new ArrayList<>();
        MyTeamsPlayerAvailabilitysObservers = new ArrayList<>();
        clearUserData();
    }

    public static void newInstance(Context context) {
        sInstance = new DataStore(context);
    }



    public synchronized void setUser(User user){
        this.user = user;
    }

    public synchronized boolean isUserLoggedIn(){
        return user.isLoggedIn();
    }

    public synchronized String getUserName(){
        return user.getName();
    }

    public synchronized String getUserTeam(){
        return user.getTeamName();
    }

    public synchronized int getUserTeamID(){
        return user.getTeamID();
    }

    public synchronized int getUserTeamCaptainID() {
        return user.getCaptainID();
    }

    public synchronized String getUserTeamCaptainName() {
        return user.getCaptainName();
    }

    public synchronized int getUserTeamColorPrimary() {
        return Color.parseColor(user.getTeamColorPrimary());
    }

    public synchronized int getUserTeamColorSecondary() {
        return Color.parseColor(user.getTeamColorSecondary());
    }

    public synchronized boolean isUserCaptain(){
        return user.isCaptain();
    }

    public synchronized boolean isReferee(){
        return user.isReferee();
    }

    public synchronized String userToJSON(){
        return user.toString();
    }

    /**
     * Data calls
     */

    /**
     * API Calls and thier callbacks
     */

    public synchronized void loadAllTeams(){
        if(!loadedAllTeams) {
            new TeamList(this, context).execute();
            loadedAllTeams = true;
        }
    }



    public synchronized void loadLeaguesTeams(int leagueID){
        if(!loadedLeagueTeams.contains(leagueID)){
            new TeamList(this, leagueID, context).execute();
            loadedLeagueTeams.add(leagueID);
        }

    }

    public synchronized void teamListCallback(ResponseStatus successful, TeamList.CallType callType){
        if(successful.getStatus()){
            Log.d(TAG, "The call TeamList was successful");
        } else {
            Log.d(TAG, "The call TeamList was not successful");
        }
    }

    public synchronized void loadMyLeagues(){
        if(!loadedMyLeagues){
            new TeamLeagues(this, user.getTeamID(), context).execute();
            loadedMyLeagues = true;
        }
    }

    public synchronized void teamLeaguesCallback(ResponseStatus responseStatus){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call TeamLeagues was successful, notify my observers");
        } else{
            Log.d(TAG, "The call TeamLeagues was not successful");
        }
    }

    public synchronized void loadAllLeagues(){
        if(!loadedAllLeagues){
            new LeagueList(this, context).execute();
            loadedAllLeagues = false;
        }
    }

    public synchronized void leagueListCallback(ResponseStatus responseStatus){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueList was successful, notify my observers");
        } else{
            Log.d(TAG, "The call LeagueList was not successful");
        }
    }

    public synchronized void loadLeagueScores(int leagueID){
        if(!loadedLeagueScores.contains(leagueID)) {
            new LeagueScores(this, context, leagueID).execute();
            loadedLeagueScores.add(leagueID);
        }
    }

    @Override
    public synchronized void leagueScoresCallback(ResponseStatus responseStatus, int leagueID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was successful, notify my observers");
        } else{
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was not successful");
        }
    }

    public synchronized void loadLeagueFixtures(int leagueID){
        if(!loadedLeagueFixtures.contains(leagueID)) {
            new LeagueSchedule(this, context, leagueID).execute();
            loadedLeagueFixtures.add(leagueID);
        }
    }

    @Override
    public synchronized void leagueScheduleCallback(ResponseStatus responseStatus, int leagueID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was successful, notify my observers");
        } else{
            Log.d(TAG, "The call LeagueScores for leagueID " + leagueID + " was not successful");
        }
    }

    public synchronized void loadLeagueStandings(int leagueID){
        if(!loadedLeagueStandings.contains(leagueID)) {
            new LeagueView(this, context, leagueID).execute();
            loadedLeagueStandings.add(leagueID);
        }
    }
    @Override
    public synchronized void leagueViewCallback(ResponseStatus responseStatus, int leagueID) {
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call LeagueStandings for leagueID " + leagueID + " was successful, notify my observers");
        } else{
            Log.d(TAG, "The call LeagueStandings for leagueID " + leagueID + " was not successful");
        }
    }

    public synchronized void loadTeamsFixtures(int leagueID, int teamID){
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

    @Override
    public synchronized void teamScheduleCallback(ResponseStatus responseStatus, int leagueID, int teamID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call TeamSchedule for leagueID " + leagueID + " for teamID "+ teamID+" was successful, notify my observers");
        } else{
            Log.d(TAG, "The call TeamSchedule for leagueID " + leagueID + " for teamID "+ teamID+" was not successful");
        }
    }

    public synchronized void loadTeamsLeagueScore(int leagueID, int teamID){
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

    @Override
    public synchronized void teamScoresCallback(ResponseStatus responseStatus, int leagueID, int teamID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call TeamScores for leagueID " + leagueID + " for teamID "+ teamID+" was successful, notify my observers");
        } else{
            Log.d(TAG, "The call TeamScores for leagueID " + leagueID + " for teamID "+ teamID+" was not successful");
        }
    }

    public synchronized void loadMyUpcomingGames(){

    }

    public synchronized void loadMyUpcomingRefGames(){
        if(!loadedRefGames) {
            new RefGames(this, context).execute();
            loadedRefGames = true;
        }
    }

    @Override
    public synchronized void refGamesCallback(ResponseStatus responseStatus){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call RefGames was successful, notify my observers");
        } else{
            Log.d(TAG, "The call RefGames was not successful");
        }
    }

    public synchronized void loadMyAvailability(int matchID){
        if(!myMatchesAvailability.contains(matchID)){
            new UpAvailability(this,context, matchID).execute();
            myMatchesAvailability.add(matchID);
        }
    }

    public synchronized void setMyAvailability(boolean isPlaying, int matchID){
        if(!myMatchesAvailability.contains(matchID)){
            myMatchesAvailability.add(matchID);
        }
        new UpAvailability(this, context, isPlaying ? 1 : 0, matchID).execute();
    }

    public synchronized void setPlayersAvailability(boolean isPlaying, int userID, int matchID){
        new UpAvailability(this, context, isPlaying ? 1 : 0, matchID, userID).execute();
    }

    public synchronized void upAvailabilityCallback(ResponseStatus responseStatus, boolean isPlaying, UpAvailability.CallType callType, int matchID, int userID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call UpAvailability was successful, notify my observers");
        } else{
            Log.d(TAG, "The call UpAvailability was not successful");
        }
    }

    public synchronized void loadMatchPlayersAvailability(int matchID){
        if(!matchesAvailability.contains(matchID)){
            new TeamAvailability(this, context, matchID).execute();
            matchesAvailability.add(matchID);
        }
    }

    public synchronized void teamAvailabilityCallback(ResponseStatus responseStatus, int matchID){
        if(responseStatus.getStatus()){
            Log.d(TAG, "The call teamAvailability for matchID " + matchID + " was successful, notify my observers");
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
     * Observers registration
     */

    public void registerAllTeamsObservers(Observer observer) {
        AllTeamsObservers.add(observer);
    }

    public void registerMyLeaguesObserver(Observer observer) {
        MyLeaguesObservers.add(observer);
    }

    public void registerAllLeagueObserver(Observer observer) {
        AllLeagueObservers.add(observer);
    }

    public void registerLeagueScoreObserver(Observer observer) {
        LeagueScoreObservers.add(observer);
    }

    public void registerLeaguesFixturesObserver(Observer observer) {
        LeaguesFixturesObservers.add(observer);
    }

    public void registerLeaguesStandingsObserver(Observer observer) {
        LeaguesStandingsObservers.add(observer);
    }

    public void registerTeamsFixturesObserver(Observer observer) {
        TeamsFixturesObservers.add(observer);
    }

    public void registerTeamsScoresObserver(Observer observer) {
        TeamsScoresObservers.add(observer);
    }

    public void registerMyUpcomingGamesObserver(Observer observer) {
        MyUpcomingGamesObservers.add(observer);
    }

    public void registerMyUpcomingRefereeObserver(Observer observer) {
        MyUpcomingRefereeObservers.add(observer);
    }

    public void registerLeagueTeamsObserver(Observer observer) {
        LeagueTeamsObservers.add(observer);
    }

    public void registerMyUpcomingGameAvailabilitysObserver(Observer observer) {
        MyUpcomingGameAvailabilitysObservers.add(observer);
    }

    public void registerMyTeamsPlayerAvailabilitysObserver(Observer observer) {
        MyTeamsPlayerAvailabilitysObservers.add(observer);
    }

    /**
     * Observers unregistration
     */

    public void unregisterAllTeamsObservers(Observer observer) {
        AllTeamsObservers.remove(observer);
    }

    public void unregisterMyLeaguesObserver(Observer observer) {
        MyLeaguesObservers.remove(observer);
    }

    public void unregisterAllLeagueObserver(Observer observer) {
        AllLeagueObservers.remove(observer);
    }

    public void unregisterLeagueScoreObserver(Observer observer) {
        LeagueScoreObservers.remove(observer);
    }

    public void unregisterLeaguesFixturesObserver(Observer observer) {
        LeaguesFixturesObservers.remove(observer);
    }

    public void unregisterLeaguesStandingsObserver(Observer observer) {
        LeaguesStandingsObservers.remove(observer);
    }

    public void unregisterTeamsFixturesObserver(Observer observer) {
        TeamsFixturesObservers.remove(observer);
    }

    public void unregisterTeamsScoresObserver(Observer observer) {
        TeamsScoresObservers.remove(observer);
    }

    public void unregisterMyUpcomingGamesObserver(Observer observer) {
        MyUpcomingGamesObservers.remove(observer);
    }

    public void unregisterMyUpcomingRefereeObserver(Observer observer) {
        MyUpcomingRefereeObservers.remove(observer);
    }

    public void unregisterLeagueTeamsObserver(Observer observer) {
        LeagueTeamsObservers.remove(observer);
    }

    public void unregisterMyUpcomingGameAvailabilitysObserver(Observer observer) {
        MyUpcomingGameAvailabilitysObservers.remove(observer);
    }

    public void unregisterMyTeamsPlayerAvailabilitysObserver(Observer observer) {
        MyTeamsPlayerAvailabilitysObservers.remove(observer);
    }

    /**
     * Observer notification
     */

    private void notifyAllTeamsObservers(final Object data) {
        for (final Observer observer : AllTeamsObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyMyLeaguesObservers(final Object data) {
        for (final Observer observer : MyLeaguesObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyAllLeagueObservers(final Object data) {
        for (final Observer observer : AllLeagueObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyLeagueScoreObservers(final Object data) {
        for (final Observer observer : LeagueScoreObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyLeaguesFixturesObservers(final Object data) {
        for (final Observer observer : LeaguesFixturesObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyLeaguesStandingsObservers(final Object data) {
        for (final Observer observer : LeaguesStandingsObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyTeamsFixturesObservers(final Object data) {
        for (final Observer observer : TeamsFixturesObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyTeamsScoresObservers(final Object data) {
        for (final Observer observer : TeamsScoresObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyMyUpcomingGamesObservers(final Object data) {
        for (final Observer observer : MyUpcomingGamesObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyMyUpcomingRefereeObservers(final Object data) {
        for (final Observer observer : MyUpcomingRefereeObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyLeagueTeamsObservers(final Object data) {
        for (final Observer observer : LeagueTeamsObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyMyUpcomingGameAvailabilitysObservers(final Object data) {
        for (final Observer observer : MyUpcomingGameAvailabilitysObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }

    private void notifyMyTeamsPlayerAvailabilitysObservers(final Object data) {
        for (final Observer observer : MyTeamsPlayerAvailabilitysObservers) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    observer.update(null, data);
                }
            }).start();
        }
    }
}
