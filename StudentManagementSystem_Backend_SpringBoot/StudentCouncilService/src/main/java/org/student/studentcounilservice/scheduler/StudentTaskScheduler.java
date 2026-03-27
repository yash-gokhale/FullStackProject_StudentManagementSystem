package org.student.studentcounilservice.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.student.studentcounilservice.service.RedisService;
import org.student.studentcounilservice.service.StudentCouncilService;
import org.student.studentmanagementdto.StudentElectionDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentTaskScheduler {



    @Autowired
    private RedisService redisService;

    @Autowired
    private StudentCouncilService studentCouncilService;

    @Scheduled(fixedRate = 5000)
    public void scheduledTask() {
        if(redisService.get("electionFlag") == null  && redisService.getSize() > 0 ){
            studentCouncilService.stopElection();
        }
        else if(redisService.get("electionFlag") != null && (Boolean) redisService.get("electionFlag") == true && redisService.getKeyRemainingTime("electionFlag") <= 120) {
            StudentElectionDTO electionData = new StudentElectionDTO();
            electionData.setStudentIdList((ArrayList<String>) redisService.get("election_data"));
            studentCouncilService.countVotes(electionData);
            studentCouncilService.stopElection();
        }
    }
}
