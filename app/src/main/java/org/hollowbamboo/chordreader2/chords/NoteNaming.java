package org.hollowbamboo.chordreader2.chords;

import org.hollowbamboo.chordreader2.R;
import org.hollowbamboo.chordreader2.util.EnumMultiMapBuilder;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/** Note names accepted by the parser. The first alias is the canonical printed form. */
public enum NoteNaming {

    English(R.string.pref_note_naming_english,
            new EnumMultiMapBuilder<ChordRoot, String>(ChordRoot.class)
                    .put(ChordRoot.A, "A")
                    .put(ChordRoot.Bb, "B♭", "Bb", "A#", "Asharp", "Bflat", "A♯")
                    .put(ChordRoot.B, "B", "Cb", "Cflat", "C♭")
                    .put(ChordRoot.C, "C", "B#", "Bsharp", "B♯")
                    .put(ChordRoot.Db, "D♭", "Db", "C#", "Dflat", "Csharp", "C♯")
                    .put(ChordRoot.D, "D")
                    .put(ChordRoot.Eb, "E♭", "Eb", "D#", "Eflat", "Dsharp", "D♯")
                    .put(ChordRoot.E, "E", "Fb", "Fflat", "F♭")
                    .put(ChordRoot.F, "F", "E#", "Esharp", "E♯")
                    .put(ChordRoot.Gb, "G♭", "Gb", "F#", "Gflat", "Fsharp", "F♯")
                    .put(ChordRoot.G, "G")
                    .put(ChordRoot.Ab, "A♭", "Ab", "G#", "Aflat", "Gsharp", "G♯")
                    .build()),

    EnglishWithSharps(R.string.pref_note_naming_english_sharps,
            new EnumMultiMapBuilder<ChordRoot, String>(ChordRoot.class)
                    .put(ChordRoot.A, "A")
                    .put(ChordRoot.Bb, "A♯", "A#", "Bb", "B♭", "Asharp", "Bflat")
                    .put(ChordRoot.B, "B", "Cb", "Cflat", "C♭")
                    .put(ChordRoot.C, "C", "B#", "Bsharp", "B♯")
                    .put(ChordRoot.Db, "C♯", "C#", "Db", "D♭", "Dflat", "Csharp")
                    .put(ChordRoot.D, "D")
                    .put(ChordRoot.Eb, "D♯", "D#", "Eb", "E♭", "Eflat", "Dsharp")
                    .put(ChordRoot.E, "E", "Fb", "Fflat", "F♭")
                    .put(ChordRoot.F, "F", "E#", "Esharp", "E♯")
                    .put(ChordRoot.Gb, "F♯", "F#", "Gb", "G♭", "Gflat", "Fsharp")
                    .put(ChordRoot.G, "G")
                    .put(ChordRoot.Ab, "G♯", "G#", "Ab", "A♭", "Aflat", "Gsharp")
                    .build()),

    NorthernEuropean(R.string.pref_note_naming_northern,
            new EnumMultiMapBuilder<ChordRoot, String>(ChordRoot.class)
                    .put(ChordRoot.A, "A")
                    .put(ChordRoot.Bb, "B", "A♯", "A#", "Asharp")
                    .put(ChordRoot.B, "H")
                    .put(ChordRoot.C, "C")
                    .put(ChordRoot.Db, "C♯", "C#", "Db", "D♭", "Dflat", "Csharp")
                    .put(ChordRoot.D, "D")
                    .put(ChordRoot.Eb, "D♯", "D#", "Eb", "E♭", "Eflat", "Dsharp")
                    .put(ChordRoot.E, "E", "Fb", "Fflat", "F♭")
                    .put(ChordRoot.F, "F", "E#", "Esharp", "E♯")
                    .put(ChordRoot.Gb, "F♯", "F#", "Gb", "G♭", "Gflat", "Fsharp")
                    .put(ChordRoot.G, "G")
                    .put(ChordRoot.Ab, "G♯", "G#", "Ab", "A♭", "Aflat", "Gsharp")
                    .build()),

    SouthernEuropean(R.string.pref_note_naming_southern,
            new EnumMultiMapBuilder<ChordRoot, String>(ChordRoot.class)
                    .put(ChordRoot.A, "La")
                    .put(ChordRoot.Bb, "Ti♭", "Tib", "La#", "La♯")
                    .put(ChordRoot.B, "Ti")
                    .put(ChordRoot.C, "Do")
                    .put(ChordRoot.Db, "Re♭", "Reb", "RŽb", "Do#", "Do♯")
                    .put(ChordRoot.D, "Re", "RŽ")
                    .put(ChordRoot.Eb, "Mi♭", "Mib", "Re#", "Re♯")
                    .put(ChordRoot.E, "Mi")
                    .put(ChordRoot.F, "Fa")
                    .put(ChordRoot.Gb, "So♭", "Solb", "Sob", "Fa#", "Fa♯")
                    .put(ChordRoot.G, "Sol", "So")
                    .put(ChordRoot.Ab, "La♭", "Lab", "So#", "Sol#", "So♯", "Sol♯")
                    .build());

    private final EnumMap<ChordRoot, List<String>> noteNames;
    private final Map<String, ChordRoot> lookupMap = new HashMap<>();
    private final int printableNameResource;

    NoteNaming(int printableNameResource, EnumMap<ChordRoot, List<String>> noteNames) {
        if (noteNames.size() != 12) {
            throw new IllegalArgumentException("must have 12 notes");
        }
        this.noteNames = noteNames;
        this.printableNameResource = printableNameResource;

        for (ChordRoot root : ChordRoot.values()) {
            for (String alias : noteNames.get(root)) {
                lookupMap.put(alias.toLowerCase(Locale.ROOT), root);
            }
        }
    }

    public List<String> getAllNames() {
        List<String> result = new ArrayList<>();
        for (ChordRoot root : ChordRoot.values()) {
            result.addAll(noteNames.get(root));
        }
        return result;
    }

    public ChordRoot findByAlias(String alias) {
        return lookupMap.get(alias.toLowerCase(Locale.ROOT));
    }

    public List<String> getNames(ChordRoot root) {
        return noteNames.get(root);
    }

    public int getPrintableNameResource() {
        return printableNameResource;
    }
}
