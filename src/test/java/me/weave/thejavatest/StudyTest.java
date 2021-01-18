package me.weave.thejavatest;

import org.junit.jupiter.api.*;

import java.time.Duration;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    @Test
    @DisplayName("스터디 만들기 \uD83D\uDE31")
    void create_new_study() {
        Study study = new Study(1);
        IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> new Study(-10));


        assertTimeout(Duration.ofSeconds(100), () -> {
            new Study(10);
            Thread.sleep(1000);
        });

        assertTimeoutPreemptively(Duration.ofSeconds(100), () -> {
            new Study(10);
            Thread.sleep(1000);
        });

        assertNotNull(study);
//        assertEquals(StudyStatus.DRAFT, study.getStatus(), "test fail message");
        assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "test fail" + StudyStatus.DRAFT + "message");
        assertEquals(StudyStatus.DRAFT, study.getStatus(), new Supplier<String>() {
            @Override
            public String get() {
                return "fail Message";
            }
        });
        assertTrue(study.getLimit() > 0, "fail Message");
        assertAll(
                () -> assertNotNull(study),
                () ->  assertEquals(StudyStatus.DRAFT, study.getStatus(), () -> "test fail" + StudyStatus.DRAFT + "message"),
                () -> assertTrue(study.getLimit() > 0, "fail Message")
        );
        System.out.println("create");
    }

    @Test
    void create_new_study_again() {
        System.out.println("create1");
    }

    /**
     * 모든 테스트 코드가 실행되기전 한 번 실행
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("beforeAll");
    }

    /**
     * 모든 테스트 코드가 실행된 후 한 번 실행
     */
    @AfterAll
    static void afterAll() {
        System.out.println("afterAll");
    }

    @BeforeEach
    void BeforeEach() {
        System.out.println("BeforeEach");
    }

    @AfterEach
    void AfterEach() {
        System.out.println("AfterEach");
    }



}