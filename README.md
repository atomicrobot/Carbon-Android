## Project Setup

### Setting up a new project
- `git clone git@github.com:atomicrobot/Carbon-Android.git`
- `cd Carbon-Android`
- Download Python if needed https://www.python.org/downloads/
- `python renamePackage.py com.demo.mobile`
- (optional) Rename the project directory from `Carbon-Android` to something meaningful.
- (optional) Add a git remote to the project (a clean repository has been setup for you).
- (optional) Adding a new activity: `./gradlew app:screen -Ppackage=com.demo.mobile -Pscreen=SignIn`

### First Run
- When you open up a project for the first time, if you get a "Please specify Android SDK" message when trying to run the app then you need to run a Gradle Sync from Android Studio.

### Crashlytics
To register an app to an organization you will need to go to the Crashlytics web dashboard, go to
the organization settings, and then get the keys and secrets it displays.
- Update `app/src/main/AndroidManifest.xml` with the organization api key.

### Signing configs
*DO NOT* use the demo keystore in your apps.

- Note: If you are creating signing keys, consider setting up Google Play App Signing (https://developer.android.com/studio/publish/app-signing.html#google-play-app-signing)
- Run `distribution/keys/generateKey.sh release` and update `app/build.gradle` signing configs appropriately.

## Quality

### Automated Checks
- Unit tests - `./gradlew allChecks`

## Gradle and plugins

To see what the dependency tree currently looks like:
- `./gradlew app:dependencies`

## Continuous Integration Setup

These are written in the context of a TeamCity CI setup.

## Jacoco Test Report
  - `./gradlew clean jacocoTestReport`
  - To view report go to <ProjectDir>/build/reports/jacoco/jacocoTestReport/html/index.html

### Gradle tasks
This will pull in the current Git SHA and auto incrementing build number as part of the build.

`continuousIntegration -Pfingerprint=%build.vcs.number% -PbuildNumber=%build.counter% -PdisablePreDex`

Also make sure the CI server is set to use the Gradle wrapper.

### Artifact Paths
```
app/build/outputs/apk/**/*-release.apk => apks
app/build/outputs/mapping/**/release/mapping.txt => proguard

app/build/reports/lint-results.html => quality/lint
app/build/reports/pmd/ => quality/pmd
app/build/reports/checkstyle/ => quality/checkstyle

app/build/reports/tests/testDevDebugUnitTest => quality/tests
app/build/reports/androidTests/connected/flavors/DEV => quality/androidTests
app/build/reports/coverage/dev/debug/ => quality/coverage
```

### Project Reports
- "Lint" with a start page of `quality/lint/index.html`
- "PMD" with a start page of `quality/pmd/pmd.html`
- "Unit Tests" with a start page of `quality/tests/index.html`
- "Integration Tests" with a start page of `quality/integrationTests/index.html`

### Supported Android Versions
The Carbon app will only support those Android versions that Google Security continues to support:
https://endoflife.date/android

As of July 15, 2022, the latest Android version that's supported is Android 10. Therefore the minSDK
is set to `29`

License
=======

    Copyright 2014-2019 Atomic Robot, LLC

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
