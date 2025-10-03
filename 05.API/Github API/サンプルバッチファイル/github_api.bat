@echo off
setlocal

REM ---------------
REM  User settings
REM ---------------

set GITHUB_TOKEN=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

REM set GITHUB_API=/orgs/YourOrganization/members
REM set GITHUB_API=/orgs/YourOrganization/repos

set GITHUB_API=/user/repos

REM set PROXY=-x http://proxy.example.com:10080

REM -----------------
REM  Call GitHub API
REM -----------------
set GITHUB_BASEURL=https://api.github.com

FOR /L %%A IN (1, 1, 5) DO (
    curl -k -s %PROXY% -u :%GITHUB_TOKEN% "%GITHUB_BASEURL%%GITHUB_API%?per_page=100&page=%%A"
)

cmd /k
