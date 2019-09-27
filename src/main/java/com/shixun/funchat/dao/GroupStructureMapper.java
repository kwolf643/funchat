package com.shixun.funchat.dao;

import com.shixun.funchat.entity.GroupStructure;
import com.shixun.funchat.entity.GroupStructureKey;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupStructureMapper {
    int deleteSelective(GroupStructure record);

    int deleteByPrimaryKey(GroupStructureKey key);

    int insert(GroupStructure record);

    int insertSelective(GroupStructure record);

    GroupStructure selectByPrimaryKey(GroupStructureKey key);

    int updateByPrimaryKeySelective(GroupStructure record);

    int updateByPrimaryKey(GroupStructure record);
}