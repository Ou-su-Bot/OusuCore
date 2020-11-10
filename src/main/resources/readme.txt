# OusuCore | for personal use | Beta 1.0

Este projeto foi feito para facilitar a forma em que eu faço os bots.
Este projeto me fez poupar bastante tempo na criação de bots,
pude criar bots mais rapido que o normal.

OBS: Este projeto tinha uma ideia inicial de rodar vários bots ao mesmo tempo, mas isso foi descontinuado,
sofreu um rework, pois tinha vestigios da antiga ideia, que fazia o codigo do projeto ficar desnecessariamente poluido.

Para desfrutar completamente o uso, recomendo utilizar o OusuCore-Commons.
### Directory Struture

OusuCore:.
├───./bots
│   ├─── YourBot.jar
│   └───./YourBot
│       ├───assets
│       ├───fonts
│       └───language
├───./dependency
├───./library
├───./logs
├─── settings.json
├─── YourBot_sqlite.db
└─── OusuCore.jar

#### Settings File
{
  "Token": "yourbottoken",
  "Shards": 1,
  "ChunkFilter": false, (eu recomendo false)
  "CacheFlag": { (coloque os cache flags que quer utilizar)
    "ACTIVITY": true,
    "VOICE_STATEE": true (caso o nome esteja errado não será ativado)
  },
  "GatewayIntents": { (mesma função para o gateway intents)
    "GUILD_MEMBERS": true
  }
}

* Cache flag e GatewayIntets

 > Caso não queira ativar o padrão só deixe o JsonObject nulo.
  "CacheFlag": null,
  "GatewayIntents": {
    "DEFAULT": true
  }

  "CacheFlag": {
    "DEFAULT": true
  },
  "GatewayIntents": null
  > ou se desejar configuração padrão, deixe DEFAULT