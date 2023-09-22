package dev.luzifer.data.access.shit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

@Value
@Getter
@RequiredArgsConstructor
@ToString
@EqualsAndHashCode
public class ChampInfo {

    int champId;
    int leagueTier;
    int leaguePoints;
    int champLevel;
    int won;
    int categoryId;
    int goldEarned;
    int killingSpree;
    int kills;
    int deaths;
    int assists;
    int damageDone;
    int damageTaken;
    int damageShielded;
    int heal;
    int selfHeal;
    int matchId;
    int playerId;
}
