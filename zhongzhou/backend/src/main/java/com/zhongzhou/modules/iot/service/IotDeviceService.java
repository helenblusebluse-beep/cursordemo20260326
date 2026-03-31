package com.zhongzhou.modules.iot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhongzhou.common.PageResult;
import com.zhongzhou.modules.bedpreview.entity.PreviewBed;
import com.zhongzhou.modules.bedpreview.entity.PreviewFloor;
import com.zhongzhou.modules.bedpreview.entity.PreviewRoom;
import com.zhongzhou.modules.bedpreview.mapper.PreviewBedMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewFloorMapper;
import com.zhongzhou.modules.bedpreview.mapper.PreviewRoomMapper;
import com.zhongzhou.modules.customer.entity.CustomerProfile;
import com.zhongzhou.modules.customer.mapper.CustomerProfileMapper;
import com.zhongzhou.modules.iot.dto.IotDeviceCreateRequest;
import com.zhongzhou.modules.iot.dto.IotDeviceUpdateRequest;
import com.zhongzhou.modules.iot.entity.IotDevice;
import com.zhongzhou.modules.iot.entity.IotProduct;
import com.zhongzhou.modules.iot.mapper.IotDeviceMapper;
import com.zhongzhou.modules.iot.mapper.IotProductMapper;
import com.zhongzhou.modules.iot.vo.IotDeviceDetailVO;
import com.zhongzhou.modules.iot.vo.IotDeviceOptionVO;
import com.zhongzhou.modules.iot.vo.IotDeviceVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IotDeviceService {
    public static final String TYPE_PORTABLE = "随身设备";
    public static final String TYPE_FIXED = "固定设备";

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final IotDeviceMapper mapper;
    private final IotProductMapper productMapper;
    private final IotProductService productService;
    private final CustomerProfileMapper customerMapper;
    private final PreviewBedMapper bedMapper;
    private final PreviewRoomMapper roomMapper;
    private final PreviewFloorMapper floorMapper;

    public IotDeviceService(IotDeviceMapper mapper, IotProductMapper productMapper, IotProductService productService,
                            CustomerProfileMapper customerMapper, PreviewBedMapper bedMapper,
                            PreviewRoomMapper roomMapper, PreviewFloorMapper floorMapper) {
        this.mapper = mapper;
        this.productMapper = productMapper;
        this.productService = productService;
        this.customerMapper = customerMapper;
        this.bedMapper = bedMapper;
        this.roomMapper = roomMapper;
        this.floorMapper = floorMapper;
    }

    public PageResult<IotDeviceVO> page(String deviceName, Long productId, String deviceType, int page, int pageSize) {
        String name = StringUtils.hasText(deviceName) ? deviceName.trim() : null;
        String type = StringUtils.hasText(deviceType) ? deviceType.trim() : null;
        LambdaQueryWrapper<IotDevice> q = new LambdaQueryWrapper<IotDevice>()
                .eq(IotDevice::getIsDeleted, 0)
                .like(name != null, IotDevice::getDeviceName, name)
                .eq(productId != null, IotDevice::getProductId, productId)
                .eq(type != null, IotDevice::getDeviceType, type)
                .orderByDesc(IotDevice::getId);
        Page<IotDevice> p = mapper.selectPage(new Page<>(page, pageSize), q);
        Map<Long, String> productNames = loadProductNames(p.getRecords());
        List<IotDeviceVO> rows = p.getRecords().stream()
                .map(e -> toListVO(e, productNames.get(e.getProductId())))
                .collect(Collectors.toList());
        return new PageResult<>(p.getTotal(), rows);
    }

    public IotDeviceDetailVO detail(Long id) {
        IotDevice e = getActive(id);
        IotProduct pr = productMapper.selectById(e.getProductId());
        IotDeviceDetailVO vo = new IotDeviceDetailVO();
        vo.setId(e.getId());
        vo.setDeviceName(e.getDeviceName());
        vo.setRemarkName(e.getRemarkName());
        vo.setProductId(e.getProductId());
        vo.setProductName(pr == null ? "" : pr.getProductName());
        String pk = "";
        if (pr != null && StringUtils.hasText(pr.getAliyunProductKey())) {
            pk = pr.getAliyunProductKey().trim();
        } else if (StringUtils.hasText(e.getProductKey())) {
            pk = e.getProductKey().trim();
        }
        vo.setProductKey(pk);
        vo.setDeviceSecret(e.getDeviceSecret() == null ? "" : e.getDeviceSecret());
        vo.setDeviceType(e.getDeviceType());
        vo.setAccessLocationDisplay(e.getAccessLocationDisplay());
        vo.setBindElderId(e.getBindElderId());
        vo.setBindBedId(e.getBindBedId());
        vo.setProtocol(e.getProtocol());
        vo.setOnlineStatus(e.getOnlineStatus());
        vo.setOnlineStatusLabel(Integer.valueOf(1).equals(e.getOnlineStatus()) ? "在线" : "离线");
        vo.setRegion(e.getRegion() == null ? "" : e.getRegion());
        vo.setNodeType(e.getNodeType() == null ? "" : e.getNodeType());
        vo.setAuthMethod(e.getAuthMethod() == null ? "" : e.getAuthMethod());
        vo.setIpAddress(e.getIpAddress() == null ? "" : e.getIpAddress());
        vo.setFirmwareVersion(e.getFirmwareVersion() == null ? "" : e.getFirmwareVersion());
        vo.setCreatorName(e.getCreatorName() == null ? "" : e.getCreatorName());
        vo.setCreatedTime(e.getCreatedTime() == null ? "" : FMT.format(e.getCreatedTime()));
        vo.setActivationTime(e.getActivationTime() == null ? "" : FMT.format(e.getActivationTime()));
        vo.setUpdatedTime(e.getUpdatedTime() == null ? "" : FMT.format(e.getUpdatedTime()));
        return vo;
    }

    @Transactional
    public Long create(IotDeviceCreateRequest req) {
        productService.requireActive(req.getProductId());
        assertDeviceNameUnique(req.getDeviceName().trim(), null);
        validateTypeAndBind(req.getDeviceType(), req.getBindElderId(), req.getBindBedId());
        assertProductBindingUnique(req.getProductId(), req.getBindElderId(), req.getBindBedId(), null);

        IotDevice e = new IotDevice();
        e.setDeviceName(req.getDeviceName().trim());
        e.setRemarkName(req.getRemarkName().trim());
        e.setProductId(req.getProductId());
        e.setDeviceType(req.getDeviceType().trim());
        applyBind(e, req.getDeviceType(), req.getBindElderId(), req.getBindBedId());
        e.setProductKey("");
        e.setDeviceKey("");
        e.setDeviceSecret("");
        e.setRegion("华东2 (上海)");
        e.setNodeType("设备");
        e.setAuthMethod("设备密钥");
        e.setIpAddress("");
        e.setFirmwareVersion("");
        e.setCreatorName("管理员");
        e.setActivationTime(LocalDateTime.now());
        e.setProtocol("MQTT");
        e.setOnlineStatus(0);
        e.setIsDeleted(0);
        mapper.insert(e);
        return e.getId();
    }

    @Transactional
    public void update(Long id, IotDeviceUpdateRequest req) {
        IotDevice e = getActive(id);
        validateTypeAndBind(req.getDeviceType(), req.getBindElderId(), req.getBindBedId());
        assertProductBindingUnique(e.getProductId(), req.getBindElderId(), req.getBindBedId(), id);

        e.setRemarkName(req.getRemarkName().trim());
        e.setDeviceType(req.getDeviceType().trim());
        applyBind(e, req.getDeviceType(), req.getBindElderId(), req.getBindBedId());
        mapper.updateById(e);
    }

    @Transactional
    public void remove(Long id) {
        IotDevice e = getActive(id);
        e.setIsDeleted(1);
        e.setBindElderId(null);
        e.setBindBedId(null);
        mapper.updateById(e);
    }

    public List<IotDeviceOptionVO> listOptions() {
        return mapper.selectList(new LambdaQueryWrapper<IotDevice>()
                        .eq(IotDevice::getIsDeleted, 0)
                        .orderByAsc(IotDevice::getId))
                .stream().map(d -> {
                    IotDeviceOptionVO o = new IotDeviceOptionVO();
                    o.setId(d.getId());
                    o.setProductId(d.getProductId());
                    o.setDeviceName(d.getDeviceName());
                    return o;
                }).collect(Collectors.toList());
    }

    private void applyBind(IotDevice e, String deviceType, Long bindElderId, Long bindBedId) {
        if (TYPE_PORTABLE.equals(deviceType)) {
            e.setBindElderId(bindElderId);
            e.setBindBedId(null);
            CustomerProfile c = customerMapper.selectOne(new LambdaQueryWrapper<CustomerProfile>()
                    .eq(CustomerProfile::getId, bindElderId)
                    .eq(CustomerProfile::getIsDeleted, 0));
            if (c == null) {
                throw new IllegalArgumentException("老人不存在");
            }
            e.setAccessLocationDisplay(c.getCustomerNickname());
        } else if (TYPE_FIXED.equals(deviceType)) {
            e.setBindElderId(null);
            e.setBindBedId(bindBedId);
            e.setAccessLocationDisplay(buildBedLocationDisplay(bindBedId));
        } else {
            throw new IllegalArgumentException("设备类型无效");
        }
    }

    private String buildBedLocationDisplay(Long bedId) {
        PreviewBed bed = bedMapper.selectOne(new LambdaQueryWrapper<PreviewBed>()
                .eq(PreviewBed::getId, bedId)
                .eq(PreviewBed::getIsDeleted, 0));
        if (bed == null) {
            throw new IllegalArgumentException("床位不存在");
        }
        PreviewRoom room = roomMapper.selectOne(new LambdaQueryWrapper<PreviewRoom>()
                .eq(PreviewRoom::getId, bed.getRoomId())
                .eq(PreviewRoom::getIsDeleted, 0));
        if (room == null) {
            throw new IllegalArgumentException("房间不存在");
        }
        PreviewFloor floor = floorMapper.selectOne(new LambdaQueryWrapper<PreviewFloor>()
                .eq(PreviewFloor::getId, room.getFloorId())
                .eq(PreviewFloor::getIsDeleted, 0));
        if (floor == null) {
            throw new IllegalArgumentException("楼层不存在");
        }
        return floor.getFloorName() + "/" + room.getRoomNo() + "/" + bed.getBedName() + "床位";
    }

    private void validateTypeAndBind(String deviceType, Long bindElderId, Long bindBedId) {
        if (TYPE_PORTABLE.equals(deviceType)) {
            if (bindElderId == null) {
                throw new IllegalArgumentException("请选择接入位置（老人）");
            }
            if (bindBedId != null) {
                throw new IllegalArgumentException("随身设备不能绑定床位");
            }
        } else if (TYPE_FIXED.equals(deviceType)) {
            if (bindBedId == null) {
                throw new IllegalArgumentException("请选择接入位置（楼层-房间-床位）");
            }
            if (bindElderId != null) {
                throw new IllegalArgumentException("固定设备不能绑定老人");
            }
        } else {
            throw new IllegalArgumentException("设备类型无效");
        }
    }

    private void assertProductBindingUnique(Long productId, Long bindElderId, Long bindBedId, Long excludeDeviceId) {
        if (bindElderId != null) {
            LambdaQueryWrapper<IotDevice> q = new LambdaQueryWrapper<IotDevice>()
                    .eq(IotDevice::getIsDeleted, 0)
                    .eq(IotDevice::getProductId, productId)
                    .eq(IotDevice::getBindElderId, bindElderId);
            if (excludeDeviceId != null) {
                q.ne(IotDevice::getId, excludeDeviceId);
            }
            if (mapper.selectCount(q) > 0) {
                throw new IllegalArgumentException("该老人/位置已绑定该产品，请重新选择");
            }
        }
        if (bindBedId != null) {
            LambdaQueryWrapper<IotDevice> q = new LambdaQueryWrapper<IotDevice>()
                    .eq(IotDevice::getIsDeleted, 0)
                    .eq(IotDevice::getProductId, productId)
                    .eq(IotDevice::getBindBedId, bindBedId);
            if (excludeDeviceId != null) {
                q.ne(IotDevice::getId, excludeDeviceId);
            }
            if (mapper.selectCount(q) > 0) {
                throw new IllegalArgumentException("该老人/位置已绑定该产品，请重新选择");
            }
        }
    }

    private void assertDeviceNameUnique(String deviceName, Long excludeId) {
        LambdaQueryWrapper<IotDevice> q = new LambdaQueryWrapper<IotDevice>()
                .eq(IotDevice::getIsDeleted, 0)
                .eq(IotDevice::getDeviceName, deviceName);
        if (excludeId != null) {
            q.ne(IotDevice::getId, excludeId);
        }
        if (mapper.selectCount(q) > 0) {
            throw new IllegalArgumentException("设备名称已存在，请重新输入");
        }
    }

    private IotDevice getActive(Long id) {
        IotDevice e = mapper.selectOne(new LambdaQueryWrapper<IotDevice>()
                .eq(IotDevice::getId, id)
                .eq(IotDevice::getIsDeleted, 0));
        if (e == null) {
            throw new IllegalArgumentException("设备不存在");
        }
        return e;
    }

    private Map<Long, String> loadProductNames(List<IotDevice> devices) {
        List<Long> ids = devices.stream().map(IotDevice::getProductId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (ids.isEmpty()) {
            return Map.of();
        }
        return productMapper.selectList(new LambdaQueryWrapper<IotProduct>().in(IotProduct::getId, ids))
                .stream().collect(Collectors.toMap(IotProduct::getId, IotProduct::getProductName, (a, b) -> a));
    }

    private IotDeviceVO toListVO(IotDevice e, String productName) {
        IotDeviceVO vo = new IotDeviceVO();
        vo.setId(e.getId());
        vo.setDeviceName(e.getDeviceName());
        vo.setRemarkName(e.getRemarkName());
        vo.setProductName(productName == null ? "" : productName);
        vo.setAccessLocationDisplay(e.getAccessLocationDisplay());
        vo.setDeviceType(e.getDeviceType());
        vo.setCreatedTime(e.getCreatedTime() == null ? "" : FMT.format(e.getCreatedTime()));
        return vo;
    }
}
