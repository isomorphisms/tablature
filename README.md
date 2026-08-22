# Tablature

An Android chord-chart reader derived from ChordReader 2. It can fetch and save chord charts, highlight and auto-scroll them, transpose chords or account for a capo, and show common chord variations.

## Source layout

The application is deliberately flat:

- `src/` — handwritten Java and Kotlin source
- `test/` — unit tests
- `res/` — Android resources; Android requires the resource-type subdirectories here
- `AndroidManifest.xml` — application manifest
- `build.gradle` — the single Android application module

Package names remain unchanged for compatibility, but they no longer dictate a matching filesystem hierarchy.

## Chord notation

Printed chord names prefer ordinary music symbols while the parser continues to accept older ASCII and word spellings:

- `♯` instead of `#`
- `♭` instead of `b`
- `°` for diminished
- `ø7` for half-diminished seventh
- `Δ7` for major seventh

For example, `C#7b9` is still accepted and is printed as `C♯7♭9`.

The transposition core is being moved toward small symbolic operations as well: its semitone displacement is `Δ`, and shifting a chord root is expressed internally as `root + Δ`.

## Origin and attribution

This repository is a fork of ChordReader 2 by HollowBamboo, itself derived from the earlier ChordReader work by Nolan Lawson and Marcel Klehr. The existing project history, copyright notices, translations, assets, and license remain in the repository.

The original Android application was designed to remain useful on older devices and supports local files, transposition, capo handling, chord highlighting, auto-scroll, and chord variations.

## License

GNU GPLv3+; see `LICENSE`.
