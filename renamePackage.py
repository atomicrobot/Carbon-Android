#!/usr/bin/python

# Usage renamePackage.sh package
# Ex: renamePackage.sh com.demo.mobile

import os, sys
import shutil
import platform
from functools import reduce

#deletes files and folders
def nuke(folders):
	for f in folders:
		try:
			shutil.rmtree(f)
		except:
			None

	return

def refactorPackagenameInFile(file,oldPackageName, newPackageName):
	f = open(file, 'r')
	contents = f.read()
	f.close()

	refactored = contents.replace(oldPackageName, newPackageName)
	f = open(file, 'w')
	f.write(refactored)
	return

stuffToRemove = [".gradle", ".git", ".idea", "build", "app/build", ".iml", "local.properties"]
dirChar = '\\' if platform.system() == 'Windows' else '/'
args = sys.argv
if (len(args) != 2):
	print("please enter a new package name")
	exit()

new_package = args[1]
original_package = "com.mycompany.myapp"

#fold left with lambda expressions! FTW! a is input (prev value), b is the next item in the list
new_package_directory = dirChar + new_package.lower().replace('.', dirChar) + dirChar
original_package_directory = dirChar + original_package.lower().replace('.', dirChar) + dirChar

#original_package_directory = "/" + reduce((lambda a,b: a + '/' + b), original_package.split('.')) + "/"

print(new_package_directory)
print(original_package_directory)

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

	#if (len(files) > 0):
	#	reduce ((lambda a,b: print(os.path.join(root, b)) if b.endswith('.java') else None), files)


#nuke(stuffToRemove)

#os.system('git init')
#os.system('git add .')
#os.system('git commit -q -m "Initial import"')
