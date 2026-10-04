# Portfolio Optimizer Classic

Android app (Java, no Compose) that rebalances a private securities portfolio with
three classical strategies, blended by three sliders. Data comes from Yahoo
Finance's public endpoints and is stored locally in `filesDir/portfolio.json`.
The maintainer writes German — answer in German.

## Where things are

- `app/src/main/java/de/mm/portfoliooptimizerclassic/` — 12 classes, three activities
  (`MainActivity`, `ManageSecuritiesActivity`, `OptimizeActivity`), the maths in
  `PortfolioOptimizer`, the data layer in `Portfolio` / `Security` / `DataConverter`,
  networking in `YahooFinanceService`.
- `tools/generate_icons.py` regenerates the legacy mipmaps from the same geometry as
  `res/drawable/ic_launcher_*.xml`. Change the vectors and the script together, never
  one alone. Needs Pillow.
- `.github/workflows/build.yml` builds a release APK on every push and publishes a
  GitHub release on a `v*` tag. `docs/RELEASING.md` covers the signing secrets.

## Current state

Released: `v1.0.0` and `v1.0.1` exist. CI builds green, the code compiles and the
unit tests pass. The feared `compileSdk { release(36) { minorApiLevel = 1 } }`
problem did not materialise: the workflow's fallback to plain
`platforms;android-36` is what actually carries the build.

The legal review changed how releases look: the asset is now always
`PortfolioOptimizerClassic.apk` (plus `SHA256SUMS.txt`), so the README's
`releases/latest/download/…` link is stable. `v1.0.0`/`v1.0.1` predate that and
predate the licence texts inside the APK; the stable link only works from the
first release after the change.

One thing is open: the four `RELEASE_*` secrets do not exist yet, so published
APKs are **debug-signed** (the release notes say so). They install, but no later
signed build can update them. Add the secrets as `docs/RELEASING.md` describes and
cut a new tag.

## Commands

    ./gradlew testDebugUnitTest      # unit tests
    ./gradlew assembleRelease        # APK in app/build/outputs/apk/release/

Needs JDK 21 and Android SDK platform 36 (minor API level 36.1). minSdk is 24.

## Conventions

- Every user-facing string lives in `res/values/strings.xml` **and**
  `res/values-de/strings.xml`. The two must stay in sync, including the number and
  order of format placeholders, or the build fails.
- `README.md` is English and `README.de.md` is German; they are translations of one
  another. Change one and translate the change into the other in the same commit.
  Nothing checks this, so it only holds if you do it.
- The licence is PolyForm Noncommercial 1.0.0: source-available, not OSI open source.
  Keep the canonical text in `LICENSE` unchanged. Do not suggest an OSI licence or
  a licence badge. A new dependency must be permissively licensed and must be added
  to `THIRD-PARTY-NOTICES.md`.
- The APK ships without the repository, so the licence texts travel inside it:
  `app/build.gradle.kts` copies `LICENSE`, `THIRD-PARTY-NOTICES.md` and `LICENSES/`
  into `assets/legal/`, and About → Licences shows them. Android strips the
  `META-INF/LICENSE*`/`NOTICE*` files out of dependency jars, so a dependency that
  carries notices of its own (Commons Math does: BSD-style Minpack, odex, Mersenne
  Twister, Sobol) needs them copied verbatim into `LICENSES/` and listed in
  `LegalNotices.FILES` and `LegalComplianceTest`. CI fails if they are missing from
  the APK.
- Yahoo is reached through undocumented endpoints that Yahoo has not authorised
  third parties to use. Keep the `User-Agent` honest (never pose as a browser),
  keep the pause between requests, and keep the disclaimer in the About screen, on
  the optimiser screen and in the READMEs. Do not offer a commercial licence for
  the app without saying the data source must be replaced first.
- `.idea/` per-machine files (`deploymentTargetSelector.xml` holds a device serial
  and a local path) are gitignored; do not re-add them.
- `isMinifyEnabled = false` on purpose: the Gson keep rules in `proguard-rules.pro`
  are written but never verified on a device.
- The working copy is Windows with CRLF. On a Linux side, set
  `git config core.autocrlf true` in the repo first, or every file shows as modified.

## Known, deliberately unfixed

- Rotating `ManageSecuritiesActivity` while editing a position adds a duplicate
  instead of updating it — needs `onSaveInstanceState`.
- `Security` is not fully thread-safe while a sync rewrites its history. The crash is
  gone (indices are clamped); a briefly inconsistent chart is still possible.
- Monthly prices are interpolated to daily, which understates volatility by roughly a
  factor of 30 and skews comparisons against securities with daily data.
