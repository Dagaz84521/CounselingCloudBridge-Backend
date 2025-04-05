package com.ecnu.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleOfDayVO implements Serializable {
    private List<ScheduleCounselorVO> counselorList;

    private List<ScheduleSupervisor> supervisorList;
}
