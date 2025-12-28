FROM node:18-alpine

WORKDIR /app

COPY package.json ./
RUN npm install --production

COPY src ./src
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

ENV NODE_ENV=production
EXPOSE 8080

ENTRYPOINT ["/entrypoint.sh"]
