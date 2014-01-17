The following are a couple of pointers to building the project.

Development Requirements
---------------
- Git on path
- Maven 3.1.1 (or later) on path
- Latest version of the ADT (19+) on path with Android 4.0+ artifacts and all extra artifacts downloaded.
- IntelliJ 13+ is recommended for the IDE.

Project Setup
---------------
- Download a zip copy of the repository.
- You'll want to run something like `projectSetup.sh "App Name" "com.yourcompany.appname"`.
- Delete projectSetup.xml once the project files have been updated and laid out for you.
- Import the project to git (`git init` and then add the remotes, push, etc)
- Open the project up in your favorite IDE.
- You'll also probably need a release key at some point.  Go to the distribution/keys directory and run `generateKey.sh release`

Device/Emulator
---------------
Ensure that you have a emulator with Android 4.1 or higher running or an equivalent device attached. If you would like
the build to start and emulator ensure that an avd with the name `16instrumentationtest` exists.  If you are using an
emulator there is value in setting up HAXM so you use the much faster x86 emulator images.

Full Build
----------
A full build can be executed with the command:

`mvn clean install`

This will build everything as well as deploy the app to the emulator/device and execute the instrumentation tests there.

Release Build
-------------
The release build can be invoked with something like this (ideally on a CI environment):

``mvn clean install -P release -Dbuild.number=123 -Dfingerprint=`git rev-parse HEAD` `` (markdown escaped)

which will in turn sign and zipalign the apk.