package org.hollowbamboo.chordreader2.chords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Basic chord quality. The first alias is the canonical printed form. */
public enum ChordQuality {

    Major(Arrays.asList("", "major", "maj", "M")),
    Minor(Arrays.asList("m", "minor", "min")),
    Augmented(Arrays.asList("+", "aug", "augmented", "#5", "♯5")),
    Diminished(Arrays.asList("°", "dim", "diminished"));

    private final List<String> aliases;

    ChordQuality(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public static List<String> getAllAliases() {
        List<String> result = new ArrayList<>();
        for (ChordQuality quality : values()) {
            result.addAll(quality.aliases);
        }
        return result;
    }

    private static final Map<String, ChordQuality> lookupMap = new HashMap<>();

    static {
        for (ChordQuality quality : values()) {
            for (String alias : quality.aliases) {
                lookupMap.put(alias.toLowerCase(Locale.ROOT), quality);
            }
        }
    }

    public static ChordQuality findByAlias(String alias) {
        if (alias.equals("m")) {
            return Minor;
        }
        if (alias.equals("M")) {
            return Major;
        }
        return lookupMap.get(alias.toLowerCase(Locale.ROOT));
    }
}
