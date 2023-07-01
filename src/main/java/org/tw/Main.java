package org.tw;

public class Main {
    public static void main(String[] args) {
        Song song = new Song(args);
        System.out.println(song.song(args));
    }
}