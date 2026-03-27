package org.student.studentcounilservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.student.constants.KafkaConstants;
import org.student.studentcounilservice.model.StudentCouncil;
import org.student.studentcounilservice.repository.StudentCouncilRepository;
import org.student.studentmanagementdto.StudentElectionDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class StudentCouncilService {
    @Autowired
    private RedisService redisService;

    @Autowired
    private StudentCouncilRepository studentCouncilRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void initiateElection(StudentElectionDTO studentElectionDTO) {
        Map<String,Object> electionMap = new HashMap<>();


        if(redisService.get("electionFlag")!=null && (Boolean) redisService.get("electionFlag") == true) {
            throw new RuntimeException("Election is already in progress");
        }

        redisService.deleteAll();

        for(String studentId: studentElectionDTO.getStudentIdList()){
            redisService.savePermanent(studentId, 0);
        }
        redisService.savePermanent("election_data", studentElectionDTO.getStudentIdList());
        redisService.save("electionFlag", true, Duration.ofMinutes(5));

        electionMap.put("electionFlag", true);
        electionMap.put("studentList", studentElectionDTO);

        kafkaTemplate.send(KafkaConstants.ELECTION_NOTIFY, electionMap);
    }

    public void stopElection() {
        Map<String,Object> electionMap = new HashMap<>();
        electionMap.put("electionFlag", false);
        redisService.deleteAll();
        kafkaTemplate.send(KafkaConstants.ELECTION_NOTIFY, electionMap);
    }

    public String processVotes(String studentId) {
        if(redisService.get("electionFlag")!=null && (Boolean) redisService.get("electionFlag") == true) {
            redisService.update(studentId, (Integer) redisService.get(studentId) + 1);
            kafkaTemplate.send(KafkaConstants.ELECTION_VOTE_SENT, studentId);
            return "Vote received for student ID: " + studentId;
        }
        else
            return "No active election. Vote not counted.";
    }

    public void countVotes(StudentElectionDTO studentElectionDTO ) {
        Map<String,Integer> electionVotesMap = new HashMap<>();
        for(String studentId: studentElectionDTO.getStudentIdList()){
            Integer votes = (Integer) redisService.get(studentId);
                electionVotesMap.put(studentId, votes);
        }

        Map<String,Integer> electionVotesNewMap = electionVotesMap.entrySet().stream()
            .sorted(Comparator.comparingInt(Map.Entry::getValue))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        String secretary = electionVotesNewMap.keySet().stream().limit(1).toList().get(0);
        String vicePresident = electionVotesNewMap.keySet().stream().skip(1).limit(1).toList().get(0);
        String president = electionVotesNewMap.keySet().stream().skip(2).limit(1).toList().get(0);

        StudentCouncil studentCouncil = new StudentCouncil();
        studentCouncil.setEvent_date(LocalDateTime.now());

        studentCouncil.setEvent_name("ELECTION");

        String id = studentCouncil.getEvent_name().substring(0,5) + new Random().nextInt(10000);

        studentCouncil.setEvent_id(id);
        studentCouncil.setPresident(president);
        studentCouncil.setVice_president(vicePresident);
        studentCouncil.setSecretary(secretary);

        studentCouncilRepository.save(studentCouncil);
    }
}
