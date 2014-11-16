package company.businessinc.networking;

/**
 * Created by gp on 16/11/14.
 */
public class UserNew {
    static private String endPoint = "/user/new";

    public boolean register(String userName, String password, String email, String name){
        return false;
    }

    public boolean register(String userName, String password, String email, String name, Integer teamID){
        return false;
    }

    public String getEndPoint() {
        return endPoint;
    }
}
