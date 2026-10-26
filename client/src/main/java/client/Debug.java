package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class Debug {

    private final static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Test line");
        scanner.nextLine();
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("The console has been cleared!");
        scanner.nextLine();
    }
}
