## Project Setup

### Setting up a new project
- `git clone git@github.com:madebyatomicrobot/android-starter-project.git`
- `cd android-starter-project`
- Download Python if needed https://www.python.org/downloads/
- `python renamePackage.py com.demo.mobile`
- (optional) Rename the project directory from `android-starter-project` to something meaningful.
- (optional) Add a git remote to the project (a clean repository has been setup for you).
- (optional) Adding a new activity: `./gradlew app:screen -Ppackage=com.demo.mobile -Pscreen=SignIn`

### Crashlytics
To register an app to an organization you will need to go to the Crashlytics web dashboard, go to
the organization settings, and then get the keys and secrets it displays.
- Update `app/src/main/AndroidManifest.xml` with the organization api key.

### Signing configs
*DO NOT* use the demo keystore in your apps.  Run `distribution/keys/generateKey.sh release` and update
`app/build.gradle` signing configs appropriately.

## Quality

### Automated Checks
- Unit tests - `./gradlew allChecks`

## Gradle and plugins

To see what the dependency tree currently looks like:
`./gradlew app:dependencies`

To see what dependencies might be out of date:
`./gradlew app:dependencyUpdates -Drevision=release`

## Continuous Integration Setup

These are written in the context of a TeamCity CI setup.

### Gradle tasks
This will pull in the current Git SHA and auto incrementing build number as part of the build.

`continuousIntegration -Pfingerprint=%build.vcs.number% -PbuildNumber=%build.counter% -PdisablePreDex`

Also make sure the CI server is set to use the Gradle wrapper.

### Artifact Paths
```
app/build/outputs/apk/dev/release/*-release.apk => apks
app/build/outputs/apk/prod/release/*-release.apk => apks

app/build/outputs/mapping/dev/release/mapping.txt => proguard/dev/mapping.txt
app/build/outputs/mapping/prod/release/mapping.txt => proguard/prod/mapping.txt

app/build/reports/lint-results.html => quality/lint
app/build/reports/findbugs/ => quality/findbugs
app/build/reports/pmd/ => quality/pmd
app/build/reports/checkstyle/ => quality/checkstyle

app/build/reports/tests/testDevDebugUnitTest => quality/tests
app/build/reports/androidTests/connected/flavors/DEV => quality/androidTests
```

### Project Reports
- "Lint" with a start page of `quality/lint/index.html`
- "Findbugs" `quality/findbugs/findbugs.html`
- "PMD" with a start page of `quality/pmd/pmd.html`
- "Unit Tests" with a start page of `quality/tests/index.html`
- "Integration Tests" with a start page of `quality/integrationTests/index.html`

License
=======

    Copyright 2016 Atomic Robot, LLC

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
