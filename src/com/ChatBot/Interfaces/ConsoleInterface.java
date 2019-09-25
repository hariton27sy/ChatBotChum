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


    public void greetUser(UserInfo user){
        System.out.println(String.format("Hello, %s!", user.username));
    }

    public UserInfo initializeNewUser(){
        send("Enter your name: ");
        return new UserInfo(receive());
    }
}
