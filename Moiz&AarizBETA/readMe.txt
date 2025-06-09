 Horde from Hell – Beta
---------------------------------------------------------------------------------------------------
Welcome to Horde from Hell. This basic version demonstrates core features
such as player movement, tile-based collision detection, and random
tree & zombie spawning on a dynamically generated map.

Game Controls:
  • W, A, S, D : Use these keys to move the player.
  • SPACE (hold in front of trees) : Chop Tree
  • R : Place Wall (with wood, on grass)
  • F : Place Floor (with wood, on water)

Game Description:
  • The game world is constructed from a tile map defined in a text file.
  • The map generates randomly each time you run the game:
       – Random trees (or bushes) are spawned on land tiles.
       – No two trees overlap, thanks to placement checks.
  • Normally, collision detection stops you from walking onto water,
    but you can also see dynamic elements like trees and barrier modifications.
  • (Note: In this demo, you can “walk over water” only if appropriate
    building or collision adjustments are made. This demo shows a basic setup.)
  • Zombies randomly spawn on the map and path find towards the player
  • For testing, zombies die when they touch the player
  • A bar is presented showing the amount of zombies left

Additional Notes:
  • Every time you run the game, the map generates with a random arrangement
    of trees on the land tiles.
  • In future versions, buildings will be implemented as separate entities,
    and a save/load system will ensure consistency across sessions.
  • There is a bug where you can place a wall on the tile that you are on and get stuck, will fix later

Thank you for trying out Deme for Horde from Hell!!!