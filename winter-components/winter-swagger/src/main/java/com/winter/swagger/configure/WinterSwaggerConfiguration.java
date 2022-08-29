package com.winter.swagger.configure;

import com.github.xiaoymin.knife4j.spring.configuration.Knife4jProperties;
import com.winter.common.config.WinterConfig;
import com.winter.common.utils.StringUtils;
import com.winter.common.utils.spring.SpringUtils;
import com.winter.swagger.domain.WinterSwaggerApiGroupInfo;
import com.winter.swagger.domain.WinterSwaggerApiHeaderParameterInfo;
import com.winter.swagger.domain.WinterSwaggerApiInfo;
import com.winter.swagger.properties.WinterSwaggerProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.SpringfoxWebConfiguration;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.configuration.SwaggerCommonConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Swagger 配置
 * <p>
 * </p>
 *
 * @author Damien
 * @description
 * @create 2022/8/17 15:44
 */
@EnableOpenApi
@EnableConfigurationProperties({Knife4jProperties.class, WinterSwaggerProperties.class})
@Import({SpringfoxWebConfiguration.class, SwaggerCommonConfiguration.class, WinterConfig.class})
public class WinterSwaggerConfiguration implements WebMvcConfigurer {

    @Bean
    public boolean createWinterSwaggerDockets(WinterConfig winterConfig, WinterSwaggerProperties winterSwaggerProperties,
                                              WinterSwaggerApiInfo winterSwaggerApiInfo) {
        if (StringUtils.isEmpty(winterSwaggerApiInfo.getVersion())) {
            winterSwaggerApiInfo.setVersion(winterConfig.getVersion());
        }
        winterSwaggerApiInfo.getGroups().forEach(group -> loadDocket(winterSwaggerApiInfo, group, winterSwaggerProperties));
        return true;
    }

    /**
     * 创建 Swagger 的 Docket 并注入 Spring 容器中
     *
     * @param api
     * @param group
     * @return
     */
    private void loadDocket(WinterSwaggerApiInfo api, WinterSwaggerApiGroupInfo group, WinterSwaggerProperties winterSwaggerProperties) {
        Docket docket = SpringUtils.getBean(group.getBeanId());
        ApiSelectorBuilder builder = docket.groupName(group.getGroupName())
                .enable(winterSwaggerProperties.isEnabled())
                .apiInfo(createApiInfo(api))
                .select()
                // 扫描所有有注解的api，用这种方式更灵活
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class));
        if (group.getAnnotation() != null) {
            builder = builder.apis(RequestHandlerSelectors.withClassAnnotation(group.getAnnotation()));
        } else if (group.getPackages() != null && group.getPackages().size() > 0) {
            Predicate<RequestHandler> apiRredicate = null;
            for (String pck : group.getPackages()) {
                Predicate<RequestHandler> predicate = RequestHandlerSelectors.basePackage(pck);
                if (apiRredicate == null) {
                    apiRredicate = predicate;
                }
            }
            builder = builder.apis(apiRredicate);
        } else {
            builder = builder.apis(RequestHandlerSelectors.withClassAnnotation(Api.class));
        }
        docket = builder.paths(PathSelectors.any())
                .build()
                /* 设置安全模式，swagger可以设置访问token */
                .securitySchemes(securitySchemes())
                .securityContexts(securityContexts())
                .pathMapping(winterSwaggerProperties.getPathMapping());
        List<RequestParameter> operationParameters = new ArrayList<>();

        if (api.getHeaderParameters() != null) {
            for (WinterSwaggerApiHeaderParameterInfo headerParameterInfo : api.getHeaderParameters()) {
                operationParameters.add(createHeaderParameter(headerParameterInfo));
            }
        }
        docket.globalRequestParameters(operationParameters);
    }

    /**
     * 安全模式，这里指定token通过Authorization头请求头传递
     */
    private List<SecurityScheme> securitySchemes() {
        List<SecurityScheme> apiKeyList = new ArrayList<SecurityScheme>();
        apiKeyList.add(new ApiKey("Authorization", "Authorization", In.HEADER.toValue()));
        return apiKeyList;
    }

    /**
     * 安全上下文
     */
    private List<SecurityContext> securityContexts() {
        List<SecurityContext> securityContexts = new ArrayList<>();
        securityContexts.add(
                SecurityContext.builder()
                        .securityReferences(defaultAuth())
                        .operationSelector(o -> o.requestMappingPattern().matches("/.*"))
                        .build());
        return securityContexts;
    }

    /**
     * 默认的安全上引用
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        List<SecurityReference> securityReferences = new ArrayList<>();
        securityReferences.add(new SecurityReference("Authorization", authorizationScopes));
        return securityReferences;
    }

    /**
     * 创建 API 作者信息
     *
     * @param apiInfo aip信息
     * @return 作者信息
     */
    private Contact createContactInfo(WinterSwaggerApiInfo apiInfo) {
        return new Contact(apiInfo.getAuthorName(), apiInfo.getAuthorUrl(), apiInfo.getAuthorEmail());
    }

    /**
     * 创建 api 头参数
     *
     * @param headerParameter 请求头
     * @return
     */
    private RequestParameter createHeaderParameter(WinterSwaggerApiHeaderParameterInfo headerParameter) {
        Set<MediaType> accepts = new HashSet<>();
        accepts.add(new MediaType(headerParameter.getDataType()));
        RequestParameterBuilder parameterBuilder = new RequestParameterBuilder();
        parameterBuilder.name(headerParameter.getName()).description(headerParameter.getDescription())
                .in(ParameterType.HEADER)
                .accepts(accepts)
                .required(headerParameter.isRequired());
        return parameterBuilder.build();
    }

    /**
     * 创建 API 信息
     *
     * @param apiInfo Api 的配置信息
     * @return API 信息
     */
    private ApiInfo createApiInfo(WinterSwaggerApiInfo apiInfo) {
        return new ApiInfoBuilder()
                .title(apiInfo.getTitle())
                .description(apiInfo.getDescription())
                .version(apiInfo.getVersion())
                .contact(createContactInfo(apiInfo))
                .license(apiInfo.getLicense())
                .licenseUrl(apiInfo.getLicenseUrl())
                .build();
    }
}
