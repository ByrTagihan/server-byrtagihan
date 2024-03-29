package serverbyrtagihan;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.TimeZone;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class ServerByrtagihanApplication extends SpringBootServletInitializer {
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public DataSource getDataSource() {
		return DataSourceBuilder.create()
				.driverClassName("com.mysql.cj.jdbc.Driver")
				.url("jdbc:mysql://localhost:3306/byr_tagihan")
				.username("root")
				.password("12345678")
				.build();
	}
	public static void main(String[] args) {
		SpringApplication.run(ServerByrtagihanApplication.class, args);
		System.out.println("Selesai");
	}
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
		return jacksonObjectMapperBuilder -> {
			jacksonObjectMapperBuilder.timeZone(TimeZone.getTimeZone("Asia/Jakarta"));
		};
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(ServerByrtagihanApplication.class);
	}
}
