package com.ChatBot;

import com.ChatBot.Interfaces.ConsoleInterface;

public class Chefot {
    public void main(String[] args) {
        if (args.length == 0) {
            var inout = new ConsoleInterface();
            inout.main(new String[0]);
        }
        else {
            System.out.println("Help");
        }
    }
}
