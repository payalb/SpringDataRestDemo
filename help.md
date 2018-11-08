Spring Data REST configuration is defined in a class called RepositoryRestMvcConfiguration and you can import that class into your application’s configuration
To customize the configuration, register a RepositoryRestConfigurer (or extend RepositoryRestConfigurerAdapter) and implement or override the configure…-methods relevant to your use case.

Spring Data REST uses a RepositoryDetectionStrategy to determine whether a repository is exported as a REST resource. The RepositoryDiscoveryStrategies enumeration includes the following values:

Table 1. Repository detection strategies
Name

Description

DEFAULT

Exposes all public repository interfaces but considers the exported flag of @(Repository)RestResource.

ALL

Exposes all repositories independently of type visibility and annotations.

ANNOTATION

Only repositories annotated with @(Repository)RestResource are exposed, unless their exported flag is set to false.

VISIBILITY

Only public repositories annotated are exposed.


 you can do change the base URI by setting a single property in application.properties, as follows:

spring.data.rest.basePath=/api

@Configuration
class CustomRestMvcConfiguration {

  @Bean
  public RepositoryRestConfigurer repositoryRestConfigurer() {

    return new RepositoryRestConfigurerAdapter() {

      @Override
      public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setBasePath("/api");
      }
    };
  }
}


Alternatively, you can register a custom implementation of RepositoryRestConfigurer as a Spring bean and make sure it gets picked up by component scanning, as follows:

@Component
public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {

  @Override
  public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
    config.setBasePath("/api");
  }
}

You can alter the following properties:

Table 2. Spring Boot configurable properties
Property

Description

basePath

the root URI for Spring Data REST

defaultPageSize

change the default for the number of items served in a single page

maxPageSize

change the maximum number of items in a single page

pageParamName

change the name of the query parameter for selecting pages

limitParamName

change the name of the query parameter for the number of items to show in a page

sortParamName

change the name of the query parameter for sorting

defaultMediaType

change the default media type to use when none is specified

returnBodyOnCreate

change whether a body should be returned when creating a new entity

returnBodyOnUpdate

change whether a body should be returned when updating an entity


For the resources exposed, we use a set of default status codes:

200 OK: For plain GET requests.

201 Created: For POST and PUT requests that create new resources.

204 No Content: For PUT, PATCH, and DELETE requests when the configuration is set to not return response bodies for resource updates (RepositoryRestConfiguration.returnBodyOnUpdate). If the configuration value is set to include responses for PUT, 200 OK is returned for updates, and 201 Created is returned for resource created through PUT.

By default, Spring Data REST uses HAL to render responses. HAL defines the links to be contained in a property of the returned document.

The GET method supports the following media types:

application/hal+json

application/json


To set the page size to any other number, add a size parameter, as follows:

http://localhost:8080/people/?size=5

To have your results sorted on a particular property, add a sort URL parameter with the name of the property on which you want to sort the results. You can control the direction of the sort by appending a comma (,) to the the property name plus either asc or desc

curl -v "http://localhost:8080/people/search/nameStartsWith?name=K&sort=name,desc"

If you want to serialize or deserialize a domain type in a special way, you can register your own implementations with Jackson’s ObjectMapper, and the Spring Data REST exporter transparently handles those domain objects correctly. To add serializers from your setupModule method implementation, you can do something like the following:

@Override
public void setupModule(SetupContext context) {
  SimpleSerializers serializers = new SimpleSerializers();
  SimpleDeserializers deserializers = new SimpleDeserializers();

  serializers.addSerializer(MyEntity.class, new MyEntitySerializer());
  deserializers.addDeserializer(MyEntity.class, new MyEntityDeserializer());

  context.addSerializers(serializers);
  context.addDeserializers(deserializers);
}
