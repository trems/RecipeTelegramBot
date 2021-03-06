package application.telegramBot.properties;


import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import java.util.Map;


public class ConfigurationParser {
	private static final String CONFIG_PATH = "/home/ubuntu/telegramBot/recipeTelegramBot/config/telegramBot.yml";

	public static void parseConfigurations() {
		Yaml yaml = new Yaml();

		InputStream input = null;
		try {
			input = new FileInputStream(new File(CONFIG_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Map config = yaml.load(input);
		Map proxyConfig = (Map) config.get(BotConfiguration.PROXY_KEY);
		Map botConfig = (Map) config.get(BotConfiguration.BOT_KEY);
		Map guiConfig = (Map) botConfig.get(BotConfiguration.GUI);
		Map handlerConfig = (Map) botConfig.get(BotConfiguration.HANDLER);

		BotConfiguration.setProxyHost(String.valueOf(proxyConfig.get(BotConfiguration.PROXY_HOST_KEY)));
		BotConfiguration.setProxyPort((Integer) proxyConfig.get(BotConfiguration.PROXY_PORT_KEY));
		BotConfiguration.setProxyUser(String.valueOf(proxyConfig.get(BotConfiguration.PROXY_USER_KEY)));
		BotConfiguration.setProxyPassword(String.valueOf(proxyConfig.get(BotConfiguration.PROXY_PASSWORD_KEY)));
		BotConfiguration.setBotUsername(String.valueOf(botConfig.get(BotConfiguration.BOT_USERNAME)));
		BotConfiguration.setBotToken(String.valueOf(botConfig.get(BotConfiguration.BOT_TOKEN)));
		BotConfiguration.setNodeQuantityInRow((Integer) guiConfig.get(BotConfiguration.NODE_QUANTITY_IN_ROW));
		BotConfiguration.setRecordParsingLimit((Integer) handlerConfig.get(BotConfiguration.RECORD_PARSING_LIMIT));
		BotConfiguration.setPreferredRecordOutputLimit((Integer) handlerConfig.get(BotConfiguration.PREFERRED_RECORD_OUTPUT_LIMIT));
	}
}
