FROM node:10
LABEL description="Buster API"

WORKDIR /opt/app

COPY database.json /opt/app

COPY package.json /opt/app
COPY yarn.lock /opt/app

RUN yarn install --production

COPY dist /opt/app/dist

EXPOSE 4242

ENTRYPOINT ["node", "dist/index.js"]