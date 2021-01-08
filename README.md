# Deploy
### Create config file

```yaml
telegram:
  bot-login: 'YOUR_BOT_LOGIN'
  bot-token: 'YOUR_BOT_TOKEN'
  bot-chats:
    'YOUR_USERNAME': 'YOUR_CHAR_ID'
```

> Field "YOUR_USERNAME" need to send messages by REST api

> You can get "YOUR_CHAR_ID" from telegram bot https://t.me/getidsbot 

### Create docker config

```bash
> docker config create telegram.yaml YOUR_FILE_NAME
```

### Create docker volume to store database

```bash
> docker volume create --name=hass-telegram-bot
```

### Deploy stack to docker swarm

```bash
> docker stack deploy --compose-file=docker-compose.yaml YOUR_STACK_NAME
```
