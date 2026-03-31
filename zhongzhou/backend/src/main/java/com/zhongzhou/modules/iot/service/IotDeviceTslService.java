package com.zhongzhou.modules.iot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.iot.entity.IotDevice;
import com.zhongzhou.modules.iot.entity.IotDeviceTslPropertyHistory;
import com.zhongzhou.modules.iot.entity.IotDeviceTslRuntime;
import com.zhongzhou.modules.iot.entity.IotDeviceTslServiceInvocation;
import com.zhongzhou.modules.iot.entity.IotTslModule;
import com.zhongzhou.modules.iot.mapper.IotDeviceMapper;
import com.zhongzhou.modules.iot.mapper.IotDeviceTslPropertyHistoryMapper;
import com.zhongzhou.modules.iot.mapper.IotDeviceTslRuntimeMapper;
import com.zhongzhou.modules.iot.mapper.IotDeviceTslServiceInvocationMapper;
import com.zhongzhou.modules.iot.mapper.IotTslModuleMapper;
import com.zhongzhou.modules.iot.vo.IotTslHistoryRowVO;
import com.zhongzhou.modules.iot.vo.IotTslModuleVO;
import com.zhongzhou.modules.iot.vo.IotTslRuntimeRowVO;
import com.zhongzhou.modules.iot.vo.IotTslServiceInvocationRowVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IotDeviceTslService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /** 演示数据对齐原型的时间锚点 */
    private static final LocalDateTime DEMO_NOW = LocalDateTime.of(2048, 10, 15, 15, 0, 0);

    private final IotDeviceMapper deviceMapper;
    private final IotTslModuleMapper moduleMapper;
    private final IotDeviceTslRuntimeMapper runtimeMapper;
    private final IotDeviceTslPropertyHistoryMapper historyMapper;
    private final IotDeviceTslServiceInvocationMapper serviceInvocationMapper;

    public IotDeviceTslService(IotDeviceMapper deviceMapper, IotTslModuleMapper moduleMapper,
                               IotDeviceTslRuntimeMapper runtimeMapper, IotDeviceTslPropertyHistoryMapper historyMapper,
                               IotDeviceTslServiceInvocationMapper serviceInvocationMapper) {
        this.deviceMapper = deviceMapper;
        this.moduleMapper = moduleMapper;
        this.runtimeMapper = runtimeMapper;
        this.historyMapper = historyMapper;
        this.serviceInvocationMapper = serviceInvocationMapper;
    }

    public List<IotTslModuleVO> listModules(Long deviceId) {
        requireDevice(deviceId);
        return moduleMapper.selectList(new LambdaQueryWrapper<IotTslModule>()
                        .eq(IotTslModule::getDeviceId, deviceId)
                        .eq(IotTslModule::getIsDeleted, 0)
                        .orderByAsc(IotTslModule::getSortOrder)
                        .orderByAsc(IotTslModule::getId))
                .stream().map(m -> {
                    IotTslModuleVO vo = new IotTslModuleVO();
                    vo.setId(m.getId());
                    vo.setModuleName(m.getModuleName());
                    vo.setModuleKey(m.getModuleKey());
                    return vo;
                }).collect(Collectors.toList());
    }

    public PageResult<IotTslRuntimeRowVO> pageRunningStatus(Long deviceId, Long moduleId, int page, int pageSize) {
        requireDevice(deviceId);
        requireModule(deviceId, moduleId);
        LambdaQueryWrapper<IotDeviceTslRuntime> q = new LambdaQueryWrapper<IotDeviceTslRuntime>()
                .eq(IotDeviceTslRuntime::getDeviceId, deviceId)
                .eq(IotDeviceTslRuntime::getModuleId, moduleId)
                .eq(IotDeviceTslRuntime::getIsDeleted, 0)
                .orderByAsc(IotDeviceTslRuntime::getSortOrder)
                .orderByAsc(IotDeviceTslRuntime::getId);
        Page<IotDeviceTslRuntime> p = runtimeMapper.selectPage(new Page<>(page, pageSize), q);
        List<IotTslRuntimeRowVO> rows = p.getRecords().stream().map(r -> {
            IotTslRuntimeRowVO vo = new IotTslRuntimeRowVO();
            vo.setId(r.getId());
            vo.setPropIdentifier(r.getPropIdentifier());
            vo.setFunctionName(r.getFunctionName());
            vo.setDataValue(r.getDataValue());
            vo.setUpdateTime(r.getUpdateTime() == null ? "" : FMT.format(r.getUpdateTime()));
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public PageResult<IotTslHistoryRowVO> pagePropertyHistory(Long deviceId, Long moduleId, String propIdentifier,
                                                              String rangeType, String customStart, String customEnd,
                                                              int page, int pageSize) {
        requireDevice(deviceId);
        requireModule(deviceId, moduleId);
        if (!StringUtils.hasText(propIdentifier)) {
            throw new IllegalArgumentException("标识符不能为空");
        }
        LocalDateTime[] range = resolveRange(rangeType, customStart, customEnd);
        LambdaQueryWrapper<IotDeviceTslPropertyHistory> q = new LambdaQueryWrapper<IotDeviceTslPropertyHistory>()
                .eq(IotDeviceTslPropertyHistory::getDeviceId, deviceId)
                .eq(IotDeviceTslPropertyHistory::getModuleId, moduleId)
                .eq(IotDeviceTslPropertyHistory::getPropIdentifier, propIdentifier.trim())
                .eq(IotDeviceTslPropertyHistory::getIsDeleted, 0)
                .ge(IotDeviceTslPropertyHistory::getReportTime, range[0])
                .le(IotDeviceTslPropertyHistory::getReportTime, range[1])
                .orderByDesc(IotDeviceTslPropertyHistory::getReportTime);
        Page<IotDeviceTslPropertyHistory> p = historyMapper.selectPage(new Page<>(page, pageSize), q);
        List<IotTslHistoryRowVO> rows = p.getRecords().stream().map(h -> {
            IotTslHistoryRowVO vo = new IotTslHistoryRowVO();
            vo.setRawValue(h.getRawValue());
            vo.setReportTime(h.getReportTime() == null ? "" : FMT.format(h.getReportTime()));
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    /** 服务调用（下行）：按时间范围分页，每页固定条数由 limit 控制；offset 为已加载条数（与 limit 对齐）。 */
    public PageResult<IotTslServiceInvocationRowVO> pageServiceInvocations(Long deviceId, Long moduleId,
                                                                           String rangeType, String customStart, String customEnd,
                                                                           int offset, int limit) {
        requireDevice(deviceId);
        requireModule(deviceId, moduleId);
        if (limit < 1) {
            throw new IllegalArgumentException("每页条数无效");
        }
        if (offset < 0 || offset % limit != 0) {
            throw new IllegalArgumentException("偏移量无效");
        }
        String rt = rangeType == null ? "1h" : rangeType.trim();
        if ("custom".equals(rt) && (!StringUtils.hasText(customStart) || !StringUtils.hasText(customEnd))) {
            return new PageResult<>(0, Collections.emptyList());
        }
        LocalDateTime[] range = resolveRange(rangeType, customStart, customEnd);
        LambdaQueryWrapper<IotDeviceTslServiceInvocation> q = new LambdaQueryWrapper<IotDeviceTslServiceInvocation>()
                .eq(IotDeviceTslServiceInvocation::getDeviceId, deviceId)
                .eq(IotDeviceTslServiceInvocation::getModuleId, moduleId)
                .eq(IotDeviceTslServiceInvocation::getIsDeleted, 0)
                .ge(IotDeviceTslServiceInvocation::getInvokeTime, range[0])
                .le(IotDeviceTslServiceInvocation::getInvokeTime, range[1])
                .orderByDesc(IotDeviceTslServiceInvocation::getInvokeTime);
        long total = serviceInvocationMapper.selectCount(q);
        Page<IotDeviceTslServiceInvocation> p = serviceInvocationMapper.selectPage(
                new Page<>(offset / limit + 1L, limit), q);
        List<IotTslServiceInvocationRowVO> rows = p.getRecords().stream().map(r -> {
            IotTslServiceInvocationRowVO vo = new IotTslServiceInvocationRowVO();
            vo.setInvokeTime(r.getInvokeTime() == null ? "" : FMT.format(r.getInvokeTime()));
            vo.setServiceIdentifier(r.getServiceIdentifier());
            vo.setServiceName(r.getServiceName());
            vo.setInputParams(r.getInputParams());
            vo.setOutputParams(r.getOutputParams());
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(total, rows);
    }

    private LocalDateTime[] resolveRange(String rangeType, String customStart, String customEnd) {
        String rt = rangeType == null ? "1h" : rangeType.trim();
        if ("custom".equals(rt)) {
            if (!StringUtils.hasText(customStart) || !StringUtils.hasText(customEnd)) {
                throw new IllegalArgumentException("自定义时间范围不能为空");
            }
            try {
                LocalDateTime s = LocalDateTime.parse(customStart.trim(), FMT);
                LocalDateTime e = LocalDateTime.parse(customEnd.trim(), FMT);
                if (s.isAfter(e)) {
                    throw new IllegalArgumentException("开始时间不能晚于结束时间");
                }
                return new LocalDateTime[]{s, e};
            } catch (DateTimeParseException ex) {
                throw new IllegalArgumentException("时间格式应为 yyyy-MM-dd HH:mm:ss");
            }
        }
        LocalDateTime end = DEMO_NOW;
        LocalDateTime start;
        switch (rt) {
            case "24h":
                start = end.minusHours(24);
                break;
            case "7d":
                start = end.minusDays(7);
                break;
            case "1h":
            default:
                start = end.minusHours(1);
                break;
        }
        return new LocalDateTime[]{start, end};
    }

    private void requireDevice(Long deviceId) {
        IotDevice d = deviceMapper.selectOne(new LambdaQueryWrapper<IotDevice>()
                .eq(IotDevice::getId, deviceId)
                .eq(IotDevice::getIsDeleted, 0));
        if (d == null) {
            throw new IllegalArgumentException("设备不存在");
        }
    }

    private void requireModule(Long deviceId, Long moduleId) {
        if (moduleId == null) {
            throw new IllegalArgumentException("模块不能为空");
        }
        IotTslModule m = moduleMapper.selectOne(new LambdaQueryWrapper<IotTslModule>()
                .eq(IotTslModule::getId, moduleId)
                .eq(IotTslModule::getDeviceId, deviceId)
                .eq(IotTslModule::getIsDeleted, 0));
        if (m == null) {
            throw new IllegalArgumentException("模块不存在");
        }
    }
}
