package com.praveen.emp;

import static springfox.documentation.builders.PathSelectors.regex;

import java.time.LocalDate;
import java.util.ArrayList;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Predicate;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.DocExpansion;
import springfox.documentation.swagger.web.ModelRendering;
import springfox.documentation.swagger.web.OperationsSorter;
import springfox.documentation.swagger.web.TagsSorter;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger.web.UiConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class SpringbootBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootBackendApplication.class);
	}

	@Bean
	public Docket backendServiceApi(Environment environment, TypeResolver typeResolver) {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfo("Employee Registration App", "", "", Strings.EMPTY,
						new Contact("Praveen Oruganti Tech Team", "https://praveenorugantitech.blogspot.com/",
								"praveenorugantitech@gmail.com"),
						Strings.EMPTY, Strings.EMPTY, new ArrayList<>()))
				.select().apis(RequestHandlerSelectors.any()).paths(paths()).build().pathMapping("/")
				.directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
				.alternateTypeRules(AlternateTypeRules.newRule(
						typeResolver.resolve(DeferredResult.class,
								typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
						typeResolver.resolve(WildcardType.class)))
				.useDefaultResponseMessages(false);
	}

	private Predicate<String> paths() {
		return regex("/api/v1_0/.*");
	}

	@Bean
	@ConditionalOnMissingBean
	public TypeResolver typeResolver() {
		return new TypeResolver();
	}

	@Bean
	UiConfiguration uiConfig(Environment environment) {
		return UiConfigurationBuilder.builder().deepLinking(true).displayOperationId(false).defaultModelsExpandDepth(1)
				.defaultModelExpandDepth(1).defaultModelRendering(ModelRendering.EXAMPLE).displayRequestDuration(false)
				.docExpansion(DocExpansion.NONE).filter(false).maxDisplayedTags(null)
				.operationsSorter(OperationsSorter.ALPHA).showExtensions(false).tagsSorter(TagsSorter.ALPHA)
				.supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS).validatorUrl(null).build();
	}

}
