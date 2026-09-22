package org.hollowbamboo.chordreader2.chords;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Added tones and power chords. The first alias is the canonical printed form. */
public enum ChordAdded {

    Add6(Arrays.asList("add6")),
    Add9(Arrays.asList("add9", "2")),
    Add11(Arrays.asList("add11", "4")),
    Augmented9(Arrays.asList("+9", "aug9")),
    AugmentedMajor9(Arrays.asList("+M9", "augmaj9")),
    Major6(Arrays.asList("6", "maj6", "major6", "M6")),
    Major6Flat5(Arrays.asList("6♭5", "6b5")),
    SixNine(Arrays.asList("6/9", "6add9", "6/add9", "69")),
    NineFlat5(Arrays.asList("9♭5", "9b5")),
    NineSharp5(Arrays.asList("9♯5", "9#5")),
    PowerChord(Arrays.asList("5"));

    private final List<String> aliases;

    ChordAdded(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public static List<String> getAllAliases() {
        List<String> result = new ArrayList<>();
        for (ChordAdded added : values()) {
            result.addAll(added.aliases);
        }
        return result;
    }

    private static final Map<String, ChordAdded> lookupMap = new HashMap<>();

    static {
        for (ChordAdded added : values()) {
            for (String alias : added.aliases) {
                lookupMap.put(alias.toLowerCase(Locale.ROOT), added);
            }
        }
    }

    public static ChordAdded findByAlias(String alias) {
        return lookupMap.get(alias.toLowerCase(Locale.ROOT));
    }
}
