# Minecraft Creative Mode Advancement Collection Server Game

This project serves as a Minecraft paper server plugin that implementing a custom server game, in which players collect as many advancements as possible via creative mode. This plugin records players' advancements and functions to display them, while also exerts some control over the game process.

## Advancement Progress Control

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

## Event Management

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

## Game Display

To maximize the atmosphere of the game, the plugin provides a display module which should shift players' perspective from "I'm just playing Minecraft" to "I'm in a high-stakes competitive race."

### Scoreboard

A scoreboard show the momentum of the game, including:
- top 5 players with the highest total score,
- player's own total score and rank, it will show at the bottom of the scoreboard player rank list if the player is not in the top 5, or show among competitors. This line should be highlighted.
- score formula display, which is small footer indicating the current multipliers ($k=1, 3, 5$) to remind players that Goals and Challenges are the key to jumping ranks.
The scoreboard will be updated by LazyTicker.

### Boss Bar

A boss bar constitutes proximity display, showing the proximity to the Lead.
- Text: telling the player who is currently leading, their score, and how many points the player is behind.
- Progress Bar: showing the player's progress towards the leader as a percentage.
- Color: changing color based on proximity to the leader.
    - Green: within 10% of the leader's score.
    - Yellow: within 30% of the leader's score.
    - Red: more than 30% behind the leader.
The boss bar will also be updated by LazyTicker.

