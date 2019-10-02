package me.siyoung.springbootrest.events;


import me.siyoung.springbootrest.common.BaseControllerTest;
import me.siyoung.springbootrest.common.TestDescription;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.stream.IntStream;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class EventControllerTests extends BaseControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 동작하는 이벤트 등록")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("spring")
                .description("rest api start")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,11,23,14,15))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,11,24,14,15))
                .beginEventDateTime(LocalDateTime.of(2019,11,23,14,15))
                .endEventDateTime(LocalDateTime.of(2019,11,30,23,59))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();
        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE,MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andDo(document("create-event",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query events"),
                                linkWithRel("update-event").description("link to update event"),
                                linkWithRel("profile").description("link to show profile")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("close time of begin of new event"),
                                fieldWithPath("beginEventDateTime").description("begin time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("end time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(
                                fieldWithPath("id").description("id of new event "),
                                fieldWithPath("name").description("Name of new event"),
                                fieldWithPath("description").description("description of new event"),
                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
                                fieldWithPath("closeEnrollmentDateTime").description("close time of begin of new event"),
                                fieldWithPath("beginEventDateTime").description("begin time of begin of new event"),
                                fieldWithPath("endEventDateTime").description("end time of end of new event"),
                                fieldWithPath("location").description("location of new event"),
                                fieldWithPath("basePrice").description("basePrice of new event"),
                                fieldWithPath("maxPrice").description("maxPrice of new event"),
                                fieldWithPath("limitOfEnrollment").description("limit of enrollment"),
                                fieldWithPath("free").description("free check of new event"),
                                fieldWithPath("offline").description("offline check of new event"),
                                fieldWithPath("eventStatus").description("event status"),
                                fieldWithPath("_links.self.href").description("link to self"),
                                fieldWithPath("_links.query-events.href").description("link to query events"),
                                fieldWithPath("_links.update-event.href").description("link to update event"),
                                fieldWithPath("_links.profile.href").description("link to see profile")
                        )

                ))
        ;
    }

    @Test
    @TestDescription("입력값이 비어있을 때 에러가 나는 테스")
    public void Bad_Request() throws Exception {
        Event event = Event.builder().build();
        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest())

        ;
    }

    @Test
    @TestDescription("입력값이 잘못되어 있는 경우 발생하는 테스트")
    public void CreateEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("spring")
                .description("rest api start")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,11,25,14,15))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,11,24,14,15))
                .beginEventDateTime(LocalDateTime.of(2019,11,23,14,15))
                .endEventDateTime(LocalDateTime.of(2019,11,22,23,59))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("content[0].objectName").exists())
                .andExpect(jsonPath("content[0].defaultMessage").exists())
                .andExpect(jsonPath("content[0].code").exists())
                .andExpect(jsonPath("_links.index").exists())

        ;
    }

    @Test
    @TestDescription("10개씩 이벤트를 조회하는 테스트")
    public void queryEvent() throws Exception{
        //given
        IntStream.range(0,30).forEach(this::generateEvent);

        //when && then
        this.mockMvc.perform(get("/api/events")
                .param("page", "1")
                .param("size","10")
                .param("sort","name,DESC")
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("page").exists())
                .andExpect(jsonPath("_embedded.eventList[0]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("query-events"))
        ;
    }
    @Test
    @TestDescription("이벤트 하나를 조회하는 테스트")
    public void getEvent() throws Exception{
        //given
        Event event = this.generateEvent(100);
        //when && then

        mockMvc.perform(get("/api/events/{id}",event.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").exists())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())
                .andDo(document("get-an-event"))
        ;
    }
    @Test
    @TestDescription("이벤트 하나를 조회할 때 실패하는 테스트404")
    public void getEvent404() throws Exception{

        mockMvc.perform(get("/api/events/1110039"))
                .andExpect(status().isNotFound());
    }
    @Test
    @TestDescription("정상적으로 동작하는 수정 API 테스트")
    public void updateEvent() throws Exception{
        //given
        Event event = generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String updated_event = "updated event";
        eventDto.setName(updated_event);
        //when && then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))

                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("update-event"))
        ;
    }
    @Test
    @TestDescription("입력값이 비어있어 실패하는 수정 API 테스트")
    public void updateEvent400Empty() throws Exception{
        //given
        Event event = generateEvent(200);
        EventDto eventDto = new EventDto();
        //when && then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }
    @Test
    @TestDescription("입력 값이 이상해서 실패하는 이벤트 수정 API 테스트")
    public void updateEvent400wrong() throws Exception{
        //given
        Event event = generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        eventDto.setBasePrice(20000);
        eventDto.setMaxPrice(10000);
        //when && then
        this.mockMvc.perform(put("/api/events/{id}", event.getId())
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))

                    .andDo(print())
                    .andExpect(status().isBadRequest());
    }
    @Test
    @TestDescription("없는 이벤트를 수정하려다 실패하는 테스트")
    public void updateEventNotExist() throws Exception{
        //given
        Event event = generateEvent(200);
        EventDto eventDto = this.modelMapper.map(event, EventDto.class);
        String updated_event = "updated event";
        eventDto.setName(updated_event);
        //when && then
        this.mockMvc.perform(put("/api/events/100234")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))

                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private Event generateEvent(int index) {
        Event event = Event.builder()
                .name("event"+index)
                .description("rest api start")
                .beginEnrollmentDateTime(LocalDateTime.of(2019,11,22,14,15))
                .closeEnrollmentDateTime(LocalDateTime.of(2019,11,23,14,15))
                .beginEventDateTime(LocalDateTime.of(2019,11,24,14,15))
                .endEventDateTime(LocalDateTime.of(2019,11,25,23,59))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역 D2")
                .build();
        return this.eventRepository.save(event);

    }


}
