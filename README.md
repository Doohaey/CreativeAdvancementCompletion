# Minecraft Creative Mode Advancement Collection Server Game

This project serves as a Minecraft paper server plugin that implementing a custom server game, in which players collect as many advancements as possible via creative mode. This plugin records players' advancements and functions to display them, while also exerts some control over the game process.

## Environment Requirements

- Java 21 or above
- Gradle 8.12.1
- Minecraft Paper server, version 1.21.11

## Rules of the Game

- Players are only allowed to gain advancements when they are in creative mode.
- Players can only gain advancements when the event is active, which is controlled by server operators.
- Players earn points based on the advancements they acquire:
  - Advancement: 1 point
  - Goal: 3 points
  - Challenge: 5 points
- No cheating or exploiting bugs to gain advancements is allowed.
- No more rules. Any gameplay during the event in the event server is allowed, including:
    - Griefing or trolling other players
    - Stealing items from other players, etc.

## Plugin Features

### Advancement Progress Control

Once the plugin is enabled on the server, a player could only be granted advancements when they:
- met the advancement criteria(progress) in Minecraft game play,
- are in creative mode,
- met the above two conditions at the time when `event.active=true`.
    - which is `false` by default, and can be controlled by server **operators** via commands.
        - `/cac start` to set `event.active=true`
        - `/cac stop` to set `event.active=false`
        - `/cac toggle` to toggle the value of `event.active`
        - `/cac status` to check the current status of `event.active`
        Otherwise, the advancement will not be granted to a player.
        And any progress towards an advancement they acquired will be reset immediately.
        Especially, if the progress is related with item possession, the items will be removed from their inventory.

Technically, each period during the start and the stop commands is defined as a "session" named by "session_" + timestamp of the start command.

### Event Management

As mentioned above, server operators can control the event status via commands. Only game data during the active period will be recorded. And only those which generated during the same session will be compared together in the leaderboard.

Advancements players acquired are recorded by the plugin and turned into game data.
This plugin records or infers these data during the event period:
- player:
  - raw data of advancements acquired,
    - list of advancements acquired and the timestamps when they were acquired,
    - number of advancements acquired, categorized by,
      - parents(story, nether, end, adventure, husbandry),
      - advancement types(Advancement, Goal, Challenge)
      and with the total number,
  - score calculated based on the advancements acquired, for each category mentioned above and the total score,
    -  $Score = \Sigma kn$, 
      - where k is the weight of each advancement type, 
        - Advancement: 1 point by default,
        - Goal: 3 points by default,
        - Challenge: 5 points by default,
      - n is the number of advancements acquired of that type,
- global:
  - list of players who acquired each advancement and the timestamps when they acquired them,
  - total score rank,
  - list of players who acquired each advancement.

### Game Display

To maximize the atmosphere of the game, the plugin provides a display module which should shift players' perspective from "I'm just playing Minecraft" to "I'm in a high-stakes competitive race."

#### Scoreboard

A scoreboard show the momentum of the game, including:
- top 5 players with the highest total score,
- player's own total score and rank, it will show at the bottom of the scoreboard player rank list if the player is not in the top 5, or show among competitors. This line should be highlighted.
- score formula display, which is small footer indicating the current multipliers ($k=1, 3, 5$) to remind players that Goals and Challenges are the key to jumping ranks.
The scoreboard will be updated by LazyTicker.

#### Boss Bar

A boss bar constitutes proximity display, showing the proximity to the Lead.
- Text: telling the player who is currently leading, their score, and how many points the player is behind.
- Progress Bar: showing the player's progress towards the leader as a percentage.
- Color: changing color based on proximity to the leader.
    - Green: within 10% of the leader's score.
    - Yellow: within 30% of the leader's score.
    - Red: more than 30% behind the leader.
The boss bar will also be updated by LazyTicker.

#### The Action Bar

Since the action bar is right above the inventory, it handles real-time updates on advancement acquisition.
- When a player acquires a new advancement, the action bar displays the name of the advancement and the points earned from it, which lasts for 5 seconds.
- Category Tracker: when a player acquires an advancement, the action bar also shows the number of advancements acquired in that category (Story, Nether, End, Adventure, Husbandry) out of the total number of advancements in that category.
The action bar will be updated immediately when a player acquires an advancement.

#### Chat Messaging

To enhance the competitive atmosphere, the plugin sends chat messages to all players when someone acquires a new advancement.
- The chat message includes the player's name, the name of the advancement acquired, and the points.
    - When the advancement is a Challenge, the message is highlighted to emphasize its significance.
- When the acquired advancement results in a change in the leaderboard (top 5), an additional chat message announces the new leaderboard standings.

#### Command Inquiry

Players can use commands to access detailed tactical data that is too dense for the constant HUD display.

- `/cac me`: displays a comprehensive summary of the player's personal game state.
    - Current total score and global rank.
    - Detailed breakdown of advancements acquired in each category (Story, Nether, End, Adventure, Husbandry).
    - Recent acquisition history with timestamps.
- `/cac top`: displays the full leaderboard standings.
    - Shows the complete list of all players and their scores in chat for a full view of the competition.
- `/cac check [player]`: allows for scouting a competitor's progress.
    - Shows a target player's rank and their total score.
    - Highlights which specific Challenges the competitor has already completed.
- `/cac help`: provides a quick reference for the game's scoring rules.
    - Lists the current point weights for Advancements, Goals, and Challenges.