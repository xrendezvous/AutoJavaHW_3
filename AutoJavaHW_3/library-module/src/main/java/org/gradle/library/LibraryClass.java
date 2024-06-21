package org.gradle.library;

public class LibraryClass {
    private String message;

    public LibraryClass(String message) {
        this.message = message;
    }

    public void displayMessage() {
        System.out.println("овідомлення з файлу LibraryClass: " + message);
    }
}