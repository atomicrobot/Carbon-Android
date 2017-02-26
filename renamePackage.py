#!/usr/bin/python

# Usage renamePackage.sh package
# Ex: renamePackage.sh com.demo.mobile

import os, sys
import shutil
import platform
from functools import reduce

stuffToRemove = [".gradle", ".git", ".idea", "build", "app/build", ".iml", "local.properties"]
dirChar = '\\' if platform.system() == 'Windows' else '/'
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
		try:
			if (platform.system() == 'Windows'):
				f = f.replace('/', '\\')
				print('Removing ' + f)
				os.system('rmdir /s /q ' + f)
			else:
				print('Removing ' + f)
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

		#if (len(files) > 0):
		#	reduce ((lambda a,b: print(os.path.join(root, b)) if b.endswith('.java') else None), files)

nuke(stuffToRemove)
refactorAllFolders()

f = open('complete.txt', 'r')
artwork = f.read()
f.close()
os.remove('complete.txt')

os.system('git init')
os.system('git add .')
os.system('git commit -q -m "Initial import from github.com/madebyatomicrobot/android-starter-project"')

#print artwork :)
print(artwork)