package company.businessinc.networking;


/**
 * Created by gp on 18/11/14.
 */
public enum APICallType {
    LeagueList("/league/list/"),
    LeagueScores("/league/scores/"),
    LeagueView("/league/view/"),
    RefGames("/ref/games/"),
    ScoreSubmit("/score/submit/"),
    TeamScores("/team/scores/"),
    TeamList("/team/list/"),
    TeamSchedule("/team/schedule/"),
    UserEdit("/user/edit/"),
    UserLogin("/user/login/"),
    UserReset("/user/reset/"),
    UserResetPassword("/user/reset/password");

    public String getEndPoint(){
        return text.toString();
    }
    private final String text;

    private APICallType(final String a){
        text = a;
    }
}
