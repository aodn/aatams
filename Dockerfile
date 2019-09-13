FROM ubuntu:16.04

ARG BUILDER_UID=9999

ENV GRAILS_VERSION 1.3.7
ENV JAVA_TOOL_OPTIONS -Duser.home=/home/builder
ENV JAVA_HOME /usr/lib/jvm/java-7-openjdk-amd64
ENV GRAILS_HOME /usr/lib/jvm/grails
ENV PATH $GRAILS_HOME/bin:$PATH

RUN apt-get update && add-apt-repository ppa:openjdk-r/ppa \
    && apt-get install -y --no-install-recommends 
    software-properties-common \
    ca-certificates \
    git \
    openjdk-7-jdk \
    python \
    unzip \
    wget \
    && rm -rf /var/lib/apt/lists/*

RUN wget -q https://bootstrap.pypa.io/get-pip.py \
    && python get-pip.py pip==18.1 \
    && rm -rf get-pip.py

RUN pip install \
    bump2version==0.5.10

WORKDIR /usr/lib/jvm
RUN wget https://github.com/grails/grails-core/releases/download/v$GRAILS_VERSION/grails-$GRAILS_VERSION.zip && \
    unzip grails-$GRAILS_VERSION.zip && \
    rm -rf grails-$GRAILS_VERSION.zip && \
    ln -s grails-$GRAILS_VERSION grails

RUN useradd --create-home --no-log-init --shell /bin/bash --uid $BUILDER_UID builder
USER builder
WORKDIR /home/builder
