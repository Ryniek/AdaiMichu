FROM openjdk:17-jdk-slim-buster
MAINTAINER adaimichu.pl
COPY target/adaimichal-0.0.1-SNAPSHOT.jar adaimichal-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/adaimichal-0.0.1-SNAPSHOT.jar"]