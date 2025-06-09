public class Main {
  public static void main(String[] args) {
    // Set resource path explicitly
    System.setProperty("resource.path", System.getProperty("resource.path", ""));
    new GameFrame();
  }
}