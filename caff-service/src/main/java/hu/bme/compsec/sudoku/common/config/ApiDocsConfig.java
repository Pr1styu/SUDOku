package hu.bme.compsec.sudoku.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class ApiDocsConfig {

    private static final String AUTH_SERVER_URL = "http://127.0.0.1:9000/oauth2";
    private static final String CLIENT_ID = "frontend";
    private static final String CLIENT_SECRET = "secret";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .securitySchemes(Collections.singletonList(securityScheme()))
//                .securityContexts(Collections.singletonList(securityContext()))
                .select()
                .apis(RequestHandlerSelectors.basePackage("hu.bme.compsec.sudoku"))
                .paths(PathSelectors.any())
                .build();
    }

//    @Bean
//    public SecurityConfiguration security() {
//        return SecurityConfigurationBuilder.builder()
//                .clientId(CLIENT_ID)
//                .clientSecret(CLIENT_SECRET)
//                .scopeSeparator(" ")
//                .useBasicAuthenticationWithAccessCodeGrant(true)
//                .build();
//    }

    /**
     * A security scheme mondja meg hogy működik a security.
     * @return
     */
//    private SecurityScheme securityScheme() {
//        GrantType grantType = new AuthorizationCodeGrantBuilder()
//                .tokenEndpoint(b -> b.url(AUTH_SERVER_URL + "/token").tokenName("oauthtoken"))
//                .tokenRequestEndpoint(b -> b.url(AUTH_SERVER_URL + "/authorize").clientIdName(CLIENT_ID).clientSecretName(CLIENT_SECRET)).build();
//
//        return new OAuthBuilder().name("spring_oauth")
//                .scopes(Collections.singletonList(new AuthorizationScope("openid", "oidc")))
//                .grantTypes(Collections.singletonList(grantType))
//                .build();
//    }

    /**
     * A security context mondja meg, mikor kell a security-t használni. Most akkor használjuk ha a végpontban nincs /public
     * @return
     */
//    private SecurityContext securityContext() {
//        return SecurityContext.builder()
//                .securityReferences(Collections.singletonList(new SecurityReference("spring_oauth", new AuthorizationScope[] {})))
//                .operationSelector(s -> !s.requestMappingPattern().matches(".*/public.*"))
//                .build();
//    }

}
