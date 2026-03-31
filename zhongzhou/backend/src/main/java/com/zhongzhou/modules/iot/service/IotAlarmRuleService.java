package com.zhongzhou.modules.iot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.iot.dto.IotAlarmRuleSaveRequest;
import com.zhongzhou.modules.iot.entity.IotAlarmRule;
import com.zhongzhou.modules.iot.entity.IotDevice;
import com.zhongzhou.modules.iot.entity.IotProduct;
import com.zhongzhou.modules.iot.entity.IotDeviceTslRuntime;
import com.zhongzhou.modules.iot.entity.IotTslModule;
import com.zhongzhou.modules.iot.mapper.IotAlarmRuleMapper;
import com.zhongzhou.modules.iot.mapper.IotDeviceTslRuntimeMapper;
import com.zhongzhou.modules.iot.mapper.IotDeviceMapper;
import com.zhongzhou.modules.iot.mapper.IotProductMapper;
import com.zhongzhou.modules.iot.mapper.IotTslModuleMapper;
import com.zhongzhou.modules.iot.vo.IotAlarmRuleVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class IotAlarmRuleService {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final IotAlarmRuleMapper ruleMapper;
    private final IotDeviceMapper deviceMapper;
    private final IotProductMapper productMapper;
    private final IotTslModuleMapper tslModuleMapper;
    private final IotDeviceTslRuntimeMapper runtimeMapper;

    public IotAlarmRuleService(IotAlarmRuleMapper ruleMapper, IotDeviceMapper deviceMapper, IotProductMapper productMapper,
                               IotTslModuleMapper tslModuleMapper, IotDeviceTslRuntimeMapper runtimeMapper) {
        this.ruleMapper = ruleMapper;
        this.deviceMapper = deviceMapper;
        this.productMapper = productMapper;
        this.tslModuleMapper = tslModuleMapper;
        this.runtimeMapper = runtimeMapper;
    }

    public PageResult<IotAlarmRuleVO> page(String ruleName, Long productId, int page, int pageSize) {
        String rn = StringUtils.hasText(ruleName) ? ruleName.trim() : null;
        LambdaQueryWrapper<IotAlarmRule> q = new LambdaQueryWrapper<IotAlarmRule>()
                .eq(IotAlarmRule::getIsDeleted, 0)
                .eq(rn != null, IotAlarmRule::getRuleName, rn)
                .eq(productId != null, IotAlarmRule::getProductId, productId)
                .orderByDesc(IotAlarmRule::getId);
        Page<IotAlarmRule> p = ruleMapper.selectPage(new Page<>(page, pageSize), q);
        Map<Long, String> deviceNames = loadDeviceNames(p.getRecords());
        Map<Long, String> productNames = loadProductNames(p.getRecords());
        List<IotAlarmRuleVO> rows = p.getRecords().stream()
                .map(e -> toVO(e, deviceNames, productNames))
                .collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public IotAlarmRuleVO get(Long id) {
        IotAlarmRule e = getActive(id);
        Map<Long, String> deviceNames = loadDeviceNames(List.of(e));
        Map<Long, String> productNames = loadProductNames(List.of(e));
        return toVO(e, deviceNames, productNames);
    }

    public void setEnabled(Long id, int enabled) {
        IotAlarmRule e = getActive(id);
        if (enabled != 0 && enabled != 1) {
            throw new IllegalArgumentException("状态无效");
        }
        e.setEnabled(enabled);
        ruleMapper.updateById(e);
    }

    public List<String> listModulesByProduct(Long productId) {
        requireProduct(productId);
        List<Long> deviceIds = listDeviceIdsByProduct(productId);
        if (deviceIds.isEmpty()) return List.of();
        return tslModuleMapper.selectList(new LambdaQueryWrapper<IotTslModule>()
                        .in(IotTslModule::getDeviceId, deviceIds)
                        .eq(IotTslModule::getIsDeleted, 0)
                        .orderByAsc(IotTslModule::getSortOrder, IotTslModule::getId))
                .stream()
                .map(IotTslModule::getModuleName)
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> listFunctions(Long productId, String moduleName) {
        requireProduct(productId);
        if (!StringUtils.hasText(moduleName)) {
            throw new IllegalArgumentException("模块不能为空");
        }
        List<Long> deviceIds = listDeviceIdsByProduct(productId);
        if (deviceIds.isEmpty()) return List.of();
        List<Long> moduleIds = tslModuleMapper.selectList(new LambdaQueryWrapper<IotTslModule>()
                        .in(IotTslModule::getDeviceId, deviceIds)
                        .eq(IotTslModule::getIsDeleted, 0)
                        .eq(IotTslModule::getModuleName, moduleName.trim()))
                .stream().map(IotTslModule::getId).collect(Collectors.toList());
        if (moduleIds.isEmpty()) return List.of();
        return runtimeMapper.selectList(new LambdaQueryWrapper<IotDeviceTslRuntime>()
                        .in(IotDeviceTslRuntime::getModuleId, moduleIds)
                        .eq(IotDeviceTslRuntime::getIsDeleted, 0)
                        .orderByAsc(IotDeviceTslRuntime::getSortOrder, IotDeviceTslRuntime::getId))
                .stream()
                .map(IotDeviceTslRuntime::getFunctionName)
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    public Long create(IotAlarmRuleSaveRequest req) {
        IotAlarmRule e = new IotAlarmRule();
        fill(e, req);
        e.setIsDeleted(0);
        ruleMapper.insert(e);
        return e.getId();
    }

    public void update(Long id, IotAlarmRuleSaveRequest req) {
        IotAlarmRule e = getActive(id);
        fill(e, req);
        ruleMapper.updateById(e);
    }

    public void remove(Long id) {
        IotAlarmRule e = getActive(id);
        e.setIsDeleted(1);
        ruleMapper.updateById(e);
    }

    private IotAlarmRule getActive(Long id) {
        IotAlarmRule e = ruleMapper.selectOne(new LambdaQueryWrapper<IotAlarmRule>()
                .eq(IotAlarmRule::getId, id)
                .eq(IotAlarmRule::getIsDeleted, 0));
        if (e == null) {
            throw new IllegalArgumentException("规则不存在");
        }
        return e;
    }

    private void fill(IotAlarmRule e, IotAlarmRuleSaveRequest req) {
        String rn = req.getRuleName().trim();
        if (rn.length() > 20) {
            throw new IllegalArgumentException("报警规则名称不能超过20个字符");
        }
        if (!rn.matches("^[\\u4e00-\\u9fa5A-Za-z0-9]+$")) {
            throw new IllegalArgumentException("报警规则名称不能包含特殊符号");
        }
        e.setRuleName(rn);
        e.setProductId(req.getProductId());
        e.setModuleName(req.getModuleName().trim());
        e.setDeviceId(req.getDeviceId());
        e.setFunctionName(req.getFunctionName().trim());
        String dt = req.getDataType().trim();
        if (!"elder".equals(dt) && !"device".equals(dt)) {
            throw new IllegalArgumentException("报警数据类型无效");
        }
        e.setDataType(dt);
        e.setCompareType(req.getCompareType().trim().toUpperCase());
        e.setThresholdValue(req.getThresholdValue());
        e.setPersistCycles(req.getPersistCycles());
        e.setTimeStart(parseTime(req.getTimeStart(), "生效开始时间"));
        e.setTimeEnd(parseTime(req.getTimeEnd(), "生效结束时间"));
        if (!MUTE_CYCLES.contains(req.getMuteCycleMinutes())) {
            throw new IllegalArgumentException("报警沉默周期无效");
        }
        e.setMuteCycleMinutes(req.getMuteCycleMinutes());
        e.setEnabled(req.getEnabled());
    }

    private static final Set<Integer> MUTE_CYCLES = Set.of(5, 10, 15, 30, 60, 180, 360, 720, 1440);

    private static LocalTime parseTime(String s, String label) {
        if (!StringUtils.hasText(s)) {
            throw new IllegalArgumentException(label + "不能为空");
        }
        try {
            return LocalTime.parse(s.trim(), TIME_FMT);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException(label + "格式应为 HH:mm:ss");
        }
    }

    private Map<Long, String> loadDeviceNames(List<IotAlarmRule> rules) {
        List<Long> ids = rules.stream().map(IotAlarmRule::getDeviceId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Map.of();
        }
        return deviceMapper.selectList(new LambdaQueryWrapper<IotDevice>()
                        .in(IotDevice::getId, ids)
                        .eq(IotDevice::getIsDeleted, 0))
                .stream().collect(Collectors.toMap(IotDevice::getId, IotDevice::getDeviceName, (a, b) -> a));
    }

    private Map<Long, String> loadProductNames(List<IotAlarmRule> rules) {
        List<Long> ids = rules.stream().map(IotAlarmRule::getProductId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Map.of();
        }
        return productMapper.selectList(new LambdaQueryWrapper<IotProduct>()
                        .in(IotProduct::getId, ids)
                        .eq(IotProduct::getIsDeleted, 0))
                .stream().collect(Collectors.toMap(IotProduct::getId, IotProduct::getProductName, (a, b) -> a));
    }

    private IotAlarmRuleVO toVO(IotAlarmRule e, Map<Long, String> deviceNames, Map<Long, String> productNames) {
        IotAlarmRuleVO vo = new IotAlarmRuleVO();
        vo.setId(e.getId());
        vo.setRuleName(e.getRuleName());
        vo.setProductId(e.getProductId());
        vo.setProductName(productNames.getOrDefault(e.getProductId(), ""));
        vo.setModuleName(e.getModuleName() == null ? "" : e.getModuleName());
        vo.setDeviceId(e.getDeviceId());
        if (e.getDeviceId() != null) {
            vo.setDeviceDisplay(deviceNames.getOrDefault(e.getDeviceId(), ""));
        } else {
            vo.setDeviceDisplay("全部设备");
        }
        vo.setFunctionName(e.getFunctionName());
        vo.setDataType(e.getDataType());
        vo.setDataTypeLabel("device".equals(e.getDataType()) ? "设备异常数据" : "老人异常数据");
        vo.setCompareType(e.getCompareType());
        vo.setThresholdValue(e.getThresholdValue());
        int cycles = e.getPersistCycles() != null ? e.getPersistCycles() : 1;
        vo.setPersistCycles(cycles);
        vo.setTimeStart(e.getTimeStart() == null ? "" : TIME_FMT.format(e.getTimeStart()));
        vo.setTimeEnd(e.getTimeEnd() == null ? "" : TIME_FMT.format(e.getTimeEnd()));
        vo.setMuteCycleMinutes(e.getMuteCycleMinutes());
        vo.setMuteCycleLabel(muteCycleLabel(e.getMuteCycleMinutes()));
        vo.setAlarmRuleText(buildAlarmRuleText(e.getFunctionName(), e.getCompareType(), e.getThresholdValue(), cycles));
        vo.setEffectivePeriod(vo.getTimeStart() + "-" + vo.getTimeEnd());
        vo.setEnabled(e.getEnabled());
        vo.setEnabledLabel(Integer.valueOf(1).equals(e.getEnabled()) ? "启用" : "禁用");
        vo.setCreatedTime(e.getCreatedTime() == null ? "" : FMT.format(e.getCreatedTime()));
        return vo;
    }

    /** 报警规则 = 功能名称 + 运算符 + 阈值，持续 x 个周期就报警 */
    public static String buildAlarmRuleText(String functionName, String compareType, BigDecimal threshold, int persistCycles) {
        String fn = functionName == null ? "" : functionName;
        String ct = compareType == null ? "GTE" : compareType.toUpperCase();
        String sym = compareSymbol(ct);
        String num = threshold == null ? "0" : threshold.stripTrailingZeros().toPlainString();
        String mid;
        if ("LT".equals(ct) || "LTE".equals(ct)) {
            mid = sym + " " + num;
        } else {
            mid = sym + num;
        }
        return fn + mid + "，持续" + persistCycles + "个周期就报警";
    }

    private static String compareSymbol(String ct) {
        switch (ct) {
            case "GT":
                return ">";
            case "GTE":
                return ">=";
            case "LT":
                return "<";
            case "LTE":
                return "<=";
            case "EQ":
                return "==";
            default:
                return ct;
        }
    }

    private static String muteCycleLabel(Integer minutes) {
        if (minutes == null) return "";
        switch (minutes) {
            case 5: return "5分钟";
            case 10: return "10分钟";
            case 15: return "15分钟";
            case 30: return "30分钟";
            case 60: return "60分钟";
            case 180: return "3小时";
            case 360: return "6小时";
            case 720: return "12小时";
            case 1440: return "24小时";
            default: return minutes + "分钟";
        }
    }

    private void requireProduct(Long productId) {
        if (productId == null) throw new IllegalArgumentException("所属产品不能为空");
        IotProduct p = productMapper.selectOne(new LambdaQueryWrapper<IotProduct>()
                .eq(IotProduct::getId, productId)
                .eq(IotProduct::getIsDeleted, 0));
        if (p == null) throw new IllegalArgumentException("所属产品不存在");
    }

    private List<Long> listDeviceIdsByProduct(Long productId) {
        return deviceMapper.selectList(new LambdaQueryWrapper<IotDevice>()
                        .eq(IotDevice::getProductId, productId)
                        .eq(IotDevice::getIsDeleted, 0))
                .stream().map(IotDevice::getId).collect(Collectors.toList());
    }
}
