
FROM cassandra:latest
MAINTAINER MartinFIT

# replace docker's official entrypoint here
COPY docker-entrypoint.sh /docker-entrypoint.sh

# make my modified entrypoint executable
RUN chmod a+x docker-entrypoint.sh

# ENTRYPOINT will be called in parent-docker-image
