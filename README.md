# openmrs-contrib-script-cleangithub

A simple script for cleaning up the GitHub repos for OpenMRS. This uses my [GitHub API](https://github.com/bmamlin/GitHubApi.groovy) to run and it obviously requires appropriate permissions on GitHub. I'm sharing it here for a few reasons: (1) for others with similar privileges within OpenMRS to use if they wish or for us to setup an automated process to do these tasks, (2) an easy way to ensure I don't lose this code, and (3) in the miniscule chance that it might help someone with some unrelated task.

## What this script does

1. Adds all public repos to dev teams for [OpenMRS](https://github.com/openmrs/).
2. Closes any GitHub wikis or issue trackers to support OpenMRS convention of using https://wiki.openmrs.org/ and http://issues.openmrs.org/ for these purposes.
