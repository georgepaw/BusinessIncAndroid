package company.businessinc.bathtouch;

import android.net.Uri;
import android.provider.BaseColumns;

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
     * Table names
     */
    public static final String ALLTEAMS_TABLE_NAME = "AllTeams";

    public static final String[] TABLES = {ALLTEAMS_TABLE_NAME};

    /**
     * Content URI for modification tables
     */
    public static final Uri ALLTEAMS_TABLE_CONTENTURI =
            Uri.withAppendedPath(CONTENT_URI, ALLTEAMS_TABLE_NAME);

    // The content provider database name
    public static final String DATABASE_NAME = "BathTouchDB";

    // The starting version of the database
    public static final int DATABASE_VERSION = 1;
}
