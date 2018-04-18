# Glossary

/ = project root directory

# Prerequisities

> Start the derby database
./asadmin start-database

# Run server

Directory: /

gradle build -x :integrationtesting:test

# Run integration tests

Directory: /integrationtesting

gradle build

^ Issue with gradle, freezes at 'gradle build'.
Run with 'gradle build --debug' and/or 'gradle build --stacktrace' to find out what's causing the error. Not solved for now.
Issue solved: do not 'get' on google.com with a 'baseurl' set in setup() method.
