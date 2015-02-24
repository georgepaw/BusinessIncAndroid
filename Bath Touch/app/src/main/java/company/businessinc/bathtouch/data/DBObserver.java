package company.businessinc.bathtouch.data;

/**
 * Created by Grzegorz on 18/02/2015.
 */
public interface DBObserver {
    void notify(String tableName, Object data);
}
