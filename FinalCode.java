// This Java program is a simple music player application that manages users and their playlists using a binary search tree (BST). 
// The program provides several functionalities, such as adding songs, playing a random song, adjusting volume, viewing song history, and determining friendships based on the percentage of common songs.

package abc;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;


class Song {
    private String title;
    private String artist;
    private int duration; // in seconds
    private boolean playing; // indicates whether the song is currently playing or paused

    public Song(String title, String artist, int duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.playing = false; // by default, song is not playing
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void togglePlaying() {
        playing = !playing; // toggles the playing state
    }
}


class TreeNode {
    private String userName;
    private List<Song> songs;
    private List<Song> history; // List to store history of played songs
    private TreeNode leftChild;
    private TreeNode rightChild;
    private int volumeLevel;

    public TreeNode(String userName, Song song) {
        this.userName = userName;
        this.songs = new ArrayList<>();
        this.history = new ArrayList<>(); // Initialize history list
        this.songs.add(song);
        this.leftChild = null;
        this.rightChild = null;
        this.volumeLevel = 50;
    }


    public String getUserName() {
        return userName;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public List<Song> getHistory() {
        return history;
    }

    public TreeNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(TreeNode leftChild) {
        this.leftChild = leftChild;
    }

    public TreeNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(TreeNode rightChild) {
        this.rightChild = rightChild;
    }

    public void addSong(Song song) {
        this.songs.add(song);
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(int volumeLevel) {
        if (volumeLevel >= 0 && volumeLevel <= 100) {
            this.volumeLevel = volumeLevel;
        } else {
            System.out.println("Volume level must be between 0 and 100.");
        }
    }

    // Method to add played song to history
    public void addToHistory(Song song) {
        history.add(song);
    }
}



class BinaryTree {
    private TreeNode root;

    public BinaryTree() {
        root = null;
    }
    public TreeNode getRoot() {
        return root;
    }


    public void insert(String userName, Song song) {
        root = insertRecursive(root, userName, song);
    }

    private TreeNode insertRecursive(TreeNode current, String userName, Song song) {
        if (current == null) {
            return new TreeNode(userName, song);
        }

        if (userName.compareTo(current.getUserName()) < 0) {
            current.setLeftChild(insertRecursive(current.getLeftChild(), userName, song));
        } else if (userName.compareTo(current.getUserName()) > 0) {
            current.setRightChild(insertRecursive(current.getRightChild(), userName, song));
        } else {
            // User already exists, add song to the existing user
            current.addSong(song);
        }

        return current;
    }


    public void adjustVolume(String userName, int volumeLevel) {
        TreeNode userNode = findUser(root, userName);
        if (userNode != null) {
            userNode.setVolumeLevel(volumeLevel);
            System.out.println("Volume adjusted for user " + userName + " to level " + volumeLevel);
        } else {
            System.out.println("User not found.");
        }
    }

    private TreeNode findUser(TreeNode current, String userName) {
        if (current == null) {
            return null;
        }

        if (userName.equals(current.getUserName())) {
            return current;
        }

        return userName.compareTo(current.getUserName()) < 0
            ? findUser(current.getLeftChild(), userName)
            : findUser(current.getRightChild(), userName);
    }


    public void playRandomSong(String userName) {
        TreeNode userNode = findUser(root, userName);
        if (userNode != null) {
            List<Song> songs = userNode.getSongs();
            if (!songs.isEmpty()) {
                Random random = new Random();
                Song randomSong = songs.get(random.nextInt(songs.size()));
                randomSong.togglePlaying();
                System.out.println("Playing: " + randomSong.getTitle() + " by " + randomSong.getArtist());
                userNode.addToHistory(randomSong); // Add the played song to history
            } else {
                System.out.println("No songs available for user " + userName);
            }
        } else {
            System.out.println("User not found.");
        }
    }


    public void viewHistory(String userName) {
        TreeNode userNode = findUser(root, userName);
        if (userNode != null) {
            List<Song> history = userNode.getHistory();
            if (!history.isEmpty()) {
                System.out.println("Song history for " + userName + ":");
                for (Song song : history) {
                    System.out.println(song.getTitle() + " by " + song.getArtist());
                }
            } else {
                System.out.println("No history available for user " + userName);
            }
        } else {
            System.out.println("User not found.");
        }
    }



    public void determineFriendship(String userName1, String userName2) {
        TreeNode userNode1 = findUser(root, userName1);
        TreeNode userNode2 = findUser(root, userName2);

        if (userNode1 != null && userNode2 != null) {
            Set<String> set1 = new HashSet<>();
            for (Song song : userNode1.getSongs()) {
                set1.add(song.getTitle());
            }

            Set<String> set2 = new HashSet<>();
            for (Song song : userNode2.getSongs()) {
                set2.add(song.getTitle());
            }

            // Find intersection of both sets
            set1.retainAll(set2);
            int commonSongCount = set1.size();
            int totalSongs = userNode1.getSongs().size() + userNode2.getSongs().size() - commonSongCount;

            double friendshipPercentage = (double) commonSongCount / totalSongs * 100;
            System.out.println("Friendship percentage between " + userName1 + " and " + userName2 + " is: " + friendshipPercentage + "%");
        } else {
            System.out.println("One or both users not found.");
        }
    }
}


public class MusicPlayer {
    public static void main(String[] args) {

        BinaryTree userTree = new BinaryTree();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter 1 to add a song, 2 to play a random song, 3 to adjust volume, 4 to view song history, 5 to determine friendship, 6 to exit:");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    System.out.println("Enter user name:");
                    String userName = scanner.nextLine();
                    System.out.println("Enter song title:");
                    String title = scanner.nextLine();
                    System.out.println("Enter artist name:");
                    String artist = scanner.nextLine();
                    System.out.println("Enter song duration (in seconds):");
                    int duration = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character

                    userTree.insert(userName, new Song(title, artist, duration));
                    System.out.println("Song added successfully.");
                    break;

                case 2:
                    System.out.println("Enter user name:");
                    String userToPlaySong = scanner.nextLine();
                    userTree.playRandomSong(userToPlaySong);
                    break;

                case 3:
                    System.out.println("Enter user name:");
                    String userToAdjustVolume = scanner.nextLine();
                    System.out.println("Enter volume level (0-100):");
                    int volumeLevel = scanner.nextInt();
                    scanner.nextLine(); // Consume the newline character
                    userTree.adjustVolume(userToAdjustVolume, volumeLevel);
                    break;

                case 4:
                    System.out.println("Enter user name:");
                    String userToViewHistory = scanner.nextLine();
                    userTree.viewHistory(userToViewHistory);
                    break;

                case 5:
                    System.out.println("Enter first user name:");
                    String user1 = scanner.nextLine();
                    System.out.println("Enter second user name:");
                    String user2 = scanner.nextLine();
                    userTree.determineFriendship(user1, user2);
                    break;

                case 6:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }
}
