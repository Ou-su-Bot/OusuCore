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
├─── Settings.ini
├─── SQLConfiguration.ini
└─── OusuCore.jar
```

<h4>Settings File</h4>

```ini
[BotConfiguration]
token = BOT-TOKEN
shards = 1

[OtherConfiguration]
chunkfilter = true
cacheflag = default
gatewayintents = default
```
* CacheFlag & GatewayIntents
Only the default option is available

<h4>SQL File</h4>

```ini
[Database]
template = postgres
; Only Postgres

[DatabaseCredentials]
server = localhost
database = postgres
port = 5432
user = postgres
password = 123

```
