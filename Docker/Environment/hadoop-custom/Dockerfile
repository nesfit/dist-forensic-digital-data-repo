
FROM sequenceiq/hadoop-docker:latest
MAINTAINER MartinFIT

ADD core-site.xml $HADOOP_PREFIX/etc/hadoop/core-site.xml
ADD bootstrap.sh /etc/bootstrap.sh

RUN chown root:root /etc/bootstrap.sh
RUN chmod 700 /etc/bootstrap.sh

# bootstrap.sh will be called in parent-docker-image
