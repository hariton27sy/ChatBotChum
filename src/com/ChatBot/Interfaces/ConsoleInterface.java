package com.ChatBot.Interfaces;

import java.util.Scanner;

public class ConsoleInterface implements InOutInterface {
    @java.lang.Override
    public String receive() {
        var scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    @java.lang.Override
    public void send(String answer) {
        System.out.println(answer);
    }
}
