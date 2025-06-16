
import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Users, Search, User, LogOut } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const Sidebar = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { logout, user } = useAuth();

  const menuItems = [
    { path: '/admin', label: 'Dashboard', icon: Search },
    { path: '/admin/users', label: 'Quản lý User', icon: Users },
    { path: '/admin/vaccines', label: 'Quản lý Vaccine', icon: Search },
  ];

  const isActive = (path: string) => {
    if (path === '/admin') {
      return location.pathname === '/admin';
    }
    return location.pathname.startsWith(path);
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="bg-gradient-to-b from-blue-900 to-blue-800 text-white w-64 min-h-screen p-4 relative">
      <div className="mb-8">
        <h2 className="text-2xl font-bold text-center">Admin Panel</h2>
        <div className="mt-4 p-3 bg-blue-800 rounded-lg">
          <div className="flex items-center space-x-2">
            <User className="w-5 h-5" />
            <div>
              <p className="font-medium">{user?.fullName || user?.name}</p>
              <p className="text-sm text-blue-200">{user?.role}</p>
            </div>
          </div>
        </div>
      </div>

      <nav className="space-y-2">
        {menuItems.map((item) => (
          <Link
            key={item.path}
            to={item.path}
            className={`flex items-center space-x-3 p-3 rounded-lg transition-colors duration-200 ${
              isActive(item.path)
                ? 'bg-blue-700 text-white'
                : 'text-blue-100 hover:bg-blue-700 hover:text-white'
            }`}
          >
            <item.icon className="w-5 h-5" />
            <span>{item.label}</span>
          </Link>
        ))}
      </nav>

      <div className="absolute bottom-4 left-4 right-4">
        <button
          onClick={handleLogout}
          className="flex items-center space-x-3 p-3 w-full text-blue-100 hover:bg-blue-700 hover:text-white rounded-lg transition-colors duration-200"
        >
          <LogOut className="w-5 h-5" />
          <span>Đăng xuất</span>
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
