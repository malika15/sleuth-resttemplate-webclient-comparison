package sleuth.webmvc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@EnableAutoConfiguration
@RestController
public class FrontendUsingWebClient {

    @Autowired
    WebClient webClient;
    private Logger log = LoggerFactory.getLogger("FrontendUsingWebClient");

    public static void main(String[] args) {
        SpringApplication.run(FrontendUsingWebClient.class,
            "--spring.application.name=FrontendUsingWebClient",
            "--server.port=8081"
        );
    }

    @Bean
    WebClient webClient() {
        return WebClient.builder().exchangeStrategies(ExchangeStrategies.builder().codecs(c ->
            c.defaultCodecs().enableLoggingRequestDetails(true)).build()
        ).baseUrl("http://localhost:9000").build();
    }

    @RequestMapping("/")
    public Mono<String> callBackend() {
        log.info("Frontend WebClient::Begin");
        Mono<String> response = webClient.get().uri("/api").retrieve().bodyToMono(String.class)
            .doFirst(() -> log.info("Frontend WebClient ::doFirst"))
            .doOnSubscribe(subscription -> log.info("Frontend WebClient::doOnSubscribe"))
            .doOnRequest(value -> log.info("Frontend WebClient::doOnRequest"))
            .doOnSuccess(s -> log.info("Frontend WebClient::doOnSuccess"))
            .doOnEach(stringSignal -> log.info("Frontend WebClient::doOnEach"))
            .doOnNext(s -> log.info("Frontend WebClient::doOnNext"))
            .doAfterTerminate(() -> log.info("Frontend::doAfterTerminate"))
            .doFinally(signalType -> log.info("Frontend WebClient::doFinally"))
            .doOnCancel(() -> log.info("Frontend WebClient::doOnCancel"));
        log.info("Frontend WebClient::End");
        return response;
    }

}
