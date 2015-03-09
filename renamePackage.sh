#!/bin/sh
 
# Usage renamePackage.sh [package]
# Ex: renamePackage.sh com.atomicrobot.app
 
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
    find . -name "$1" \
    | gawk -v replacement="\\\\1$new_package_directory\\\\2" '{ updated = gensub(/(.+)\/com\/mycompany\/myapp\/(.+)/, replacement, $0); print $0, updated; }' \
    | xargs -n 2 bash -c 'parent_mv "$@"'
 
    find . -name "$1" \
    | xargs sed -i '' s/$original_package/$new_package/g 
}
 
echo
echo "----------"
echo "(Dev) Showing what things look like before:"
find .
echo
 
update_filetypes "*.java"
update_filetypes "*.xml"
update_filetypes "*.gradle"
find . -depth -empty -delete
 
echo "(Dev) Showing what things look like after:"
find .
echo
