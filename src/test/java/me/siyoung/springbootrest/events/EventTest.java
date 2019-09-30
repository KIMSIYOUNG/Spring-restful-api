package me.siyoung.springbootrest.events;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("siyoung spring rest api")
                .description("rest api development with spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        String name ="event";
        String description = "test event";

        Event event = new Event();
        event.setName("event");
        event.setDescription("test event");

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }
}