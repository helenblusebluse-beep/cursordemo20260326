# 折扣码设计文档与落地计划

## 概述

为结账流程添加折扣码支持。用户在结账页面输入折扣码，系统对折扣码进行校验，然后在税前将折扣应用到订单总额，并在订单与支付流程中保持金额一致与可追踪。

## 实现方案

- 添加 `DiscountService`，并实现 `applyDiscount()` 与校验逻辑：
  - 过期时间校验
  - 使用次数限制校验
  - 状态/启用标志校验
  - 最低订单金额等业务规则校验
- 修改 `PricingService.calculateTotal()`，以接受可选的折扣参数（例如 `discountCode` 或 `discountContext`）：
  - 先计算商品小计与运费
  - 在税前调用 `DiscountService.applyDiscount()` 计算折扣金额与折后金额
  - 在折后金额基础上计算税费，得到最终应付金额
- 在前端 `OrderSummary` 组件中添加折扣码输入框与交互逻辑：
  - 用户输入折扣码并点击“验证”
  - 调用 `/api/discount/validate` 接口校验折扣码
  - 根据返回结果更新价格展示与错误提示
- 添加 `/api/discount/validate` 接口：
  - 接收折扣码与当前订单金额/明细
  - 内部调用 `DiscountService` 执行校验与折扣试算
  - 返回折扣有效性、折扣金额、折后金额与提示文案
  - 实际创建订单与最终金额计算时，仍在 `PricingService.calculateTotal()` 内部重新校验折扣，避免前端篡改

## 任务

- 创建 `discount_codes` 表的迁移脚本：
  - 定义字段：`code`, `type`, `amount`, `max_uses`, `used_count`, `expires_at`, `min_order_amount`, `status`, `created_at`, `updated_at` 等
  - 支持不同折扣类型（百分比折扣、固定金额折扣等）
- 创建带有 `applyDiscount()` 的 `DiscountService`：
  - 输入：订单金额（及可选订单明细）、折扣码字符串
  - 输出：折扣金额、折后金额、状态码与说明
  - 包含过期时间、使用次数、状态、最低消费等校验逻辑
- 在 `PricingService.calculateTotal()` 中添加折扣处理步骤：
  - 增加可选折扣参数
  - 在税前调用 `DiscountService.applyDiscount()`，并在返回结果基础上继续计算税费
  - 确保订单持久化与支付金额使用同一套折扣后金额
- 在 `OrderSummary` 组件中添加折扣码输入 UI：
  - 输入框 + 验证按钮 + 折扣信息展示区域
  - 调用 `/api/discount/validate` 接口并根据响应更新界面
- 添加 `/api/discount/validate` 路由：
  - 后端控制器接收请求并委托 `DiscountService` 执行校验
  - 返回统一格式的 JSON 响应供前端展示与计算
- 为所有折扣类型和边界情况编写测试：
  - 服务层：折扣码不存在、已过期、超出使用次数、未满足最低金额、百分比/固定金额折扣等
  - API 层：`/api/discount/validate` 正常与异常场景
  - 前端：`OrderSummary` 组件中折扣码输入与校验交互

