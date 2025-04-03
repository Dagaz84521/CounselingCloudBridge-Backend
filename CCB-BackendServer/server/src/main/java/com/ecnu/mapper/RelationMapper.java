package com.ecnu.mapper;

import com.ecnu.vo.OnlineCounselor;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface RelationMapper {

    Page<OnlineCounselor> getOnlineCounselor(Long currentId);
}
