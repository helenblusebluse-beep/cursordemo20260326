import React, { useState } from 'react';
import { X, User, Bell, Palette, Shield, Globe, Moon, Volume2, Lock, Eye } from 'lucide-react';

interface SettingsProps {
  onClose: () => void;
  username: string;
}

export function Settings({ onClose, username }: SettingsProps) {
  const [activeSection, setActiveSection] = useState<'account' | 'notification' | 'appearance' | 'privacy'>('account');
  
  // 设置状态
  const [settings, setSettings] = useState({
    // 账户设置
    displayName: username,
    email: 'user@example.com',
    
    // 通知设置
    emailNotifications: true,
    desktopNotifications: false,
    soundEnabled: true,
    newMessageAlert: true,
    systemUpdates: false,
    
    // 外观设置
    theme: 'light',
    fontSize: 'medium',
    language: 'zh-CN',
    
    // 隐私设置
    showOnlineStatus: true,
    allowDataCollection: false,
    shareUsageData: false,
  });

  const menuItems = [
    { id: 'account', label: '账户设置', icon: User },
    { id: 'notification', label: '通知设置', icon: Bell },
    { id: 'appearance', label: '外观设置', icon: Palette },
    { id: 'privacy', label: '隐私与安全', icon: Shield },
  ];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/30 backdrop-blur-sm">
      <div className="w-[900px] h-[700px] bg-white rounded-[10px] shadow-2xl flex overflow-hidden">
        {/* 左侧导航 */}
        <div className="w-[240px] bg-[#F5F7FB] border-r border-gray-200 p-4">
          <div className="mb-6">
            <h2 className="text-lg font-semibold text-[#1C1C1E] px-3">设置</h2>
          </div>
          <nav className="space-y-1">
            {menuItems.map((item) => {
              const Icon = item.icon;
              return (
                <button
                  key={item.id}
                  onClick={() => setActiveSection(item.id as any)}
                  className={`w-full flex items-center gap-3 px-3 py-3 rounded-[10px] transition-colors ${
                    activeSection === item.id
                      ? 'bg-[#007AFF] text-white'
                      : 'text-gray-700 hover:bg-gray-200'
                  }`}
                >
                  <Icon className="w-5 h-5" />
                  <span>{item.label}</span>
                </button>
              );
            })}
          </nav>
        </div>

        {/* 右侧内容 */}
        <div className="flex-1 flex flex-col">
          {/* 头部 */}
          <div className="h-[70px] flex items-center justify-between px-6 border-b border-gray-200">
            <h3 className="text-xl font-semibold text-[#1C1C1E]">
              {menuItems.find((item) => item.id === activeSection)?.label}
            </h3>
            <button
              onClick={onClose}
              className="w-9 h-9 rounded-full bg-gray-100 hover:bg-gray-200 flex items-center justify-center transition-colors"
            >
              <X className="w-5 h-5 text-gray-600" />
            </button>
          </div>

          {/* 内容区 */}
          <div className="flex-1 overflow-y-auto p-6">
            {/* 账户设置 */}
            {activeSection === 'account' && (
              <div className="space-y-6">
                <div className="bg-[#F5F7FB] rounded-[10px] p-6">
                  <h4 className="font-semibold text-[#1C1C1E] mb-4 flex items-center gap-2">
                    <User className="w-5 h-5 text-[#007AFF]" />
                    基本信息
                  </h4>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        用户名
                      </label>
                      <input
                        type="text"
                        value={settings.displayName}
                        onChange={(e) =>
                          setSettings({ ...settings, displayName: e.target.value })
                        }
                        className="w-full h-10 px-3 bg-white border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                      />
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        邮箱
                      </label>
                      <input
                        type="email"
                        value={settings.email}
                        onChange={(e) =>
                          setSettings({ ...settings, email: e.target.value })
                        }
                        className="w-full h-10 px-3 bg-white border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                      />
                    </div>
                  </div>
                </div>

                <div className="bg-[#F5F7FB] rounded-[10px] p-6">
                  <h4 className="font-semibold text-[#1C1C1E] mb-4 flex items-center gap-2">
                    <Lock className="w-5 h-5 text-[#007AFF]" />
                    密码与安全
                  </h4>
                  <button className="w-full h-10 bg-white border border-gray-300 rounded-[10px] hover:bg-gray-50 transition-colors text-[#1C1C1E] font-medium">
                    修改密码
                  </button>
                </div>
              </div>
            )}

            {/* 通知设置 */}
            {activeSection === 'notification' && (
              <div className="space-y-6">
                <div className="bg-[#F5F7FB] rounded-[10px] p-6">
                  <h4 className="font-semibold text-[#1C1C1E] mb-4 flex items-center gap-2">
                    <Bell className="w-5 h-5 text-[#007AFF]" />
                    通知偏好
                  </h4>
                  <div className="space-y-4">
                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">邮件通知</div>
                        <div className="text-sm text-gray-500">接收重要更新的邮件通知</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.emailNotifications}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              emailNotifications: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>

                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">桌面通知</div>
                        <div className="text-sm text-gray-500">在桌面显示消息通知</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.desktopNotifications}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              desktopNotifications: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>

                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">声音提示</div>
                        <div className="text-sm text-gray-500">收到消息时播放提示音</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.soundEnabled}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              soundEnabled: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>

                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">新消息提醒</div>
                        <div className="text-sm text-gray-500">收到新消息时显示提醒</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.newMessageAlert}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              newMessageAlert: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>

                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">系统更新</div>
                        <div className="text-sm text-gray-500">接收系统更新和维护通知</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.systemUpdates}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              systemUpdates: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {/* 外观设置 */}
            {activeSection === 'appearance' && (
              <div className="space-y-6">
                <div className="bg-[#F5F7FB] rounded-[10px] p-6">
                  <h4 className="font-semibold text-[#1C1C1E] mb-4 flex items-center gap-2">
                    <Palette className="w-5 h-5 text-[#007AFF]" />
                    主题设置
                  </h4>
                  <div className="space-y-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-3">
                        主题模式
                      </label>
                      <div className="grid grid-cols-3 gap-3">
                        <button
                          onClick={() => setSettings({ ...settings, theme: 'light' })}
                          className={`h-24 rounded-[10px] border-2 flex flex-col items-center justify-center gap-2 transition-all ${
                            settings.theme === 'light'
                              ? 'border-[#007AFF] bg-blue-50'
                              : 'border-gray-300 bg-white hover:border-gray-400'
                          }`}
                        >
                          <div className="w-10 h-10 bg-white rounded-full border-2 border-gray-300"></div>
                          <span className="text-sm font-medium">浅色</span>
                        </button>
                        <button
                          onClick={() => setSettings({ ...settings, theme: 'dark' })}
                          className={`h-24 rounded-[10px] border-2 flex flex-col items-center justify-center gap-2 transition-all ${
                            settings.theme === 'dark'
                              ? 'border-[#007AFF] bg-blue-50'
                              : 'border-gray-300 bg-white hover:border-gray-400'
                          }`}
                        >
                          <div className="w-10 h-10 bg-gray-800 rounded-full border-2 border-gray-600"></div>
                          <span className="text-sm font-medium">深色</span>
                        </button>
                        <button
                          onClick={() => setSettings({ ...settings, theme: 'auto' })}
                          className={`h-24 rounded-[10px] border-2 flex flex-col items-center justify-center gap-2 transition-all ${
                            settings.theme === 'auto'
                              ? 'border-[#007AFF] bg-blue-50'
                              : 'border-gray-300 bg-white hover:border-gray-400'
                          }`}
                        >
                          <div className="w-10 h-10 bg-gradient-to-r from-white to-gray-800 rounded-full border-2 border-gray-400"></div>
                          <span className="text-sm font-medium">自动</span>
                        </button>
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        字体大小
                      </label>
                      <select
                        value={settings.fontSize}
                        onChange={(e) =>
                          setSettings({ ...settings, fontSize: e.target.value })
                        }
                        className="w-full h-10 px-3 bg-white border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                      >
                        <option value="small">小</option>
                        <option value="medium">中（默认）</option>
                        <option value="large">大</option>
                      </select>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-2">
                        语言
                      </label>
                      <select
                        value={settings.language}
                        onChange={(e) =>
                          setSettings({ ...settings, language: e.target.value })
                        }
                        className="w-full h-10 px-3 bg-white border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                      >
                        <option value="zh-CN">简体中文</option>
                        <option value="zh-TW">繁體中文</option>
                        <option value="en-US">English</option>
                        <option value="ja-JP">日本語</option>
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            )}

            {/* 隐私与安全 */}
            {activeSection === 'privacy' && (
              <div className="space-y-6">
                <div className="bg-[#F5F7FB] rounded-[10px] p-6">
                  <h4 className="font-semibold text-[#1C1C1E] mb-4 flex items-center gap-2">
                    <Shield className="w-5 h-5 text-[#007AFF]" />
                    隐私设置
                  </h4>
                  <div className="space-y-4">
                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">显示在线状态</div>
                        <div className="text-sm text-gray-500">让其他用户看到您的在线状态</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.showOnlineStatus}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              showOnlineStatus: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>

                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">数据收集</div>
                        <div className="text-sm text-gray-500">允许收集匿名使用数据以改进服务</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.allowDataCollection}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              allowDataCollection: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>

                    <div className="flex items-center justify-between py-2">
                      <div>
                        <div className="font-medium text-[#1C1C1E]">共享使用数据</div>
                        <div className="text-sm text-gray-500">与合作伙伴共享匿名使用统计</div>
                      </div>
                      <label className="relative inline-flex items-center cursor-pointer">
                        <input
                          type="checkbox"
                          checked={settings.shareUsageData}
                          onChange={(e) =>
                            setSettings({
                              ...settings,
                              shareUsageData: e.target.checked,
                            })
                          }
                          className="sr-only peer"
                        />
                        <div className="w-11 h-6 bg-gray-300 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-[#007AFF]"></div>
                      </label>
                    </div>
                  </div>
                </div>

                <div className="bg-red-50 border border-red-200 rounded-[10px] p-6">
                  <h4 className="font-semibold text-red-700 mb-3">危险操作</h4>
                  <button className="w-full h-10 bg-white border-2 border-red-500 text-red-500 rounded-[10px] hover:bg-red-50 transition-colors font-medium">
                    清除所有聊天记录
                  </button>
                </div>
              </div>
            )}
          </div>

          {/* 底部按钮 */}
          <div className="h-[70px] flex items-center justify-end gap-3 px-6 border-t border-gray-200">
            <button
              onClick={onClose}
              className="px-5 py-2 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors"
            >
              取消
            </button>
            <button
              onClick={() => {
                alert('设置已保存');
                onClose();
              }}
              className="px-5 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors"
            >
              保存设置
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
