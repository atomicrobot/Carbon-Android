## Project Setup

### Crashlytics
To register an app to an organization you will need to go to the Crashlytics web dashboard, go to
the organization settings, and then get the keys and secrets it displays.
- Update `app/src/main/AndroidManifest.xml` with the organization api key.
- Update `app/crashlytics.properties` with the organization build secret.

### Signing configs
*DO NOT* use the demo keystore in your apps.  Run `distribution/keys/generateKey.sh release` and update
`app/build.gradle` signing configs appropriately.

## Quality

### Static Analysis
- Lint - `./gradlew lint`
- FindBugs - `./gradlew findbugs`
- PMD - `./gradlew pmd`

### Automated Tests
- Unit tests - `./gradlew test`
- Instrumentation tests - `./gradlew connectedCheck`

## Build Server

### Building the app
This is approximately the gradle command to use for a TeamCity configuration:
`app:assembleRelease -Pfingerprint=%build.vcs.number% -PbuildNumber=%build.number%`
This will pull in the current Git SHA and auto incrementing build number as part of the build.

### Build Chain
Recommend setting up a three step build chain.

1. Run the static analysis tools (see above)
2. Run the automated tests (see above)
3. Build the app (see above)
4. Optional: have the build server automatically tag the build in version control

### Performance gain
Add `-PdisablePreDex` to all of the Gradle commands that would result in building the app module to eliminate some overhead.