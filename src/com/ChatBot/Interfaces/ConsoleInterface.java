package com.ChatBot.Interfaces;

import com.ChatBot.Core.UserInfo;

import java.util.Scanner;

public class ConsoleInterface implements InOutInterface {
    private Scanner scanner;

    public ConsoleInterface(){
        scanner = new Scanner(System.in);
    }


    public String receive() {
        return scanner.nextLine();
    }

    public void send(String answer) {
        System.out.println(answer);
    }
}
