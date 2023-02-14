package com.hummer.common.core.domain;

import java.util.ArrayList;
import java.util.List;

public class FileSystemResultItemExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public FileSystemResultItemExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andResultIdIsNull() {
            addCriterion("result_id is null");
            return (Criteria) this;
        }

        public Criteria andResultIdIsNotNull() {
            addCriterion("result_id is not null");
            return (Criteria) this;
        }

        public Criteria andResultIdEqualTo(String value) {
            addCriterion("result_id =", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdNotEqualTo(String value) {
            addCriterion("result_id <>", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdGreaterThan(String value) {
            addCriterion("result_id >", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdGreaterThanOrEqualTo(String value) {
            addCriterion("result_id >=", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdLessThan(String value) {
            addCriterion("result_id <", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdLessThanOrEqualTo(String value) {
            addCriterion("result_id <=", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdLike(String value) {
            addCriterion("result_id like", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdNotLike(String value) {
            addCriterion("result_id not like", value, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdIn(List<String> values) {
            addCriterion("result_id in", values, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdNotIn(List<String> values) {
            addCriterion("result_id not in", values, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdBetween(String value1, String value2) {
            addCriterion("result_id between", value1, value2, "resultId");
            return (Criteria) this;
        }

        public Criteria andResultIdNotBetween(String value1, String value2) {
            addCriterion("result_id not between", value1, value2, "resultId");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Long value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Long value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Long value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Long value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Long value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Long> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Long> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Long value1, Long value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Long value1, Long value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdIsNull() {
            addCriterion("vulnerability_id is null");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdIsNotNull() {
            addCriterion("vulnerability_id is not null");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdEqualTo(String value) {
            addCriterion("vulnerability_id =", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdNotEqualTo(String value) {
            addCriterion("vulnerability_id <>", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdGreaterThan(String value) {
            addCriterion("vulnerability_id >", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdGreaterThanOrEqualTo(String value) {
            addCriterion("vulnerability_id >=", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdLessThan(String value) {
            addCriterion("vulnerability_id <", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdLessThanOrEqualTo(String value) {
            addCriterion("vulnerability_id <=", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdLike(String value) {
            addCriterion("vulnerability_id like", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdNotLike(String value) {
            addCriterion("vulnerability_id not like", value, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdIn(List<String> values) {
            addCriterion("vulnerability_id in", values, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdNotIn(List<String> values) {
            addCriterion("vulnerability_id not in", values, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdBetween(String value1, String value2) {
            addCriterion("vulnerability_id between", value1, value2, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andVulnerabilityIdNotBetween(String value1, String value2) {
            addCriterion("vulnerability_id not between", value1, value2, "vulnerabilityId");
            return (Criteria) this;
        }

        public Criteria andPkgNameIsNull() {
            addCriterion("pkg_name is null");
            return (Criteria) this;
        }

        public Criteria andPkgNameIsNotNull() {
            addCriterion("pkg_name is not null");
            return (Criteria) this;
        }

        public Criteria andPkgNameEqualTo(String value) {
            addCriterion("pkg_name =", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameNotEqualTo(String value) {
            addCriterion("pkg_name <>", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameGreaterThan(String value) {
            addCriterion("pkg_name >", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameGreaterThanOrEqualTo(String value) {
            addCriterion("pkg_name >=", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameLessThan(String value) {
            addCriterion("pkg_name <", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameLessThanOrEqualTo(String value) {
            addCriterion("pkg_name <=", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameLike(String value) {
            addCriterion("pkg_name like", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameNotLike(String value) {
            addCriterion("pkg_name not like", value, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameIn(List<String> values) {
            addCriterion("pkg_name in", values, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameNotIn(List<String> values) {
            addCriterion("pkg_name not in", values, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameBetween(String value1, String value2) {
            addCriterion("pkg_name between", value1, value2, "pkgName");
            return (Criteria) this;
        }

        public Criteria andPkgNameNotBetween(String value1, String value2) {
            addCriterion("pkg_name not between", value1, value2, "pkgName");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionIsNull() {
            addCriterion("installed_version is null");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionIsNotNull() {
            addCriterion("installed_version is not null");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionEqualTo(String value) {
            addCriterion("installed_version =", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionNotEqualTo(String value) {
            addCriterion("installed_version <>", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionGreaterThan(String value) {
            addCriterion("installed_version >", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionGreaterThanOrEqualTo(String value) {
            addCriterion("installed_version >=", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionLessThan(String value) {
            addCriterion("installed_version <", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionLessThanOrEqualTo(String value) {
            addCriterion("installed_version <=", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionLike(String value) {
            addCriterion("installed_version like", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionNotLike(String value) {
            addCriterion("installed_version not like", value, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionIn(List<String> values) {
            addCriterion("installed_version in", values, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionNotIn(List<String> values) {
            addCriterion("installed_version not in", values, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionBetween(String value1, String value2) {
            addCriterion("installed_version between", value1, value2, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andInstalledVersionNotBetween(String value1, String value2) {
            addCriterion("installed_version not between", value1, value2, "installedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionIsNull() {
            addCriterion("fixed_version is null");
            return (Criteria) this;
        }

        public Criteria andFixedVersionIsNotNull() {
            addCriterion("fixed_version is not null");
            return (Criteria) this;
        }

        public Criteria andFixedVersionEqualTo(String value) {
            addCriterion("fixed_version =", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionNotEqualTo(String value) {
            addCriterion("fixed_version <>", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionGreaterThan(String value) {
            addCriterion("fixed_version >", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionGreaterThanOrEqualTo(String value) {
            addCriterion("fixed_version >=", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionLessThan(String value) {
            addCriterion("fixed_version <", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionLessThanOrEqualTo(String value) {
            addCriterion("fixed_version <=", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionLike(String value) {
            addCriterion("fixed_version like", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionNotLike(String value) {
            addCriterion("fixed_version not like", value, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionIn(List<String> values) {
            addCriterion("fixed_version in", values, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionNotIn(List<String> values) {
            addCriterion("fixed_version not in", values, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionBetween(String value1, String value2) {
            addCriterion("fixed_version between", value1, value2, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andFixedVersionNotBetween(String value1, String value2) {
            addCriterion("fixed_version not between", value1, value2, "fixedVersion");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceIsNull() {
            addCriterion("severity_source is null");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceIsNotNull() {
            addCriterion("severity_source is not null");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceEqualTo(String value) {
            addCriterion("severity_source =", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceNotEqualTo(String value) {
            addCriterion("severity_source <>", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceGreaterThan(String value) {
            addCriterion("severity_source >", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceGreaterThanOrEqualTo(String value) {
            addCriterion("severity_source >=", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceLessThan(String value) {
            addCriterion("severity_source <", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceLessThanOrEqualTo(String value) {
            addCriterion("severity_source <=", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceLike(String value) {
            addCriterion("severity_source like", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceNotLike(String value) {
            addCriterion("severity_source not like", value, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceIn(List<String> values) {
            addCriterion("severity_source in", values, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceNotIn(List<String> values) {
            addCriterion("severity_source not in", values, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceBetween(String value1, String value2) {
            addCriterion("severity_source between", value1, value2, "severitySource");
            return (Criteria) this;
        }

        public Criteria andSeveritySourceNotBetween(String value1, String value2) {
            addCriterion("severity_source not between", value1, value2, "severitySource");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlIsNull() {
            addCriterion("primary_url is null");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlIsNotNull() {
            addCriterion("primary_url is not null");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlEqualTo(String value) {
            addCriterion("primary_url =", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlNotEqualTo(String value) {
            addCriterion("primary_url <>", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlGreaterThan(String value) {
            addCriterion("primary_url >", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlGreaterThanOrEqualTo(String value) {
            addCriterion("primary_url >=", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlLessThan(String value) {
            addCriterion("primary_url <", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlLessThanOrEqualTo(String value) {
            addCriterion("primary_url <=", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlLike(String value) {
            addCriterion("primary_url like", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlNotLike(String value) {
            addCriterion("primary_url not like", value, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlIn(List<String> values) {
            addCriterion("primary_url in", values, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlNotIn(List<String> values) {
            addCriterion("primary_url not in", values, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlBetween(String value1, String value2) {
            addCriterion("primary_url between", value1, value2, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andPrimaryUrlNotBetween(String value1, String value2) {
            addCriterion("primary_url not between", value1, value2, "primaryUrl");
            return (Criteria) this;
        }

        public Criteria andSeverityIsNull() {
            addCriterion("severity is null");
            return (Criteria) this;
        }

        public Criteria andSeverityIsNotNull() {
            addCriterion("severity is not null");
            return (Criteria) this;
        }

        public Criteria andSeverityEqualTo(String value) {
            addCriterion("severity =", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityNotEqualTo(String value) {
            addCriterion("severity <>", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityGreaterThan(String value) {
            addCriterion("severity >", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityGreaterThanOrEqualTo(String value) {
            addCriterion("severity >=", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityLessThan(String value) {
            addCriterion("severity <", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityLessThanOrEqualTo(String value) {
            addCriterion("severity <=", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityLike(String value) {
            addCriterion("severity like", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityNotLike(String value) {
            addCriterion("severity not like", value, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityIn(List<String> values) {
            addCriterion("severity in", values, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityNotIn(List<String> values) {
            addCriterion("severity not in", values, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityBetween(String value1, String value2) {
            addCriterion("severity between", value1, value2, "severity");
            return (Criteria) this;
        }

        public Criteria andSeverityNotBetween(String value1, String value2) {
            addCriterion("severity not between", value1, value2, "severity");
            return (Criteria) this;
        }

        public Criteria andPublishedDateIsNull() {
            addCriterion("published_date is null");
            return (Criteria) this;
        }

        public Criteria andPublishedDateIsNotNull() {
            addCriterion("published_date is not null");
            return (Criteria) this;
        }

        public Criteria andPublishedDateEqualTo(String value) {
            addCriterion("published_date =", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateNotEqualTo(String value) {
            addCriterion("published_date <>", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateGreaterThan(String value) {
            addCriterion("published_date >", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateGreaterThanOrEqualTo(String value) {
            addCriterion("published_date >=", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateLessThan(String value) {
            addCriterion("published_date <", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateLessThanOrEqualTo(String value) {
            addCriterion("published_date <=", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateLike(String value) {
            addCriterion("published_date like", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateNotLike(String value) {
            addCriterion("published_date not like", value, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateIn(List<String> values) {
            addCriterion("published_date in", values, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateNotIn(List<String> values) {
            addCriterion("published_date not in", values, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateBetween(String value1, String value2) {
            addCriterion("published_date between", value1, value2, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andPublishedDateNotBetween(String value1, String value2) {
            addCriterion("published_date not between", value1, value2, "publishedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateIsNull() {
            addCriterion("last_modified_date is null");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateIsNotNull() {
            addCriterion("last_modified_date is not null");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateEqualTo(String value) {
            addCriterion("last_modified_date =", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateNotEqualTo(String value) {
            addCriterion("last_modified_date <>", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateGreaterThan(String value) {
            addCriterion("last_modified_date >", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateGreaterThanOrEqualTo(String value) {
            addCriterion("last_modified_date >=", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateLessThan(String value) {
            addCriterion("last_modified_date <", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateLessThanOrEqualTo(String value) {
            addCriterion("last_modified_date <=", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateLike(String value) {
            addCriterion("last_modified_date like", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateNotLike(String value) {
            addCriterion("last_modified_date not like", value, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateIn(List<String> values) {
            addCriterion("last_modified_date in", values, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateNotIn(List<String> values) {
            addCriterion("last_modified_date not in", values, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateBetween(String value1, String value2) {
            addCriterion("last_modified_date between", value1, value2, "lastModifiedDate");
            return (Criteria) this;
        }

        public Criteria andLastModifiedDateNotBetween(String value1, String value2) {
            addCriterion("last_modified_date not between", value1, value2, "lastModifiedDate");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table file_system_result_item
     *
     * @mbg.generated do_not_delete_during_merge Wed Oct 12 23:33:01 CST 2022
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table file_system_result_item
     *
     * @mbg.generated Wed Oct 12 23:33:01 CST 2022
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}