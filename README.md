# KWCommandsBukkit

Brings [KWCommands](https://github.com/JonathanxD/KWCommands).

## Using

Add dependency to KWCommandsBukkit in your `plugin.yml` and use the service `KWCommandsBukkitServer`.

### As a shaded dependency

If KWCommandsBukkit is shaded in your plugin (relocation recommended), you should not add `KWCommandsBukkit` as dependency in your `plugin.yml`, and you must invoke 
`KWCommandsBukkitInit.init(Plugin)` to initialize KWCommands.
