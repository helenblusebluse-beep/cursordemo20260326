package com.zhongzhou.modules.iot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.iot.entity.IotAlarmRecord;
import com.zhongzhou.modules.iot.entity.IotAlarmRule;
import com.zhongzhou.modules.iot.entity.IotDevice;
import com.zhongzhou.modules.iot.entity.IotProduct;
import com.zhongzhou.modules.iot.dto.IotAlarmRecordHandleRequest;
import com.zhongzhou.modules.iot.mapper.IotAlarmRecordMapper;
import com.zhongzhou.modules.iot.mapper.IotAlarmRuleMapper;
import com.zhongzhou.modules.iot.mapper.IotDeviceMapper;
import com.zhongzhou.modules.iot.mapper.IotProductMapper;
import com.zhongzhou.modules.iot.vo.IotAlarmRecordVO;
import com.zhongzhou.modules.iot.service.IotAlarmRuleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IotAlarmRecordService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final IotAlarmRecordMapper recordMapper;
    private final IotAlarmRuleMapper ruleMapper;
    private final IotDeviceMapper deviceMapper;
    private final IotProductMapper productMapper;

    public IotAlarmRecordService(IotAlarmRecordMapper recordMapper, IotAlarmRuleMapper ruleMapper,
                                 IotDeviceMapper deviceMapper, IotProductMapper productMapper) {
        this.recordMapper = recordMapper;
        this.ruleMapper = ruleMapper;
        this.deviceMapper = deviceMapper;
        this.productMapper = productMapper;
    }

    public PageResult<IotAlarmRecordVO> page(String deviceName, String createdDate, Integer handleStatus, int page, int pageSize) {
        String dn = StringUtils.hasText(deviceName) ? deviceName.trim() : null;
        List<Long> deviceIds = null;
        if (dn != null) {
            deviceIds = deviceMapper.selectList(new LambdaQueryWrapper<IotDevice>()
                            .eq(IotDevice::getIsDeleted, 0)
                            .eq(IotDevice::getDeviceName, dn))
                    .stream().map(IotDevice::getId).collect(Collectors.toList());
            if (deviceIds.isEmpty()) {
                return new PageResult<>(0L, List.of());
            }
        }
        LocalDateTime start = null;
        LocalDateTime end = null;
        if (StringUtils.hasText(createdDate)) {
            try {
                LocalDate d = LocalDate.parse(createdDate.trim());
                start = d.atStartOfDay();
                end = d.plusDays(1).atStartOfDay();
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("创建时间格式应为 yyyy-MM-dd");
            }
        }
        LambdaQueryWrapper<IotAlarmRecord> q = new LambdaQueryWrapper<IotAlarmRecord>()
                .eq(IotAlarmRecord::getIsDeleted, 0)
                .in(deviceIds != null, IotAlarmRecord::getDeviceId, deviceIds)
                .ge(start != null, IotAlarmRecord::getAlarmTime, start)
                .lt(end != null, IotAlarmRecord::getAlarmTime, end)
                .eq(handleStatus != null, IotAlarmRecord::getHandleStatus, handleStatus)
                .orderByDesc(IotAlarmRecord::getAlarmTime, IotAlarmRecord::getId);
        Page<IotAlarmRecord> p = recordMapper.selectPage(new Page<>(page, pageSize), q);
        Map<Long, IotAlarmRule> rules = loadRules(p.getRecords());
        Map<Long, IotDevice> devices = loadDevices(p.getRecords());
        Map<Long, String> productNames = loadProductNames(rules.values().stream().map(IotAlarmRule::getProductId).filter(Objects::nonNull).collect(Collectors.toList()));
        List<IotAlarmRecordVO> rows = p.getRecords().stream().map(e -> toVO(e, rules, devices, productNames)).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public void handle(Long id, IotAlarmRecordHandleRequest req) {
        IotAlarmRecord e = recordMapper.selectOne(new LambdaQueryWrapper<IotAlarmRecord>()
                .eq(IotAlarmRecord::getId, id)
                .eq(IotAlarmRecord::getIsDeleted, 0));
        if (e == null) {
            throw new IllegalArgumentException("告警记录不存在");
        }
        if (e.getHandleStatus() != null && e.getHandleStatus() == 1) {
            throw new IllegalArgumentException("该记录已处理");
        }
        LocalDateTime t;
        try {
            t = LocalDateTime.parse(req.getHandleTime().trim(), FMT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("处理时间格式应为 yyyy-MM-dd HH:mm:ss");
        }
        String r = req.getHandleResult() == null ? "" : req.getHandleResult().trim();
        if (r.isEmpty()) throw new IllegalArgumentException("处理结果不能为空");
        if (r.length() > 100) throw new IllegalArgumentException("处理结果不能超过100字符");
        if (!r.matches("^[\\u4e00-\\u9fa5A-Za-z0-9，。,.、；;：:（）()\\-\\s]+$")) {
            throw new IllegalArgumentException("处理结果不能包含特殊表情/非常规符号");
        }
        e.setHandleStatus(1);
        e.setHandledTime(t);
        e.setHandleResult(r);
        recordMapper.updateById(e);
    }

    private Map<Long, IotAlarmRule> loadRules(List<IotAlarmRecord> list) {
        List<Long> ids = list.stream().map(IotAlarmRecord::getRuleId).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) return Map.of();
        return ruleMapper.selectList(new LambdaQueryWrapper<IotAlarmRule>().in(IotAlarmRule::getId, ids))
                .stream().collect(Collectors.toMap(IotAlarmRule::getId, x -> x, (a, b) -> a));
    }

    private Map<Long, IotDevice> loadDevices(List<IotAlarmRecord> list) {
        List<Long> ids = list.stream().map(IotAlarmRecord::getDeviceId).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) return Map.of();
        return deviceMapper.selectList(new LambdaQueryWrapper<IotDevice>()
                        .in(IotDevice::getId, ids)
                        .eq(IotDevice::getIsDeleted, 0))
                .stream().collect(Collectors.toMap(IotDevice::getId, x -> x, (a, b) -> a));
    }

    private Map<Long, String> loadProductNames(List<Long> ids) {
        if (ids.isEmpty()) return Map.of();
        return productMapper.selectList(new LambdaQueryWrapper<IotProduct>().in(IotProduct::getId, ids))
                .stream().collect(Collectors.toMap(IotProduct::getId, IotProduct::getProductName, (a, b) -> a));
    }

    private IotAlarmRecordVO toVO(IotAlarmRecord e, Map<Long, IotAlarmRule> rules, Map<Long, IotDevice> devices, Map<Long, String> productNames) {
        IotAlarmRecordVO vo = new IotAlarmRecordVO();
        IotAlarmRule r = rules.get(e.getRuleId());
        IotDevice d = devices.get(e.getDeviceId());
        vo.setId(e.getId());
        vo.setRuleId(e.getRuleId());
        vo.setRuleName(r == null ? "" : r.getRuleName());
        vo.setDeviceId(e.getDeviceId());
        vo.setDeviceName(d == null ? "" : d.getDeviceName());
        vo.setProductName(r == null ? "" : productNames.getOrDefault(r.getProductId(), ""));
        vo.setFunctionName(r == null ? "" : r.getFunctionName());
        vo.setDataTypeLabel(r != null && "device".equals(r.getDataType()) ? "设备异常数据" : "老人异常数据");
        vo.setAccessLocation(d == null ? "" : d.getAccessLocationDisplay());
        vo.setMetricValue(e.getMetricValue());
        vo.setAlarmContent(e.getAlarmContent());
        vo.setAlarmTime(e.getAlarmTime() == null ? "" : FMT.format(e.getAlarmTime()));
        vo.setTriggerTime(vo.getAlarmTime());
        vo.setNotifyTime(vo.getAlarmTime());
        vo.setNotifyObject(r != null && "device".equals(r.getDataType()) ? "后勤部-维修工" : "老人所对应的护理员");
        vo.setAlarmRuleText(r == null ? "" : IotAlarmRuleService.buildAlarmRuleText(r.getFunctionName(), r.getCompareType(), r.getThresholdValue(), r.getPersistCycles() == null ? 1 : r.getPersistCycles()));
        vo.setHandleStatus(e.getHandleStatus());
        vo.setHandleStatusLabel(Integer.valueOf(1).equals(e.getHandleStatus()) ? "已处理" : "待处理");
        vo.setHandledTime(e.getHandledTime() == null ? "" : FMT.format(e.getHandledTime()));
        vo.setHandleResult(e.getHandleResult() == null ? "" : e.getHandleResult());
        return vo;
    }
}
