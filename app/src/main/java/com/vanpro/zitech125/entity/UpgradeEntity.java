package com.vanpro.zitech125.entity;

/**
 * Created by Jinsen on 16/9/20.
 */
public class UpgradeEntity {

//    "appName": "轻烟",
//            "packageName": "com.lingzhong.qingyan",
//            "versionCode": 1,
//            "versionName": "1.0.0",
//            "channel": "huawei",
//            "downloadUrl": "http://7xlkpr.com1.z0.glb.clouddn.com/app/qingyan/2016-09-07/app-huawei-debug-1.0.0.apk",
//            "filesize": 4152911,
//            "fileMd5": "836ffa7e89036ee053ba2be624bd537c",
//            "releaseNotes": "初版",
//            "releaseDate": "2016-09-07"

    private String appName;
    private String packageName;
    private int versionCode;
    private String versionName;
    private String channel;
    private String downloadUrl;
    private int filesize;
    private String fileMd5;
    private String releaseNotes;
    private String releaseDate;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getFileMd5() {
        return fileMd5;
    }

    public void setFileMd5(String fileMd5) {
        this.fileMd5 = fileMd5;
    }

    public int getFilesize() {
        return filesize;
    }

    public void setFilesize(int filesize) {
        this.filesize = filesize;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
