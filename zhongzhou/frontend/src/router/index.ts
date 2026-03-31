import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AppointmentRegisterView from '../views/AppointmentRegisterView.vue'
import VisitRegisterView from '../views/VisitRegisterView.vue'
import HealthAssessmentView from '../views/HealthAssessmentView.vue'
import HealthAssessmentDetailView from '../views/HealthAssessmentDetailView.vue'
import CheckinManageView from '../views/CheckinManageView.vue'
import CheckinApplyView from '../views/CheckinApplyView.vue'
import CheckoutManageView from '../views/CheckoutManageView.vue'
import CheckoutApplyView from '../views/CheckoutApplyView.vue'
import RoomTypeSettingsView from '../views/RoomTypeSettingsView.vue'
import BedPreviewView from '../views/BedPreviewView.vue'
import SmartBedView from '../views/SmartBedView.vue'
import ContractTrackingView from '../views/ContractTrackingView.vue'
import ContractDetailView from '../views/ContractDetailView.vue'
import CustomerManageView from '../views/CustomerManageView.vue'
import NursingItemView from '../views/NursingItemView.vue'
import NursingPlanView from '../views/NursingPlanView.vue'
import NursingCareLevelView from '../views/NursingCareLevelView.vue'
import ResponsibleElderView from '../views/ResponsibleElderView.vue'
import TaskScheduleView from '../views/TaskScheduleView.vue'
import TaskScheduleDetailView from '../views/TaskScheduleDetailView.vue'
import OrderManageView from '../views/OrderManageView.vue'
import OrderDetailView from '../views/OrderDetailView.vue'
import RefundManageView from '../views/RefundManageView.vue'
import BillDetailView from '../views/BillDetailView.vue'
import PrepayRechargeView from '../views/PrepayRechargeView.vue'
import BalanceQueryView from '../views/BalanceQueryView.vue'
import ArrearsElderView from '../views/ArrearsElderView.vue'
import IotDeviceView from '../views/IotDeviceView.vue'
import IotDeviceDetailView from '../views/IotDeviceDetailView.vue'
import IotAlarmRuleView from '../views/IotAlarmRuleView.vue'
import IotAlarmRuleFormView from '../views/IotAlarmRuleFormView.vue'
import IotAlarmRecordView from '../views/IotAlarmRecordView.vue'
import ProfileCenterView from '../views/ProfileCenterView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/appointments/register',
      name: 'appointment-register',
      component: AppointmentRegisterView,
    },
    {
      path: '/visits/register',
      name: 'visit-register',
      component: VisitRegisterView,
    },
    {
      path: '/health-assessments',
      name: 'health-assessments',
      component: HealthAssessmentView,
    },
    {
      path: '/health-assessments/:id',
      name: 'health-assessment-detail',
      component: HealthAssessmentDetailView,
    },
    {
      path: '/checkins',
      name: 'checkin-manage',
      component: CheckinManageView,
    },
    {
      path: '/checkins/apply',
      name: 'checkin-apply',
      component: CheckinApplyView,
    },
    {
      path: '/checkins/:id',
      name: 'checkin-detail',
      component: CheckinApplyView,
    },
    {
      path: '/checkouts',
      name: 'checkout-manage',
      component: CheckoutManageView,
    },
    {
      path: '/checkouts/apply',
      name: 'checkout-apply',
      component: CheckoutApplyView,
    },
    {
      path: '/checkouts/:id',
      name: 'checkout-detail',
      component: CheckoutApplyView,
    },
    {
      path: '/room-types',
      name: 'room-type-settings',
      component: RoomTypeSettingsView,
    },
    {
      path: '/bed-preview',
      name: 'bed-preview',
      component: BedPreviewView,
    },
    {
      path: '/smart-beds',
      name: 'smart-beds',
      component: SmartBedView,
    },
    {
      path: '/contracts',
      name: 'contract-tracking',
      component: ContractTrackingView,
    },
    {
      path: '/contracts/:id',
      name: 'contract-detail',
      component: ContractDetailView,
    },
    {
      path: '/customers',
      name: 'customer-manage',
      component: CustomerManageView,
    },
    {
      path: '/nursing-items',
      name: 'nursing-items',
      component: NursingItemView,
    },
    {
      path: '/nursing-plans',
      name: 'nursing-plans',
      component: NursingPlanView,
    },
    {
      path: '/nursing-care-levels',
      name: 'nursing-care-levels',
      component: NursingCareLevelView,
    },
    {
      path: '/responsible-elders',
      name: 'responsible-elders',
      component: ResponsibleElderView,
    },
    {
      path: '/task-schedules',
      name: 'task-schedules',
      component: TaskScheduleView,
    },
    {
      path: '/task-schedules/:id',
      name: 'task-schedule-detail',
      component: TaskScheduleDetailView,
    },
    {
      path: '/orders',
      name: 'order-manage',
      component: OrderManageView,
    },
    {
      path: '/orders/:id',
      name: 'order-detail',
      component: OrderDetailView,
    },
    {
      path: '/refunds',
      name: 'refund-manage',
      component: RefundManageView,
    },
    {
      path: '/bills/:id',
      name: 'bill-detail',
      component: BillDetailView,
    },
    {
      path: '/prepay-recharges',
      name: 'prepay-recharges',
      component: PrepayRechargeView,
    },
    {
      path: '/balance-queries',
      name: 'balance-queries',
      component: BalanceQueryView,
    },
    {
      path: '/arrears-elders',
      name: 'arrears-elders',
      component: ArrearsElderView,
    },
    {
      path: '/iot/devices',
      name: 'iot-devices',
      component: IotDeviceView,
    },
    {
      path: '/iot/devices/:id',
      name: 'iot-device-detail',
      component: IotDeviceDetailView,
    },
    {
      path: '/iot/alarm-rules',
      name: 'iot-alarm-rules',
      component: IotAlarmRuleView,
    },
    {
      path: '/iot/alarm-rules/add',
      name: 'iot-alarm-rules-add',
      component: IotAlarmRuleFormView,
    },
    {
      path: '/iot/alarm-rules/:id/edit',
      name: 'iot-alarm-rules-edit',
      component: IotAlarmRuleFormView,
    },
    {
      path: '/iot/alarm-records',
      name: 'iot-alarm-records',
      component: IotAlarmRecordView,
    },
    {
      path: '/profile',
      name: 'profile-center',
      component: ProfileCenterView,
    },
  ],
})

export default router
