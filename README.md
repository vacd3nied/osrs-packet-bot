
# DBot - OSRS Packet Bot

This is a partially open source OSRS packet bot. Completely experimental.

## Project Structure

- [dbot-api](/dbot-api/src/main/java/net/runelite/api) - bot api (based off runelite)

- [dbot-client](/dbot-client/src/main/java/com/dbot/client) - client

## Build

```shell script
./mvnw install
```

## Run

No autodownload of game patch, therefore you should copy
patched.jar to <user_home>/.dbot/patched.jar

Add "render" option to run with rendering.

```shell script
java -jar dbot-client-0.1-SNAPSHOT-shaded.jar <username> <password>
```
