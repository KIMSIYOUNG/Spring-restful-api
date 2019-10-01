package me.siyoung.springbootrest.events;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import me.siyoung.springbootrest.common.TestDescription;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
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
    @Parameters
    public void free(int basePrice, int maxPrice, boolean isFree){
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //when
        event.updated();
        //then
        assertThat(event.isFree()).isEqualTo(isFree);

    }
    public Object[] parametersForFree(){
        return new Object[]{
                new Object[]{0,0,true},
                new Object[]{0,100,false},
                new Object[]{100,0,false},
                new Object[]{100,200,false}
        };
    }

    @Test
    @TestDescription("위치 정보에 따라 오프라인과 온라인 유뮤의 구분")
    @Parameters
    public void location(String location, boolean isOffline){
        //given
        Event event = Event.builder()
                .location(location)
                .build();
        //when
        event.updated();
        //then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }
    public Object[] parametersForLocation(){
        return new Object[]{
                new Object[]{"강남",true},
                new Object[]{null,false},
                new Object[]{"          ",false}
        };
    }

}