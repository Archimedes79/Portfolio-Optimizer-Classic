# Release-Anleitung

## Wie ein Release entsteht

Der Workflow [`.github/workflows/build.yml`](../.github/workflows/build.yml)
läuft bei jedem Push auf `master`, bei jedem Pull Request und bei jedem Tag,
der mit `v` beginnt.

- **Push / PR:** Unit-Tests + Release-APK. Die APK hängt als Artefakt am
  Workflow-Lauf (90 Tage).
- **Tag `v1.2.3`:** zusätzlich ein GitHub Release mit `PortfolioOptimizerClassic.apk`
  und `SHA256SUMS.txt` als Anhang. `versionName` wird aus dem Tag abgeleitet
  (`v1.2.3` &rarr; `1.2.3`), `versionCode` aus der Lauf-Nummer.

Der Dateiname der APK ist **absichtlich fest**, damit der Link
`https://github.com/Archimedes79/Portfolio-Optimizer-Classic/releases/latest/download/PortfolioOptimizerClassic.apk`
in der README für jedes künftige Release gilt. Mit welchem Schlüssel signiert
wurde, steht in den Release-Notizen. Die Releases `v1.0.0` und `v1.0.1`
stammen aus der Zeit davor und heißen noch `PortfolioOptimizerClassic-<tag>-debugsigned.apk`;
der feste Link greift erst ab dem ersten Release danach.

Der Workflow läuft auf einem gewöhnlichen GitHub-Runner (`ubuntu-latest`), ohne
Docker. Vor der Veröffentlichung prüft er, dass die Lizenztexte in der APK
stecken (`assets/legal/`) &ndash; fehlen sie, schlägt der Build fehl, statt eine
APK zu veröffentlichen, die gegen die Lizenzen verstößt. Gradle kopiert sie beim
Bauen aus `LICENSE`, `THIRD-PARTY-NOTICES.md` und `LICENSES/` in die APK; es gibt
keine zweite Kopie zu pflegen.

Ein Release veröffentlichen:

```bash
git tag v1.0.0
git push origin v1.0.0
```

## Signaturschlüssel hinterlegen

Ohne Schlüssel wird die APK mit dem Android-Debug-Key signiert: installierbar,
aber nicht als Update einer anders signierten Installation und nicht
Play-Store-tauglich. Mit eigenem Schlüssel ändert sich nichts am Ablauf &ndash;
der Workflow greift automatisch darauf zu, sobald die Secrets existieren.

**1. Schlüssel erzeugen** (einmalig, lokal, gut aufbewahren &ndash; geht er
verloren, lässt sich die App nie wieder aktualisieren):

```bash
keytool -genkeypair -v \
  -keystore release.jks \
  -keyalg RSA -keysize 4096 -validity 10000 \
  -alias portfolio-optimizer
```

**2. In Base64 umwandeln:**

```bash
base64 -w0 release.jks > release.jks.base64     # Windows/PowerShell:
# [Convert]::ToBase64String([IO.File]::ReadAllBytes("release.jks")) > release.jks.base64
```

**3. Als Secrets hinterlegen** unter *Settings &rarr; Secrets and variables
&rarr; Actions &rarr; New repository secret*:

| Secret | Inhalt |
| --- | --- |
| `RELEASE_KEYSTORE_BASE64` | Inhalt von `release.jks.base64` |
| `RELEASE_KEYSTORE_PASSWORD` | Passwort des Keystores |
| `RELEASE_KEY_ALIAS` | `portfolio-optimizer` |
| `RELEASE_KEY_PASSWORD` | Passwort des Schlüssels |

Der nächste Build ist signiert; die Release-Notizen nennen dann den Upload-Key
statt des Debug-Keys. Der Dateiname bleibt `PortfolioOptimizerClassic.apk`.

**Die `release.jks` gehört nicht ins Repository.** `.gitignore` schließt
`*.jks`, `*.keystore` und `keystore.properties` bereits aus.

## Lokal signiert bauen

Dieselben Werte funktionieren lokal über `gradle.properties` im Benutzerordner
(`~/.gradle/gradle.properties`, nicht im Projekt):

```properties
RELEASE_KEYSTORE_FILE=C:/Users/<du>/keys/release.jks
RELEASE_KEYSTORE_PASSWORD=...
RELEASE_KEY_ALIAS=portfolio-optimizer
RELEASE_KEY_PASSWORD=...
```

Danach signiert `./gradlew assembleRelease` automatisch mit diesem Schlüssel.
