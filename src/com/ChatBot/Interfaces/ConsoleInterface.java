package com.ChatBot.Interfaces;

import com.ChatBot.Core.MessageHandler;

import java.util.Scanner;

public class ConsoleInterface {
    public void main(String[] args) {
        var bot = new MessageHandler();
        var scanner = new Scanner(System.in);
        while (true) {
            var userInput = scanner.nextLine();
            var answer = bot.HandleMessage(userInput);
            System.out.println(answer);
        }
    }

    public void WriteHelloMessage() {
        System.out.println("While i can do nothing!");
    }
}
