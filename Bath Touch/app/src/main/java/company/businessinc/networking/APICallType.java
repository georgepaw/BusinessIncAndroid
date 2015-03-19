package company.businessinc.networking;


/**
 * Created by gp on 18/11/14.
 */
public enum APICallType {
    DeviceRegister("/device/register/"),
    DeviceUnregister("/device/unregister/"),
    LeagueList("/league/list/"),
    LeagueSchedule("/league/schedule/"),
    LeagueScores("/league/scores/"),
    LeagueToday("/league/today/"),
    LeagueView("/league/view/"),
    RefGames("/ref/games/"),
    RequestMessages("/request/messages/"),
    RequestPlayers("/request/players/"),
    ScoreSubmit("/score/submit/"),
    TeamAvailability("/team/availability/"),
    TeamScores("/team/scores/"),
    TeamList("/team/list/"),
    TeamLeagues("/team/leagues/"),
    TeamSchedule("/team/schedule/"),
    UpAvailability("/up/availability/"),
    UserNew("/user/new/"),
    UserEdit("/user/edit/"),
    UserLogin("/user/login/"),
    UserReset("/user/reset/"),
    UserResetPassword("/user/reset/password/");

    public String getEndPoint(){
        return text.toString();
    }
    private final String text;

    private APICallType(final String a){
        text = a;
    }
}
