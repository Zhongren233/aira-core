package moe.aira.onebot.osu;

import java.util.HashSet;
import java.util.Set;

public enum Mod {
    None(0), NoFail(1), Easy(2), TouchDevice(4), Hidden(8), HardRock(16), SuddenDeath(32), DoubleTime(64), Relax(128), HalfTime(256), Nightcore(512), Flashlight(1024), Autoplay(2048), SpunOut(4096), Relax2(8192), Perfect(16384), Key4(32768), Key5(65536), Key6(131072), Key7(262144), Key8(524288), FadeIn(1048576), Random(2097152), Cinema(4194304), Target(8388608), Key9(16777216), KeyCoop(33554432), Key1(67108864), Key3(134217728), Key2(268435456), ScoreV2(536870912), Mirror(1073741824);

    Mod(long value) {
        this.value = value;
    }

    public static Set<Mod> getMods(long modsValue) {
        if (None.getValue() == modsValue) {
            return new HashSet<>();

        }

        HashSet<Mod> mods = new HashSet<>();
        for (final Mod mod : values()) {
            if (mod.equals(None)) {
                continue;
            }

            if ((mod.getValue() & modsValue) == mod.getValue()) {
                mods.add(mod);
            }

        }


        return mods;

    }

    public final long getValue() {
        return value;
    }

    private final long value;
}
