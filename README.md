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
