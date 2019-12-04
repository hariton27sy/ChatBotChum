package com.ChatBot.Interfaces;

import com.ChatBot.Core.IBotLogic;
import com.ChatBot.DataBases.IDataStorage;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface IUserInterface {
    public void initialize(IBotLogic botLogic);
    public void start() throws Exception;
    public void stop();
}
