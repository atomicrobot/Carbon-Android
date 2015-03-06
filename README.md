### Crashlytics
To register an app to an organization you will need to go to the Crashlytics web dashboard, go to
the organization settings, and then get the keys and secrets it displays.
- Update `app/src/main/AndroidManifest.xml` with the organization api key.
- Update `app/crashlytics.properties` with the organization build secret.

### Signing configs
*DO NOT* use the demo keystore in your apps.  Run `distribution/keys/generateKey.sh` and update
`app/build.gradle` signing configs appropriately.