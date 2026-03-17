import React, { useState } from 'react';
import { User, MessageCircleQuestion, Settings, Search, Edit2, Trash2, X, ChevronDown, ChevronUp } from 'lucide-react';
import { PromptEditor } from './PromptEditor';
import { PromptManager } from './PromptManager';
import { PromptEditDialog } from './PromptEditDialog';

interface Prompt {
  id: string;
  title: string;
  content: string;
  group: string;
  createdAt?: string;
  isFavorite?: boolean;
}

interface QA {
  id: string;
  question: string;
  answer: string;
}

export function RightPanel() {
  const [activePanel, setActivePanel] = useState<'none' | 'prompts' | 'qa'>('none');
  const [hoveredPromptId, setHoveredPromptId] = useState<string | null>(null);
  const [selectedGroup, setSelectedGroup] = useState<string>('全部');
  const [expandedQAId, setExpandedQAId] = useState<string | null>(null);
  const [showPromptEditor, setShowPromptEditor] = useState(false);
  const [showPromptManager, setShowPromptManager] = useState(false);
  const [showPromptEditDialog, setShowPromptEditDialog] = useState(false);
  const [editingPrompt, setEditingPrompt] = useState<Prompt | null>(null);

  const [groups, setGroups] = useState(['全部', '工作', '学习', '生活']);
  const [prompts, setPrompts] = useState<Prompt[]>([
    { id: '1', title: '代码审查助手', content: '帮我审查以下代码...', group: '工作', createdAt: '2026-02-10', isFavorite: false },
    { id: '2', title: '周报生成器', content: '根据本周工作内容生成周报...', group: '工作', createdAt: '2026-02-12', isFavorite: true },
    { id: '3', title: '学习计划制定', content: '帮我制定一个学习计划...', group: '学习', createdAt: '2026-02-14', isFavorite: false },
    { id: '4', title: '英语翻译', content: '请将以下内容翻译成英语...', group: '学习', createdAt: '2026-02-15', isFavorite: false },
  ]);

  const [qaList] = useState<QA[]>([
    { id: '1', question: '如何开始使用AI助手？', answer: '首先点击"新建对话"按钮，然后在输入框中输入您的问题，AI助手会为您提供详细的回答。' },
    { id: '2', question: '支持哪些功能？', answer: '支持多轮对话、提示词管理、对话历史记录、消息复制、朗读、收藏和下载等功能。' },
    { id: '3', question: '如何保存对话记录？', answer: '所有对话会自动保存在左侧边栏，您可以随时查看历史对话，也可以下载对话内容。' },
  ]);

  const filteredPrompts = selectedGroup === '全部'
    ? prompts
    : prompts.filter(p => p.group === selectedGroup);

  const handleDeletePrompt = (id: string) => {
    setPrompts(prompts.filter(p => p.id !== id));
  };

  const handleSavePrompt = (title: string, content: string, group: string) => {
    const newPrompt: Prompt = {
      id: String(Date.now()),
      title,
      content,
      group,
      createdAt: new Date().toISOString().split('T')[0],
      isFavorite: false,
    };
    setPrompts([newPrompt, ...prompts]);
  };

  const handleAddGroup = (groupName: string) => {
    if (!groups.includes(groupName)) {
      setGroups([...groups, groupName]);
    }
  };

  const handleDeleteGroup = (groupName: string) => {
    setGroups(groups.filter(g => g !== groupName));
    // 如果删除的分组有提示词，将它们移动到"工作"分组
    setPrompts(prompts.map(p => p.group === groupName ? { ...p, group: '工作' } : p));
  };

  const handleToggleFavorite = (id: string) => {
    setPrompts(prompts.map(p => 
      p.id === id ? { ...p, isFavorite: !p.isFavorite } : p
    ));
  };

  const handleEditPrompt = (prompt: Prompt) => {
    setEditingPrompt(prompt);
    setShowPromptEditDialog(true);
  };

  const handleUpdatePrompt = (id: string | null, title: string, content: string, group: string) => {
    if (id) {
      // 编辑现有提示词
      setPrompts(prompts.map(p => 
        p.id === id ? { ...p, title, content, group } : p
      ));
    } else {
      // 创建新提示词
      const newPrompt: Prompt = {
        id: String(Date.now()),
        title,
        content,
        group,
        createdAt: new Date().toISOString().split('T')[0],
        isFavorite: false,
      };
      setPrompts([newPrompt, ...prompts]);
    }
  };

  return (
    <>
      {/* 提示词管理界面 */}
      {showPromptManager && (
        <PromptManager
          onClose={() => setShowPromptManager(false)}
          prompts={prompts.map(p => ({
            ...p,
            createdAt: p.createdAt || '2026-02-17',
            isFavorite: p.isFavorite || false
          }))}
          groups={['全部提示词', ...groups.filter(g => g !== '全部')]}
          onSavePrompt={handleSavePrompt}
          onDeletePrompt={handleDeletePrompt}
          onToggleFavorite={handleToggleFavorite}
          onAddGroup={handleAddGroup}
          onDeleteGroup={handleDeleteGroup}
        />
      )}

      {/* 提示词编辑器全屏界面 */}
      {showPromptEditor && (
        <PromptEditor
          onClose={() => setShowPromptEditor(false)}
          onSave={handleSavePrompt}
          existingGroups={groups.filter(g => g !== '全部')}
          onAddGroup={handleAddGroup}
          onDeleteGroup={handleDeleteGroup}
        />
      )}

      {/* 提示词编辑对话框 */}
      {showPromptEditDialog && editingPrompt && (
        <PromptEditDialog
          onClose={() => setShowPromptEditDialog(false)}
          prompt={editingPrompt}
          onSave={handleUpdatePrompt}
          existingGroups={groups.filter(g => g !== '全部')}
          onAddGroup={handleAddGroup}
          onDeleteGroup={handleDeleteGroup}
        />
      )}

      {/* 固定小浮窗 */}
      <div className="fixed right-6 top-1/2 -translate-y-1/2 w-[60px] h-[120px] bg-white/80 backdrop-blur-sm rounded-[10px] shadow-lg border border-gray-200 flex flex-col items-center justify-center gap-2 z-40">
        <button
          onClick={() => setActivePanel(activePanel === 'prompts' ? 'none' : 'prompts')}
          className={`w-10 h-10 rounded-[10px] flex items-center justify-center transition-colors ${
            activePanel === 'prompts' ? 'bg-[#007AFF] text-white' : 'hover:bg-gray-100 text-gray-600'
          }`}
        >
          <User className="w-5 h-5" />
        </button>
        <button
          onClick={() => setActivePanel(activePanel === 'qa' ? 'none' : 'qa')}
          className={`w-10 h-10 rounded-[10px] flex items-center justify-center transition-colors ${
            activePanel === 'qa' ? 'bg-[#007AFF] text-white' : 'hover:bg-gray-100 text-gray-600'
          }`}
        >
          <MessageCircleQuestion className="w-5 h-5" />
        </button>
      </div>

      {/* 我的提示词侧栏 */}
      {activePanel === 'prompts' && (
        <div className="fixed right-0 top-0 w-[320px] h-full bg-white shadow-xl border-l border-gray-200 z-50 flex flex-col animate-in slide-in-from-right duration-300">
          {/* 标题栏 */}
          <div className="h-[70px] flex items-center justify-between px-4 border-b border-gray-200">
            <h2 className="text-lg font-semibold text-[#1C1C1E]">我的提示词</h2>
            <button
              onClick={() => setActivePanel('none')}
              className="w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center hover:bg-gray-300 transition-colors"
            >
              <X className="w-5 h-5 text-gray-600" />
            </button>
          </div>

          <div className="p-4 border-b border-gray-200">
            <div className="flex items-center gap-2 mb-4">
              <div className="flex-1 relative">
                <input
                  type="text"
                  placeholder="搜索提示词..."
                  className="w-full h-10 pl-10 pr-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
                <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
              </div>
              <button
                onClick={() => setShowPromptManager(true)}
                className="h-10 px-4 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors flex items-center gap-1"
              >
                <Settings className="w-4 h-4" />
                管理
              </button>
            </div>

            {/* 分组按钮 */}
            <div className="flex items-center gap-2 flex-wrap">
              {groups.map((group) => (
                <button
                  key={group}
                  onClick={() => setSelectedGroup(group)}
                  className={`px-3 py-1.5 rounded-[10px] text-sm transition-colors ${
                    selectedGroup === group
                      ? 'bg-[#007AFF] text-white'
                      : 'bg-gray-100 text-gray-600 hover:bg-gray-200'
                  }`}
                >
                  {group}
                </button>
              ))}
            </div>
          </div>

          {/* 提示词列表 */}
          <div className="flex-1 overflow-y-auto p-4 space-y-3">
            {filteredPrompts.map((prompt) => (
              <div
                key={prompt.id}
                className="p-4 bg-white border border-gray-200 rounded-[10px] hover:shadow-md transition-shadow cursor-pointer"
                onMouseEnter={() => setHoveredPromptId(prompt.id)}
                onMouseLeave={() => setHoveredPromptId(null)}
              >
                <div className="flex items-start justify-between mb-2">
                  <h4 className="font-medium text-[#1C1C1E]">{prompt.title}</h4>
                  {hoveredPromptId === prompt.id && (
                    <div className="flex items-center gap-2">
                      <button
                        className="text-gray-500 hover:text-[#007AFF]"
                        onClick={() => handleEditPrompt(prompt)}
                      >
                        <Edit2 className="w-4 h-4" />
                      </button>
                      <button
                        className="text-gray-500 hover:text-red-500"
                        onClick={() => handleDeletePrompt(prompt.id)}
                      >
                        <Trash2 className="w-4 h-4" />
                      </button>
                    </div>
                  )}
                </div>
                <p className="text-sm text-gray-600 line-clamp-2">{prompt.content}</p>
                <span className="inline-block mt-2 px-2 py-0.5 bg-gray-100 text-xs text-gray-600 rounded">
                  {prompt.group}
                </span>
              </div>
            ))}
          </div>
        </div>
      )}

      {/* 问答弹窗 */}
      {activePanel === 'qa' && (
        <div className="fixed inset-0 bg-black/30 backdrop-blur-sm z-50 flex items-center justify-center p-6">
          <div className="w-[60%] max-w-3xl bg-white rounded-[10px] shadow-2xl max-h-[80vh] flex flex-col relative">
            {/* 关闭按钮 */}
            <button
              onClick={() => setActivePanel('none')}
              className="absolute top-4 right-4 w-8 h-8 bg-gray-200 rounded-full flex items-center justify-center hover:bg-gray-300 transition-colors z-10"
            >
              <X className="w-5 h-5 text-gray-600" />
            </button>

            <div className="p-6 border-b border-gray-200">
              <h2 className="text-xl font-semibold text-[#1C1C1E] mb-4">常见问题</h2>
              <div className="relative">
                <input
                  type="text"
                  placeholder="搜索问题..."
                  className="w-full h-10 pl-10 pr-3 border border-gray-300 rounded-[10px] focus:outline-none focus:border-[#007AFF]"
                />
                <Search className="w-4 h-4 text-gray-400 absolute left-3 top-1/2 -translate-y-1/2" />
              </div>
            </div>

            {/* 问答列表 */}
            <div className="flex-1 overflow-y-auto p-6 space-y-3">
              {qaList.map((qa) => (
                <div key={qa.id} className="border border-gray-200 rounded-[10px] overflow-hidden">
                  <button
                    onClick={() => setExpandedQAId(expandedQAId === qa.id ? null : qa.id)}
                    className="w-full px-4 py-3 flex items-center justify-between hover:bg-gray-50 transition-colors"
                  >
                    <span className="font-medium text-[#1C1C1E] text-left">{qa.question}</span>
                    {expandedQAId === qa.id ? (
                      <ChevronUp className="w-5 h-5 text-gray-500 flex-shrink-0" />
                    ) : (
                      <ChevronDown className="w-5 h-5 text-gray-500 flex-shrink-0" />
                    )}
                  </button>
                  {expandedQAId === qa.id && (
                    <div className="px-4 py-3 bg-gray-50 border-t border-gray-200">
                      <p className="text-gray-700">{qa.answer}</p>
                    </div>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </>
  );
}