# JUnit

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

## org.junit.jupiter.api.Assertions.*


- 실제 값이 기대한 값과 같은지 확인
  - assertEqulas(expected, actual)
- 값이 null이 아닌지 확인
  - assertNotNull(actual)
- 다음 조건이 참(true)인지 확인
  - assertTrue(boolean)
- 모든 확인 구문 확인
  - assertAll(executables...)
- 예외 발생 확인
  - assertThrows(expectedType, executable)
- 특정 시간 안에 실행이 완료되는지 확인
  - assertTimeout(duration, executable)
  
마지막 매개변수로 Supplier<String> 타입의 인스턴스를 람다 형태로 제공할 수 있다.
  
복잡한 메시지 생성해야 하는 경우 사용하면 실패한 경우에만 해당 메시지를 만들게 할 수 있다.

> AssertJ, Hemcrest, Truth 등의 라이브러리를 사용할 수도 있다.

## 조건에 따라 테스트 실행하기

특정한 조건을 만족하는 경우에 테스트를 실행하는 방법

- `org.junit.jupiter.api.Assumptions.*`
  - assumeTrue(조건)
  - assumingThat(조건, 테스트)

- `@Enabled___ 와 @Disabled___`
  - OnOS
  - OnJre
  - IfSystemProperty
  - IfEnvironmentVariable
  - If
  
## JUnit 5: 태깅과 필터링

![img](/images/1.JPG)

> https://maven.apache.org/guides/introduction/introduction-to-profiles.html
>
> https://junit.org/junit5/docs/current/user-guide/#running-tests-tag-expressions

## 커스텀 태그

JUnit 5 애노테이션을 조합하여 커스텀 태그를 만들 수 있다.

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Tag("fast")
@Test
public @interface FastTest {
}
```
```java
@FastTest
@DisplayName("스터디 만들기 fast")
void create_new_study() {

@SlowTest
@DisplayName("스터디 만들기 slow")
void create_new_study_again() {

```

## 테스트 반복하기

- @RepeatedTest
  - 반복 횟수와 반복 테스트 이름을 설정할 수 있다.
    - {displayName}
    - {currentRepetition}
    - {totalRepetitions}
  - RepetitionInfo 타입의 인자를 받을 수 있다.

- @ParameterizedTest
  - 테스트에 여러 다른 매개변수를 대입해가며 반복 실행한다.
    - {displayName}
    - {index}
    - {arguments}
    - {0}, {1}, ...

- 인자 값들의 소스
  - @ValueSource
  - @NullSource, @EmptySource, @NullAndEmptySource
  - @EnumSource
  - @MethodSource
  - @CvsSource
  - @CvsFileSource
  - @ArgumentSource

- 인자 값 타입 변환
  - 암묵적인 타입 변환
    - [레퍼런스](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-argument-conversion-implicit) 참고
  - 명시적인 타입 변환
    - SimpleArgumentConverter 상속 받은 구현체 제공
    - @ConvertWith

- 인자 값 조합
  - ArgumentsAccessor
  - 커스텀 Accessor
    - ArgumentsAggregator 인터페이스 구현
    - @AggregateWith

> https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests

## 테스트 인스턴스

- JUnit 은 테스트 메소드 마다 테스트 인스턴스를 새로 만든다.
  - 이것이 기본 전략.
  - 테스트 메소드를 독립적으로 실행하여 예상치 못한 부작용을 방지하기 위함이다.
  - 이 전략을 JUnit 5에서 변경할 수 있다.

- @TestInstance(Lifecycle.PER_CLASS)
  - 테스트 클래스당 인스턴스를 하나만 만들어 사용한다.
  - 경우에 따라, 테스트 간에 공유하는 모든 상태를 @BeforeEach 또는 @AfterEach에서 초기화 할 필요가 있다.
  - @BeforeAll과 @AfterAll을 인스턴스 메소드 또는 인터페이스에 정의한 default 메소드로 정의할 수도 있다.

## 테스트 순서

실행할 테스트 메소드 특정한 순서에 의해 실행되지만 어떻게 그 순서를 정하는지는 의도적으로 분명히 하지 않는다. (테스트 인스턴스를 테스트 마다 새로 만드는 것과 같은 이유)

경우에 따라, 특정 순서대로 테스트를 실행하고 싶을 때도 있다. 그 경우에는 테스트 메소드를 원하는 순서에 따라 실행하도록 @TestInstance(Lifecycle.PER_CLASS)와 함께 @TestMethodOrder를 사용할 수 있다.

- MethodOrderer 구현체를 설정한다.
- 기본 구현체
  - Alphanumeric
  - OrderAnnoation
  - Random

## junit-platform.properties

JUnit 설정 파일로, 클래스패스 루트 (src/test/resources/)에 넣어두면 적용된다.

- 테스트 인스턴스 라이프사이클 설정
  - junit.jupiter.testinstance.lifecycle.default = per_class

- 확장팩 자동 감지 기능
  - junit.jupiter.extensions.autodetection.enabled = true

- @Disabled 무시하고 실행하기
  - junit.jupiter.conditions.deactivate = org.junit.*DisabledCondition

- 테스트 이름 표기 전략 설정
  - junit.jupiter.displayname.generator.default = \ org.junit.jupiter.api.DisplayNameGenerator$ReplaceUnderscores

## 확장 모델

JUnit 4의 확장 모델은 @RunWith(Runner), TestRule, MethodRule.
JUnit 5의 확장 모델은 단 하나, Extension.

- 확장팩 등록 방법
  - 선언적인 등록 @ExtendWith
  - 프로그래밍 등록 @RegisterExtension
  - 자동 등록 자바 ServiceLoader 이용

- 확장팩 만드는 방법
  - 테스트 실행 조건
  - 테스트 인스턴스 팩토리
  - 테스트 인스턴스 후-처리기
  - 테스트 매개변수 리졸버
  - 테스트 라이프사이클 콜백
  - 예외 처리
  - ...

> https://junit.org/junit5/docs/current/user-guide/#extensions

## JUnit 4 마이그레이션

junit-vintage-engine을 의존성으로 추가하면, JUnit 5의 junit-platform으로 JUnit 3과 4로 작성된 테스트를 실행할 수 있다.

- @Rule은 기본적으로 지원하지 않지만, junit-jupiter-migrationsupport 모듈이 제공하는 @EnableRuleMigrationSupport를 사용하면 다음 타입의 Rule을 지원한다.
  - ExternalResource
  - Verifier
  - ExpectedException
