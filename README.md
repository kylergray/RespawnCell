# RespawnCell
A plugin to send players to a cell when they die and respawn

## Commands
### RespawnCell (`/respawncell`, `/rc`, `/clrespawn`, `clr`)
#### Set Respawn Point (`/respawncell setspawn`)
 - Sets the point players will be teleported to when they die and are in a cell. This location will be the location of the player executing the command and is stored in `data.yml`.
#### Set Release Point (`/respawncell setspawn`)
 - Sets the point players will be teleported to when they are released from their cell. This location will be the location of the player executing the command and is stored in `data.yml`.
#### Send Player to Jail (`/respawncell jail <Player> <Time>`)
 - Sends the given Player (`<Player>`) to jail for the specified length of time (`<Time>`) in minutes. The player will be teleported to jail and will have a bossbar displayed to them counting down the remaining time.
#### Free Player from Jail (`/respawncell release <Player>`)
- Releases the given Player (`<Player>`) from jail sending them to the specified rlease point.
