package com.zhongzhou.modules.taskschedule.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.taskschedule.dto.TaskCancelRequest;
import com.zhongzhou.modules.taskschedule.dto.TaskExecuteRequest;
import com.zhongzhou.modules.taskschedule.dto.TaskRescheduleRequest;
import com.zhongzhou.modules.taskschedule.entity.TaskExecuteRecord;
import com.zhongzhou.modules.taskschedule.entity.TaskRefundApply;
import com.zhongzhou.modules.taskschedule.entity.TaskSchedule;
import com.zhongzhou.modules.taskschedule.mapper.TaskExecuteRecordMapper;
import com.zhongzhou.modules.taskschedule.mapper.TaskRefundApplyMapper;
import com.zhongzhou.modules.taskschedule.mapper.TaskScheduleMapper;
import com.zhongzhou.modules.taskschedule.vo.TaskScheduleDetailVO;
import com.zhongzhou.modules.taskschedule.vo.TaskScheduleItemVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TaskScheduleService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final TaskScheduleMapper taskScheduleMapper;
    private final TaskExecuteRecordMapper executeRecordMapper;
    private final TaskRefundApplyMapper refundApplyMapper;

    public TaskScheduleService(TaskScheduleMapper taskScheduleMapper,
                               TaskExecuteRecordMapper executeRecordMapper,
                               TaskRefundApplyMapper refundApplyMapper) {
        this.taskScheduleMapper = taskScheduleMapper;
        this.executeRecordMapper = executeRecordMapper;
        this.refundApplyMapper = refundApplyMapper;
    }

    public PageResult<TaskScheduleItemVO> page(String elderName, String caregiverName, String nursingItemName,
                                               String beginTime, String endTime, Integer status, int pageNo, int pageSize) {
        LocalDateTime begin = (beginTime == null || beginTime.isBlank()) ? null : parse(beginTime);
        LocalDateTime end = (endTime == null || endTime.isBlank()) ? null : parse(endTime);
        LambdaQueryWrapper<TaskSchedule> qw = new LambdaQueryWrapper<TaskSchedule>()
                .eq(TaskSchedule::getIsDeleted, 0)
                .eq(status != null, TaskSchedule::getStatus, status)
                .like(elderName != null && !elderName.isBlank(), TaskSchedule::getElderName, elderName)
                .like(caregiverName != null && !caregiverName.isBlank(), TaskSchedule::getCaregiverName, caregiverName)
                .like(nursingItemName != null && !nursingItemName.isBlank(), TaskSchedule::getNursingItemName, nursingItemName)
                .ge(begin != null, TaskSchedule::getExpectedServiceTime, begin)
                .le(end != null, TaskSchedule::getExpectedServiceTime, end)
                .orderByAsc(TaskSchedule::getExpectedServiceTime)
                .orderByAsc(TaskSchedule::getId);
        Page<TaskSchedule> page = taskScheduleMapper.selectPage(new Page<>(pageNo, pageSize), qw);
        List<TaskScheduleItemVO> records = page.getRecords().stream().map(this::toItemVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), records);
    }

    public TaskScheduleDetailVO detail(Long id) {
        TaskSchedule e = mustGet(id);
        TaskScheduleDetailVO vo = new TaskScheduleDetailVO();
        copyBase(vo, e);
        vo.setElderGender(defaultStr(e.getElderGender()));
        vo.setElderAge(e.getElderAge() == null ? 0 : e.getElderAge());
        vo.setCareLevelName(defaultStr(e.getCareLevelName()));
        vo.setCaregiverNames(defaultStr(e.getCaregiverNames()));
        vo.setElderAvatarUrl(defaultStr(e.getElderAvatarUrl()));
        vo.setOrderNo(defaultStr(e.getOrderNo()));
        vo.setCreatedBy(defaultStr(e.getCreatedBy()));
        vo.setRemark(defaultStr(e.getRemark()));
        vo.setCancelReason(defaultStr(e.getCancelReason()));
        TaskExecuteRecord latest = executeRecordMapper.selectOne(new LambdaQueryWrapper<TaskExecuteRecord>()
                .eq(TaskExecuteRecord::getTaskId, id)
                .orderByDesc(TaskExecuteRecord::getId)
                .last("limit 1"));
        if (latest != null) {
            vo.setExecuteTime(format(latest.getExecuteTime()));
            vo.setExecuteBy(defaultStr(latest.getExecuteBy()));
            vo.setExecuteImageName(defaultStr(latest.getExecuteImageName()));
            vo.setExecuteImageUrl(defaultStr(latest.getExecuteImageUrl()));
            vo.setExecuteRecord(defaultStr(latest.getExecuteRecord()));
        } else {
            vo.setExecuteTime(format(e.getExecuteTime()));
            vo.setExecuteBy(defaultStr(e.getExecuteBy()));
            vo.setExecuteImageName(defaultStr(e.getExecuteImageName()));
            vo.setExecuteImageUrl(defaultStr(e.getExecuteImageUrl()));
            vo.setExecuteRecord(defaultStr(e.getExecuteRecord()));
        }
        return vo;
    }

    @Transactional
    public void cancel(Long id, TaskCancelRequest req) {
        TaskSchedule e = mustGet(id);
        if (!Objects.equals(e.getStatus(), 1)) {
            throw new IllegalArgumentException("仅待执行任务可取消");
        }
        e.setStatus(3);
        e.setCancelReason(req.getCancelReason().trim());
        e.setCancelBy("管理员");
        e.setCancelTime(LocalDateTime.now());
        taskScheduleMapper.updateById(e);
        if ("护理计划外".equals(e.getTaskType())) {
            TaskRefundApply ra = new TaskRefundApply();
            ra.setTaskId(id);
            ra.setElderName(e.getElderName());
            ra.setRefundReason(req.getCancelReason().trim());
            ra.setStatus(1);
            refundApplyMapper.insert(ra);
        }
    }

    @Transactional
    public void execute(Long id, TaskExecuteRequest req) {
        TaskSchedule e = mustGet(id);
        if (!Objects.equals(e.getStatus(), 1)) {
            throw new IllegalArgumentException("仅待执行任务可执行");
        }
        LocalDateTime executeTime = parse(req.getExecuteTime());
        e.setStatus(2);
        e.setExecuteTime(executeTime);
        e.setExecuteBy(req.getExecuteBy().trim());
        e.setExecuteImageName(req.getExecuteImageName().trim());
        e.setExecuteImageUrl(req.getExecuteImageUrl().trim());
        e.setExecuteRecord(req.getExecuteRecord() == null ? "" : req.getExecuteRecord().trim());
        taskScheduleMapper.updateById(e);

        TaskExecuteRecord r = new TaskExecuteRecord();
        r.setTaskId(id);
        r.setExecuteTime(executeTime);
        r.setExecuteBy(req.getExecuteBy().trim());
        r.setExecuteImageName(req.getExecuteImageName().trim());
        r.setExecuteImageUrl(req.getExecuteImageUrl().trim());
        r.setExecuteRecord(req.getExecuteRecord() == null ? "" : req.getExecuteRecord().trim());
        executeRecordMapper.insert(r);
    }

    public void reschedule(Long id, TaskRescheduleRequest req) {
        TaskSchedule e = mustGet(id);
        if (!Objects.equals(e.getStatus(), 1)) {
            throw new IllegalArgumentException("仅待执行任务可改期");
        }
        e.setExpectedServiceTime(parse(req.getExpectedServiceTime()));
        taskScheduleMapper.updateById(e);
    }

    private TaskSchedule mustGet(Long id) {
        TaskSchedule e = taskScheduleMapper.selectOne(new LambdaQueryWrapper<TaskSchedule>()
                .eq(TaskSchedule::getId, id)
                .eq(TaskSchedule::getIsDeleted, 0));
        if (e == null) throw new IllegalArgumentException("任务不存在");
        return e;
    }

    private TaskScheduleItemVO toItemVO(TaskSchedule e) {
        TaskScheduleItemVO vo = new TaskScheduleItemVO();
        copyBase(vo, e);
        return vo;
    }

    private void copyBase(TaskScheduleItemVO vo, TaskSchedule e) {
        vo.setId(e.getId());
        vo.setElderName(e.getElderName());
        vo.setBedNo(e.getBedNo());
        vo.setNursingItemName(e.getNursingItemName());
        vo.setTaskType(e.getTaskType());
        vo.setCaregiverName(e.getCaregiverName());
        vo.setExpectedServiceTime(format(e.getExpectedServiceTime()));
        vo.setCreatedTime(format(e.getCreatedTime()));
        vo.setExecuteBy(defaultStr(e.getExecuteBy()));
        vo.setExecuteTime(format(e.getExecuteTime()));
        vo.setCancelBy(defaultStr(e.getCancelBy()));
        vo.setCancelTime(format(e.getCancelTime()));
        vo.setStatus(e.getStatus());
        vo.setStatusLabel(statusLabel(e.getStatus()));
    }

    private String statusLabel(Integer status) {
        if (Objects.equals(status, 1)) return "待执行";
        if (Objects.equals(status, 2)) return "已执行";
        if (Objects.equals(status, 3)) return "已取消";
        return "-";
    }

    private LocalDateTime parse(String s) {
        try {
            return LocalDateTime.parse(s.trim(), FMT);
        } catch (Exception ex) {
            throw new IllegalArgumentException("时间格式错误，应为yyyy-MM-dd HH:mm:ss");
        }
    }

    private String format(LocalDateTime time) {
        return time == null ? "" : FMT.format(time);
    }

    private String defaultStr(String s) {
        return s == null ? "" : s;
    }
}
