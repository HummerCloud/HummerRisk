package com.hummer.system.mapper;

import com.hummer.common.core.domain.HistoryCloudNativeConfigResult;
import com.hummer.common.core.domain.HistoryCloudNativeConfigResultExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryCloudNativeConfigResultMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    long countByExample(HistoryCloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int deleteByExample(HistoryCloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int insert(HistoryCloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int insertSelective(HistoryCloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    List<HistoryCloudNativeConfigResult> selectByExampleWithBLOBs(HistoryCloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    List<HistoryCloudNativeConfigResult> selectByExample(HistoryCloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    HistoryCloudNativeConfigResult selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int updateByExampleSelective(@Param("record") HistoryCloudNativeConfigResult record, @Param("example") HistoryCloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") HistoryCloudNativeConfigResult record, @Param("example") HistoryCloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int updateByExample(@Param("record") HistoryCloudNativeConfigResult record, @Param("example") HistoryCloudNativeConfigResultExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int updateByPrimaryKeySelective(HistoryCloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(HistoryCloudNativeConfigResult record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_cloud_native_config_result
     *
     * @mbg.generated Tue Oct 11 07:02:04 CST 2022
     */
    int updateByPrimaryKey(HistoryCloudNativeConfigResult record);
}