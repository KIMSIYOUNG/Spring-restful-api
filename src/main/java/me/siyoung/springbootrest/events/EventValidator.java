package me.siyoung.springbootrest.events;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Component
public class EventValidator{

    public void validate(EventDto eventDto, Errors errors) {
        if(eventDto.getBasePrice()>eventDto.getMaxPrice()&&eventDto.getMaxPrice()!=0){
            errors.rejectValue("basePrice","WrongValue","BasePrice is weird");
            errors.rejectValue("maxPrice","WrongValue","maxPrice is weird");
        }

        LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
        if(endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())||
            endEventDateTime.isBefore(eventDto.getBeginEventDateTime())||
            endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())){
            errors.rejectValue("endEventDateTime","wrongValue","endEventDateTime is weird");
        }

        //TODO BeginEventDateTime && CloseEnrollmentDateTime


    }
}
