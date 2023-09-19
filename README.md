# Getting Started

### Build and run jar file
build jar with command `./mvnw package` on mac and linux or `.\mvnw.cmd package` on windows

start jar with command `java -jar ./telegram-bot-catherine.jar` from directory `target`

### Server start
1. Положить application.properties на сервере в папку, откуда будет выполнен запуск
2. Запускаем `java -jar ./telegram-bot-catherine.jar`

### Copy files to server
scp -i ~/.ssh/public_key application.properties telegram-bot-catherine.jar username@{host}:/home/username/

### Start in a background
java -jar telegram-bot-catherine.jar > out.log 2>&1 &

### Find and kill java background process
ps aux | grep java  
kill -${signal} ${java.pid}

SIGKILL(9) - terminate immediately  
SIGTERM(15) - gracefully terminate  