FROM node:18-alpine

WORKDIR /app

COPY package.json ./
RUN npm install --production

COPY src ./src
COPY public ./public
COPY templates ./templates

ENV PORT=8080
EXPOSE 8080

CMD ["node", "src/server.js"]
