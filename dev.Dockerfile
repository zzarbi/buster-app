FROM node:10
LABEL description="Buster API Dev"

WORKDIR /opt/app

COPY tsconfig.json /opt/app/tsconfig.json

COPY package.json /opt/app
COPY yarn.lock /opt/app

RUN yarn install
