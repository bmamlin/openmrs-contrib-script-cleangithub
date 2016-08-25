#!/usr/bin/env groovy

// Use our handy API to GitHub (https://github.com/bmamlin/GitHubApi.groovy)
def github = new GitHubApi()

// This will add a repo to a given team
def addRepoToTeam = { repo, team ->
  def resp = github.put("/teams/$team.id/repos/openmrs/$repo.name", '')
}

// This will turn off issues and wiki for a given repo
def editRepo = { repo ->
  def body = """{"name":"$repo.name", "has_issues":false, "has_wiki":false}"""
  def resp = github.patch("/repos/openmrs/$repo.name", body)
}

// Define teams used for managing dev privileges
// (eventually will want to remove old groups of Full & Partial Committers)
coreTeams = ['Full Committers', 'Partial Committers', 'Repo Owners', '/dev/1', '/dev/2', '/dev/3', '/dev/4', '/dev/5']

// Ignore the couple internal repos
reposToIgnore = ['openmrs-contrib-itsmresources', 'openmrs-contrib-bambooagent']

// Fetch list of repos and teams
repos = github.get('/orgs/openmrs/repos').findAll{ !(it.name in reposToIgnore) }
teams = github.get('/orgs/openmrs/teams').findAll{ (it.name in coreTeams) }
println "OpenMRS org has ${repos.size()} repos"

// Scan all teams
for (team in teams) {
  # Get the teams repos
  teamRepoNames = github.get("/teams/$team.id/repos").collect{ it.name }

  # Find all repos not assigned to the team
  missing = repos.findAll{ !(it.name in teamRepoNames) && it.name != 'openmrs-core' }
  print "$team.name team has ${teamRepoNames.size()} repos, "
  println missing.size() > 0 ? "missing from these: ${missing.collect{it.name}}" : "missing none"

  # Add any repos to the team that were missing
  for (repo in missing) {
    print "fixing..."
    addRepoToTeam(repo, team)
    println "done."
  }
}

// Scan for any repos using GitHub issues/wiki and disable them
// OpenMRS projects should be using community resources (wiki + JIRA)
wikisOrIssues = repos.findAll{ it.has_wiki || it.has_issues }
println "wikis or issues (should be empty): ${wikisOrIssues.collect{it.name}}"
for (repo in wikisOrIssues) {
  print "fixing..."
  editRepo(repo)
  println "done."
}
