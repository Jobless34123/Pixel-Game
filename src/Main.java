//Author: Moiz Nawazani and Aariz Farooqui
//Date Started: May 14, 2025
//Date Finished: June 13, 2025
// Description: Survive the Zombie Apocalypse is a 2D top-down survival game where
// players must fend off waves of zombies while managing resources and health.
// The game features a variety of zombie types, each with unique behaviors and
// challenges. Players can collect items, build defenses, and strategize to survive
// as long as possible against increasingly difficult zombie hordes.
public class Main {
  public static void main(String[] args) {
    // Create an AudioPlayer instance and pass the resource path of your wav file.
    AudioPlayer player = new AudioPlayer("/music/background.wav");
    player.playLoop();

    // Now start your game frame.
    new GameFrame();
  }
}