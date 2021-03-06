package company.businessinc.bathtouch.data;

import android.net.Uri;
import company.businessinc.dataModels.*;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class DBProviderContract {

    //Keywords
    public static final String CREATE_TABLE = "CREATE TABLE";
    public static final String INTEGER = "INTEGER";
    public static final String TEXT = "TEXT";
    public static final String PRIMARY_KEY = "PRIMARY_KEY";

    public static final String SELECTION_LEAGUEID = League.KEY_LEAGUEID + " = ?";
    public static final String SELECTION_TEAMID = Team.KEY_TEAMID + " = ?";
    public static final String SELECTION_LEAGUEIDANDTEAMID = League.KEY_LEAGUEID + " = ? AND " + Team.KEY_TEAMID + " = ?";
    public static final String SELECTION_LEAGUEIDANDMATCHID = League.KEY_LEAGUEID + " = ? AND " + Match.KEY_MATCHID + " = ?";
    public static final String SELECTION_LEAGUEIDANDMATCHIDANDTEAMID = League.KEY_LEAGUEID + " = ? AND " + Match.KEY_MATCHID + " = ? AND " + Team.KEY_TEAMID + " = ?";
    public static final String SELECTION_MATCHID = Match.KEY_MATCHID + " = ?";
    public static final String SELECTION_MATCHIDANDUSERID = Match.KEY_MATCHID + " = ? AND " + Player.KEY_USERID + " = ?";
    public static final String SELECTION_MATCHIDANDISPLAYING = Match.KEY_MATCHID + " = ? AND " + Player.KEY_ISPLAYING + " = ?";
    public static final String SELECTION_SCORES_ARE_NULL = Match.KEY_TEAMONEPOINTS + " is null AND " + Match.KEY_TEAMTWOPOINTS + " is null";
    public static final String SELECTION_SCORES_ARE_NOT_NULL = Match.KEY_TEAMONEPOINTS + " is not null AND " + Match.KEY_TEAMTWOPOINTS + " is not null";

    public static final String SQL_DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS";

    // The URI scheme used for content URIs
    public static final String SCHEME = "content";

    // The provider's authority
    public static final String AUTHORITY = "company.businessinc.bathtouch";

    /**
     * The DataProvider content URI
     */
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    /**
     * Integers which indicate which query to run
     */
    public static final int ALLTEAMS_URL_QUERY = 0;
    public static final int MYLEAGUES_URL_QUERY = 1;
    public static final int ALLLEAGUES_URL_QUERY = 2;
    public static final int LEAGUESSCORE_URL_QUERY = 3;
    public static final int LEAGUESFIXTURES_URL_QUERY = 4;
    public static final int LEAGUESSTANDINGS_URL_QUERY = 5;
    public static final int TEAMSFIXTURES_URL_QUERY = 6;
    public static final int TEAMSSCORES_URL_QUERY = 7;
    public static final int MYUPCOMINGGAMES_URL_QUERY = 8;
    public static final int MYUPCOMINGREFEREEGAMES_URL_QUERY = 9;
    public static final int LEAGUETEAMS_URL_QUERY = 10;
    public static final int MYUPCOMINGGAMESAVAILABILITY_URL_QUERY = 11;
    public static final int MYTEAMPLAYERSAVAILABILITY_URL_QUERY = 12;
    public static final int LIVELEAGUE_URL_QUERY = 13;
    public static final int CACHEDREQUESTS_URL_QUERY = 14;
    public static final int MESSAGES_URL_QUERY = 15;
    public static final int LEAGUETODAY_URL_QUERY = 16;

    /**
     * Table names
     */
    public static final String ALLTEAMS_TABLE_NAME = "AllTeams";
    public static final String MYLEAGUES_TABLE_NAME = "MyLeagues";
    public static final String ALLLEAGUES_TABLE_NAME = "AllLeagues";
    public static final String LEAGUESSCORE_TABLE_NAME = "LeaguesScore";
    public static final String LEAGUESFIXTURES_TABLE_NAME = "LeaguesFixtures";
    public static final String LEAGUESSTANDINGS_TABLE_NAME = "LeaguesStandings";
    public static final String TEAMSFIXTURES_TABLE_NAME = "TeamsFixtures";
    public static final String TEAMSSCORES_TABLE_NAME = "TeamsScores";
    public static final String MYUPCOMINGGAMES_TABLE_NAME = "MyUpcomingGames";
    public static final String MYUPCOMINGREFEREEGAMES_TABLE_NAME = "MyUpcomingRefereeGames";
    public static final String LEAGUETEAMS_TABLE_NAME = "LeagueTeams";
    public static final String MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME = "MyUpcomingGamesAvailability";
    public static final String MYTEAMPLAYERSAVAILABILITY_TABLE_NAME = "MyTeamPlayersAvailability";
    public static final String LIVELEAGUE_TABLE_NAME = "LiveLeague";
    public static final String CACHEDREQUESTS_TABLE_NAME = "CachedRequests";
    public static final String MESSAGES_TABLE_NAME = "Messages";
    public static final String LEAGUETODAY_TABLE_NAME = "LeagueToday";
    public static final String TEAMHISTORIC_TABLE_NAME = "TeamHistoric";
    public static final String LEAGUEHISTORIC_TABLE_NAME = "LeagueHistoric";

    public static final String[] TABLES = {ALLTEAMS_TABLE_NAME, MYLEAGUES_TABLE_NAME, ALLLEAGUES_TABLE_NAME, LEAGUESSCORE_TABLE_NAME,
            LEAGUESFIXTURES_TABLE_NAME, LEAGUESSTANDINGS_TABLE_NAME, TEAMSFIXTURES_TABLE_NAME, TEAMSSCORES_TABLE_NAME,
            MYUPCOMINGGAMES_TABLE_NAME, MYUPCOMINGREFEREEGAMES_TABLE_NAME, LEAGUETEAMS_TABLE_NAME,
            MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME, MYTEAMPLAYERSAVAILABILITY_TABLE_NAME, LIVELEAGUE_TABLE_NAME, CACHEDREQUESTS_TABLE_NAME,
            MESSAGES_TABLE_NAME, LEAGUETODAY_TABLE_NAME, TEAMHISTORIC_TABLE_NAME, LEAGUEHISTORIC_TABLE_NAME};

    /**
     * Content URI for modification tables
     */
    public static final Uri ALLTEAMS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, ALLTEAMS_TABLE_NAME);
    public static final Uri MYLEAGUES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYLEAGUES_TABLE_NAME);
    public static final Uri ALLLEAGUES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, ALLLEAGUES_TABLE_NAME);
    public static final Uri LEAGUESSCORE_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUESSCORE_TABLE_NAME);
    public static final Uri LEAGUESFIXTURES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUESFIXTURES_TABLE_NAME);
    public static final Uri LEAGUESSTANDINGS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUESSTANDINGS_TABLE_NAME);
    public static final Uri TEAMSFIXTURES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, TEAMSFIXTURES_TABLE_NAME);
    public static final Uri TEAMSSCORES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, TEAMSSCORES_TABLE_NAME);
    public static final Uri MYUPCOMINGGAMES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYUPCOMINGGAMES_TABLE_NAME);
    public static final Uri MYUPCOMINGREFEREEGAMES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYUPCOMINGREFEREEGAMES_TABLE_NAME);
    public static final Uri LEAGUETEAMS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, LEAGUETEAMS_TABLE_NAME);
    public static final Uri MYUPCOMINGGAMESAVAILABILITY_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME);
    public static final Uri MYTEAMPLAYERSAVAILABILITY_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYTEAMPLAYERSAVAILABILITY_TABLE_NAME);

    /**
     * Create tables strings
     */
    public static final String CREATE_ALLTEAMS_TABLE = CREATE_TABLE + " " + ALLTEAMS_TABLE_NAME + "( " + Team.CREATE_TABLE + ", PRIMARY KEY("+Team.KEY_TEAMID+"))";

    public static final String CREATE_MYLEAGUES_TABLE = CREATE_TABLE + " " + MYLEAGUES_TABLE_NAME + "( " + League.CREATE_TABLE + " )";

    public static final String CREATE_ALLLEAGUES_TABLE = CREATE_TABLE + " " + ALLLEAGUES_TABLE_NAME + "( " + League.CREATE_TABLE + " )";

    public static final String CREATE_LEAGUESSCORE_TABLE = CREATE_TABLE + " " + LEAGUESSCORE_TABLE_NAME + "( " +
                                                                Match.CREATE_TABLE + ", PRIMARY KEY("+Match.KEY_MATCHID+"))";

    public static final String CREATE_LEAGUESFIXTURES_TABLE = CREATE_TABLE + " " + LEAGUESFIXTURES_TABLE_NAME + "( " +
                                                                Match.CREATE_TABLE + ", PRIMARY KEY("+Match.KEY_MATCHID+"))";

    public static final String CREATE_LEAGUESSTANDINGS_TABLE = CREATE_TABLE + " " + LEAGUESSTANDINGS_TABLE_NAME + "( " +
                                                               League.KEY_LEAGUEID + "\t" + INTEGER + "," +
                                                               LeagueTeam.CREATE_TABLE + ", PRIMARY KEY("+League.KEY_LEAGUEID+ "," + LeagueTeam.KEY_TEAMID +"))";

    public static final String CREATE_TEAMSFIXTURES_TABLE = CREATE_TABLE + " " + TEAMSFIXTURES_TABLE_NAME + "( " +
                                                                Team.KEY_TEAMID + "\t" + INTEGER + "," +
                                                                Match.CREATE_TABLE + ", PRIMARY KEY(" + Team.KEY_TEAMID+ "," + Match.KEY_MATCHID +"))";

    public static final String CREATE_TEAMSSCORES_TABLE = CREATE_TABLE + " " + TEAMSSCORES_TABLE_NAME + "( " +
                                                                Team.KEY_TEAMID + "\t" + INTEGER + "," +
                                                                Match.CREATE_TABLE + ", PRIMARY KEY(" + Team.KEY_TEAMID+ "," + Match.KEY_MATCHID +"))";

    public static final String CREATE_MYUPCOMINGGAMES_TABLE = CREATE_TABLE + " " + MYUPCOMINGGAMES_TABLE_NAME + "( " +
                                                                Match.CREATE_TABLE  + ", PRIMARY KEY("+Match.KEY_MATCHID+"))";

    public static final String CREATE_MYUPCOMINGREFEREEGAMES_TABLE = CREATE_TABLE + " " + MYUPCOMINGREFEREEGAMES_TABLE_NAME + "( " +
                                                                Match.CREATE_TABLE + ", PRIMARY KEY("+Match.KEY_MATCHID+"))";

    public static final String CREATE_LEAGUETEAMS_TABLE = CREATE_TABLE + " " + LEAGUETEAMS_TABLE_NAME + "( " +
            League.KEY_LEAGUEID + "\t" + INTEGER + "," +
            Team.CREATE_TABLE + ", PRIMARY KEY("+League.KEY_LEAGUEID+ "," + Team.KEY_TEAMID +"))";


    public static final String CREATE_MYUPCOMINGGAMESAVAILABILITY_TABLE = CREATE_TABLE + " " + MYUPCOMINGGAMESAVAILABILITY_TABLE_NAME + "( " +
                                                                Match.KEY_MATCHID + "\t" + INTEGER + " " + PRIMARY_KEY + "," +
                                                                Player.KEY_ISPLAYING + "\t" + INTEGER + ", PRIMARY KEY("+Match.KEY_MATCHID+"))";

    public static final String CREATE_MYTEAMPLAYERSAVAILABILITY_TABLE = CREATE_TABLE + " " + MYTEAMPLAYERSAVAILABILITY_TABLE_NAME + "( " +
                                                                Match.KEY_MATCHID + "\t" + INTEGER + " " + PRIMARY_KEY + "," +
                                                                Player.CREATE_TABLE + ", PRIMARY KEY("+Match.KEY_MATCHID+ "," + Player.KEY_USERID +"))";

    public static final String CREATE_LIVELEAGUE_TABLE = CREATE_TABLE + " " + LIVELEAGUE_TABLE_NAME + "( " + League.CREATE_TABLE + " )";

    public static final String CREATE_CACHEDREQUEST_TABLE = CREATE_TABLE + " " + CACHEDREQUESTS_TABLE_NAME + "( " + CachedRequest.CREATE_TABLE + " )";

    public static final String CREATE_MESSAGES_TABLE = CREATE_TABLE + " " + MESSAGES_TABLE_NAME + "( " +
            Message.CREATE_TABLE + ", PRIMARY KEY("+Message.KEY_MATCHID + "," + Message.KEY_TEAMID+"))";

    public static final String CREATE_LEAGUE_TODAY = CREATE_TABLE + " " + LEAGUETODAY_TABLE_NAME + "( " +
                                                        Match.CREATE_TABLE + ", PRIMARY KEY(" + Match.KEY_MATCHID +"))";

    public static final String CREATE_TEAMHISTORIC_TABLE = CREATE_TABLE + " " + TEAMHISTORIC_TABLE_NAME + "( " +
                                                                League.KEY_LEAGUEID + "\t" + INTEGER + "," +
                                                                HistoricTeam.CREATE_TABLE + ", PRIMARY KEY("+League.KEY_LEAGUEID+ "," + HistoricTeam.KEY_TEAMID +"))";

    public static final String CREATE_LEAGUEHISTORIC_TABLE = CREATE_TABLE + " " + LEAGUEHISTORIC_TABLE_NAME + "( " +
                                                                League.KEY_LEAGUEID + "\t" + INTEGER + "," +
                                                                HistoricTeam.CREATE_TABLE + ", PRIMARY KEY("+League.KEY_LEAGUEID+ "," + HistoricTeam.KEY_TEAMID +"))";


    public static final String[] CREATE_TABLES = {CREATE_ALLTEAMS_TABLE, CREATE_MYLEAGUES_TABLE, CREATE_ALLLEAGUES_TABLE, CREATE_LEAGUESSCORE_TABLE,
            CREATE_LEAGUESFIXTURES_TABLE ,CREATE_LEAGUESSTANDINGS_TABLE, CREATE_TEAMSFIXTURES_TABLE,
            CREATE_TEAMSSCORES_TABLE, CREATE_MYUPCOMINGGAMES_TABLE, CREATE_MYUPCOMINGREFEREEGAMES_TABLE, CREATE_LEAGUETEAMS_TABLE,
            CREATE_MYUPCOMINGGAMESAVAILABILITY_TABLE, CREATE_MYTEAMPLAYERSAVAILABILITY_TABLE, CREATE_LIVELEAGUE_TABLE, CREATE_CACHEDREQUEST_TABLE,
            CREATE_MESSAGES_TABLE, CREATE_LEAGUE_TODAY, CREATE_TEAMHISTORIC_TABLE, CREATE_LEAGUEHISTORIC_TABLE};

    // The content provider database name
    public static final String DATABASE_NAME = "BathTouchDB";

    // The starting version of the database
    public static final int DATABASE_VERSION = 1;
}
