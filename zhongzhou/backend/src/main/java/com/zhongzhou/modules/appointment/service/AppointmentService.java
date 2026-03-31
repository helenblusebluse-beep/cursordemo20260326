package com.zhongzhou.modules.appointment.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.appointment.dto.AppointmentConfirmRequest;
import com.zhongzhou.modules.appointment.dto.AppointmentCreateRequest;
import com.zhongzhou.modules.appointment.entity.VisitAppointment;
import com.zhongzhou.modules.appointment.mapper.VisitAppointmentMapper;
import com.zhongzhou.modules.appointment.vo.AppointmentVO;
import com.zhongzhou.modules.appointment.vo.PendingAppointmentVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    public static final int STATUS_PENDING = 1;
    public static final int STATUS_COMPLETED = 2;
    public static final int STATUS_CANCELED = 3;

    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    private final VisitAppointmentMapper appointmentMapper;

    public AppointmentService(VisitAppointmentMapper appointmentMapper) {
        this.appointmentMapper = appointmentMapper;
    }

    public PageResult<AppointmentVO> page(String visitorName, String visitorPhone, Integer status, Integer reserveType, int page, int pageSize) {
        LambdaQueryWrapper<VisitAppointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VisitAppointment::getIsDeleted, 0);
        if (StringUtils.hasText(visitorName)) {
            wrapper.like(VisitAppointment::getVisitorName, visitorName.trim());
        }
        if (StringUtils.hasText(visitorPhone)) {
            wrapper.like(VisitAppointment::getVisitorPhone, visitorPhone.trim());
        }
        if (status != null) {
            wrapper.eq(VisitAppointment::getStatus, status);
        }
        if (reserveType != null) {
            wrapper.eq(VisitAppointment::getReserveType, reserveType);
        }
        wrapper.orderByDesc(VisitAppointment::getId);

        Page<VisitAppointment> p = appointmentMapper.selectPage(new Page<>(page, pageSize), wrapper);
        List<AppointmentVO> rows = p.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public void create(AppointmentCreateRequest req) {
        VisitAppointment entity = new VisitAppointment();
        entity.setReserveType(req.getReserveType());
        entity.setVisitorName(req.getVisitorName().trim());
        entity.setVisitorPhone(req.getVisitorPhone().trim());
        entity.setElderName(req.getElderName().trim());
        entity.setScheduledTime(req.getScheduledTime());
        entity.setStatus(STATUS_PENDING);
        entity.setIsDeleted(0);
        entity.setCreatedTime(LocalDateTime.now());
        entity.setUpdatedTime(LocalDateTime.now());
        appointmentMapper.insert(entity);
    }

    public List<PendingAppointmentVO> pendingList() {
        List<VisitAppointment> list = appointmentMapper.selectList(new LambdaQueryWrapper<VisitAppointment>()
                .eq(VisitAppointment::getIsDeleted, 0)
                .eq(VisitAppointment::getStatus, STATUS_PENDING)
                .orderByDesc(VisitAppointment::getId));
        return list.stream().map(this::toPendingVO).collect(Collectors.toList());
    }

    public void confirm(Long id, AppointmentConfirmRequest req) {
        VisitAppointment entity = getExist(id);
        if (!STATUS_PENDINGEquals(entity.getStatus())) {
            throw new IllegalArgumentException("仅待上门状态可确认到访");
        }
        LambdaUpdateWrapper<VisitAppointment> update = new LambdaUpdateWrapper<>();
        update.eq(VisitAppointment::getId, id)
                .set(VisitAppointment::getConfirmedTime, req.getConfirmedTime())
                .set(VisitAppointment::getStatus, STATUS_COMPLETED)
                .set(VisitAppointment::getUpdatedTime, LocalDateTime.now());
        appointmentMapper.update(null, update);
    }

    public void confirmFromVisit(Long id, LocalDateTime visitTime) {
        VisitAppointment entity = getExist(id);
        if (!STATUS_PENDINGEquals(entity.getStatus())) {
            throw new IllegalArgumentException("仅待上门状态可办理来访");
        }
        LambdaUpdateWrapper<VisitAppointment> update = new LambdaUpdateWrapper<>();
        update.eq(VisitAppointment::getId, id)
                .set(VisitAppointment::getConfirmedTime, visitTime)
                .set(VisitAppointment::getStatus, STATUS_COMPLETED)
                .set(VisitAppointment::getUpdatedTime, LocalDateTime.now());
        appointmentMapper.update(null, update);
    }

    public void cancel(Long id) {
        VisitAppointment entity = getExist(id);
        if (!STATUS_PENDINGEquals(entity.getStatus())) {
            throw new IllegalArgumentException("仅待上门状态可取消");
        }
        LambdaUpdateWrapper<VisitAppointment> update = new LambdaUpdateWrapper<>();
        update.eq(VisitAppointment::getId, id)
                .set(VisitAppointment::getStatus, STATUS_CANCELED)
                .set(VisitAppointment::getUpdatedTime, LocalDateTime.now());
        appointmentMapper.update(null, update);
    }

    public void delete(Long id) {
        getExist(id);
        LambdaUpdateWrapper<VisitAppointment> update = new LambdaUpdateWrapper<>();
        update.eq(VisitAppointment::getId, id)
                .set(VisitAppointment::getIsDeleted, 1)
                .set(VisitAppointment::getUpdatedTime, LocalDateTime.now());
        appointmentMapper.update(null, update);
    }

    private VisitAppointment getExist(Long id) {
        VisitAppointment entity = appointmentMapper.selectOne(new LambdaQueryWrapper<VisitAppointment>()
                .eq(VisitAppointment::getId, id)
                .eq(VisitAppointment::getIsDeleted, 0)
                .last("limit 1"));
        if (entity == null) {
            throw new IllegalArgumentException("预约记录不存在");
        }
        return entity;
    }

    private AppointmentVO toVO(VisitAppointment e) {
        AppointmentVO vo = new AppointmentVO();
        vo.setId(e.getId());
        vo.setReserveType(e.getReserveType());
        vo.setReserveTypeLabel(resolveReserveTypeLabel(e.getReserveType()));
        vo.setVisitorName(e.getVisitorName());
        vo.setVisitorPhone(e.getVisitorPhone());
        vo.setElderName(e.getElderName());
        vo.setScheduledTime(e.getScheduledTime() == null ? null : e.getScheduledTime().format(DT));
        vo.setCreatedTime(e.getCreatedTime() == null ? null : e.getCreatedTime().format(DT));
        vo.setStatus(e.getStatus());
        vo.setStatusLabel(resolveStatusLabel(e.getStatus()));
        return vo;
    }

    private PendingAppointmentVO toPendingVO(VisitAppointment e) {
        PendingAppointmentVO vo = new PendingAppointmentVO();
        vo.setId(e.getId());
        vo.setVisitorName(e.getVisitorName());
        vo.setVisitorPhone(e.getVisitorPhone());
        vo.setElderName(e.getElderName());
        vo.setScheduledTime(e.getScheduledTime() == null ? null : e.getScheduledTime().format(DT));
        vo.setReserveTypeLabel(resolveReserveTypeLabel(e.getReserveType()));
        vo.setReserveType(e.getReserveType());
        return vo;
    }

    private boolean STATUS_PENDINGEquals(Integer status) {
        return status != null && status == STATUS_PENDING;
    }

    private String resolveReserveTypeLabel(Integer type) {
        if (type == null) {
            return "-";
        }
        return type == 1 ? "参观预约" : "探访预约";
    }

    private String resolveStatusLabel(Integer status) {
        if (status == null) {
            return "未知";
        }
        if (status == STATUS_PENDING) {
            return "待上门";
        }
        if (status == STATUS_COMPLETED) {
            return "已完成";
        }
        if (status == STATUS_CANCELED) {
            return "已取消";
        }
        return "未知";
    }
}
