FROM hub.bilibili.co/lifajin/zulu-openjdk:17
COPY target/demo-1.0.1.jar demo.jar
EXPOSE 9999
CMD java -jar -Duser.timezone=GMT+8 demo.jar --spring.profiles.active=prod