package company.businessinc.bathtouch.data;

import android.net.Uri;
import android.provider.BaseColumns;

import company.businessinc.dataModels.League;
import company.businessinc.dataModels.Team;

/**
 * Created by Grzegorz on 27/01/2015.
 */
public class DBProviderContract {

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
    public static final int GETALLTEAMS_URL_QUERY = 0;
    public static final int GETMYLEAGUES_URL_QUERY = 1;
    public static final int GETALLLEAGUES_URL_QUERY = 2;

    /**
     * Table names
     */
    public static final String ALLTEAMS_TABLE_NAME = "AllTeams";
    public static final String MYLEAGUES_TABLE_NAME = "MyLeagues";
    public static final String ALLLEAGUES_TABLE_NAME = "AllLeagues";

    public static final String[] TABLES = {ALLTEAMS_TABLE_NAME, MYLEAGUES_TABLE_NAME, ALLLEAGUES_TABLE_NAME};

    /**
     * Content URI for modification tables
     */
    public static final Uri ALLTEAMS_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, ALLTEAMS_TABLE_NAME);
    public static final Uri MYLEAGUES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, MYLEAGUES_TABLE_NAME);
    public static final Uri ALLLEAGUES_TABLE_CONTENTURI = Uri.withAppendedPath(CONTENT_URI, ALLLEAGUES_TABLE_NAME);


    /**
     * Create tables strings
     */
    public static final String CREATE_ALLTEAMS_TABLE = "CREATE TABLE " + ALLTEAMS_TABLE_NAME + "( " + Team.CREATE_TABLE + " )";
    public static final String CREATE_MYLEAGUES_TABLE = "CREATE TABLE " + MYLEAGUES_TABLE_NAME + "( " + League.CREATE_TABLE + " )";
    public static final String CREATE_ALLLEAGUES_TABLE = "CREATE TABLE " + ALLLEAGUES_TABLE_NAME + "( " + League.CREATE_TABLE + " )";


    public static final String[] CREATE_TABLES = {CREATE_ALLTEAMS_TABLE, CREATE_MYLEAGUES_TABLE, CREATE_ALLLEAGUES_TABLE};

    // The content provider database name
    public static final String DATABASE_NAME = "BathTouchDB";

    // The starting version of the database
    public static final int DATABASE_VERSION = 1;
}
