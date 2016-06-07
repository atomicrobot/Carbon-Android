#!/bin/sh
 
# Usage renamePackage.sh package
# Ex: renamePackage.sh com.demo.mobile

export new_package="$1"
export original_package="com.mycompany.myapp"
 
export new_package_directory="/"`echo $new_package | tr '.' '/'`"/"
export original_package_directory="/"`echo $original_package | tr '.' '/'`"/"
 
# These params have a different offset because of xargs
parent_mv(){
    source="$0";
    target="$1";
    targetDir="$(dirname $target)"
    mkdir -p "$targetDir"
    mv "$source" "$targetDir"
}
export -f parent_mv
 
update_filetypes() {
    # Would be nice to externalize the regex
    find . -name "$1" -not -name "*.png" -not -path "gradle" \
    | gawk -v replacement="\\\\1$new_package_directory\\\\2" '{ updated = gensub(/(.+)\/com\/mycompany\/myapp\/(.+)/, replacement, $0); print $0, updated; }' \
    | xargs -n 2 bash -c 'parent_mv "$@"'
 
    find . -name "$1" -not -name "*.png" -not -path "gradle" \
    | xargs sed -i '' s/$original_package/$new_package/g 
}


rm -rf .gradle
rm -rf .git
rm -rf .idea
rm -rf build
rm -rf app/build
rm -r *.iml
rm local.properties

update_filetypes "*.java"
update_filetypes "*.xml"
update_filetypes "*.properties"
update_filetypes "*.gradle"
find . -depth -empty -delete

git init
git add .
git commit -q -m "Initial import"