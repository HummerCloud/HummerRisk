package com.hummer.common.mapper.mapper;

import com.hummer.common.core.domain.CloudResource;
import com.hummer.common.core.domain.CloudResourceExample;
import com.hummer.common.core.domain.CloudResourceWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CloudResourceMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    long countByExample(CloudResourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int deleteByExample(CloudResourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int insert(CloudResourceWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int insertSelective(CloudResourceWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    List<CloudResourceWithBLOBs> selectByExampleWithBLOBs(CloudResourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    List<CloudResource> selectByExample(CloudResourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    CloudResourceWithBLOBs selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int updateByExampleSelective(@Param("record") CloudResourceWithBLOBs record, @Param("example") CloudResourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") CloudResourceWithBLOBs record, @Param("example") CloudResourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int updateByExample(@Param("record") CloudResource record, @Param("example") CloudResourceExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int updateByPrimaryKeySelective(CloudResourceWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(CloudResourceWithBLOBs record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_resource
     *
     * @mbg.generated Sat Oct 01 17:57:01 CST 2022
     */
    int updateByPrimaryKey(CloudResource record);
}