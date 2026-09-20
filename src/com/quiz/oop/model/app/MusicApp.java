package com.quiz.oop.model.app;

/**
 * ============================================================================
 * [Lecture 4: Polymorphism & Lecture 6: Multitasking]
 * MusicApp: แอปพลิเคชันเล่นเพลง
 * ============================================================================
 */
public class MusicApp extends App {
    private boolean isPlaying = false;
    private int currentSongIndex = 0;
    private final String[] playlist = {
        "Cyber City Sunset - Android Beats",
        "Object Oriented Symphony - Java Maestros",
        "Multithreading Flow - Runtime Harmony",
        "MVC Architecture Beat - Controller Groove"
    };

    public MusicApp() {
        super("เพลง (Music)", "com.android.music", "🎵", 30);
    }

    public boolean isPlaying() { return isPlaying; }
    public String getCurrentTrack() { return playlist[currentSongIndex]; }

    @Override
    public String renderContent() {
        String spinClass = isPlaying ? "spinning" : "";
        String playBtnIcon = isPlaying ? "⏸️" : "▶️";

        return """
            <div class='music-container'>
                <div class='music-record %s'>
                    <div class='record-center'>🎧</div>
                </div>
                <div class='music-meta'>
                    <h3>%s</h3>
                    <p>%s</p>
                </div>
                <div class='music-progress'>
                    <div class='music-progress-bar' style='width: 60%%;'></div>
                </div>
                <div class='music-btns'>
                    <button class='music-btn' onclick='triggerEvent("music_action", "prev")'>⏮️</button>
                    <button class='music-btn play-btn' onclick='triggerEvent("music_action", "toggle")'>%s</button>
                    <button class='music-btn' onclick='triggerEvent("music_action", "next")'>⏭️</button>
                </div>
            </div>
            """.formatted(spinClass, getCurrentTrack(), isPlaying ? "กำลังเล่นเพลง (Background Thread Active)" : "หยุดชั่วคราว", playBtnIcon);
    }

    @Override
    public String performAction(String actionParam) {
        if ("toggle".equals(actionParam)) {
            isPlaying = !isPlaying;
            return isPlaying ? "กำลังเล่น: " + getCurrentTrack() : "หยุดเล่นเพลง";
        } else if ("next".equals(actionParam)) {
            currentSongIndex = (currentSongIndex + 1) % playlist.length;
            return "เปลี่ยนเป็นเพลง: " + getCurrentTrack();
        } else if ("prev".equals(actionParam)) {
            currentSongIndex = (currentSongIndex - 1 + playlist.length) % playlist.length;
            return "เปลี่ยนเป็นเพลง: " + getCurrentTrack();
        }
        return "Music Player Ready";
    }
}
