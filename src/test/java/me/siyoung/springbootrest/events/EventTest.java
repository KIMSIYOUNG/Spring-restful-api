package me.siyoung.springbootrest.events;

import me.siyoung.springbootrest.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    @TestDescription("빌더사용이 가능한지 테스트")
    public void builder(){
        Event event = Event.builder()
                .name("siyoung spring rest api")
                .description("rest api development with spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    @TestDescription("자바빈 규약을 따르는지 테스트")
    public void javaBean(){
        String name ="event";
        String description = "test event";

        Event event = new Event();
        event.setName("event");
        event.setDescription("test event");

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    @TestDescription("비지니스 로직에 따라 무료/유료를 구분하는지 테스트")
    public void free(){
        //given
        Event event = Event.builder()
                .basePrice(0)
                .maxPrice(0)
                .build();
        //when
        event.updated();
        //then
        assertThat(event.isFree()).isTrue();

        //given
        event = Event.builder()
                .basePrice(100)
                .maxPrice(0)
                .build();
        //when
        event.updated();
        //then
        assertThat(event.isFree()).isFalse();

        //given
        event = Event.builder()
                .basePrice(0)
                .maxPrice(100)
                .build();
        //when
        event.updated();
        //then
        assertThat(event.isFree()).isFalse();
    }
    @Test
    @TestDescription("위치 정보에 따라 오프라인과 온라인 유뮤의 구분")
    public void location(){
        //given
        Event event = Event.builder()
                .location("강남역 5번출구")
                .build();
        //when
        event.updated();
        //then
        assertThat(event.isOffline()).isTrue();

        //given
        event = Event.builder()
                .build();
        //when
        event.updated();
        //then
        assertThat(event.isOffline()).isFalse();

    }

}