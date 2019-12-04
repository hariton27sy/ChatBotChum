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
        System.out.println("Я - шефот, и могу помочь тебе выбрать блюдо на вечер. Или на утро. Или перекус.\n" +
                "В общем, не стесняйся, говори, что ты хочешь, а я подскажу ;)\n" +
                        "Для того, чтобы узнать, как со мной работать, напиши '/help'");
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
        System.out.print("Введи имя: ");
        return scanner.nextLine();
    }
}
