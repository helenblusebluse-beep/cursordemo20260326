package com.zhongzhou.modules.visit.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.appointment.service.AppointmentService;
import com.zhongzhou.modules.appointment.vo.PendingAppointmentVO;
import com.zhongzhou.modules.visit.dto.VisitCheckinRequest;
import com.zhongzhou.modules.visit.dto.VisitCreateRequest;
import com.zhongzhou.modules.visit.dto.VisitCheckoutRequest;
import com.zhongzhou.modules.visit.entity.VisitRecord;
import com.zhongzhou.modules.visit.mapper.VisitRecordMapper;
import com.zhongzhou.modules.visit.vo.VisitRecordVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class VisitService {
    public static final int STATUS_IN = 1;
    public static final int STATUS_OUT = 2;

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final VisitRecordMapper visitRecordMapper;
    private final AppointmentService appointmentService;

    public VisitService(VisitRecordMapper visitRecordMapper, AppointmentService appointmentService) {
        this.visitRecordMapper = visitRecordMapper;
        this.appointmentService = appointmentService;
    }

    public PageResult<VisitRecordVO> page(String visitorName, String visitorPhone, LocalDateTime startTime, LocalDateTime endTime, Integer reserveType, int page, int pageSize) {
        LambdaQueryWrapper<VisitRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VisitRecord::getIsDeleted, 0);
        if (StringUtils.hasText(visitorName)) {
            wrapper.like(VisitRecord::getVisitorName, visitorName.trim());
        }
        if (StringUtils.hasText(visitorPhone)) {
            wrapper.eq(VisitRecord::getVisitorPhone, visitorPhone.trim());
        }
        if (startTime != null) {
            wrapper.ge(VisitRecord::getCheckinTime, startTime);
        }
        if (endTime != null) {
            wrapper.le(VisitRecord::getCheckinTime, endTime);
        }
        if (reserveType != null) {
            wrapper.eq(VisitRecord::getReserveType, reserveType);
        }
        wrapper.orderByDesc(VisitRecord::getId);

        Page<VisitRecord> p = visitRecordMapper.selectPage(new Page<>(page, pageSize), wrapper);
        List<VisitRecordVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public List<PendingAppointmentVO> pendingAppointments() {
        return appointmentService.pendingList();
    }

    public void create(VisitCreateRequest req) {
        VisitRecord record = new VisitRecord();
        record.setAppointmentId(0L);
        record.setReserveType(req.getReserveType());
        record.setVisitorName(req.getVisitorName().trim());
        record.setVisitorPhone(req.getVisitorPhone().trim());
        record.setElderName(req.getElderName().trim());
        record.setCheckinTime(req.getCheckinTime());
        record.setStatus(STATUS_IN);
        record.setIsDeleted(0);
        record.setCreatedTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());
        visitRecordMapper.insert(record);
    }

    public void checkin(VisitCheckinRequest req) {
        List<PendingAppointmentVO> list = appointmentService.pendingList().stream()
                .filter(it -> it.getId().equals(req.getAppointmentId()))
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            throw new IllegalArgumentException("预约记录不存在或不是待上门状态");
        }
        PendingAppointmentVO appt = list.get(0);

        VisitRecord record = new VisitRecord();
        record.setAppointmentId(appt.getId());
        record.setReserveType(appt.getReserveType());
        record.setVisitorName(appt.getVisitorName());
        record.setVisitorPhone(appt.getVisitorPhone());
        record.setElderName(appt.getElderName());
        record.setCheckinTime(req.getCheckinTime());
        record.setStatus(STATUS_IN);
        record.setIsDeleted(0);
        record.setCreatedTime(LocalDateTime.now());
        record.setUpdatedTime(LocalDateTime.now());
        visitRecordMapper.insert(record);

        // 联动预约状态：待上门 -> 已完成（完成闭环）
        appointmentService.confirmFromVisit(appt.getId(), req.getCheckinTime());
    }

    public void checkout(Long id, VisitCheckoutRequest req) {
        VisitRecord record = getExist(id);
        if (record.getStatus() == null || record.getStatus() != STATUS_IN) {
            throw new IllegalArgumentException("仅在访状态可办理离院");
        }
        if (record.getCheckinTime() != null && req.getCheckoutTime().isBefore(record.getCheckinTime())) {
            throw new IllegalArgumentException("离院时间不能早于来访时间");
        }
        LambdaUpdateWrapper<VisitRecord> update = new LambdaUpdateWrapper<>();
        update.eq(VisitRecord::getId, id)
                .set(VisitRecord::getCheckoutTime, req.getCheckoutTime())
                .set(VisitRecord::getStatus, STATUS_OUT)
                .set(VisitRecord::getUpdatedTime, LocalDateTime.now());
        visitRecordMapper.update(null, update);
    }

    public void delete(Long id) {
        getExist(id);
        LambdaUpdateWrapper<VisitRecord> update = new LambdaUpdateWrapper<>();
        update.eq(VisitRecord::getId, id)
                .set(VisitRecord::getIsDeleted, 1)
                .set(VisitRecord::getUpdatedTime, LocalDateTime.now());
        visitRecordMapper.update(null, update);
    }

    private VisitRecord getExist(Long id) {
        VisitRecord record = visitRecordMapper.selectOne(new LambdaQueryWrapper<VisitRecord>()
                .eq(VisitRecord::getId, id)
                .eq(VisitRecord::getIsDeleted, 0)
                .last("limit 1"));
        if (record == null) {
            throw new IllegalArgumentException("来访记录不存在");
        }
        return record;
    }

    private VisitRecordVO toVO(VisitRecord e) {
        VisitRecordVO vo = new VisitRecordVO();
        vo.setId(e.getId());
        vo.setAppointmentId(e.getAppointmentId());
        vo.setReserveType(e.getReserveType());
        vo.setReserveTypeLabel(resolveReserveTypeLabel(e.getReserveType()));
        vo.setVisitorName(e.getVisitorName());
        vo.setVisitorPhone(e.getVisitorPhone());
        vo.setElderName(e.getElderName());
        vo.setCheckinTime(e.getCheckinTime() == null ? null : e.getCheckinTime().format(DT));
        vo.setCreatedTime(e.getCreatedTime() == null ? null : e.getCreatedTime().format(DT));
        vo.setCheckoutTime(e.getCheckoutTime() == null ? null : e.getCheckoutTime().format(DT));
        vo.setStatus(e.getStatus());
        vo.setStatusLabel(e.getStatus() != null && e.getStatus() == STATUS_IN ? "在访" : "已离院");
        return vo;
    }

    private String resolveReserveTypeLabel(Integer reserveType) {
        if (reserveType == null) {
            return "-";
        }
        switch (reserveType) {
            case 1:
                return "参观来访";
            case 2:
                return "探访来访";
            default:
                return "其他";
        }
    }
}
