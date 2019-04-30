#!/usr/bin/python

# Usage renamePackage.py package
# Ex: renamePackage.py com.demo.mobile

import os, sys
import shutil
import platform
from functools import reduce

stuffToRemove = [".gradle", ".git", ".idea", "build", "app/build", ".iml", "local.properties"]
dirChar = os.sep
args = sys.argv
if (len(args) != 2):
	print("please enter a new package name")
	exit()

new_package = args[1]
original_package = "com.mycompany.myapp"
new_package_directory = dirChar + new_package.lower().replace('.', dirChar) + dirChar
original_package_directory = dirChar + original_package.lower().replace('.', dirChar) + dirChar

#deletes files and folders
def nuke(folders):
	for f in folders:
		f = f.replace('/', dirChar)#Make sure it has the correct dir separator
		print('Removing ' + f)
		try:
			if (platform.system() == 'Windows'):
				os.system('rmdir /s /q ' + f)
			else:
				os.system('rm -rf ' + f)
		except:
			None

	return

def refactorPackagenameInFile(file,oldPackageName, newPackageName):
	#only refactor these files
	if (file.endswith(".java") or file.endswith(".kt") or file.endswith(".xml") or file.endswith(".properties") or file.endswith(".txt") or file.endswith(".gradle")):
		f = open(file, 'r')
		contents = f.read()
		f.close()

		refactored = contents.replace(oldPackageName, newPackageName)
		f = open(file, 'w')
		f.write(refactored)
		f.close()
	return

def refactorAllFolders():
	for root, dir, files in os.walk('app'):
		for f in files:
			fpath = os.path.join(root, f)
			if original_package_directory in fpath:
				oldPath = fpath
				newPath = fpath.replace(original_package_directory,new_package_directory)
				try:#attempt to make the new package directory incase it doesn't exist
					os.makedirs((root+dirChar).replace(original_package_directory, new_package_directory))
				except:
					None
				shutil.copy(oldPath, newPath)#copy the file to the new path
				refactorPackagenameInFile(newPath, original_package, new_package)
			else:
				refactorPackagenameInFile(fpath, original_package, new_package)

	for root, dir, files in os.walk('app/src'):
		#only use the first iteration, we just want the immidate children of this folder
		for folder in dir:
			folderpath = 'app' + dirChar + 'src' + dirChar + folder + dirChar + 'java' + dirChar + 'com' + dirChar + 'mycompany'
			shutil.rmtree(folderpath)
		break

nuke(stuffToRemove)
refactorAllFolders()

os.system('git init')
os.system('git add .')
os.system('git commit -q -m "Initial import from github.com/atomicrobot/Carbon-Android"')

print('all done :)')
os.system('git log --oneline')
