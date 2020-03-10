FROM maven:3-jdk-13

COPY . /tmp/
EXPOSE 8080
RUN cd /tmp && mvn clean install
CMD cd /tmp && mvn spring-boot:run