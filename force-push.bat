set /p commit="Commit Message: "
IF commit=="" (
	echo "Commit message cannot be empty"
) ELSE (
	rem cmd.exe /k "@echo OFF & cls & git checkout --orphan latest_branch & git add -A & git commit -am \"%commit%\" & git branch -D master & git branch -m master & git push -f origin master"
)