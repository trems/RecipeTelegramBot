package application.telegramBot.tasks;


import application.telegramBot.RecipeTelegramBot;
import application.telegramBot.commands.Command;
import application.telegramBot.reply.Reply;
import application.telegramBot.commands.CommandParser;
import application.telegramBot.handlers.AbstractHandler;
import application.telegramBot.handlers.CommandRequestHandler;
import application.telegramBot.handlers.RecipeRequestHandler;
import application.telegramBot.services.SendingService;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;


public class RecipeBotTask implements Runnable {
	private Message message;
	private CallbackQuery callbackQuery;
	private RecipeTelegramBot telegramBot;
	private boolean isCallbackRequest;


	public RecipeBotTask(RecipeTelegramBot telegramBot, Message message, boolean isCallbackRequest) {
		this.message = message;
		this.telegramBot = telegramBot;
		this.isCallbackRequest = isCallbackRequest;
	}

	public RecipeBotTask(RecipeTelegramBot telegramBot, CallbackQuery callbackQuery, boolean isCallbackRequest) {
		this.callbackQuery = callbackQuery;
		this.telegramBot = telegramBot;
		this.isCallbackRequest = isCallbackRequest;
	}

	@Override
	public void run() {
		if (!isCallbackRequest) {
			Command command = CommandParser.parseRequest(message.getText());
			AbstractHandler handler = getHandlerForRequest(command);
			Reply reply = handler.handleRequest();
			reply.setChatId(message.getChatId());
			SendingService.getInstance().sendReply(telegramBot, reply);
		} else {
			SendingService.getInstance().sendSelectedRecipeFromCache(telegramBot, callbackQuery.getMessage().getChatId(), callbackQuery.getData());
		}
	}

	private AbstractHandler getHandlerForRequest(Command command) {
		switch (command) {
			case HELP:
			case START:
				return new CommandRequestHandler(command);
			case REQUEST:
				SendingService.getInstance().sendAwaitingReply(telegramBot, message.getChatId());
				return new RecipeRequestHandler(command);
			default:
				throw new IllegalStateException("Unexpected value: " + command.name());
		}
	}
}
