package com.zhongzhou.modules.dashboard.service;

import com.zhongzhou.modules.dashboard.vo.DashboardVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {
    private final StringRedisTemplate redisTemplate;

    public DashboardService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public DashboardVO getHomeDashboard() {
        try {
            redisTemplate.opsForValue().setIfAbsent("zhongzhou:dashboard:last-access", String.valueOf(System.currentTimeMillis()));
        } catch (Exception ignored) {
            // Redis 不可用时仍返回页面数据，避免影响首页展示。
        }

        DashboardVO vo = new DashboardVO();
        vo.setUpdateTime(LocalDate.now().toString());
        vo.setOverview(buildOverview());
        vo.setProfile(buildProfile());
        vo.setServiceTrend(buildTrend());
        vo.setLevelDistribution(buildLevels());
        vo.setAgeDistribution(buildAges());
        vo.setAppointments(buildAppointments());
        vo.setQuickActions(buildQuickActions());
        return vo;
    }

    private DashboardVO.Overview buildOverview() {
        DashboardVO.Overview overview = new DashboardVO.Overview();
        overview.elderCount = 485;
        overview.bedCount = 570;
        overview.serviceTotal = "7.2w";
        overview.employeeCount = 172;
        overview.incomeTotal = "75.6w";
        return overview;
    }

    private DashboardVO.Profile buildProfile() {
        DashboardVO.Profile profile = new DashboardVO.Profile();
        profile.name = "管理员";
        profile.title = "养老照护站长";
        profile.greeting = "您好，愿您今天工作顺利，照护服务平安有序。";
        return profile;
    }

    private List<DashboardVO.TrendPoint> buildTrend() {
        List<DashboardVO.TrendPoint> points = new ArrayList<>();
        points.add(newTrend("10-09", 30));
        points.add(newTrend("10-10", 28));
        points.add(newTrend("10-11", 40));
        points.add(newTrend("10-12", 31));
        points.add(newTrend("10-13", 18));
        points.add(newTrend("10-14", 12));
        points.add(newTrend("10-15", 20));
        return points;
    }

    private DashboardVO.TrendPoint newTrend(String date, int count) {
        DashboardVO.TrendPoint point = new DashboardVO.TrendPoint();
        point.date = date;
        point.count = count;
        return point;
    }

    private List<DashboardVO.LevelItem> buildLevels() {
        List<DashboardVO.LevelItem> items = new ArrayList<>();
        items.add(newLevel("特护护理等级", 137));
        items.add(newLevel("一级护理等级", 120));
        items.add(newLevel("二级护理等级", 102));
        items.add(newLevel("三级护理等级", 84));
        items.add(newLevel("四级护理等级", 42));
        return items;
    }

    private DashboardVO.LevelItem newLevel(String name, int value) {
        DashboardVO.LevelItem item = new DashboardVO.LevelItem();
        item.name = name;
        item.value = value;
        return item;
    }

    private List<DashboardVO.AgeItem> buildAges() {
        List<DashboardVO.AgeItem> items = new ArrayList<>();
        items.add(newAge("60岁以下", 25, 30));
        items.add(newAge("60-70岁", 60, 70));
        items.add(newAge("71-80岁", 40, 50));
        items.add(newAge("81-90岁", 18, 22));
        items.add(newAge("90岁以上", 25, 42));
        return items;
    }

    private DashboardVO.AgeItem newAge(String range, int male, int female) {
        DashboardVO.AgeItem item = new DashboardVO.AgeItem();
        item.range = range;
        item.male = male;
        item.female = female;
        return item;
    }

    private List<DashboardVO.AppointmentItem> buildAppointments() {
        List<DashboardVO.AppointmentItem> list = new ArrayList<>();
        list.add(newAppointment("09:00", "探访", "安权", "13666667776"));
        list.add(newAppointment("10:00", "问诊", "李玉芬", "13988889999"));
        list.add(newAppointment("14:30", "康复", "周卫国", "13811112222"));
        return list;
    }

    private DashboardVO.AppointmentItem newAppointment(String time, String type, String elderName, String contact) {
        DashboardVO.AppointmentItem item = new DashboardVO.AppointmentItem();
        item.time = time;
        item.type = type;
        item.elderName = elderName;
        item.contact = contact;
        return item;
    }

    private List<DashboardVO.QuickAction> buildQuickActions() {
        List<DashboardVO.QuickAction> list = new ArrayList<>();
        list.add(newAction("visit", "来访登记"));
        list.add(newAction("checkin", "入住申请"));
        list.add(newAction("checkout", "退住申请"));
        list.add(newAction("bed", "床位预览"));
        list.add(newAction("user", "用户管理"));
        list.add(newAction("order", "订单管理"));
        list.add(newAction("refund", "退款管理"));
        list.add(newAction("report", "报表数据"));
        return list;
    }

    private DashboardVO.QuickAction newAction(String key, String label) {
        DashboardVO.QuickAction action = new DashboardVO.QuickAction();
        action.key = key;
        action.label = label;
        return action;
    }
}
