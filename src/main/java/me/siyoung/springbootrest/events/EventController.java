package me.siyoung.springbootrest.events;

import me.siyoung.springbootrest.common.ErrorsResource;
import org.apache.coyote.Response;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;


@Controller
@RequestMapping(value = "/api/events", produces = MediaTypes.HAL_JSON_UTF8_VALUE)
public class EventController {

    @Autowired
    EventRepository eventRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    EventValidator eventValidator;

    @PostMapping
    public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors) {
        if (errors.hasErrors()) {
            return badRequest(errors);
        }
        eventValidator.validate(eventDto, errors);
        if (errors.hasErrors()) {
            return badRequest(errors);
        }


        Event event = modelMapper.map(eventDto, Event.class);
        event.updated();
        Event newEvent = eventRepository.save(event);


        ControllerLinkBuilder selfLinkBuilder = linkTo(EventController.class).slash(event.getId());
        URI createUri = selfLinkBuilder.toUri();
        EventResource eventResource = new EventResource(event);
        eventResource.add(linkTo(EventController.class).withRel("query-events"));
        eventResource.add(linkTo(EventController.class).withRel("update-event"));
        eventResource.add(new Link("/docs/index.html#resources-events-create").withRel("profile"));
        return ResponseEntity.created(createUri).body(eventResource);
    }

    @GetMapping
    public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler pagedResourcesAssembler) {
        Page<Event> page = this.eventRepository.findAll(pageable);
        var pagedResources = pagedResourcesAssembler.toResource(page, e -> new EventResource((Event) e));
        pagedResources.add(new Link("/docs/index.html#resources-events-list").withRel("profile"));

        return ResponseEntity.ok(pagedResources);
    }

    @GetMapping("/{id}")
    public ResponseEntity getEvent(@PathVariable Integer id) {
        Optional<Event> optionalEvent = this.eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Event event = optionalEvent.get();
        EventResource eventResource = new EventResource(event);
        eventResource.add(new Link("/docs/index.html#resources-event-get").withRel("profile"));
        return ResponseEntity.ok(eventResource);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateEvent(@PathVariable Integer id,
                                      @RequestBody @Valid EventDto eventDto
                                        ,Errors errors){
        Optional<Event> byId = eventRepository.findById(id);
        //검증 : 실패하는 테스트
        if(byId.isEmpty())
            return ResponseEntity.notFound().build();
        if(errors.hasErrors())
            return badRequest(errors);
        eventValidator.validate(eventDto,errors);
        if(errors.hasErrors())
            return badRequest(errors);

        Event existEvent = byId.get();
        modelMapper.map(eventDto,existEvent);
        EventResource eventResource = new EventResource(existEvent);
        eventResource.add(new Link("/docs/index.html#resources-event-update").withRel("profile"));
        return ResponseEntity.ok(eventResource);

    }

    private ResponseEntity badRequest(Errors errors) {
        return ResponseEntity.badRequest().body(new ErrorsResource(errors));
    }

}
