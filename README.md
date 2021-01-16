### Links
- [Get chat id](https://t.me/getidsbot)
- [Bot Father](https://t.me/BotFather)


# Usage
### Config example

```yaml
login: 'shopping_list_bot'
token: '6040231338:ACGS96Ql4bK6SoQ7024J8_FGdSX_sxshZvc'
users:
    - username: 'andrii'
      chatId: '223714846' 
```

> Field "YOUR_USERNAME" need to send messages by REST api

> You can get "YOUR_CHAR_ID" from telegram bot https://t.me/getidsbot 

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
