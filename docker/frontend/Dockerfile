FROM node:20.12.1-alpine

WORKDIR /usr/src/app

ENV PORT 3000

RUN apk add --update python3 make g++ && rm -rf /var/cache/apk/*

RUN curl -fsSL https://bun.sh/install | bash

COPY frontend/package*.json ./

RUN bun install

EXPOSE 3000

COPY . .

CMD ["bun", "run", "dev"]