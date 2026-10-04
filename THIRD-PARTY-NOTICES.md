# Third-party notices

Portfolio Optimizer Classic bundles the open-source components listed below.
Each remains under its own licence; those licences govern those components.
The complete licence texts are in [`LICENSES/`](LICENSES/) and are also packaged
inside the app (**About → Licences**), because the APK is distributed without
the repository.

## Apache License 2.0

Full text: [`LICENSES/Apache-2.0.txt`](LICENSES/Apache-2.0.txt).

Direct dependencies:

| Component | Version in the APK | Copyright |
| --- | --- | --- |
| [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) | 3.1.0 (JitPack tag `v3.1.0`) | Copyright 2020 Philipp Jahoda |
| [Apache Commons Math](https://commons.apache.org/proper/commons-math/) | 3.6.1 | Copyright 2001-2016 The Apache Software Foundation (see the next section) |
| [Gson](https://github.com/google/gson) | 2.11.0 | Copyright 2008 Google Inc. |
| [Material Components for Android](https://github.com/material-components/material-components-android) | 1.13.0 | Copyright Google LLC |
| [AndroidX](https://developer.android.com/jetpack/androidx) AppCompat, Activity, ConstraintLayout, Core, RecyclerView | AppCompat 1.7.1, Activity 1.12.4, ConstraintLayout 2.2.1, Core 1.16.0, RecyclerView 1.3.2 (resolved; the catalogue in `gradle/libs.versions.toml` asks for a lower Core) | Copyright The Android Open Source Project |

Pulled in transitively by the libraries above, all under the Apache License 2.0:

| Component | Version |
| --- | --- |
| Further AndroidX libraries (annotation, arch-core, cardview, collection, concurrent-futures, coordinatorlayout, core-viewtree, cursoradapter, customview, drawerlayout, dynamicanimation, emoji2, fragment, graphics-shapes, interpolator, lifecycle, loader, navigationevent, profileinstaller, resourceinspection, savedstate, startup, tracing, transition, vectordrawable, versionedparcelable, viewpager, viewpager2, compose runtime-annotation) | as resolved by Gradle for `releaseRuntimeClasspath` |
| [Kotlin standard library](https://kotlinlang.org/) | 2.2.10 |
| [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) | 1.9.0 |
| [JetBrains Annotations](https://github.com/JetBrains/java-annotations) | 23.0.0 |
| [JSpecify](https://jspecify.dev/) | 1.0.0 |
| [Error Prone annotations](https://errorprone.info/) | 2.27.0 |
| Guava `listenablefuture` stub | 1.0 |

The copyright in each of these belongs to its respective authors and is stated in
the projects' own repositories.

## Apache Commons Math and the notices it carries

Apache Commons Math ships its own `NOTICE` file, reproduced verbatim here and in
[`LICENSES/commons-math3-NOTICE.txt`](LICENSES/commons-math3-NOTICE.txt):

> Apache Commons Math
> Copyright 2001-2016 The Apache Software Foundation
>
> This product includes software developed at
> The Apache Software Foundation (http://www.apache.org/).
>
> This product includes software developed for Orekit by
> CS Systèmes d'Information (http://www.c-s.fr/)
> Copyright 2010-2012 CS Systèmes d'Information

Its `LICENSE.txt`, reproduced verbatim in
[`LICENSES/commons-math3-LICENSE.txt`](LICENSES/commons-math3-LICENSE.txt),
additionally carries the licence terms of code the library derives from. These
BSD-style licences require the copyright notice and disclaimer to be reproduced
in binary redistributions, which is why they ship with this app:

- Minpack (University of Chicago) &ndash; this product includes software
  developed by the University of Chicago, as Operator of Argonne National
  Laboratory.
- `odex` (Ernst Hairer and Gerhard Wanner)
- Mersenne Twister (Makoto Matsumoto and Takuji Nishimura)
- Sobol direction numbers (Frances Y. Kuo and Stephen Joe)

## Build and test tooling (not shipped in the app)

| Component | Version | Licence |
| --- | --- | --- |
| [Gradle Wrapper](https://gradle.org/) (`gradle/wrapper/gradle-wrapper.jar`, `gradlew`) | see `gradle/wrapper/gradle-wrapper.properties` | Apache License 2.0 |
| [JUnit 4](https://junit.org/junit4/) | 4.13.2 | Eclipse Public License 1.0 |
| [AndroidX Test / Espresso](https://developer.android.com/training/testing) | see `gradle/libs.versions.toml` | Apache License 2.0 |

## Market data

The app does not bundle any market data. At run time your device requests prices
from the endpoints behind Yahoo Finance's website. Those are **not a documented
or licensed API**: Yahoo discontinued its official finance API in 2017 and offers
none today.

- **No permission.** The copyright holder of this project has no agreement with
  Yahoo, and Yahoo has not authorised this app. Yahoo's terms of service restrict
  automated access to its services and commercial use of them. Being reachable
  without a login does not amount to permission.
- **Private use only.** The app is offered for private, non-commercial,
  personal use, at the user's own responsibility. It must not be used to run a
  service, to build a data collection or to redistribute prices.
- **No licence to the data.** Neither this project's licence nor any commercial
  licence for this software grants any right to Yahoo's data. Anyone who wants to
  use the software commercially must first replace the data source with one
  they are licensed to use.
- **No guarantee.** The data may be delayed, incomplete or wrong, and may become
  unavailable at any time, including by Yahoo blocking the app.
- **Identification.** The app identifies itself honestly in its `User-Agent`
  header instead of posing as a web browser, and spaces its requests.

Yahoo and Yahoo Finance are trademarks of their owner. This project is not
affiliated with, endorsed by or sponsored by Yahoo; the names are used only to
say where the data comes from.
