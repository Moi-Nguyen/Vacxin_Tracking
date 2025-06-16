import React, { useState, useEffect } from 'react';
import { Users, Search, User } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { apiService } from '../services/api';
import { User as UserType } from '../types/auth';
import { Vaccine } from '../types/vaccine';

const DashboardPage = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalVaccines: 0,
    activeVaccines: 0,
  });
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const [usersData, vaccinesData] = await Promise.all([
          apiService.getAllUsers(),
          apiService.getVaccines()
        ]);

        setStats({
          totalUsers: usersData.length,
          totalVaccines: vaccinesData.vaccines.length,
          activeVaccines: vaccinesData.vaccines.filter((v: Vaccine) => v.isActive).length,
        });
      } catch (error) {
        console.error('Lỗi khi tải thống kê:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchStats();
  }, []);

  const statCards = [
    {
      title: 'Tổng số người dùng',
      value: stats.totalUsers,
      icon: Users,
      color: 'bg-blue-500',
      description: 'Tất cả người dùng trong hệ thống'
    },
    {
      title: 'Tổng số vaccine',
      value: stats.totalVaccines,
      icon: Search,
      color: 'bg-green-500',
      description: 'Tất cả vaccine đã đăng ký'
    },
    {
      title: 'Vaccine đang hoạt động',
      value: stats.activeVaccines,
      icon: User,
      color: 'bg-purple-500',
      description: 'Vaccine hiện tại có sẵn'
    },
  ];

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          {[1, 2, 3].map((i) => (
            <Card key={i} className="animate-pulse">
              <CardHeader className="pb-3">
                <div className="h-4 bg-gray-200 rounded w-3/4"></div>
              </CardHeader>
              <CardContent>
                <div className="h-8 bg-gray-200 rounded w-1/2 mb-2"></div>
                <div className="h-3 bg-gray-200 rounded w-full"></div>
              </CardContent>
            </Card>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-800">Dashboard</h1>
        <p className="text-gray-600">Chào mừng đến với trang quản trị</p>
      </div>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {statCards.map((card, index) => (
          <Card key={index} className="hover:shadow-lg transition-shadow duration-200">
            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-3">
              <CardTitle className="text-sm font-medium text-gray-600">
                {card.title}
              </CardTitle>
              <div className={`p-2 rounded-full ${card.color}`}>
                <card.icon className="w-4 h-4 text-white" />
              </div>
            </CardHeader>
            <CardContent>
              <div className="text-2xl font-bold text-gray-800 mb-1">
                {card.value.toLocaleString()}
              </div>
              <CardDescription className="text-xs">
                {card.description}
              </CardDescription>
            </CardContent>
          </Card>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Hoạt động gần đây</CardTitle>
            <CardDescription>Các thao tác mới nhất trong hệ thống</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              <div className="flex items-center space-x-3 p-3 bg-blue-50 rounded-lg">
                <div className="w-2 h-2 bg-blue-500 rounded-full"></div>
                <div>
                  <p className="text-sm font-medium">Hệ thống khởi động thành công</p>
                  <p className="text-xs text-gray-500">Vừa xong</p>
                </div>
              </div>
              <div className="flex items-center space-x-3 p-3 bg-green-50 rounded-lg">
                <div className="w-2 h-2 bg-green-500 rounded-full"></div>
                <div>
                  <p className="text-sm font-medium">Dữ liệu đã được đồng bộ</p>
                  <p className="text-xs text-gray-500">5 phút trước</p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Hướng dẫn sử dụng</CardTitle>
            <CardDescription>Các tính năng chính của hệ thống</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-3">
              <div className="flex items-start space-x-3">
                <Users className="w-5 h-5 text-blue-500 mt-1" />
                <div>
                  <p className="text-sm font-medium">Quản lý User</p>
                  <p className="text-xs text-gray-500">Thêm, sửa, xóa và xem thông tin người dùng</p>
                </div>
              </div>
              <div className="flex items-start space-x-3">
                <Search className="w-5 h-5 text-green-500 mt-1" />
                <div>
                  <p className="text-sm font-medium">Quản lý Vaccine</p>
                  <p className="text-xs text-gray-500">Quản lý thông tin vaccine và kho</p>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default DashboardPage;
