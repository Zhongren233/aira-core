package moe.aira.onebot.osu;

import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserScore {
    @Override
    public String toString() {
        return "UserScore{" + "score='" + score + "'" + ", mods=" + mods + ", userId='" + userId + "'" + ", mapId='" + mapId + "'" + "}";
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Set<Mod> getMods() {
        return mods;
    }

    public void setMods(Set<Mod> mods) {
        this.mods = mods;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    private String score;
    private Set<Mod> mods;
    private String userId;
    private String mapId;

    public String toMessage() {
        mods.remove(Mod.NoFail);
        mods.remove(Mod.DoubleTime);
        String collect = mods.stream().map(Enum::toString).collect(Collectors.joining(","));
        return mapId + "\t" + userId + "\t" +(collect.isEmpty() ? "Empty" : collect) + "\t" + score;
    }
}
