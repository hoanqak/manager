package com.manager.data;

import com.manager.dto.CheckInOutDTO;
import com.manager.dto.LeaveApplicationDTO;
import com.manager.dto.MessageDemoDTO;
import com.manager.dto.ProfileDTO;
import com.manager.model.*;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConvertDTO {

    @Autowired
    DozerBeanMapper dozerBeanMapper;

    public LeaveApplicationDTO convertToLeaveApplicationDTO(LeaveApplication leaveApplication) {
        BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(LeaveApplication.class, LeaveApplicationDTO.class).fields("user.name", "name").exclude("position");
            }
        };
        dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(beanMappingBuilder);
        LeaveApplicationDTO leaveApplicationDTO = dozerBeanMapper.map(leaveApplication, LeaveApplicationDTO.class);
        return leaveApplicationDTO;
    }

    public MessageDemoDTO convertToMessageDemoDTO(MessageDemo messageDemo) {
        BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(MessageDemo.class, MessageDemoDTO.class).fields("to.name", "to")
                        .fields("from.name", "from").fields("idReport", "idRecord")
                        .exclude("status");
            }
        };
        dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(beanMappingBuilder);
        MessageDemoDTO messageDemoDTO = dozerBeanMapper.map(messageDemo, MessageDemoDTO.class);
        if (messageDemo.getType() == 0) {
            messageDemoDTO.setType(Notifications.REQUEST_EDIT_CHECKIN);
        } else if (messageDemo.getType() == 1) {
            messageDemoDTO.setType(Notifications.REQUEST_A_DAY_OFF);
        }

        return messageDemoDTO;
    }

    public CheckInOutDTO convertToCheckInOutDTO(CheckInOut checkInOut) {

        BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(CheckInOut.class, CheckInOutDTO.class).exclude("user").fields("user.name", "name").fields("user.id", "id_user");
            }
        };
        dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(beanMappingBuilder);
        CheckInOutDTO checkInOutDTO = dozerBeanMapper.map(checkInOut, CheckInOutDTO.class);
        dozerBeanMapper.destroy();
        return checkInOutDTO;
    }

    public ProfileDTO convertToProfileDTO(User user) {
        BeanMappingBuilder beanMappingBuilder = new BeanMappingBuilder() {
            @Override
            protected void configure() {
                mapping(User.class, ProfileDTO.class).fields("picture", "avatar").fields("name", "fullName")
                        .exclude("dateOfBirth").exclude("startDate").exclude("position");
            }
        };
        dozerBeanMapper = new DozerBeanMapper();
        dozerBeanMapper.addMapping(beanMappingBuilder);
        ProfileDTO profileDTO = dozerBeanMapper.map(user, ProfileDTO.class);
        if (user.getBirthday() != null) {
            profileDTO.setDateOfBirth(user.getBirthday().getTime());
        }
        if (user.getCreatedDate() != null) {
            long startDate = user.getCreatedDate().getTime();
            profileDTO.setStartDate(startDate);
        }
        try {
            profileDTO.setPosition(Details.positions[user.getPosition()]);
        } catch (ArrayIndexOutOfBoundsException arrE) {
        }

        return profileDTO;
    }
}
