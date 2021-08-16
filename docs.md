## JavaDoc for RespawnCell

### `public DataManager(Plugin plugin, String configPath)`

Creates a data manager to connect to configuration files for the given plugin and the given path to a configuration file

* **Parameters:**
    * `plugin` — the plugin to connect the config manager to
    * `configPath` — the path to the config file to save to

### `public void reloadConfig()`

Reloads the configuration file to reflect changes live

### `public FileConfiguration getConfig()`

Gets the config file located at the path the object is attached to

* **Returns:** the config file

### `public void saveConfig()`

Saves changes to the configuration file instance to the actual file

### `public void saveDefaultConfig()`

Saves the default values of the configuration file instance to the actual file

### `public RespawnCellCommand(RespawnCellManager jailManager, MessageManager message, Plugin plugin)`

Creates a command manager

* **Parameters:**
    * `jailManager` — the manager of the jail
    * `message` — the message manager
    * `plugin` — the plugin to connect the command manager to

### `@Override public boolean onCommand(CommandSender sender, Command command, String label, String[] args)`

Command manager for Respawn Cell

* **Parameters:**
    * `sender` — the command sender
    * `command` — the command sent
    * `label` — the label for the command
    * `args` — the arguments associated with the command
* **Returns:** whether to echo the command

### `public boolean isPlayerJailed(String playerName)`

Determines whether the given player is currently in jail

* **Parameters:** `playerName` — the name of the player check if they are jailed
* **Returns:** boolean value determining whether the player is in jail

### `public int getPlayerJailTime(String playerName)`

Gets how much longer a player has in jail in minutes. If the player is not currently in jail -1 will be returned

* **Parameters:** `playerName` — the name of the player to get the jail time for
* **Returns:** the integer value for the length of jail time in minutes

### `public void setPlayerJail(String playerName, int time)`

Puts the given player in jail for the specified amount of time. The player will be teleported to jail if they are not already in jail. If the player is in jail, their time will be updated to match

* **Parameters:**
    * `playerName` — the name of the player to put into jail
    * `time` — the length of time in minutes the player should be in jail

### `public void teleportToJail(String playerName)`

Teleports the given player to jail if they are online

* **Parameters:** `playerName` — the name of the player to teleport to jail

### `public void releasePlayer(String playerName)`

releases the given player from jail if they are in jail. This will remove their timer, teleport them to the release point, and alert them they have been freed. If the player is not online, they will be tracked and released once they re-connect

* **Parameters:** `playerName` — the name of the player to free

### `public void releaseOfflinePlayer(String playerName)`

Releases a player who was offline when they were freed. If they are now online, they will be teleported to the release location and freed

* **Parameters:** `playerName` — the name of the player to free

### `public void updateLocation(Location location, String path)`

Updates the location in the config file of the given location name to the current location given

* **Parameters:**
    * `location` — the location to change the configuration file to save
    * `path` — the name of the location to save the location under

### `public void stopBossBar()`

Stops all boss bars from running, removing all players associated with them

### `public Location getLocationFromConfig(String path)`

Gets the location for a given path from the config

* **Parameters:** `path` — the path to the desired location in the configuration
* **Returns:** the Location matching the data saved in the configuration file for the path given

### `public void updateConfig()`

Updates the config with no minute changes

### `private void updateConfig(int minutes)`

Updates the data config progressing forward all jailed players time by the specified amount

* **Parameters:** `minutes` — the amount to take off of player's jail time

### `private void updateBossBar(String player)`

Updates the boss bar of a player to the current amount of time on their sentence

* **Parameters:** `player` — the player to update the boss bar for

### `public void refreshBossBar(String player)`

Reloads a players boss bar, removing an old instance and replacing it with a current one in case the player has disconnected or the boss bar connection has been interrupted

* **Parameters:** `player` — the player to update the boss bar for

### `private void updateBossBar(String player, int time)`

Updates the boss bar of a player to the given amount of time on their sentence

* **Parameters:**
    * `player` — the player to update the boss bar for
    * `time` — the amount of time to display on the boss bar

### `public RespawnCellTabCompletor(Server server)`

Creates a RespawnCellTabCompletor for the given server

* **Parameters:** `server` — the server to initialize tab completion on

### `@Override public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)`

Implements tab completion for respawncell commands

* **Parameters:**
    * `sender` — the sender of the command
    * `command` — the command to tab complete for
    * `alias` — alias commands for the command
    * `args` — the arguments currently passed into the command
* **Returns:** a List of Strings containing all phrases to auto-complete

### `public MessageManager(FileConfiguration config)`

Creates a MessaageManager based upon the given configuration file

* **Parameters:** `config` — the configuration file to read messages from

### `public String getMessageNoPrefix(String path)`

Gets a message for the given path from the configuration file without the prefix but encoded with colors

* **Parameters:** `path` — the path to the message in the config
* **Returns:** the encoded message with color

### `public String getMessage(String path)`

Gets a message for the given path from the configuration file with the prefix and encoded with colors

* **Parameters:** `path` — the path to the message in the config
* **Returns:** the encoded message with color and a prefix

### `public JailPlayerEvent(RespawnCellManager jailManager)`

Creates a JailPlayerEvent manager that uses the given RespawnCellManager to manage jail

* **Parameters:** `jailManager` — the manager of the desired jail

### `@EventHandler public void playerDeathEvent(PlayerDeathEvent event)`

Handles player death event, adding them to jail for the desired length of time

* **Parameters:** `event` — the event

### `@EventHandler public void playerJoinEvent(PlayerJoinEvent event)`

Handles player join event, teleporting them to jail if necessary, refreshing their boss bar, and releasing them from prison if they need to be

* **Parameters:** `event` — the event

### `@EventHandler public void playerRespawnEvent(PlayerRespawnEvent event)`

Handles player respawn event, teleporting them to jail if necessary

* **Parameters:** `event` — the event