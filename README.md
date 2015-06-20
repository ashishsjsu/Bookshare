Book share/sell app for university students

Steps to run the app:
•	Please find database dumps in mongodb_dumps and mysql_dumps folder
•	Import the respective databases in your local environment

Using Command line: 
1.	Make sure you have Gradle build tool. You can install gradle from here: https://docs.gradle.org/current/userguide/installation.html 
2.	cd into the project directory
3.	build the project: gradle build 
4.	run the executable with: java –jar build/libs/booksharebackendArtifact-0.0.1-SNAPSHOT.jar 

Using Spring tool suite IDE:
1.	Import the project into Spring tool suite IDE.
2.	 Right Click => Gradle => Enable Dependency Management and Gradle => Refresh dependencies
3.	Right click and run as Spring Boot App


Browse to localhost:9000 

Login details : 
1.	username: trisha.j@sjsu.edu, password: trisha
2.	username: joe.l@sjsu.edu, password: joe

•	Verify the mysql database credentials given in Util->SqlConfig.java and change to your local configuration.

