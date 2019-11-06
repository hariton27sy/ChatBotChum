package com.ChatBot.Interfaces;

import com.ChatBot.Core.IBotLogic;
import com.ChatBot.Core.Message;

import java.util.Scanner;

public class ConsoleInterface implements IUserInterface {
    private IBotLogic botLogic;
    private Scanner scanner;
    private boolean isWorking;

    @Override
    public void initialize(IBotLogic botLogic) {
        this.botLogic = botLogic;
        scanner = new Scanner(System.in);
    }

    @Override
    public void start() throws Exception {
        isWorking = true;
        mainLoop(login());
    }

    @Override
    public void stop() {
        isWorking = false;
    }

    private void mainLoop(String username) throws Exception {
        while (isWorking) {
            var userMessage = scanner.nextLine();
            var outMessage = botLogic.analyzeAndGetAnswer(username, new Message(userMessage));
            if (outMessage.equals("Q")){
                stop();
                continue;
            }
            System.out.println(outMessage);
        }

        System.out.println("Скоро увидимся!");
    }

    private String login(){
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }
}
