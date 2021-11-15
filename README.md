# NAVI
<p align="center">
  <img src="markdown/navi.svg" width="250px" height="250px">
</p>

## Features
- Join/leave messages.
- Moderation commands (ban, kick, purge).
- Color roles.
- Simple music playback in a voice channel.

## Setting up
1. Make a [new discord application](https://discord.com/developers/applications) with a bot user account.
2. In Privileged Gateway Intents turn **on**:
    - Message Content Intent
    - Server Members Intent
3. Go to OAuth2, make an URL invite for scope *bot*. The permissions should be: *&permissions=46486663507*
4. Clone and navigate into the repository.
5. Make a new file named `.env` with contents:
    ```
    TOKEN=<your discord token>
    DEFAULT_CHANNEL=<channel id to output welcome messages>
    SPAM_CHANNEL=<channel id for fun commands>
    VOICE_CHANNEL=<voice channel id for music commands>
    ```
5. Install:
    - Using docker:
        - `docker build -t navi .`
        - `docker run -d --name=navi navi`
    - Locally (needs dependacies):
        - `mvn -f pom.xml clean compile assembly:single`
        - `java -jar navi-1.0-jar-with-dependencies.jar`

## Additional Configuration
### Setting up the muted role
Make a new role named *+muted* with disabled permissions to send messages.
### Adding color roles
Make new roles with the name scheme `-color` for example `-green` with a green color.
NAVI will autoindex those roles and automatically list them when issuing the help command.