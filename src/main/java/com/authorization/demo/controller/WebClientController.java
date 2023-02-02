package com.authorization.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
public class WebClientController {

    private final WebClient webClient;
    private final String messagesBaseUri;

    public WebClientController(WebClient webClient, @Value("${messages.base-uri}")String messagesBaseUri) {
        this.webClient = webClient;
        this.messagesBaseUri = messagesBaseUri;
    }


    @GetMapping(value = "/messages")
    public String[] getSourceProtectedMessages(
            @RegisteredOAuth2AuthorizedClient("messaging-client-authorization-code") OAuth2AuthorizedClient authorizedClient) {
        return this.webClient
                .get()
                .uri(messagesBaseUri)//去请求resource-server的接口获取资源
                .attributes(oauth2AuthorizedClient(authorizedClient))
                .retrieve()
                .bodyToMono(String[].class)
                .block();
    }
}
