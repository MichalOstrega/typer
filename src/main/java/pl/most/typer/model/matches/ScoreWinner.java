package pl.most.typer.model.matches;

public enum ScoreWinner {
    AWAY_TEAM,
    DRAW,
    HOME_TEAM;

    private static ScoreWinner getScoreWinner(Integer homeTeamScore, Integer awayTeamScore){
        return homeTeamScore < awayTeamScore ? AWAY_TEAM : (homeTeamScore == awayTeamScore ? DRAW : HOME_TEAM);
    }

    public static ScoreWinner getScoreWinner(TeamGoals teamGoals){
        return getScoreWinner(teamGoals.getHomeTeam(), teamGoals.getAwayTeam());
    }
}
