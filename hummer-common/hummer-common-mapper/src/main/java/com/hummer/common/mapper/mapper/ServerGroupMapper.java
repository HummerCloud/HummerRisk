package com.hummer.common.mapper.mapper;

import com.hummer.common.core.domain.ServerGroup;
import com.hummer.common.core.domain.ServerGroupExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ServerGroupMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    long countByExample(ServerGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int deleteByExample(ServerGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int insert(ServerGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int insertSelective(ServerGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    List<ServerGroup> selectByExample(ServerGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    ServerGroup selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int updateByExampleSelective(@Param("record") ServerGroup record, @Param("example") ServerGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int updateByExample(@Param("record") ServerGroup record, @Param("example") ServerGroupExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int updateByPrimaryKeySelective(ServerGroup record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table server_group
     *
     * @mbg.generated Wed May 25 23:33:49 CST 2022
     */
    int updateByPrimaryKey(ServerGroup record);
}