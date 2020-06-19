package sleuth.webmvc;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@EnableAutoConfiguration
@RestController
public class FrontendUsingRestTemplate {

    @Autowired
    RestTemplate restTemplate;
    private Logger log = LoggerFactory.getLogger("FrontendUsingRestTemplate");

    public static void main(String[] args) {
        SpringApplication.run(FrontendUsingRestTemplate.class,
            "--spring.application.name=FrontendUsingRestTemplate",
            "--server.port=8082"
        );
    }

    @Bean
    RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        restTemplate.getInterceptors().add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                log.info("Frontend RestTemplate::Sending request to Backend "+request.getURI().toString());
                ClientHttpResponse clientHttpResponse =  execution.execute(request, body);
                log.info("Frontend RestTemplate::Receiving response from Backend "+clientHttpResponse.getStatusCode());
                return clientHttpResponse;
            }
        });
        return restTemplate;
    }


    @RequestMapping("/")
    public ResponseEntity<String> callBackend() {
        log.info("Frontend RestTemplate::Begin");
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:9000/api", String.class);
        log.info("Frontend RestTemplate::End " + response.toString());
        return response;
    }

}
