package com.zhongzhou.modules.dashboard.vo;

import java.util.List;

public class DashboardVO {
    private String updateTime;
    private Overview overview;
    private Profile profile;
    private List<TrendPoint> serviceTrend;
    private List<LevelItem> levelDistribution;
    private List<AgeItem> ageDistribution;
    private List<AppointmentItem> appointments;
    private List<QuickAction> quickActions;

    public static class Overview {
        public int elderCount;
        public int bedCount;
        public String serviceTotal;
        public int employeeCount;
        public String incomeTotal;
    }

    public static class Profile {
        public String name;
        public String title;
        public String greeting;
    }

    public static class TrendPoint {
        public String date;
        public int count;
    }

    public static class LevelItem {
        public String name;
        public int value;
    }

    public static class AgeItem {
        public String range;
        public int male;
        public int female;
    }

    public static class AppointmentItem {
        public String time;
        public String type;
        public String elderName;
        public String contact;
    }

    public static class QuickAction {
        public String key;
        public String label;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Overview getOverview() {
        return overview;
    }

    public void setOverview(Overview overview) {
        this.overview = overview;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<TrendPoint> getServiceTrend() {
        return serviceTrend;
    }

    public void setServiceTrend(List<TrendPoint> serviceTrend) {
        this.serviceTrend = serviceTrend;
    }

    public List<LevelItem> getLevelDistribution() {
        return levelDistribution;
    }

    public void setLevelDistribution(List<LevelItem> levelDistribution) {
        this.levelDistribution = levelDistribution;
    }

    public List<AgeItem> getAgeDistribution() {
        return ageDistribution;
    }

    public void setAgeDistribution(List<AgeItem> ageDistribution) {
        this.ageDistribution = ageDistribution;
    }

    public List<AppointmentItem> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentItem> appointments) {
        this.appointments = appointments;
    }

    public List<QuickAction> getQuickActions() {
        return quickActions;
    }

    public void setQuickActions(List<QuickAction> quickActions) {
        this.quickActions = quickActions;
    }
}
