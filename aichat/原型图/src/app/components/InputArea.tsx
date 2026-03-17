import React, { useState } from 'react';
import { Send } from 'lucide-react';

export function InputArea() {
  const [message, setMessage] = useState('');

  const handleSend = () => {
    if (message.trim()) {
      console.log('发送消息:', message);
      setMessage('');
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  return (
    <div className="border-t border-gray-200 bg-white px-6 py-4">
      <div className="max-w-4xl mx-auto flex items-end gap-3">
        <textarea
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="输入您的问题... (Shift + Enter 换行)"
          className="flex-1 min-h-[60px] max-h-[200px] px-4 py-3 border border-gray-300 rounded-[10px] resize-none focus:outline-none focus:border-[#007AFF] text-[#1C1C1E] placeholder:text-gray-400"
          rows={2}
        />
        <button
          onClick={handleSend}
          disabled={!message.trim()}
          className="px-6 py-3 bg-[#007AFF] text-white rounded-[10px] hover:bg-[#0066DD] transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2 h-[60px]"
        >
          <Send className="w-5 h-5" />
          发送
        </button>
      </div>
    </div>
  );
}
