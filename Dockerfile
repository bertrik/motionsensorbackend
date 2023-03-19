FROM adoptopenjdk/openjdk14:jre-14.0.2_12-alpine
LABEL maintainer="Bertrik Sikken bertrik@gmail.com"

ADD motionsensorbackend/build/distributions/motionsensorbackend.tar /opt/

WORKDIR /opt/motionsensorbackend
ENTRYPOINT ["/opt/motionsensorbackend/bin/motionsensorbackend"]

