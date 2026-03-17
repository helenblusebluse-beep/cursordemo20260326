import React, { useState } from 'react';
import { Users, Search, Plus, Edit2, Trash2, Ban, MessageSquare, LogOut } from 'lucide-react';

interface User {
  id: string;
  username: string;
  email: string;
  role: 'admin' | 'user';
  status: 'active' | 'disabled';
  lastLogin: string;
  registeredAt: string;
}

interface UserManagementProps {
  onLogout: () => void;
  currentUser: string;
}

export function UserManagement({ onLogout, currentUser }: UserManagementProps) {
  const [searchText, setSearchText] = useState('');
  const [showAddUserDialog, setShowAddUserDialog] = useState(false);
  const [showEditUserDialog, setShowEditUserDialog] = useState(false);
  const [editingUser, setEditingUser] = useState<User | null>(null);
  const [newUser, setNewUser] = useState({
    username: '',
    email: '',
    password: '',
    role: 'user' as 'admin' | 'user',
  });

  const [users, setUsers] = useState<User[]>([
    {
      id: '1',
      username: 'admin',
      email: 'admin@example.com',
      role: 'admin',
      status: 'active',
      lastLogin: '2026-02-15 10:30',
      registeredAt: '2025-01-01',
    },
    {
      id: '2',
      username: 'zhangsan',
      email: 'zhangsan@example.com',
      role: 'user',
      status: 'active',
      lastLogin: '2026-02-14 16:20',
      registeredAt: '2025-06-15',
    },
    {
      id: '3',
      username: 'lisi',
      email: 'lisi@example.com',
      role: 'user',
      status: 'active',
      lastLogin: '2026-02-13 09:15',
      registeredAt: '2025-08-20',
    },
  ]);

  const filteredUsers = users.filter(
    (user) =>
      user.username.toLowerCase().includes(searchText.toLowerCase()) ||
      user.email.toLowerCase().includes(searchText.toLowerCase())
  );

  const stats = {
    total: users.length,
    active: users.filter((u) => u.status === 'active').length,
    disabled: users.filter((u) => u.status === 'disabled').length,
    admins: users.filter((u) => u.role === 'admin').length,
  };

  const handleAddUser = () => {
    if (newUser.username && newUser.email && newUser.password) {
      const user: User = {
        id: String(Date.now()),
        username: newUser.username,
        email: newUser.email,
        role: newUser.role,
        status: 'active',
        lastLogin: '-',
        registeredAt: new Date().toISOString().split('T')[0],
      };
      setUsers([...users, user]);
      setNewUser({ username: '', email: '', password: '', role: 'user' });
      setShowAddUserDialog(false);
    }
  };

  const handleToggleUserStatus = (id: string) => {
    setUsers(
      users.map((u) =>
        u.id === id
          ? { ...u, status: u.status === 'active' ? 'disabled' : 'active' }
          : u
      )
    );
  };

  const handleDeleteUser = (id: string) => {
    if (window.confirm('确定要删除此用户吗？')) {
      setUsers(users.filter((u) => u.id !== id));
    }
  };

  const getAvatarColor = (username: string) => {
    const colors = [
      'bg-blue-500',
      'bg-green-500',
      'bg-purple-500',
      'bg-orange-500',
      'bg-pink-500',
    ];
    const index = username.charCodeAt(0) % colors.length;
    return colors[index];
  };

  return (
    <div className="min-h-screen bg-[#F5F7FB]">
      {/* 顶部导航栏 */}
      <div className="h-[70px] bg-white border-b border-gray-200 flex items-center justify-between px-6">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 bg-[#007AFF] rounded-[10px] flex items-center justify-center">
            <MessageSquare className="w-6 h-6 text-white" strokeWidth={2.5} />
          </div>
          <div>
            <h1 className="text-xl font-semibold text-[#1C1C1E]">知识库 AI</h1>
            <span className="text-sm text-gray-500">管理后台</span>
          </div>
        </div>
        <div className="flex items-center gap-4">
          <span className="text-gray-600">
            欢迎，<span className="font-medium">{currentUser}</span>
          </span>
          <button
            onClick={onLogout}
            className="flex items-center gap-2 px-4 py-2 text-gray-600 hover:text-[#007AFF] hover:bg-blue-50 rounded-[10px] transition-colors"
          >
            <LogOut className="w-4 h-4" />
            退出登录
          </button>
        </div>
      </div>

      {/* 主内容区 */}
      <div className="p-6">
        <div className="max-w-7xl mx-auto">
          {/* 页面标题 */}
          <div className="flex items-center gap-2 mb-6">
            <Users className="w-7 h-7 text-[#007AFF]" />
            <h2 className="text-2xl font-semibold text-[#1C1C1E]">用户管理</h2>
          </div>

          {/* 统计卡片 */}
          <div className="grid grid-cols-4 gap-4 mb-6">
            <div className="bg-white rounded-[10px] border border-gray-200 p-5">
              <div className="text-sm text-gray-500 mb-2">总用户数</div>
              <div className="text-3xl font-bold text-[#1C1C1E]">{stats.total}</div>
            </div>
            <div className="bg-white rounded-[10px] border border-gray-200 p-5">
              <div className="text-sm text-gray-500 mb-2">活跃用户</div>
              <div className="text-3xl font-bold text-[#007AFF]">{stats.active}</div>
            </div>
            <div className="bg-white rounded-[10px] border border-gray-200 p-5">
              <div className="text-sm text-gray-500 mb-2">停用用户</div>
              <div className="text-3xl font-bold text-red-500">{stats.disabled}</div>
            </div>
            <div className="bg-white rounded-[10px] border border-gray-200 p-5">
              <div className="text-sm text-gray-500 mb-2">管理员</div>
              <div className="text-3xl font-bold text-[#1C1C1E]">{stats.admins}</div>
            </div>
          </div>

          {/* 搜索和操作栏 */}
          <div className="bg-white rounded-[10px] border border-gray-200 p-4 mb-4">
            <div className="flex items-center gap-3">
              <div className="flex-1 relative">
                <input
                  type="text"
                  value={searchText}
                  onChange={(e) => setSearchText(e.target.value)}
                  placeholder="搜索用户名或邮箱..."
                  className="w-full h-10 pl-10 pr-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
                <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
              </div>
              <button
                onClick={() => setShowAddUserDialog(true)}
                className="h-10 px-5 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors flex items-center gap-2"
              >
                <Plus className="w-4 h-4" />
                添加用户
              </button>
            </div>
          </div>

          {/* 用户列表 */}
          <div className="bg-white rounded-[10px] border border-gray-200 overflow-hidden">
            <table className="w-full">
              <thead className="bg-gray-50 border-b border-gray-200">
                <tr>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                    用户名
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                    邮箱
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                    角色
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                    状态
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                    最后登录
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                    注册时间
                  </th>
                  <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                    操作
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredUsers.map((user, index) => (
                  <tr
                    key={user.id}
                    className={`border-b border-gray-200 hover:bg-gray-50 transition-colors ${
                      index === filteredUsers.length - 1 ? 'border-b-0' : ''
                    }`}
                  >
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-3">
                        <div
                          className={`w-10 h-10 rounded-full flex items-center justify-center text-white font-semibold ${getAvatarColor(
                            user.username
                          )}`}
                        >
                          {user.username.charAt(0).toUpperCase()}
                        </div>
                        <span className="font-medium text-[#1C1C1E]">
                          {user.username}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-gray-600">{user.email}</td>
                    <td className="px-6 py-4">
                      <span
                        className={`px-3 py-1 rounded-full text-sm ${
                          user.role === 'admin'
                            ? 'bg-blue-100 text-blue-700'
                            : 'bg-gray-100 text-gray-700'
                        }`}
                      >
                        {user.role === 'admin' ? '管理员' : '普通用户'}
                      </span>
                    </td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <div
                          className={`w-2 h-2 rounded-full ${
                            user.status === 'active' ? 'bg-green-500' : 'bg-red-500'
                          }`}
                        />
                        <span
                          className={
                            user.status === 'active'
                              ? 'text-green-600'
                              : 'text-red-600'
                          }
                        >
                          {user.status === 'active' ? '活跃' : '停用'}
                        </span>
                      </div>
                    </td>
                    <td className="px-6 py-4 text-gray-600">{user.lastLogin}</td>
                    <td className="px-6 py-4 text-gray-600">{user.registeredAt}</td>
                    <td className="px-6 py-4">
                      <div className="flex items-center gap-2">
                        <button
                          onClick={() => handleToggleUserStatus(user.id)}
                          className="w-8 h-8 rounded-[10px] flex items-center justify-center text-gray-400 hover:text-orange-500 hover:bg-orange-50 transition-colors"
                          title={user.status === 'active' ? '停用' : '启用'}
                        >
                          <Ban className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => {
                            setEditingUser(user);
                            setShowEditUserDialog(true);
                          }}
                          className="w-8 h-8 rounded-[10px] flex items-center justify-center text-gray-400 hover:text-[#007AFF] hover:bg-blue-50 transition-colors"
                        >
                          <Edit2 className="w-4 h-4" />
                        </button>
                        <button
                          onClick={() => handleDeleteUser(user.id)}
                          className="w-8 h-8 rounded-[10px] flex items-center justify-center text-gray-400 hover:text-red-500 hover:bg-red-50 transition-colors"
                        >
                          <Trash2 className="w-4 h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* 添加用户弹窗 */}
      {showAddUserDialog && (
        <div className="fixed inset-0 bg-black/30 backdrop-blur-sm z-50 flex items-center justify-center p-6">
          <div className="w-[500px] bg-white rounded-[10px] shadow-2xl">
            <div className="h-[60px] flex items-center justify-between px-6 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-[#1C1C1E]">添加用户</h3>
              <button
                onClick={() => setShowAddUserDialog(false)}
                className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center hover:bg-gray-300 transition-colors"
              >
                ✕
              </button>
            </div>

            <div className="p-6 space-y-4">
              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  用户名
                </label>
                <input
                  type="text"
                  value={newUser.username}
                  onChange={(e) =>
                    setNewUser({ ...newUser, username: e.target.value })
                  }
                  placeholder="请输入用户名"
                  className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  邮箱
                </label>
                <input
                  type="email"
                  value={newUser.email}
                  onChange={(e) =>
                    setNewUser({ ...newUser, email: e.target.value })
                  }
                  placeholder="请输入邮箱"
                  className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  密码
                </label>
                <input
                  type="password"
                  value={newUser.password}
                  onChange={(e) =>
                    setNewUser({ ...newUser, password: e.target.value })
                  }
                  placeholder="请输入密码"
                  className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  角色
                </label>
                <select
                  value={newUser.role}
                  onChange={(e) =>
                    setNewUser({
                      ...newUser,
                      role: e.target.value as 'admin' | 'user',
                    })
                  }
                  className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                >
                  <option value="user">普通用户</option>
                  <option value="admin">管理员</option>
                </select>
              </div>
            </div>

            <div className="px-6 py-4 border-t border-gray-200 flex items-center justify-end gap-3">
              <button
                onClick={() => setShowAddUserDialog(false)}
                className="px-5 py-2 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors"
              >
                取消
              </button>
              <button
                onClick={handleAddUser}
                disabled={
                  !newUser.username || !newUser.email || !newUser.password
                }
                className="px-5 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                添加
              </button>
            </div>
          </div>
        </div>
      )}

      {/* 编辑用户弹窗 */}
      {showEditUserDialog && editingUser && (
        <div className="fixed inset-0 bg-black/30 backdrop-blur-sm z-50 flex items-center justify-center p-6">
          <div className="w-[500px] bg-white rounded-[10px] shadow-2xl">
            <div className="h-[60px] flex items-center justify-between px-6 border-b border-gray-200">
              <h3 className="text-lg font-semibold text-[#1C1C1E]">编辑用户</h3>
              <button
                onClick={() => setShowEditUserDialog(false)}
                className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center hover:bg-gray-300 transition-colors"
              >
                ✕
              </button>
            </div>

            <div className="p-6 space-y-4">
              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  用户名
                </label>
                <input
                  type="text"
                  value={editingUser.username}
                  onChange={(e) =>
                    setEditingUser({ ...editingUser, username: e.target.value })
                  }
                  placeholder="请输入用户名"
                  className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  邮箱
                </label>
                <input
                  type="email"
                  value={editingUser.email}
                  onChange={(e) =>
                    setEditingUser({ ...editingUser, email: e.target.value })
                  }
                  placeholder="请输入邮箱"
                  className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-[#1C1C1E] mb-2">
                  角色
                </label>
                <select
                  value={editingUser.role}
                  onChange={(e) =>
                    setEditingUser({
                      ...editingUser,
                      role: e.target.value as 'admin' | 'user',
                    })
                  }
                  className="w-full h-10 px-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                >
                  <option value="user">普通用户</option>
                  <option value="admin">管理员</option>
                </select>
              </div>
            </div>

            <div className="px-6 py-4 border-t border-gray-200 flex items-center justify-end gap-3">
              <button
                onClick={() => setShowEditUserDialog(false)}
                className="px-5 py-2 bg-gray-100 text-gray-700 rounded-[10px] hover:bg-gray-200 transition-colors"
              >
                取消
              </button>
              <button
                onClick={() => {
                  setUsers(
                    users.map((u) =>
                      u.id === editingUser.id ? editingUser : u
                    )
                  );
                  setShowEditUserDialog(false);
                }}
                className="px-5 py-2 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors"
              >
                保存
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}