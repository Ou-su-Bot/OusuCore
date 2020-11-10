## OusuCore | for personal use | Beta 1.0

<p>Este projeto foi feito para facilitar a forma em que eu faço os bots.
Este projeto me fez poupar bastante tempo na criação de bots,
pude criar bots mais rapido que o normal.</p>

<p>
OBS: Este projeto tinha uma ideia inicial de rodar vários bots ao mesmo tempo, mas isso foi descontinuado,
sofreu um rework, pois tinha vestigios da antiga ideia, que fazia o codigo do projeto ficar desnecessariamente poluido.
<p>
Para desfrutar completamente o uso, recomendo utilizar o OusuCore-Commons.
<h4>Directory Struture</h4>

```markdown
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
```

<h4>Settings File</h4>

```json
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
```
* Cache flag e Gateway Intents

Caso não queira ativar o padrão só deixe o JsonObject nulo.

```json
  "CacheFlag": null,
  "GatewayIntents": null
```

ou se quiser deixar as configurações padrões deixe

```json
  "CacheFlag": {
    "DEFAULT": true
  },
  "GatewayIntents": {
    "DEFAULT": true
  }
```