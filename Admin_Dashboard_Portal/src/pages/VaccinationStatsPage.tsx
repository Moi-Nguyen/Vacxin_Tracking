import React, { useState, useEffect } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, PieChart, Pie, Cell, LineChart, Line } from 'recharts';
import { Calendar, TrendingUp, Syringe, Users, Download } from 'lucide-react';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';
import { useToast } from '../hooks/use-toast';
import { apiService } from '../services/api';
import { VaccinationStats } from '../types/vaccination';

const VaccinationStatsPage = () => {
  const [stats, setStats] = useState<VaccinationStats | null>(null);
  const [loading, setLoading] = useState(true);
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [selectedYear, setSelectedYear] = useState(new Date().getFullYear().toString());
  const { toast } = useToast();

  const COLORS = ['#8884d8', '#82ca9d', '#ffc658', '#ff7300', '#00ff00', '#ff00ff', '#00ffff', '#ff8080'];

  const fetchStats = async () => {
    try {
      setLoading(true);
      const params = startDate && endDate ? { startDate, endDate } : {};
      const response = await apiService.getVaccinationStats(params);
      setStats(response);
    } catch (error) {
      toast({
        title: "Lỗi",
        description: "Không thể tải thống kê tiêm chủng",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
  }, [startDate, endDate]);

  const monthNames = [
    'Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4', 'Tháng 5', 'Tháng 6',
    'Tháng 7', 'Tháng 8', 'Tháng 9', 'Tháng 10', 'Tháng 11', 'Tháng 12'
  ];

  const chartData = stats?.monthlyStats
    ?.filter(item => item.year === parseInt(selectedYear))
    ?.map(item => ({
      month: monthNames[item.month - 1],
      count: item.count
    })) || [];

  const totalVaccinations = stats?.vaccineStats?.reduce((sum, item) => sum + item.count, 0) || 0;

  const exportData = () => {
    if (!stats) return;
    
    const csvContent = [
      ['Vaccine', 'Số lượng tiêm'],
      ...stats.vaccineStats.map(item => [item.vaccineName, item.count.toString()])
    ].map(row => row.join(',')).join('\n');

    const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `thong-ke-tiem-chung-${new Date().toISOString().split('T')[0]}.csv`;
    link.click();
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Thống kê Tiêm chủng</h1>
        <Button onClick={exportData} variant="outline">
          <Download className="w-4 h-4 mr-2" />
          Xuất Excel
        </Button>
      </div>

      {/* Filters */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center">
            <Calendar className="w-5 h-5 mr-2" />
            Lọc theo thời gian
          </CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-4 items-center">
            <div className="flex items-center space-x-2">
              <label className="text-sm font-medium">Từ ngày:</label>
              <Input
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                className="w-auto"
              />
            </div>
            <div className="flex items-center space-x-2">
              <label className="text-sm font-medium">Đến ngày:</label>
              <Input
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                className="w-auto"
              />
            </div>
            <div className="flex items-center space-x-2">
              <label className="text-sm font-medium">Năm:</label>
              <Select value={selectedYear} onValueChange={setSelectedYear}>
                <SelectTrigger className="w-32">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {[2024, 2023, 2022, 2021, 2020].map(year => (
                    <SelectItem key={year} value={year.toString()}>
                      {year}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
        </CardContent>
      </Card>

      {loading ? (
        <div className="flex justify-center py-8">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
        </div>
      ) : stats ? (
        <>
          {/* Summary Cards */}
          <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
            <Card>
              <CardContent className="p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-muted-foreground">Tổng số mũi tiêm</p>
                    <p className="text-3xl font-bold text-blue-600">{totalVaccinations}</p>
                  </div>
                  <Syringe className="w-8 h-8 text-blue-600" />
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardContent className="p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-muted-foreground">Loại vaccine</p>
                    <p className="text-3xl font-bold text-green-600">{stats.vaccineStats?.length || 0}</p>
                  </div>
                  <TrendingUp className="w-8 h-8 text-green-600" />
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardContent className="p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-muted-foreground">Vaccine phổ biến nhất</p>
                    <p className="text-lg font-bold text-purple-600">
                      {stats.vaccineStats?.[0]?.vaccineName || 'N/A'}
                    </p>
                  </div>
                  <Users className="w-8 h-8 text-purple-600" />
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardContent className="p-6">
                <div className="flex items-center justify-between">
                  <div>
                    <p className="text-sm text-muted-foreground">Trung bình/tháng</p>
                    <p className="text-3xl font-bold text-orange-600">
                      {Math.round(totalVaccinations / Math.max(chartData.length, 1))}
                    </p>
                  </div>
                  <Calendar className="w-8 h-8 text-orange-600" />
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Charts */}
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {/* Vaccine Usage Pie Chart */}
            <Card>
              <CardHeader>
                <CardTitle>Phân bố Vaccine theo số lượng sử dụng</CardTitle>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={400}>
                  <PieChart>
                    <Pie
                      data={stats.vaccineStats}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      label={({ vaccineName, percent }) => `${vaccineName} (${(percent * 100).toFixed(0)}%)`}
                      outerRadius={120}
                      fill="#8884d8"
                      dataKey="count"
                    >
                      {stats.vaccineStats?.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
                    <Tooltip formatter={(value, name) => [value, 'Số lượng']} />
                  </PieChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>

            {/* Monthly Vaccination Bar Chart */}
            <Card>
              <CardHeader>
                <CardTitle>Thống kê tiêm chủng theo tháng ({selectedYear})</CardTitle>
              </CardHeader>
              <CardContent>
                <ResponsiveContainer width="100%" height={400}>
                  <BarChart data={chartData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis 
                      dataKey="month" 
                      angle={-45}
                      textAnchor="end"
                      height={100}
                    />
                    <YAxis />
                    <Tooltip />
                    <Bar dataKey="count" fill="#8884d8" />
                  </BarChart>
                </ResponsiveContainer>
              </CardContent>
            </Card>
          </div>

          {/* Line Chart for Trend */}
          <Card>
            <CardHeader>
              <CardTitle>Xu hướng tiêm chủng theo tháng</CardTitle>
            </CardHeader>
            <CardContent>
              <ResponsiveContainer width="100%" height={300}>
                <LineChart data={chartData}>
                  <CartesianGrid strokeDasharray="3 3" />
                  <XAxis dataKey="month" />
                  <YAxis />
                  <Tooltip />
                  <Line 
                    type="monotone" 
                    dataKey="count" 
                    stroke="#8884d8" 
                    strokeWidth={3}
                    dot={{ fill: '#8884d8', strokeWidth: 2, r: 6 }}
                  />
                </LineChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>

          {/* Vaccine Statistics Table */}
          <Card>
            <CardHeader>
              <CardTitle>Chi tiết thống kê theo Vaccine</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="overflow-x-auto">
                <table className="w-full table-auto">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-4">Tên Vaccine</th>
                      <th className="text-left p-4">Số lượng tiêm</th>
                      <th className="text-left p-4">Tỷ lệ</th>
                      <th className="text-left p-4">Xếp hạng</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.vaccineStats
                      ?.sort((a, b) => b.count - a.count)
                      ?.map((vaccine, index) => (
                        <tr key={vaccine.vaccineName} className="border-b hover:bg-muted/50">
                          <td className="p-4 font-medium">{vaccine.vaccineName}</td>
                          <td className="p-4">{vaccine.count}</td>
                          <td className="p-4">
                            {totalVaccinations > 0 ? 
                              `${((vaccine.count / totalVaccinations) * 100).toFixed(1)}%` : 
                              '0%'
                            }
                          </td>
                          <td className="p-4">
                            <span className={`px-2 py-1 rounded text-sm ${
                              index === 0 ? 'bg-yellow-100 text-yellow-800' :
                              index === 1 ? 'bg-gray-100 text-gray-800' :
                              index === 2 ? 'bg-orange-100 text-orange-800' :
                              'bg-blue-100 text-blue-800'
                            }`}>
                              #{index + 1}
                            </span>
                          </td>
                        </tr>
                      ))}
                  </tbody>
                </table>
              </div>
            </CardContent>
          </Card>
        </>
      ) : (
        <Card>
          <CardContent className="p-8 text-center">
            <p className="text-muted-foreground">Không có dữ liệu thống kê</p>
          </CardContent>
        </Card>
      )}
    </div>
  );
};

export default VaccinationStatsPage;