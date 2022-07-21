package com.lira17.expensetracker.exchange;

import com.lira17.expensetracker.exchange.dto.Exchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Service
public class ExchangeService {
    private static final String AMOUNT_PARAM = "amount";
    private static final String FROM_PARAM = "from";
    private static final String TO_PARAM = "to";
    private static final String DATE_PARAM = "date";
    private static final String API_KEY_HEADER = "apikey";

    @Value("${exchange.api.base.url}")
    private String exchangeBaseUrl;

    @Value("${exchange.api.convert.path}")
    private String exchangeConvertApiPath;

    @Value("${exchange.api.token}")
    private String exchangeApiToken;

    @Value("${exchange.main.currency}")
    private String mainCurrency;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private HttpClient httpClient;

    public Currency getMainCurrency() {
        return Currency.valueOf(mainCurrency);
    }

    public Exchange getExchange(double amount, String currency, String date) {
        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(exchangeBaseUrl)
                .build()
                .get()
                .uri(uriBuilder -> uriBuilder.path(exchangeConvertApiPath)
                        .queryParam(AMOUNT_PARAM, amount)
                        .queryParam(FROM_PARAM, currency)
                        .queryParam(TO_PARAM, mainCurrency)
                        .queryParam(DATE_PARAM, date)
                        .build())
                .header(API_KEY_HEADER, exchangeApiToken)
                .retrieve()
                .bodyToMono(Exchange.class)
                .block();
    }
}
