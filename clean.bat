@echo off
git checkout --orphan latest_branch
git add -A
git commit -am "Re-index Repository"
rem git branch -D master
rem git branch -m master
git push -f origin master
git push -u --force origin master
rem remove the old files
rem git gc --aggressive --prune=all