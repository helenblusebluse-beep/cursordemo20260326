package com.zhongzhou.modules.iot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zhongzhou.modules.iot.entity.IotProduct;
import com.zhongzhou.modules.iot.mapper.IotProductMapper;
import com.zhongzhou.modules.iot.vo.IotProductVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IotProductService {
    private static final List<String[]> SYNC_ROWS = Arrays.asList(
            new String[]{"智能手表", "PK_smartwatch"},
            new String[]{"智能烟感", "PK_smoke"},
            new String[]{"智能门禁/闸机", "PK_door"}
    );

    private final IotProductMapper mapper;

    public IotProductService(IotProductMapper mapper) {
        this.mapper = mapper;
    }

    public List<IotProductVO> listActive() {
        return mapper.selectList(new LambdaQueryWrapper<IotProduct>()
                        .eq(IotProduct::getIsDeleted, 0)
                        .orderByAsc(IotProduct::getId))
                .stream().map(this::toVO).collect(Collectors.toList());
    }

    /**
     * 模拟从阿里云物联网平台拉取产品列表并落库。
     */
    @Transactional
    public void syncFromAliyun() {
        for (String[] row : SYNC_ROWS) {
            String name = row[0];
            String pk = row[1];
            IotProduct existing = mapper.selectOne(new LambdaQueryWrapper<IotProduct>()
                    .eq(IotProduct::getProductName, name)
                    .eq(IotProduct::getIsDeleted, 0)
                    .last("LIMIT 1"));
            if (existing != null) {
                existing.setAliyunProductKey(pk);
                mapper.updateById(existing);
            } else {
                IotProduct p = new IotProduct();
                p.setProductName(name);
                p.setAliyunProductKey(pk);
                p.setIsDeleted(0);
                mapper.insert(p);
            }
        }
    }

    public IotProduct requireActive(Long id) {
        IotProduct p = mapper.selectOne(new LambdaQueryWrapper<IotProduct>()
                .eq(IotProduct::getId, id)
                .eq(IotProduct::getIsDeleted, 0));
        if (p == null) {
            throw new IllegalArgumentException("产品不存在");
        }
        return p;
    }

    private IotProductVO toVO(IotProduct e) {
        IotProductVO vo = new IotProductVO();
        vo.setId(e.getId());
        vo.setProductName(e.getProductName());
        vo.setAliyunProductKey(e.getAliyunProductKey());
        return vo;
    }
}
