package org.hollowbamboo.chordreader2.chords;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hollowbamboo.chordreader2.chords.regex.ChordParser;
import org.hollowbamboo.chordreader2.helper.TransposeHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class SymbolicChordTest {

    @Test
    public void asciiAccidentalsStillParseAndPrintAsMusicSymbols() {
        Chord chord = ChordParser.parseChord("C#7b9", NoteNaming.EnglishWithSharps);

        assertNotNull(chord);
        assertEquals("C♯7♭9", chord.toPrintableString(NoteNaming.EnglishWithSharps));
    }

    @Test
    public void canonicalDiminishedHalfDiminishedAndMajorSevenParse() {
        assertEquals(
                ChordExtended.Diminished7,
                ChordParser.parseChord("B°7", NoteNaming.English).getSeventh());
        assertEquals(
                ChordExtended.HalfDiminished7,
                ChordParser.parseChord("Bø7", NoteNaming.English).getSeventh());
        assertEquals(
                ChordExtended.Major7,
                ChordParser.parseChord("FΔ7", NoteNaming.English).getSeventh());
    }

    @Test
    public void symbolicChordsAreFoundInChordLines() {
        assertEquals(
                3,
                ChordParser.findChordsInText("C♯7  FΔ7  Bø7", NoteNaming.EnglishWithSharps).size());
    }

    @Test
    public void transpositionStillWrapsTheChromaticCycle() {
        Chord chord = Chord.newChord(
                ChordRoot.C,
                ChordQuality.Major,
                null,
                null,
                null,
                null);

        Chord transposed = TransposeHelper.transposeChord(chord, 1, 0);
        assertEquals(ChordRoot.Db, transposed.getRoot());
        assertEquals("D♭", transposed.toPrintableString(NoteNaming.English));
    }
}
