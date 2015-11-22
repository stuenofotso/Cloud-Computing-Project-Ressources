############################################################
# Dockerfile to build Apache container image
# Based on Ubuntu
############################################################

# Set the base image to Ubuntu
FROM ubuntu

# File Author / Maintainer
MAINTAINER  groupe_phoenix

RUN apt-get update 
RUN  echo "downloading apache2" \
&&  apt-get install -y apache2 
RUN  echo "downloading libapache2-mod_jk" \
&&  apt-get install -y libapache2-mod-jk \
&& rm -R -f /tmp/



ADD config_files /tmp/

RUN mv -f /tmp/workers.properties /etc/libapache2-mod-jk/ \
&&  mv -f /tmp/000-default.conf /etc/apache2/sites-enabled/



EXPOSE 80

ENTRYPOINT /tmp/apache.sh


