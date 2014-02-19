#!/bin/sh
#
# This should only be run once to initialize a project. After the project has been initialized, this file can be deleted.
#
# Usage: projectSetup.sh "App Name" "com.atomicrobot.appname"
#
appName=${1}
appPackage=${2}

function updateFile() 
{
    local file=$1

    local escapedAppNameExp=`echo ${appName}`
    local sedAppNameExp="s/APP_NAME/${escapedAppNameExp}/g"

    local escapedAppPackageExp=`echo ${appPackage} | sed 's/\./\\\\./g'`
    local sedPackageExp="s/PACKAGE_NAME/${escapedAppPackageExp}/g"
	
    sed -i '' "${sedAppNameExp}" $file
    sed -i '' "${sedPackageExp}" $file
}

function updateFiles() 
{
    updateFile pom.xml
    updateFile app/pom.xml
    updateFile instrumentation/pom.xml

    updateFile app/AndroidManifest.xml
    updateFile instrumentation/AndroidManifest.xml

    updateFile app/src/main/java/MainApplication.java
    updateFile app/src/main/java/MainActivity.java
    updateFile app/src/main/java/MainFragment.java
    updateFile app/src/test/java/SampleRobolectricTest.java

    updateFile app/res/layout/activity_main.xml
    updateFile app/res/values/strings.xml

    updateFile instrumentation/src/main/java/AllTests.java
    updateFile instrumentation/src/main/java/DummyTest.java
}

function moveFilesToNewHome() 
{
	local packagePath=`echo ${appPackage} | tr '.' '/'`

    mkdir -p "app/src/main/java/${packagePath}"
    mv "app/src/main/java/MainApplication.java" "app/src/main/java/${packagePath}/MainApplication.java"

	mkdir -p "app/src/main/java/${packagePath}/ui/activities"
	mv "app/src/main/java/MainActivity.java" "app/src/main/java/${packagePath}/ui/activities/MainActivity.java"

	mkdir -p "app/src/main/java/${packagePath}/ui/fragments"
	mv "app/src/main/java/MainFragment.java" "app/src/main/java/${packagePath}/ui/fragments/MainFragment.java"

    mkdir -p "app/src/test/java/${packagePath}"
    mv "app/src/test/java/SampleRobolectricTest.java" "app/src/test/java/${packagePath}/SampleRobolectricTest.java"

    mkdir -p "instrumentation/src/main/java/${packagePath}"
    mv "instrumentation/src/main/java/AllTests.java" "instrumentation/src/main/java/${packagePath}/AllTests.java"

    mkdir -p "instrumentation/src/main/java/${packagePath}"
    mv "instrumentation/src/main/java/DummyTest.java" "instrumentation/src/main/java/${packagePath}/DummyTest.java"
}

updateFiles
moveFilesToNewHome