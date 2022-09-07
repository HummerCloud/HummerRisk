package com.hummerrisk.base.domain;

import java.io.Serializable;

public class CloudNativeConfigResult implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.config_id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String configId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.result_status
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String resultStatus;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.create_time
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private Long createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.update_time
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private Long updateTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.apply_user
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String applyUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.user_name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String userName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.return_sum
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private Long returnSum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.rule_id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String ruleId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.rule_name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String ruleName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.rule_desc
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String ruleDesc;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.severity
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String severity;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column cloud_native_config_result.result_json
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private String resultJson;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table cloud_native_config_result
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.id
     *
     * @return the value of cloud_native_config_result.id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.id
     *
     * @param id the value for cloud_native_config_result.id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.config_id
     *
     * @return the value of cloud_native_config_result.config_id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getConfigId() {
        return configId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.config_id
     *
     * @param configId the value for cloud_native_config_result.config_id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setConfigId(String configId) {
        this.configId = configId == null ? null : configId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.name
     *
     * @return the value of cloud_native_config_result.name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.name
     *
     * @param name the value for cloud_native_config_result.name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.result_status
     *
     * @return the value of cloud_native_config_result.result_status
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getResultStatus() {
        return resultStatus;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.result_status
     *
     * @param resultStatus the value for cloud_native_config_result.result_status
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus == null ? null : resultStatus.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.create_time
     *
     * @return the value of cloud_native_config_result.create_time
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public Long getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.create_time
     *
     * @param createTime the value for cloud_native_config_result.create_time
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.update_time
     *
     * @return the value of cloud_native_config_result.update_time
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public Long getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.update_time
     *
     * @param updateTime the value for cloud_native_config_result.update_time
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.apply_user
     *
     * @return the value of cloud_native_config_result.apply_user
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getApplyUser() {
        return applyUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.apply_user
     *
     * @param applyUser the value for cloud_native_config_result.apply_user
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser == null ? null : applyUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.user_name
     *
     * @return the value of cloud_native_config_result.user_name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.user_name
     *
     * @param userName the value for cloud_native_config_result.user_name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.return_sum
     *
     * @return the value of cloud_native_config_result.return_sum
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public Long getReturnSum() {
        return returnSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.return_sum
     *
     * @param returnSum the value for cloud_native_config_result.return_sum
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setReturnSum(Long returnSum) {
        this.returnSum = returnSum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.rule_id
     *
     * @return the value of cloud_native_config_result.rule_id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getRuleId() {
        return ruleId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.rule_id
     *
     * @param ruleId the value for cloud_native_config_result.rule_id
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setRuleId(String ruleId) {
        this.ruleId = ruleId == null ? null : ruleId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.rule_name
     *
     * @return the value of cloud_native_config_result.rule_name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getRuleName() {
        return ruleName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.rule_name
     *
     * @param ruleName the value for cloud_native_config_result.rule_name
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName == null ? null : ruleName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.rule_desc
     *
     * @return the value of cloud_native_config_result.rule_desc
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getRuleDesc() {
        return ruleDesc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.rule_desc
     *
     * @param ruleDesc the value for cloud_native_config_result.rule_desc
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setRuleDesc(String ruleDesc) {
        this.ruleDesc = ruleDesc == null ? null : ruleDesc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.severity
     *
     * @return the value of cloud_native_config_result.severity
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getSeverity() {
        return severity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.severity
     *
     * @param severity the value for cloud_native_config_result.severity
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setSeverity(String severity) {
        this.severity = severity == null ? null : severity.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column cloud_native_config_result.result_json
     *
     * @return the value of cloud_native_config_result.result_json
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public String getResultJson() {
        return resultJson;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column cloud_native_config_result.result_json
     *
     * @param resultJson the value for cloud_native_config_result.result_json
     *
     * @mbg.generated Thu Sep 08 05:49:32 CST 2022
     */
    public void setResultJson(String resultJson) {
        this.resultJson = resultJson == null ? null : resultJson.trim();
    }
}