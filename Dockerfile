FROM tomcat:10.1-jdk21

COPY SBI.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
