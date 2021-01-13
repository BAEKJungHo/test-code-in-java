# 애플리케이션을 테스트하는 다양한 방법

> Inflearn. 더 자바, 코드를 테스트하는 다양한 방법

## JUnit

자바 개발자가 가장 많이 사용하는 테스팅 프레임워크(자바 개발자 90% 이상이 JUnit 프레임워크를 사용함)

스프링 부트가 버전 2.2가 출시 되면서 기본 JUnit 버전을 JUnit5 로 올렸다.

- 자바 8 이상을 필요로한다.
- 대체제 
  - TestNG, Spock
- JUnit 5 구조
  - Platform : 테스트를 실행해주는 런처 제공. TestEngine API 제공
  - Jupiter : TestEngine API 구현체로 JUnit 5 제공
  - Vintage : TestEngine API 구현체로 JUnit 4 와 3을 제공
  
> https://junit.org/junit5/docs/current/user-guide/

## JUnit 5 시작하기

- 스프링 부트 2.2+ 버전 프로젝트를 만들면 기본으로 JUnit 5 의존성이 추가된다.
- 스프링 부트 프로젝트 사용하지 않는 경우에는 의존성 추가

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.5.2</version>
    <scope>test</scope>
</dependency>
```

- 기본 애노테이션
  - @Test
  - @BeforeAll / @AfterAll
  - @BeforeEach / @AfterEach
  - @Disabled
  
## JUnit 5 : 테스트 이름 표시하기

- `@DisplayNameGeneration`
  - Method 와 Class 레퍼런스를 사용해서 테스트 이름을 표기하는 방법 설정. 기본 구현체로 ReplaceUnderscores 제공

- `@DisplayName`
  - 어떤 테스트인지 테스트 이름을 보다 쉽게 표현할 수 있는 방법을 제공하는 애노테이션. @DisplayNameGeneration 보다 우선 순위가 높다.

> https://junit.org/junit5/docs/current/user-guide/#writing-tests-display-names
