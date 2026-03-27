package org.student.studentcounilservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.student.constants.KafkaConstants;
import org.student.studentcounilservice.service.StudentCouncilService;
import org.student.studentmanagementdto.StudentElectionDTO;

import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaTopicConsumer {

    @Autowired
    private StudentCouncilService studentCouncilService;

    @Autowired
    private ObjectMapper objectMapper;


    @KafkaListener(topics = KafkaConstants.ELECTION_SIGNAL, groupId = "council-group")
    public void consumeElectionSignal(Map<String, Object> electionSignal) {

        Boolean electionFlag = (Boolean) electionSignal.get("electionFlag");

        StudentElectionDTO dto = objectMapper.convertValue(electionSignal.get("studentList"), StudentElectionDTO.class);
        if (Boolean.TRUE.equals(electionFlag)) {
            studentCouncilService.initiateElection(dto);
        }
        else
            studentCouncilService.stopElection();


    }

}
