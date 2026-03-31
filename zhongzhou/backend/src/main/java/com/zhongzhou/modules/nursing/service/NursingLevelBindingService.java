package com.zhongzhou.modules.nursing.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.modules.checkin.entity.CheckinApplication;
import com.zhongzhou.modules.checkin.mapper.CheckinApplicationMapper;
import com.zhongzhou.modules.nursing.dto.NursingLevelBindRequest;
import com.zhongzhou.modules.nursing.entity.NursingLevelPlanBind;
import com.zhongzhou.modules.nursing.entity.NursingPlan;
import com.zhongzhou.modules.nursing.mapper.NursingLevelPlanBindMapper;
import com.zhongzhou.modules.nursing.mapper.NursingPlanMapper;
import com.zhongzhou.modules.nursing.vo.NursingCareLevelVO;
import com.zhongzhou.modules.nursing.vo.NursingPlanSimpleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class NursingLevelBindingService {
    private final CheckinApplicationMapper checkinApplicationMapper;
    private final NursingLevelPlanBindMapper bindMapper;
    private final NursingPlanMapper planMapper;

    public NursingLevelBindingService(CheckinApplicationMapper checkinApplicationMapper, NursingLevelPlanBindMapper bindMapper, NursingPlanMapper planMapper) {
        this.checkinApplicationMapper = checkinApplicationMapper;
        this.bindMapper = bindMapper;
        this.planMapper = planMapper;
    }

    public List<NursingCareLevelVO> listLevels() {
        List<String> levels = checkinApplicationMapper.selectList(new LambdaQueryWrapper<CheckinApplication>()
                        .eq(CheckinApplication::getIsDeleted, 0)
                        .select(CheckinApplication::getCareLevel)
                        .groupBy(CheckinApplication::getCareLevel)
                        .orderByAsc(CheckinApplication::getCareLevel))
                .stream()
                .map(CheckinApplication::getCareLevel)
                .filter(v -> v != null && !v.trim().isEmpty())
                .collect(Collectors.toList());
        Map<String, List<Long>> bindMap = bindMapper.selectList(new LambdaQueryWrapper<NursingLevelPlanBind>()
                        .eq(NursingLevelPlanBind::getIsDeleted, 0)
                        .in(!levels.isEmpty(), NursingLevelPlanBind::getCareLevelName, levels))
                .stream()
                .collect(Collectors.groupingBy(NursingLevelPlanBind::getCareLevelName,
                        Collectors.mapping(NursingLevelPlanBind::getPlanId, Collectors.toList())));
        return levels.stream().map(levelName -> {
            NursingCareLevelVO vo = new NursingCareLevelVO();
            vo.setLevelName(levelName);
            vo.setPlanIds(bindMap.getOrDefault(levelName, new ArrayList<>()));
            return vo;
        }).collect(Collectors.toList());
    }

    public List<NursingPlanSimpleVO> listAvailablePlans() {
        return planMapper.selectList(new LambdaQueryWrapper<NursingPlan>()
                        .eq(NursingPlan::getIsDeleted, 0)
                        .eq(NursingPlan::getStatus, 1)
                        .orderByAsc(NursingPlan::getSortOrder)
                        .orderByAsc(NursingPlan::getId))
                .stream()
                .map(x -> {
                    NursingPlanSimpleVO vo = new NursingPlanSimpleVO();
                    vo.setId(x.getId());
                    vo.setPlanName(x.getPlanName());
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void rebindLevel(String careLevel, NursingLevelBindRequest req) {
        boolean levelExists = checkinApplicationMapper.selectCount(new LambdaQueryWrapper<CheckinApplication>()
                .eq(CheckinApplication::getIsDeleted, 0)
                .eq(CheckinApplication::getCareLevel, careLevel)) > 0;
        if (!levelExists) {
            throw new IllegalArgumentException("护理等级不存在");
        }
        List<Long> ids = req.getPlanIds() == null ? new ArrayList<>() : req.getPlanIds().stream().distinct().collect(Collectors.toList());
        if (!ids.isEmpty()) {
            List<NursingPlan> plans = planMapper.selectBatchIds(ids);
            Map<Long, NursingPlan> planMap = plans.stream().collect(Collectors.toMap(NursingPlan::getId, Function.identity(), (a, b) -> a));
            for (Long id : ids) {
                NursingPlan p = planMap.get(id);
                if (p == null || (p.getIsDeleted() != null && p.getIsDeleted() == 1)) {
                    throw new IllegalArgumentException("护理计划不存在");
                }
            }
        }
        // 逻辑删除旧绑定
        List<NursingLevelPlanBind> oldRows = bindMapper.selectList(new LambdaQueryWrapper<NursingLevelPlanBind>()
                .eq(NursingLevelPlanBind::getCareLevelName, careLevel)
                .eq(NursingLevelPlanBind::getIsDeleted, 0));
        for (NursingLevelPlanBind old : oldRows) {
            old.setIsDeleted(1);
            bindMapper.updateById(old);
        }
        for (Long planId : ids) {
            NursingLevelPlanBind row = new NursingLevelPlanBind();
            row.setCareLevelName(careLevel);
            row.setPlanId(planId);
            row.setIsDeleted(0);
            bindMapper.insert(row);
        }
    }

    public long countPlanBindings(Long planId) {
        return bindMapper.selectCount(new LambdaQueryWrapper<NursingLevelPlanBind>()
                .eq(NursingLevelPlanBind::getPlanId, planId)
                .eq(NursingLevelPlanBind::getIsDeleted, 0));
    }

    public Map<Long, Long> countPlanBindings(List<Long> planIds) {
        if (planIds == null || planIds.isEmpty()) {
            return Map.of();
        }
        Set<Long> idSet = new HashSet<>(planIds);
        return bindMapper.selectList(new LambdaQueryWrapper<NursingLevelPlanBind>()
                        .in(NursingLevelPlanBind::getPlanId, idSet)
                        .eq(NursingLevelPlanBind::getIsDeleted, 0))
                .stream()
                .collect(Collectors.groupingBy(NursingLevelPlanBind::getPlanId, Collectors.counting()));
    }
}
