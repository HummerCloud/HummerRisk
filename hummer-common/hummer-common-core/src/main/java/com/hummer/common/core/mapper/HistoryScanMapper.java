package com.hummer.common.core.mapper;

import com.hummer.common.core.domain.HistoryScan;
import com.hummer.common.core.domain.HistoryScanExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HistoryScanMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    long countByExample(HistoryScanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int deleteByExample(HistoryScanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int insert(HistoryScan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     * 插入数据时返回主键ID
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int insertSelective(HistoryScan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    List<HistoryScan> selectByExampleWithBLOBs(HistoryScanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    List<HistoryScan> selectByExample(HistoryScanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    HistoryScan selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int updateByExampleSelective(@Param("record") HistoryScan record, @Param("example") HistoryScanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int updateByExampleWithBLOBs(@Param("record") HistoryScan record, @Param("example") HistoryScanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int updateByExample(@Param("record") HistoryScan record, @Param("example") HistoryScanExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int updateByPrimaryKeySelective(HistoryScan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int updateByPrimaryKeyWithBLOBs(HistoryScan record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table history_scan
     *
     * @mbg.generated Tue Jul 19 16:41:28 CST 2022
     */
    int updateByPrimaryKey(HistoryScan record);
}