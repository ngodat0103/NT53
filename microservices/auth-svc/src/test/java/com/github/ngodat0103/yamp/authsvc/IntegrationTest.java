// package com.github.ngodat0103.yamp.authsvc;
//
// import com.github.ngodat0103.yamp.authsvc.dto.RoleDto;
// import com.github.ngodat0103.yamp.authsvc.persistence.entity.Role;
// import com.github.ngodat0103.yamp.authsvc.persistence.repository.UserRepository;
// import com.github.ngodat0103.yamp.authsvc.persistence.repository.RoleRepository;
// import java.net.URI;
// import java.util.*;
// import org.junit.jupiter.api.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.core.ParameterizedTypeReference;
// import org.springframework.http.*;
// import org.springframework.test.context.ActiveProfiles;
// import org.springframework.test.context.DynamicPropertyRegistry;
// import org.springframework.test.context.DynamicPropertySource;
// import org.testcontainers.containers.KafkaContainer;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.utility.DockerImageName;
//
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// @ActiveProfiles("integration-test")
// class IntegrationTest {
//
//  static PostgreSQLContainer<?> postgreSQLContainer =
//      new PostgreSQLContainer<>("postgres:16.3-bullseye");
//
//  static KafkaContainer kafkaContainer =
//      new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.0"));
//
//  @BeforeAll
//  static void beforeAll() {
//    postgreSQLContainer.start();
//    kafkaContainer.start();
//  }
//
//  @DynamicPropertySource
//  static void configureProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
//    dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
//    dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
//    dynamicPropertyRegistry.add("spring.datasource.hashedPassword",
// postgreSQLContainer::getPassword);
//    dynamicPropertyRegistry.add(
//        "spring.kafka.producer.bootstrap-servers", kafkaContainer::getBootstrapServers);
//  }
//
//  @AfterAll
//  static void afterAll() {
//    postgreSQLContainer.stop();
//    kafkaContainer.stop();
//  }
//
//  @Autowired private TestRestTemplate testRestTemplate;
//  @Autowired RoleRepository roleRepository;
//  @Autowired
//  UserRepository userRepository;
//  private final Random random = new Random();
//  private final String ALREADY_EXIST_TEMPLATE = "%s with %s: %s already exists";
//  private final AccountRegisterDto accountRegisterDtoMock =
//      AccountRegisterDto.builder()
//          .username("testUser")
//          .hashedPassword("testPassword")
//          .email("test@gmail.com")
//          .firstName("firstNameTest")
//          .lastName("lastNameTest")
//          .build();
//  private final AccountResponseDto accountResponseDtoMock =
//      AccountResponseDto.builder()
//          .username(accountRegisterDtoMock.getUsername())
//          .email(accountRegisterDtoMock.getEmail())
//          .roleName("CUSTOMER")
//          .build();
//
//  private final RoleDto roleDto =
//      RoleDto.builder().uuid(null).roleName("customer").roleDescription("Customer").build();
//
//  @BeforeEach
//  void setUp() {
//    HttpHeaders headers = new HttpHeaders();
//    headers.add("Content-Type", "application/json");
//    headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
//    this.testRestTemplate
//        .getRestTemplate()
//        .setInterceptors(
//            Collections.singletonList(
//                (request, body, execution) -> {
//                  request.getHeaders().addAll(headers);
//                  return execution.execute(request, body);
//                }));
//  }
//
//  @Test
//  @DisplayName("context loads")
//  @Order(1)
//  public void contextLoads() {}
//
//  @Test
//  @DisplayName(
//      "Given nothing when create account then return accountDto with 404 because role not found")
//  @Order(2)
//  void givenNothing_whenCreateAccount_thenReturnAccountDtoWith404() {
//    var responseEntity =
//        testRestTemplate.postForEntity("/accounts", accountRegisterDtoMock, ProblemDetail.class);
//    Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), bodyResponse.getStatus());
//    Assertions.assertEquals(
//        "https://problems-registry.smartbear.com/not-found", bodyResponse.getType().toString());
//    Assertions.assertEquals("Not found", bodyResponse.getTitle());
//    Assertions.assertEquals("Role with roleName: CUSTOMER not found", bodyResponse.getDetail());
//    Assertions.assertEquals(
//        "/accounts", Objects.requireNonNull(bodyResponse.getInstance()).toString());
//  }
//
//  @Test
//  @DisplayName("Given nothing when create Role then return roleDto with 201")
//  @Order(3)
//  void givenNothing_whenCreateRole_thenReturn201() {
//    var responseBody = testRestTemplate.postForEntity("/roles", this.roleDto, RoleDto.class);
//    Assertions.assertEquals(HttpStatus.CREATED, responseBody.getStatusCode());
//    var bodyResponse = responseBody.getBody();
//    Assertions.assertNull(bodyResponse);
//  }
//
//  @Test
//  @DisplayName("Given already has role, when create new Role then return 409")
//  @Order(4)
//  void givenAlreadyHasRole_whenCreateNewRole_thenReturn409() {
//    var responseEntity =
//        testRestTemplate.postForEntity("/roles", this.roleDto, ProblemDetail.class);
//    Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(HttpStatus.CONFLICT.value(), bodyResponse.getStatus());
//    Assertions.assertEquals(
//        "https://problems-registry.smartbear.com/already-exists",
//        bodyResponse.getType().toString());
//    Assertions.assertEquals("Already exists", bodyResponse.getTitle());
//    Assertions.assertEquals(
//        String.format(
//            ALREADY_EXIST_TEMPLATE, "Role", "roleName", this.roleDto.getRoleName().toUpperCase()),
//        bodyResponse.getDetail());
//    Assertions.assertEquals(
//        "/roles", Objects.requireNonNull(bodyResponse.getInstance()).toString());
//  }
//
//  @Test
//  @DisplayName(
//      "Given already has role CUSTOMER when create account then return accountDto with 201")
//  @Order(5)
//  void givenHasCustomerRole_whenCreateAccount_thenReturn201() {
//    var responseBody =
//        testRestTemplate.postForEntity(
//            "/accounts", accountRegisterDtoMock, AccountResponseDto.class);
//    Assertions.assertEquals(HttpStatus.CREATED, responseBody.getStatusCode());
//    var bodyResponse = responseBody.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(accountRegisterDtoMock.getEmail(), bodyResponse.getEmail());
//    Assertions.assertEquals(accountRegisterDtoMock.getUsername(), bodyResponse.getUsername());
//    Assertions.assertEquals(accountResponseDtoMock.getRoleName(), bodyResponse.getRoleName());
//    Assertions.assertNotNull(bodyResponse.getUuid());
//
//    try {
//      UUID.fromString(bodyResponse.getUuid());
//    } catch (IllegalArgumentException e) {
//      Assertions.fail("UUID is not valid");
//    }
//  }
//
//  @Test
//  @DisplayName("Given username already exists when create new account then return 409")
//  @Order(7)
//  void givenUsernameAlreadyExist_whenCreateAccount_thenReturn409() {
//    var responseEntity =
//        testRestTemplate.postForEntity("/accounts", accountRegisterDtoMock, ProblemDetail.class);
//    Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(HttpStatus.CONFLICT.value(), bodyResponse.getStatus());
//    Assertions.assertEquals(
//        "https://problems-registry.smartbear.com/already-exists",
//        bodyResponse.getType().toString());
//    Assertions.assertEquals("Already exists", bodyResponse.getTitle());
//    Assertions.assertEquals(
//        String.format(
//            ALREADY_EXIST_TEMPLATE, "Account", "username", accountRegisterDtoMock.getUsername()),
//        bodyResponse.getDetail());
//    Assertions.assertEquals(
//        "/accounts", Objects.requireNonNull(bodyResponse.getInstance()).toString());
//  }
//
//  @Test
//  @DisplayName("Given email already exists when create new account then return 409")
//  @Order(8)
//  void givenAlreadyHasEmail_whenCreateNewAccount_thenReturn409() {
//    var accountRequestDto =
//        AccountRegisterDto.builder()
//            .username("testUser2" + random.nextInt())
//            .hashedPassword("testPassword")
//            .email(accountRegisterDtoMock.getEmail())
//            .firstName("firstNameTest")
//            .lastName("lastNameTest")
//            .build();
//    var responseEntity =
//        testRestTemplate.postForEntity("/accounts", accountRequestDto, ProblemDetail.class);
//    Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(HttpStatus.CONFLICT.value(), bodyResponse.getStatus());
//    Assertions.assertEquals(
//        "https://problems-registry.smartbear.com/already-exists",
//        bodyResponse.getType().toString());
//    Assertions.assertEquals("Already exists", bodyResponse.getTitle());
//    Assertions.assertEquals(
//        String.format(ALREADY_EXIST_TEMPLATE, "Account", "email", accountRequestDto.getEmail()),
//        bodyResponse.getDetail());
//    Assertions.assertEquals(
//        "/accounts", Objects.requireNonNull(bodyResponse.getInstance()).toString());
//  }
//
//  @Test
//  @DisplayName("Given has account when get accounts then return accountDto with 200")
//  @Order(9)
//  void givenHasAccount_whenGetAccount_thenReturnAccountDtoWith200() {
//    ParameterizedTypeReference<Set<AccountResponseDto>> parameterizedTypeReference =
//        new ParameterizedTypeReference<>() {};
//    var responseEntity =
//        testRestTemplate.exchange("/accounts", HttpMethod.GET, null, parameterizedTypeReference);
//    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertFalse(bodyResponse.isEmpty());
//    bodyResponse.stream()
//        .findFirst()
//        .ifPresent(
//            accountResponseDto -> {
//              Assertions.assertEquals(
//                  accountResponseDtoMock.getRoleName(), accountResponseDto.getRoleName());
//              Assertions.assertEquals(
//                  accountResponseDtoMock.getEmail(), accountResponseDto.getEmail());
//              Assertions.assertEquals(
//                  accountResponseDtoMock.getUsername(), accountResponseDto.getUsername());
//              Assertions.assertNotNull(accountResponseDto.getUuid());
//              try {
//                UUID.fromString(accountResponseDto.getUuid());
//              } catch (IllegalArgumentException e) {
//                Assertions.fail("UUID is not valid");
//              }
//            });
//  }
//
//  //  @Test
//  //  @DisplayName("Given has account when get account by uuid then return accountDto with 200")
//  //  @Order(10)
//  //  void givenHasAccount_whenGetAccountByUuid_thenReturnAccountDtoWith200() {
//  //    URI uri = URI.create("/accounts/" + accountRegisterDtoMock.getUuid());
//  //    var responseEntity = testRestTemplate.getForEntity(uri, AccountResponseDto.class);
//  //    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//  //    var bodyResponse = responseEntity.getBody();
//  //    Assertions.assertNotNull(bodyResponse);
//  //    Assertions.assertEquals(accountResponseDtoMock.getRoleName(), bodyResponse.getRoleName());
//  //    Assertions.assertEquals(accountResponseDtoMock.getEmail(), bodyResponse.getEmail());
//  //    Assertions.assertEquals(accountResponseDtoMock.getUsername(), bodyResponse.getUsername());
//  //    Assertions.assertEquals(accountResponseDtoMock.getUuid(), bodyResponse.getUuid());
//  //  }
//
//  @Test
//  @DisplayName("Given has account when get account by uuid then return accountDto with 404")
//  @Order(11)
//  void givenHasAccount_whenGetAccountByUuid_thenReturnAccountDtoWith404() {
//    URI uri = URI.create("/accounts/" + UUID.randomUUID());
//    var responseEntity = testRestTemplate.getForEntity(uri, ProblemDetail.class);
//    Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), bodyResponse.getStatus());
//    Assertions.assertEquals(
//        "https://problems-registry.smartbear.com/not-found", bodyResponse.getType().toString());
//    Assertions.assertEquals("Not found", bodyResponse.getTitle());
//    Assertions.assertEquals(
//        "Account with accountUuid: "
//            + uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1)
//            + " not found",
//        bodyResponse.getDetail());
//    Assertions.assertEquals(
//        uri.getPath(), Objects.requireNonNull(bodyResponse.getInstance()).toString());
//  }
//
//  @Test
//  @DisplayName("Given role when get roles then return roleDto with 200")
//  @Order(12)
//  void givenRole_whenGetRoles_thenReturnRoleDtoWith200() {
//    ParameterizedTypeReference<Set<RoleDto>> parameterizedTypeReference =
//        new ParameterizedTypeReference<>() {};
//    var responseEntity =
//        testRestTemplate.exchange("/roles", HttpMethod.GET, null, parameterizedTypeReference);
//    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertFalse(bodyResponse.isEmpty());
//    bodyResponse.stream()
//        .findFirst()
//        .ifPresent(
//            roleDto -> {
//              Assertions.assertEquals(
//                  this.roleDto.getRoleName().toUpperCase(), roleDto.getRoleName());
//              Assertions.assertEquals(
//                  this.roleDto.getRoleDescription(), roleDto.getRoleDescription());
//            });
//  }
//
//  @Test
//  @DisplayName("Given roleuuid not exist when get role by uuid then return problemDetail with
// 404")
//  @Order(13)
//  @Disabled("This logic is change in the code")
//  void givenUUidNotExist_whenGetRoleByUuid_thenReturnProblemDetailWith404() {
//    URI uri = URI.create("/roles/" + UUID.randomUUID());
//    var responseEntity = testRestTemplate.getForEntity(uri, ProblemDetail.class);
//    Assertions.assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), bodyResponse.getStatus());
//    Assertions.assertEquals(
//        "https://problems-registry.smartbear.com/not-found", bodyResponse.getType().toString());
//    Assertions.assertEquals("Not found", bodyResponse.getTitle());
//    Assertions.assertEquals(
//        "Role with uuid: "
//            + uri.getPath().substring(uri.getPath().lastIndexOf('/') + 1)
//            + " not found",
//        bodyResponse.getDetail());
//    Assertions.assertEquals(
//        uri.getPath(), Objects.requireNonNull(bodyResponse.getInstance()).toString());
//  }
//
//  @Test
//  @DisplayName("Given role exist when get roles then  with 200")
//  @Order(14)
//  void givenRoleExist_whenGetRoles_thenReturn200() {
//    ParameterizedTypeReference<Set<RoleDto>> parameterizedTypeReference =
//        new ParameterizedTypeReference<>() {};
//    var responseEntity =
//        testRestTemplate.exchange("/roles", HttpMethod.GET, null, parameterizedTypeReference);
//    Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertFalse(bodyResponse.isEmpty());
//    bodyResponse.stream()
//        .findFirst()
//        .ifPresent(
//            roleDto -> {
//              Assertions.assertEquals(
//                  this.roleDto.getRoleName().toUpperCase(), roleDto.getRoleName());
//              Assertions.assertEquals(
//                  this.roleDto.getRoleDescription(), roleDto.getRoleDescription());
//            });
//  }
//
//  @Test
//  @DisplayName("Given role exist when delete role then return 422 because of role is in use")
//  @Order(15)
//  void givenRoleExist_whenDeleteRole_thenReturn422() {
//    Role role =
//        roleRepository.findRoleByRoleName(this.roleDto.getRoleName().toUpperCase()).orElse(null);
//    assert role != null;
//    URI uri = URI.create("/roles/" + role.getUuid());
//    var responseEntity =
//        testRestTemplate.exchange(uri, HttpMethod.DELETE, null, ProblemDetail.class);
//    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
//    var bodyResponse = responseEntity.getBody();
//    Assertions.assertNotNull(bodyResponse);
//    Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), bodyResponse.getStatus());
//    Assertions.assertEquals(
//        "https://problems-registry.smartbear.com/business-rule-violation",
//        bodyResponse.getType().toString());
//    Assertions.assertEquals("Business Rule Violation", bodyResponse.getTitle());
//    Assertions.assertNotNull(bodyResponse.getDetail());
//    Assertions.assertTrue(bodyResponse.getDetail().contains("is still referenced"));
//    Assertions.assertEquals(
//        uri.getPath(), Objects.requireNonNull(bodyResponse.getInstance()).toString());
//  }
//
//  @Test
//  @DisplayName("Given role exist when delete role then return 204")
//  @Order(16)
//  void givenRoleExist_whenDeleteRole_thenReturn204() {
//    userRepository.deleteAll();
//    Role role =
//        roleRepository.findRoleByRoleName(this.roleDto.getRoleName().toUpperCase()).orElse(null);
//    assert role != null;
//    URI uri = URI.create("/roles/" + role.getUuid());
//    var responseEntity = testRestTemplate.exchange(uri, HttpMethod.DELETE, null, Void.class);
//    Assertions.assertEquals(HttpStatus.ACCEPTED, responseEntity.getStatusCode());
//  }
// }
