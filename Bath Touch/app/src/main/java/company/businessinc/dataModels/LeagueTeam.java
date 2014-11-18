package company.businessinc.dataModels;

/**
 * Created by gp on 18/11/14.
 */
public class LeagueTeam {
    private String teamName;
    private int leaguePoints;
    private int win;
    private int draw;
    private int lose;
    private int forfeit;
    private int position;
    private int pointsFor;
    private int pointsAgainst;

    public LeagueTeam(String teamName, int leaguePoints, int win, int draw, int lose, int forfeit, int position, int pointsFor, int pointsAgainst) {
        this.teamName = teamName;
        this.leaguePoints = leaguePoints;
        this.win = win;
        this.draw = draw;
        this.lose = lose;
        this.forfeit = forfeit;
        this.position = position;
        this.pointsFor = pointsFor;
        this.pointsAgainst = pointsAgainst;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getLeaguePoints() {
        return leaguePoints;
    }

    public void setLeaguePoints(int leaguePoints) {
        this.leaguePoints = leaguePoints;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getLose() {
        return lose;
    }

    public void setLose(int lose) {
        this.lose = lose;
    }

    public int getForfeit() {
        return forfeit;
    }

    public void setForfeit(int forfeit) {
        this.forfeit = forfeit;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPointsFor() {
        return pointsFor;
    }

    public void setPointsFor(int pointsFor) {
        this.pointsFor = pointsFor;
    }

    public int getPointsAgainst() {
        return pointsAgainst;
    }

    public void setPointsAgainst(int pointsAgainst) {
        this.pointsAgainst = pointsAgainst;
    }
}
