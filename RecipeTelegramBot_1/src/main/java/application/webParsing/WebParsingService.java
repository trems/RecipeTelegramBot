package application.webParsing;


import application.data.persistance.dto.RecipeDto;
import application.webParsing.interfaces.ParsingService;
import application.webParsing.parsingConfiguration.WebParsingConfiguration;
import application.webParsing.parsingConfiguration.WebResource;
import application.utils.QueryParamsParser;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class WebParsingService implements ParsingService {
	private static final Logger log = LogManager.getLogger(WebParsingService.class);

	private static final String URL_TAG = "a";
	private static final String URL_ATTRIBUTE = "href";
	private static final String PARAMS = ":params";

	private int recordLimit;


	public WebParsingService(int recordLimit) {
		this.recordLimit = recordLimit;
	}

	@Override
	public List<RecipeDto> getRecipesFromWebResources(String userRequest) {
		List<RecipeDto> recipes = new ArrayList<>();
		List<WebResource> webResources = getWebResourcesWithItemsAndUrls(userRequest);

		if (CollectionUtils.isEmpty(webResources)) {
			log.info(String.format("По запросу [%s] не найдено ни одного результата", userRequest));
			return recipes;
		}

		for (WebResource webResource : webResources) {
			Map<String, String> itemsAndURLs = webResource.getItemsAndURLs();

			for (String title : itemsAndURLs.keySet()) {
				ParsedWebPage parsedWebPage = new ParsedWebPage(title, itemsAndURLs.get(title), webResource.getItemPage());

				if(parsedWebPage.areMainFieldsNotEmpty()) {
					RecipeDto recipe = new RecipeDto(parsedWebPage);
					recipes.add(recipe);
				}
			}
		}
		return recipes;
	}

	private List<WebResource> getWebResourcesWithItemsAndUrls(String userRequest) {
		List<WebResource> webResources = WebParsingConfiguration.getWebResources();

		if (CollectionUtils.isEmpty(webResources)) {
			log.info("Конфигурации веб-ресурса для формирования запроса отсутствуют");
			return new ArrayList<>();
		}

		int recordCounter = 0;
		for (WebResource webResource : webResources) {
			String preparedRequestURL = getPreparedRequestURL(userRequest, webResource.getRequestURL());

			try {
				Document doc = Jsoup.connect(preparedRequestURL).get();
				Elements itemElements = doc.getElementsByClass(webResource.getItemClass());

				Document itemDoc = Jsoup.parseBodyFragment(itemElements.html());
				Elements elements = itemDoc.getElementsByTag(URL_TAG);

				fillWebResourceItemsAndUrls(recordCounter, webResource, elements);

				if (recordCounter < recordLimit) {
					break;
				}
			} catch (IOException e) {
				log.error(e);
			}
		}
		return webResources;
	}

	private void fillWebResourceItemsAndUrls(int recordCounter, WebResource webResource, Elements elements) {
		for (Element element : elements) {
			String itemTitle = element.text();

			if (StringUtils.isNotEmpty(itemTitle)) {
				String itemURL = element.attr(URL_ATTRIBUTE);

				if (StringUtils.isNotEmpty(itemURL) && recordCounter < recordLimit) {
					webResource.getItemsAndURLs().put(itemTitle, webResource.getMainURL() + itemURL);
					recordCounter ++;
				} else {
					break;
				}
			}
		}
	}

	private String getPreparedRequestURL(String userRequest, String requestURL) {
		if (StringUtils.isEmpty(userRequest) || StringUtils.isEmpty(requestURL)) {
			log.error(String.format("Некорректно заданы параметры для формирования веб-запроса: userRequest = %s , requestURL = %s", userRequest, requestURL));
			return StringUtils.EMPTY;
		}

		String preparedUserRequest = QueryParamsParser.getPreparedParamsForWebRequest(userRequest);
		return StringUtils.replace(requestURL, PARAMS, preparedUserRequest);
	}
}
