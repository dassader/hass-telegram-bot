### Links
- [Docker hub](https://hub.docker.com/r/dassader/hass-telegram-bot)
- [Get chat id](https://t.me/getidsbot)
- [Bot Father](https://t.me/BotFather)


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

### Add action to Home Assistant

```yaml
rest_command:
  send_open_shopping_list_notification:
    method: POST
    url: 'http://192.168.1.1:8080/message'
    content_type: "application/json"
    payload: '{
                "userList": ["{{ user }}"],
                "message": "🛍 Хочешь посмотреть список покупок?",
                "inlineKeyboard": [[
                  {
                    "text": "Открывай 👍🏻",
                    "data": "/shopping_list"
                  }
                ]]
              }'
```

### Call action from authomation when phone near shop

```yaml
alias: '[Hallway] Send notification when Andrii come shop'
trigger:
  - platform: state
    entity_id: input_boolean.andrii_near_shop
    from: 'off'
    to: 'on'
action:
  - service: rest_command.send_open_shopping_list_notification
    data:
      user: "andrii"
```
