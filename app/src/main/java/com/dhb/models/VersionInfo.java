package com.dhb.models;

import com.google.gson.annotations.SerializedName;

public class VersionInfo {
    @SerializedName("version")
    private String version;
    @SerializedName("device_type")
    private String deviceType;
    @SerializedName("role_id")
    private String roleId;
    @SerializedName("role_name")
    private String roleName;
    @SerializedName("id")
    private String id;
    @SerializedName("result_code")
    private String resultCode;
    @SerializedName("app_link")
    private String appLink;
    @SerializedName("version_uploaded_at")
    private String versionUploadedAt;
    @SerializedName("release_notes")
    private String releaseNotes;
    @SerializedName("created_at")
    private long createdAt;
    @SerializedName("created_by")
    private String createdBy;
    @SerializedName("updated_at")
    private long updatedAt;
    @SerializedName("updated_by")
    private String updatedBy;
    @SerializedName("sub_version")
    private String subVersion;
    @SerializedName("record_status")
    private String recordStatus;
    @SerializedName("releaseStatus")
    private String release_status;
    @SerializedName("is_mandatory")
    private String isMandatory;
    @SerializedName("minimum_supported_version")
    private String minimumSupportedVersion;
    @SerializedName("minimum_supported_sub_version")
    private String minimumSupportedSubVersion;


    // getters and setters


    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    public String getVersionUploadedAt() {
        return versionUploadedAt;
    }

    public void setVersionUploadedAt(String versionUploadedAt) {
        this.versionUploadedAt = versionUploadedAt;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getSubVersion() {
        return subVersion;
    }

    public void setSubVersion(String subVersion) {
        this.subVersion = subVersion;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public String getReleaseStatus() {
        return release_status;
    }

    public void setReleaseStatus(String release_status) {
        this.release_status = release_status;
    }

    public String getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(String isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getMinimumSupportedVersion() {
        return minimumSupportedVersion;
    }

    public void setMinimumSupportedVersion(String minimumSupportedVersion) {
        this.minimumSupportedVersion = minimumSupportedVersion;
    }

    public String getMinimumSupportedSubVersion() {
        return minimumSupportedSubVersion;
    }

    public void setMinimumSupportedSubVersion(String minimumSupportedSubVersion) {
        this.minimumSupportedSubVersion = minimumSupportedSubVersion;
    }

}
