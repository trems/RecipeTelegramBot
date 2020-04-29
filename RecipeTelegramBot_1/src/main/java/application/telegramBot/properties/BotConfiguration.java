package application.telegramBot.properties;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public class BotConfiguration {
	public static final String PROXY_KEY = "proxy";
	public static final String BOT_KEY = "telegramBot";

	public static final String PROXY_HOST_KEY = "host";
	public static final String PROXY_PORT_KEY = "port";
	public static final String PROXY_USER_KEY = "user";
	public static final String PROXY_PASSWORD_KEY = "password";

	public static final String BOT_USERNAME = "username";
	public static final String BOT_TOKEN = "token";

	public static final String GUI = "gui";
	public static final String NODE_QUANTITY_IN_ROW = "nodeQuantityInRow";

	public static final String HANDLER = "handler";
	public static final String RECORD_PARSING_LIMIT = "recordParsingLimit";
	public static final String PREFERRED_RECORD_OUTPUT_LIMIT = "preferredRecordOutputLimit";

	@Getter
	@Setter
	private static String proxyHost;
	@Getter
	@Setter
	private static int proxyPort;
	@Getter
	@Setter
	private static String proxyUser;
	@Getter
	@Setter
	private static String proxyPassword;

	@Getter
	@Setter
	private static String botUsername;
	@Getter
	@Setter
	private static String botToken;

	@Getter
	@Setter
	private static int nodeQuantityInRow;

	@Getter
	@Setter
	private static int recordParsingLimit;
	@Getter
	@Setter
	private static int preferredRecordOutputLimit;
}
