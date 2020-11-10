# OusuCore | for personal use | Beta 1.0

Este projeto foi feito para facilitar a forma em que eu faço os bots.
Este projeto me fez poupar bastante tempo na criação de bots,
pude criar bots mais rapido que o normal.

OBS: Este projeto tinha uma ideia inicial de rodar vários bots ao mesmo tempo, mas isso foi descontinuado,
sofreu um rework, pois tinha vestigios da antiga ideia, que fazia o codigo do projeto ficar desnecessariamente poluido.

Para desfrutar completamente o uso, recomendo utilizar o OusuCore-Commons.
### Directory Struture

.
├── _logs
│   └── 07/11/2020-ousubot.log
├── _bots
│   ├── YourBot.jar
│   └── _YourBot
│       ├── Fonts (load your fonts)
│       ├── Assets (bot files, and etc)
│       └── Language (bot language files '.json')
├── _dependency
│   ├── botDependency_1.jar
│   └── botDependency_2.jar
├── _library
│   └── JDA(last version).jar
├── settings.json
├── YourBot_sqlite.db
└── OusuCore.jar

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
  "GatewayIntents": { (para gateway intents crie um none)
    "DEFAULT": true
  }

  "CacheFlag": {
    "DEFAULT": true
  },
  "GatewayIntents": null
  > ou se desejar configuração padrão, deixe DEFAULT