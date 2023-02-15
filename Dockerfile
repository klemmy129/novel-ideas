FROM openjdk:19-jdk-alpine
LABEL authors="klemmy129"

EXPOSE 10443 1521 9876 61613
ENV JAVA_OPTS=""
ENV SPRING_PROFILES="oracle,activemq,stomp"

COPY novel-ideas-rest/target/novel-ideas-rest-*.jar /app.jar

CMD ["java $JAVA_OPTS -Dspring.profiles.active=${SPRING_PROFILES} -jar app.jar" ]
#CMD ["/bin/sh"]