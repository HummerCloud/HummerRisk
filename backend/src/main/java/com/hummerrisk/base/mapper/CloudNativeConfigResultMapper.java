package com.hummerrisk.base.mapper;

import com.hummerrisk.base.domain.CloudNativeConfigResult;
import com.hummerrisk.base.domain.CloudNativeConfigResultExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CloudNativeConfigResultMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    long countByExample(CloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int deleteByExample(CloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int insert(CloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int insertSelective(CloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    List<CloudNativeConfigResult> selectByExampleWithBLOBs(CloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    List<CloudNativeConfigResult> selectByExample(CloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    CloudNativeConfigResult selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int updateByExampleSelective(@Param("record") CloudNativeConfigResult record, @Param("example") CloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") CloudNativeConfigResult record, @Param("example") CloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int updateByExample(@Param("record") CloudNativeConfigResult record, @Param("example") CloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int updateByPrimaryKeySelective(CloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(CloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    int updateByPrimaryKey(CloudNativeConfigResult record);
}