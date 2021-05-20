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
  
> [Junit User Guide](https://junit.org/junit5/docs/current/user-guide/)
> 
> [Junit Sample Code](https://github.com/junit-team/junit5-samples)

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
    - 메서드를 주로 `static void 로 작성` private 은 안되고 default 는 가능하다. 그리고 return type 이 없어야 한다.
    - @BeforeAll : 모든 테스트 코드가 실행되기전 한 번 실행
    - @AfterAll : 모든 테스트 코드가 실행된 후 한 번 실행
  - @BeforeEach / @AfterEach
    - static 일 필요는 없고 return type 은 void
    - @BeforeEach : 각각의 테스트 실행 이전에 한 번씩 호출
    - @AfterEach : 각각의 테스트 실행 이후에 한 번씩 호출
  - @Disabled
    - 테스트를 실행하고 싶지 않은 경우에 사용
  
> Junit 5 부터는 클래스와 메서드를 public 으로 하지 않아도된다. JUnit4 는 둘 다 public 이어야 실행 가능함.

## JUnit 5 : 테스트 이름 표시하기

테스트 메서드 네이밍은 주로 카멜 케이스보다는 스네이크 케이스(snake case)를 사용한다. 테스트 후
콘솔 왼쪽 창에 테스트 메서드 명이 찍히는데 스네이크 케이스를 적용한 경우가 더 읽기 편하기 때문이다.

- `@DisplayNameGeneration`
  - Method 와 Class 레퍼런스를 사용해서 테스트 이름을 표기하는 방법 설정. 기본 구현체로 ReplaceUnderscores 제공
  - @DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) 
    - A_B_C 처럼 언더스코어(Underscores)로 작성된 것을 빈 공백으로 대체(replace)한다. 따라서 읽기가 더 편해진다.
- `@DisplayName`
  - 어떤 테스트인지 테스트 이름을 보다 쉽게 표현할 수 있는 방법을 제공하는 애노테이션. @DisplayNameGeneration 보다 우선 순위가 높다.
  - @DisplayName("스터디 만들기 \uD83D\uDE31")
    - 이모지 사용가능, 메서드명 대신 한글명으로 출력 가능
  - @DisplayNameGeneration 보다 @DisplayName 를 사용하는 것을 더 추천한다.
    - 언더스코어로 표현하기에는 가독성도 떨어지고 한계가 있다.
  
> https://junit.org/junit5/docs/current/user-guide/#writing-tests-display-names

## org.junit.jupiter.api.Assertions.*


- 실제 값이 기대한 값과 같은지 확인
  - assertEquals(expected, actual, failMessage)
  - 첫 번째 파라미터에는 기대하는 값, 두 번째 파라미터는 실제 값을 전달하면 된다.
  
```java
public static void assertEquals(Object expected, Object actual, String message) {
        AssertEquals.assertEquals(expected, actual, message);
    }
```

- failMessage 자리에는 Supplier 함수형 인터페이스가 올 수 있다.
  - failMessage 자리에 람다를 쓰면 좋은점이, 문자열 연산같은게 failMessage 에 존재하는 경우 최적화해서 문자열 연산을 실행한다.
    - 즉, 테스트가 실패한 경우에만 수행하며, 그냥 문자열로 적게되면 테스트 실패, 성공에 상관없이 무조건 실행한다.
    - 따라서 문자열 연산 비용이 많이 들거 같으면 람다를 사용하는 것이 낫다.

```java 
// assertEquals(StudyStatus.DRAFT, study.getStatus(), "test fail message");
assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "test fail" + StudyStatus.DRAFT + "message");
assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
            @Override
            public String get() {
                return "fail Message";
            }
        });        
```

- 값이 null이 아닌지 확인
  - assertNotNull(actual)
- 다음 조건이 참(true)인지 확인
  - assertTrue(boolean)
- 모든 확인 구문 확인
  - assertAll(executables...)
  - excutable functional interface 를 넘길 수 있.

```java
@FunctionalInterface
@API(
    status = Status.STABLE,
    since = "5.0"
)
public interface Executable {
    void execute() throws Throwable;
}
```

```java
assertAll(
        () -> assertNotNull(study),
        () ->  assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "test fail" + StudyStatus.DRAFT + "message"),
        () -> assertTrue(study.getLimit() > 0, "fail Message")
);
```  

- 예외 발생 확인
  - assertThrows(expectedType, executable)
  - 어떠한 코드를 실행할 때, 어떠한 타입의 예외가 발생하는지 확인할 수 있다.

```java
IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> new Study(-10));
```

- 특정 시간 안에 실행이 완료되는지 확인
  - assertTimeout(duration, executable)
  
```java
// 특정 시간안에, 코드가 수행되지 못하면 테스트에 실패한다.
// 따라서 Executable 파라미터를 넘길 때 해당 코드가 오래걸리면 그 만큼 테스트하는데 시간이 소요된다.
assertTimeout(Duration.ofSeconds(100), () -> {
    new Study(10);
    Thread.sleep(1000);
});


// 특정 시간안에, 코드가 수행되지 못하면 테스트에 실패한다.
// Executable 을 다 수행할 때까지 기다리지 않고 지정한 시간이 지나면 자동으로 테스트를 종료한다.
assertTimeoutPreemptively(Duration.ofSeconds(100), () -> {
  new Study(10);
  Thread.sleep(1000);
});

// Executable 안에 ThreadLocal 이 들어있는 경우
스프링 트랜잭션은 ThreadLocal 을 기본 전략으로 사용하는데
다른 스레드에서 공유하지 못한다. 트랜잭셔널 테스트는 롤백을 기본으로 테스트 하는데
ThreadLocal 이 있으면 롤백이 안되고 DB 에 반영이 되는 경우가 있을 수 있다.
즉, 트랜잭션 설정을 가지고 있는 스레드랑 별개의 코드로 Executable 코드를 실행하기 때문이다.
```
  
마지막 매개변수로 Supplier<String> 타입의 인스턴스를 람다 형태로 제공할 수 있다.
  
복잡한 메시지 생성해야 하는 경우 사용하면 실패한 경우에만 해당 메시지를 만들게 할 수 있다.

> AssertJ, Hemcrest, Truth 등의 라이브러리를 사용할 수도 있다.

## 조건에 따라 테스트 실행하기

특정한 조건을 만족하는 경우에 테스트를 실행하는 방법

- `org.junit.jupiter.api.Assumptions.*`
  - assumeTrue(조건)
  - assumingThat(조건, 테스트)

- `@Enabled___ 와 @Disabled___`
  - OnOS : @EnabledOnOs({OS.MAC, OS.LINUX, JRE.JAVA_11}), @DisabledOnOs
  - OnJre
  - IfSystemProperty
  - IfEnvironmentVariable
  - If
  
## JUnit 5: 태깅과 필터링

테스트가 여러개 있다면 테스트를 그룹화, 모듈화 할 수 있다.

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
void create_new_study() {}

@SlowTest
@DisplayName("스터디 만들기 slow")
void create_new_study_again() {}

```

## 테스트 반복하기

- @RepeatedTest
  - 반복 횟수와 반복 테스트 이름을 설정할 수 있다.
    - {displayName}
    - {currentRepetition}
    - {totalRepetitions}
  - RepetitionInfo 타입의 인자를 받을 수 있다.
  
```java
@DisplayName("스터디 만들기")
@RepeatedTest(vlaue = 10, name = "{displayName}, {currentRepetition}/{totalRepetitions}")
void repeatTest(RepetitionInfo repetitionInfo) {
    System.out.println("test" + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
}
```

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
  
```java
@ParameterizedTest
@ValueSource(strings = {"날씨가", "많이", "춥습니다."})
void parameterizedTest(String message) {
    System.out.println(message);    
}
```

- 인자 값 타입 변환
  - 암묵적인 타입 변환
    - [레퍼런스](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests-argument-conversion-implicit) 참고
  - 명시적인 타입 변환
    - SimpleArgumentConverter 상속 받은 구현체 제공
    - @ConvertWith

```java
@DisplayName("스터디 만들기")
@ParameterizedTest(name = "{index} {displayName} message={0}")
@ValueSource(ints = {10, 20, 40})
void parameterizedTest(@ConvertWith(StudyConverter.class) Study study) {
    System.out.println(study.getLimit());    
}

static class StudyConverter extends SimpleArgumentConverter {
    @Override 
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        assesrtEquals(Study.class, targetType, "Can only convert to Study");
        return new Study(Integer.parseInt(source.toString()));
    }
}
```

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
