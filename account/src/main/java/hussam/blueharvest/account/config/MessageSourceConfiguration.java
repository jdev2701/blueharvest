package hussam.blueharvest.account.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class MessageSourceConfiguration {

	private final static Logger logger = LoggerFactory.getLogger(MessageSourceConfiguration.class);
	private static final String MESSAGE_FILES_PATH = "classpath:messages";

	public MessageSourceConfiguration() {
		// Default constructor
	}

	@Bean(name = "messageSource")
	public ReloadableResourceBundleMessageSource messageSource() {
		logger.info("Loading properties from folder:{}", MESSAGE_FILES_PATH);
		ReloadableResourceBundleMessageSource msgSource = new ReloadableResourceBundleMessageSource();
		msgSource.setBasenames(MESSAGE_FILES_PATH);
		msgSource.setCacheSeconds(60);// in secs
		return msgSource;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		logger.info("Loading properties from property files...");
		PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
		pspc.setIgnoreUnresolvablePlaceholders(true);
		pspc.setOrder(1);
		return pspc;
	}

}
