############################################################
# Dockerfile to build MySQL container image
# Based on Ubuntu
############################################################

# Set the base image to Ubuntu
FROM ubuntu

# File Author / Maintainer
MAINTAINER  groupe_phoenix

RUN apt-get update 

RUN  echo mysql-server mysql-server/root_password password  | sudo debconf-set-selections \
&&  echo mysql-server mysql-server/root_password_again password  | sudo debconf-set-selections \
&&  apt-get -y install mysql-server


ADD config_files /tmp/

RUN sed -i -e"s/bind-address		= 127.0.0.1/bind-address		= 0.0.0.0/g" /etc/mysql/my.cnf \
&& cd /tmp/ \
&& service mysql start \
&&  mysql -u root --database=mysql <annuaire.sql


EXPOSE 3306

ENTRYPOINT /tmp/mysql.sh
