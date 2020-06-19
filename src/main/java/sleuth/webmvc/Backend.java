package sleuth.webmvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Date;

@EnableAutoConfiguration
@RestController
public class Backend {

    public static void main(String[] args) {
        SpringApplication.run(Backend.class,
            "--spring.application.name=backend",
            "--server.port=9000"
        );
    }

    @RequestMapping("/api")
    public Mono<String> printDate() {
        return Mono.fromSupplier(() -> "Backend:: "+new Date().toString());
    }
}
