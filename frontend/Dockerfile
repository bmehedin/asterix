FROM node:20 AS build

WORKDIR /app

COPY package*.json ./

RUN npm install

RUN npm install -g @angular/cli

COPY . .

RUN ng build --configuration=production

FROM nginx:latest

RUN rm /etc/nginx/conf.d/default.conf

COPY --from=build /app/nginx.conf /etc/nginx/conf.d

COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html

EXPOSE 80
