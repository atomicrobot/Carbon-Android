#!/usr/bin/env python3

# Usage
# ./generator.py --package com.atomicrobot.carbon --screen PurchaseConfirmation

import os
import argparse
from string import Template

try:
    import inflection
    import pystache
except ImportError:
    print("Please run 'pip3 install inflection pystache'")
    exit(0)


parser = argparse.ArgumentParser(
    description='Generates boilerplate code for an Android app')
parser.add_argument(
    '--package', required=True, help="applicationId for the app")
parser.add_argument(
    '--screen', help="The screen name to generate (UpperCamelCase format)")
args = parser.parse_args()


def screen(args):
    return {
        'package': args.package,
        'packageDirs': args.package.replace(".", os.sep),
        'screenUpperCamel': args.screen,
        'screenLowerCamel': inflection.camelize(args.screen, False),
        'screenLowerCase': args.screen.lower(),
        'screenUnderscore': inflection.underscore(args.screen)
    }, [
        createFile(
            'templates/screen/Activity.kt.mustache',
            'src/main/java/$packageDirs/ui/$screenLowerCase/${screenUpperCamel}Activity.kt'),
        createFile(
            'templates/screen/Fragment.kt.mustache',
            'src/main/java/$packageDirs/ui/$screenLowerCase/${screenUpperCamel}Fragment.kt'),
        createFile(
            'templates/screen/ViewModel.kt.mustache',
            'src/main/java/$packageDirs/ui/$screenLowerCase/${screenUpperCamel}ViewModel.kt'),
        createFile(
            'templates/screen/activity.xml.mustache',
            'src/main/res/layout/activity_${screenUnderscore}.xml'),
        createFile(
            'templates/screen/content.xml.mustache',
            'src/main/res/layout/content_${screenUnderscore}.xml'),
        createFile(
            'templates/screen/fragment.xml.mustache',
            'src/main/res/layout/fragment_${screenUnderscore}.xml'),
        createFile(
            'templates/screen/ActivityTests.kt.mustache',
            'src/androidTest/java/$packageDirs/ui/$screenLowerCase/${screenUpperCamel}ActivityEspressoTests.kt'),
        createFile(
            'templates/screen/ViewModelTests.kt.mustache',
            'src/test/java/$packageDirs/ui/$screenLowerCase/${screenUpperCamel}ViewModelTests.kt'),
        updateFile(
            'templates/screen/ViewModelFactoryModule_ImportPartial.kt.mustache',
            'src/main/java/$packageDirs/ui/ViewModelFactoryModule.kt',
            '// GENERATOR - MORE IMPORTS //'),
        updateFile(
            'templates/screen/ViewModelFactoryModule_ViewModel.kt.mustache',
            'src/main/java/$packageDirs/ui/ViewModelFactoryModule.kt',
            '// GENERATOR - MORE VIEW MODELS //'),
        updateFile(
            'templates/screen/ApplicationComponent_ImportPartial.kt.mustache',
            'src/main/java/$packageDirs/app/ApplicationComponent.kt',
            '// GENERATOR - MORE IMPORTS //'),
        updateFile(
            'templates/screen/ApplicationComponent_ActivityPartial.kt.mustache',
            'src/main/java/$packageDirs/app/ApplicationComponent.kt',
            '// GENERATOR - MORE ACTIVITIES //'),
        updateFile(
            'templates/screen/AndroidManifest_ActivityPartial.xml.mustache',
            'src/main/AndroidManifest.xml',
            '<!-- GENERATOR - MORE ACTIVITIES -->')
    ]


def generate(params, fileCommands):
    for fc in fileCommands:
        fc(params)
    print('Done!')


def createFile(templateFile, destination):
    return lambda params: _createFile(params, templateFile, destination)


def updateFile(templateFile, existingFile, markerText):
    return lambda params: _updateFile(params, templateFile, existingFile, markerText)


def _createFile(params, templateFile, destinationFile):
    destination = Template(destinationFile).substitute(**params)
    print(f'Creating {destination}')
    with open(templateFile, 'r') as tf:
        rendered = pystache.render(tf.read(), params)
        with createAndOpen(os.getcwd() + os.sep + destination, 'w') as df:
            df.write(rendered)


def _updateFile(params, templateFile, existingFile, markerText):
    existing = Template(existingFile).substitute(**params)
    print(f'Updating {existing}')
    with open(templateFile, 'r') as tf:
        with createAndOpen(os.getcwd() + os.sep + existing, 'r+') as ef:
            existing = ef.read()
            rendered = pystache.render(tf.read(), params)
            newContent = existing.replace(markerText, rendered)
            ef.seek(0)
            ef.write(newContent)


def createAndOpen(filename, mode):
    os.makedirs(os.path.dirname(filename), exist_ok=True)
    return open(filename, mode)


generate(*screen(args))
