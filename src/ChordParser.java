package org.hollowbamboo.chordreader2.chords.regex;

import android.text.TextUtils;
import org.hollowbamboo.chordreader2.chords.*;
import org.hollowbamboo.chordreader2.util.StringUtil;
import org.hollowbamboo.chordreader2.util.UtilLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChordParser {

    private static final UtilLogger log = new UtilLogger(ChordParser.class);

    // Characters that may occur in a written chord. Keep this in sync with the canonical aliases.
    private static final Pattern TOKEN_PATTERN = Pattern.compile("[\\w#♯♭+°øΔ/]+");
    private static final Pattern LOWERCASE_WORD_PATTERN = Pattern.compile("[a-z]+");

    public static Chord parseChord(CharSequence chordString, NoteNaming noteNaming) {
        Pattern pattern = ChordRegexes.getChordPattern(noteNaming);
        Matcher matcher = pattern.matcher(chordString);
        if (matcher.matches()) {
            return convertMatchedPatternToChord(matcher, noteNaming);
        }
        return null;
    }

    private static Chord convertMatchedPatternToChord(Matcher matcher, NoteNaming noteNaming) {
        String root = matcher.group(1);
        String qualityOrSeventh = matcher.group(2);
        String add = matcher.group(3);
        String sus = matcher.group(4);
        String overridingRoot = matcher.group(5);

        ChordRoot chordRoot = noteNaming.findByAlias(root);
        if (chordRoot == null) {
            return null;
        }

        ChordQuality chordQuality = ChordQuality.Major;
        ChordExtended chordSeventh = ChordExtended.findByAlias(qualityOrSeventh);

        if (chordSeventh == null) {
            chordQuality = ChordQuality.findByAlias(qualityOrSeventh);
        } else {
            chordQuality = chordSeventh.getChordQuality();
        }

        ChordAdded chordAdded = ChordAdded.findByAlias(add);
        ChordSuspended chordSuspended = ChordSuspended.findByAlias(sus);

        ChordRoot overridingChordRoot = null;
        if (!TextUtils.isEmpty(overridingRoot)) {
            overridingChordRoot = noteNaming.findByAlias(overridingRoot.substring(1));
        }

        return Chord.newChord(
                chordRoot, chordQuality, chordSeventh, chordAdded, chordSuspended, overridingChordRoot);
    }

    public static boolean containsLineWithChords(String text, NoteNaming noteNaming) {
        if (TextUtils.isEmpty(text == null ? null : text.trim())) {
            return false;
        }

        String[] lines = StringUtil.split(text, "\n");
        for (String line : lines) {
            if (isLineContainingChords(line, noteNaming)) {
                return true;
            }
        }
        log.d("found no lines containing chords in text");
        return false;
    }

    private static boolean isLineContainingChords(String line, NoteNaming noteNaming) {
        return !findChordsInTextInLine(line, 0, noteNaming).isEmpty();
    }

    public static List<ChordInText> findChordsInText(String text, NoteNaming noteNaming) {
        List<ChordInText> result = new ArrayList<>();
        String[] lines = StringUtil.split(text, "\n");
        int offset = 0;

        for (String line : lines) {
            result.addAll(findChordsInTextInLine(line, offset, noteNaming));
            offset += line.length() + 1;
        }

        Collections.sort(result, ChordInText.sortByStartIndex());
        return result;
    }

    private static List<ChordInText> findChordsInTextInLine(String line, int offset, NoteNaming noteNaming) {
        if (TextUtils.isEmpty(line.trim())) {
            return Collections.emptyList();
        }

        List<ChordInText> result = new ArrayList<>();
        TokenInText[] tokens = getTokensInTextFromLine(line);
        ChordInText[] candidateChordsInText = null;

        Pattern chordPattern = ChordRegexes.getChordPattern(noteNaming);
        Pattern chordWithParensPattern = ChordRegexes.getChordWithParensPattern(noteNaming);

        for (int i = 0; i < tokens.length; i++) {
            TokenInText tokenInText = tokens[i];
            String token = tokenInText.getToken();
            Matcher matcher = chordWithParensPattern.matcher(token);

            if (matcher.find()) {
                Chord chord = convertMatchedPatternToChord(matcher, noteNaming);
                ChordInText chordInText = ChordInText.newChordInText(
                        chord,
                        tokenInText.getStartIndex() + matcher.start() + offset + 1,
                        tokenInText.getStartIndex() + matcher.end() + offset - 1);
                result.add(chordInText);
            } else {
                matcher = chordPattern.matcher(token);
                if (matcher.matches()) {
                    Chord chord = convertMatchedPatternToChord(matcher, noteNaming);
                    ChordInText chordInText = ChordInText.newChordInText(
                            chord,
                            tokenInText.getStartIndex() + offset,
                            tokenInText.getEndIndex() + offset);

                    if (candidateChordsInText == null) {
                        candidateChordsInText = new ChordInText[tokens.length];
                    }
                    candidateChordsInText[i] = chordInText;
                }
            }
        }

        if (candidateChordsInText != null) {
            for (int i = 0; i < candidateChordsInText.length; i++) {
                ChordInText candidateChordInText = candidateChordsInText[i];
                if (candidateChordInText == null) {
                    continue;
                }

                String candidateChordString = line.substring(
                        candidateChordInText.getStartIndex() - offset,
                        candidateChordInText.getEndIndex() - offset);

                if (candidateChordString.length() == 1
                        && candidateChordInText.getEndIndex() - offset < line.length()
                        && line.charAt(candidateChordInText.getEndIndex() - offset) == '.') {
                    continue;
                }

                if (candidateChordString.length() == 1
                        && candidateChordInText.getEndIndex() - offset < line.length()
                        && line.charAt(candidateChordInText.getEndIndex() - offset) == '\'') {
                    continue;
                }

                if (candidateChordString.equals("Am")
                        && i + 1 < tokens.length
                        && "Am I".equals(line.substring(tokens[i].getStartIndex(), tokens[i + 1].getEndIndex()))) {
                    continue;
                }

                if ((candidateChordString.equals("A") || candidateChordString.equals("Am"))
                        && i + 1 < tokens.length
                        && LOWERCASE_WORD_PATTERN.matcher(tokens[i + 1].getToken()).matches()
                        && StringUtil.isAllWhitespace(line.substring(
                                tokens[i].getEndIndex(), tokens[i + 1].getStartIndex()))) {
                    continue;
                }

                result.add(candidateChordsInText[i]);
            }
        }

        return result;
    }

    private static TokenInText[] getTokensInTextFromLine(String line) {
        List<TokenInText> result = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(line);
        while (matcher.find()) {
            result.add(TokenInText.newTokenInText(matcher.group(), matcher.start(), matcher.end()));
        }
        return result.toArray(new TokenInText[0]);
    }
}
