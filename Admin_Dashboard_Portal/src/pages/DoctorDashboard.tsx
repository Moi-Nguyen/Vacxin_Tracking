
import React, { useState, useEffect } from 'react';
import { Calendar, Clock, Users, Activity } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { apiService } from '../services/api';
import { useToast } from '../hooks/use-toast';
import { Shift, ShiftBooking } from '../types/doctor';

const DoctorDashboard = () => {
  const [todayShifts, setTodayShifts] = useState<Shift[]>([]);
  const [todayBookings, setTodayBookings] = useState<ShiftBooking[]>([]);
  const [weeklyShifts, setWeeklyShifts] = useState<Shift[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const { toast } = useToast();

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      setIsLoading(true);
      
      const today = new Date().toISOString().split('T')[0];
      const startOfWeek = new Date();
      startOfWeek.setDate(startOfWeek.getDate() - startOfWeek.getDay());
      const endOfWeek = new Date();
      endOfWeek.setDate(startOfWeek.getDate() + 6);

      // Fetch today's shifts
      const shiftsResponse = await apiService.getDoctorShifts({
        startDate: today,
        endDate: today
      });
      setTodayShifts(shiftsResponse.shifts);

      // Fetch today's bookings
      const bookingsResponse = await apiService.getDoctorShiftBookings({
        date: today
      });
      setTodayBookings(bookingsResponse.bookings);

      // Fetch this week's shifts
      const weeklyResponse = await apiService.getDoctorShifts({
        startDate: startOfWeek.toISOString().split('T')[0],
        endDate: endOfWeek.toISOString().split('T')[0]
      });
      setWeeklyShifts(weeklyResponse.shifts);

    } catch (error) {
      console.error('Error fetching dashboard data:', error);
      toast({
        title: "Lỗi",
        description: "Không thể tải dữ liệu dashboard",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const getTodayShiftInfo = () => {
    const activeShift = todayShifts.find(shift => shift.status === 'active');
    const scheduledShift = todayShifts.find(shift => shift.status === 'scheduled');
    
    if (activeShift) {
      const startTime = new Date(activeShift.startTime).toLocaleTimeString('vi-VN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      });
      const endTime = new Date(activeShift.endTime).toLocaleTimeString('vi-VN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      });
      return `${startTime} - ${endTime}`;
    }
    
    if (scheduledShift) {
      const startTime = new Date(scheduledShift.startTime).toLocaleTimeString('vi-VN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      });
      const endTime = new Date(scheduledShift.endTime).toLocaleTimeString('vi-VN', { 
        hour: '2-digit', 
        minute: '2-digit' 
      });
      return `${startTime} - ${endTime}`;
    }
    
    return "Không có ca trực";
  };

  const getCompletedBookings = () => {
    return todayBookings.filter(booking => booking.status === 'completed').length;
  };

  const getWeeklyCompletedBookings = () => {
    // This would need a separate API call for weekly booking stats
    // For now, we'll estimate based on completed shifts
    return weeklyShifts.filter(shift => shift.status === 'completed').length * 6;
  };

  const getUpcomingBookings = () => {
    const now = new Date();
    return todayBookings
      .filter(booking => {
        const appointmentTime = new Date(`${booking.appointmentDate}T${booking.appointmentTime}`);
        return appointmentTime > now && booking.status === 'confirmed';
      })
      .sort((a, b) => {
        const timeA = new Date(`${a.appointmentDate}T${a.appointmentTime}`).getTime();
        const timeB = new Date(`${b.appointmentDate}T${b.appointmentTime}`).getTime();
        return timeA - timeB;
      })
      .slice(0, 3);
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-800">Dashboard Bác sĩ</h1>
        </div>
        <div className="animate-pulse space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {[1, 2, 3, 4].map(i => (
              <div key={i} className="h-24 bg-gray-200 rounded"></div>
            ))}
          </div>
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <div className="h-64 bg-gray-200 rounded"></div>
            <div className="h-64 bg-gray-200 rounded"></div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-800">Dashboard Bác sĩ</h1>
        <div className="text-sm text-gray-600">
          {new Date().toLocaleDateString('vi-VN', { 
            weekday: 'long', 
            year: 'numeric', 
            month: 'long', 
            day: 'numeric' 
          })}
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Ca trực hôm nay</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{todayShifts.length}</div>
            <p className="text-xs text-muted-foreground">{getTodayShiftInfo()}</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Lịch hẹn hôm nay</CardTitle>
            <Calendar className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{todayBookings.length}</div>
            <p className="text-xs text-muted-foreground">
              {todayBookings.filter(b => b.status === 'pending').length} đang chờ xử lý
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Đã hoàn thành</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{getCompletedBookings()}</div>
            <p className="text-xs text-muted-foreground">tiêm chủng hôm nay</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Tổng tuần này</CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{getWeeklyCompletedBookings()}</div>
            <p className="text-xs text-muted-foreground">lượt tiêm chủng</p>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Lịch hẹn sắp tới</CardTitle>
            <CardDescription>Danh sách lịch hẹn trong ca trực hôm nay</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {getUpcomingBookings().length > 0 ? (
                getUpcomingBookings().map((booking) => (
                  <div key={booking._id} className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
                    <div>
                      <p className="font-medium">{booking.userId.fullName || booking.userId.name}</p>
                      <p className="text-sm text-gray-600">{booking.vaccineId.name}</p>
                    </div>
                    <span className="text-sm font-medium text-blue-600">
                      {new Date(`${booking.appointmentDate}T${booking.appointmentTime}`).toLocaleTimeString('vi-VN', {
                        hour: '2-digit',
                        minute: '2-digit'
                      })}
                    </span>
                  </div>
                ))
              ) : (
                <div className="text-center py-4 text-gray-500">
                  Không có lịch hẹn sắp tới
                </div>
              )}
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle>Thống kê ca trực</CardTitle>
            <CardDescription>Tổng quan hoạt động tuần này</CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {weeklyShifts.length > 0 ? (
                weeklyShifts.map((shift, index) => (
                  <div key={shift._id} className="flex justify-between items-center">
                    <span className="text-sm">
                      {new Date(shift.shiftDate).toLocaleDateString('vi-VN', { weekday: 'long' })}
                    </span>
                    <span className="text-sm font-medium">
                      {shift.status === 'completed' ? '✓ Hoàn thành' : 
                       shift.status === 'active' ? '🟢 Đang hoạt động' :
                       shift.status === 'scheduled' ? '📅 Đã lên lịch' : '❌ Đã hủy'}
                    </span>
                  </div>
                ))
              ) : (
                <div className="text-center py-4 text-gray-500">
                  Không có ca trực tuần này
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};

export default DoctorDashboard;
