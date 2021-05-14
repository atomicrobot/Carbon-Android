#!/usr/bin/env bash

cd ..
rm -rf android-starter-project-copy
cp -R android-starter-project android-starter-project-copy
cd android-starter-project-copy
python renamePackage.py com.demo.mobile
grep -IR 'carbon' . | grep gradle
find . -name 'carbon' | grep gradle