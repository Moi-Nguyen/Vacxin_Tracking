import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash, Calendar, User, Syringe, ChevronLeft, ChevronRight } from 'lucide-react';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '../components/ui/dialog';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';
import { Textarea } from '../components/ui/textarea';
import { useToast } from '../hooks/use-toast';
import { apiService } from '../services/api';
import { Vaccination, VaccinationRequest } from '../types/vaccination';
import { User as UserType } from '../types/auth';
import { Vaccine } from '../types/vaccine';

const VaccinationHistoryPage = () => {
  const [vaccinations, setVaccinations] = useState<Vaccination[]>([]);
  const [users, setUsers] = useState<UserType[]>([]);
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [startDate, setStartDate] = useState('');
  const [endDate, setEndDate] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingVaccination, setEditingVaccination] = useState<Vaccination | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const { toast } = useToast();

  const [formData, setFormData] = useState<Partial<VaccinationRequest>>({
    userId: '',
    vaccineId: '',
    date: new Date().toISOString().split('T')[0],
    location: '',
    doctorId: '',
    notes: '',
  });

  const fetchVaccinations = async () => {
    try {
      setLoading(true);
      const params = {
        search: searchTerm,
        startDate,
        endDate,
        page: currentPage,
        limit: 10,
      };
      
      const response = await apiService.getAllVaccinations(params);
      setVaccinations(response.vaccinations);
      setTotalPages(response.totalPages);
    } catch (error) {
      toast({
        title: "Lỗi",
        description: "Không thể tải danh sách lịch sử tiêm chủng",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  const fetchDropdownData = async () => {
    try {
      const [usersResponse, vaccinesResponse] = await Promise.all([
        apiService.getAllUsers(),
        apiService.getVaccines(),
      ]);
      setUsers(usersResponse);
      setVaccines(vaccinesResponse.vaccines);
    } catch (error) {
      console.error('Error fetching dropdown data:', error);
    }
  };

  useEffect(() => {
    fetchDropdownData();
  }, []);

  useEffect(() => {
    fetchVaccinations();
  }, [searchTerm, startDate, endDate, currentPage]);

  const resetForm = () => {
    setFormData({
      userId: '',
      vaccineId: '',
      date: new Date().toISOString().split('T')[0],
      location: '',
      doctorId: '',
      notes: '',
    });
    setEditingVaccination(null);
  };

  const handleEdit = (vaccination: Vaccination) => {
    setEditingVaccination(vaccination);
    setFormData({
      userId: vaccination.userId.id,
      vaccineId: vaccination.vaccineId.id,
      date: vaccination.date.split('T')[0],
      location: vaccination.location,
      doctorId: vaccination.doctorId?.id || '',
      notes: vaccination.notes || '',
    });
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      if (editingVaccination) {
        await apiService.updateVaccination(editingVaccination.id, formData);
        toast({
          title: "Thành công",
          description: "Cập nhật lịch sử tiêm chủng thành công",
        });
      } else {
        await apiService.createVaccination(formData as VaccinationRequest);
        toast({
          title: "Thành công",
          description: "Tạo lịch sử tiêm chủng thành công",
        });
      }
      
      fetchVaccinations();
      setIsModalOpen(false);
      resetForm();
    } catch (error: any) {
      toast({
        title: "Lỗi",
        description: error.message,
        variant: "destructive",
      });
    }
  };

  const handleDelete = async (vaccinationId: string) => {
    if (!confirm('Bạn có chắc chắn muốn xóa lịch sử tiêm chủng này?')) {
      return;
    }

    try {
      await apiService.deleteVaccination(vaccinationId);
      toast({
        title: "Thành công",
        description: "Xóa lịch sử tiêm chủng thành công",
      });
      fetchVaccinations();
    } catch (error: any) {
      toast({
        title: "Lỗi",
        description: error.message,
        variant: "destructive",
      });
    }
  };

  const doctorUsers = users.filter(user => user.role === 'doctor');

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Lịch sử Tiêm chủng</h1>
        <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
          <DialogTrigger asChild>
            <Button onClick={resetForm} className="bg-gradient-to-r from-primary to-primary/80">
              <Plus className="w-4 h-4 mr-2" />
              Thêm Lịch sử
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl">
            <DialogHeader>
              <DialogTitle>
                {editingVaccination ? 'Chỉnh sửa Lịch sử Tiêm chủng' : 'Thêm Lịch sử Tiêm chủng mới'}
              </DialogTitle>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Người dùng *</label>
                  <Select 
                    value={formData.userId} 
                    onValueChange={(value) => setFormData({ ...formData, userId: value })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Chọn người dùng" />
                    </SelectTrigger>
                    <SelectContent>
                      {users.map((user) => (
                        <SelectItem key={user.id} value={user.id}>
                          {user.fullName || user.name} - {user.email}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Vaccine *</label>
                  <Select 
                    value={formData.vaccineId} 
                    onValueChange={(value) => setFormData({ ...formData, vaccineId: value })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Chọn vaccine" />
                    </SelectTrigger>
                    <SelectContent>
                      {vaccines.map((vaccine) => (
                        <SelectItem key={vaccine.id} value={vaccine.id}>
                          {vaccine.name} - {vaccine.manufacturer}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Ngày tiêm *</label>
                  <Input
                    type="date"
                    value={formData.date}
                    onChange={(e) => setFormData({ ...formData, date: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Bác sĩ</label>
                  <Select 
                    value={formData.doctorId} 
                    onValueChange={(value) => setFormData({ ...formData, doctorId: value })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Chọn bác sĩ" />
                    </SelectTrigger>
                    <SelectContent>
                      {doctorUsers.map((doctor) => (
                        <SelectItem key={doctor.id} value={doctor.id}>
                          {doctor.fullName || doctor.name}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium mb-1">Địa điểm *</label>
                <Input
                  value={formData.location}
                  onChange={(e) => setFormData({ ...formData, location: e.target.value })}
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-1">Ghi chú</label>
                <Textarea
                  value={formData.notes}
                  onChange={(e) => setFormData({ ...formData, notes: e.target.value })}
                  rows={3}
                />
              </div>

              <div className="flex justify-end space-x-2 pt-4">
                <Button
                  type="button"
                  variant="outline"
                  onClick={() => setIsModalOpen(false)}
                >
                  Hủy
                </Button>
                <Button type="submit">
                  {editingVaccination ? 'Cập nhật' : 'Tạo mới'}
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <Card>
        <CardHeader>
          <div className="flex flex-wrap gap-4 items-center">
            <div className="flex items-center space-x-2">
              <Search className="w-5 h-5" />
              <Input
                placeholder="Tìm kiếm theo địa điểm..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="max-w-sm"
              />
            </div>
            <div className="flex items-center space-x-2">
              <Calendar className="w-5 h-5" />
              <Input
                type="date"
                value={startDate}
                onChange={(e) => setStartDate(e.target.value)}
                className="max-w-sm"
              />
              <span>đến</span>
              <Input
                type="date"
                value={endDate}
                onChange={(e) => setEndDate(e.target.value)}
                className="max-w-sm"
              />
            </div>
          </div>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex justify-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
            </div>
          ) : (
            <>
              <div className="overflow-x-auto">
                <table className="w-full table-auto">
                  <thead>
                    <tr className="border-b">
                      <th className="text-left p-4">Người dùng</th>
                      <th className="text-left p-4">Vaccine</th>
                      <th className="text-left p-4">Ngày tiêm</th>
                      <th className="text-left p-4">Địa điểm</th>
                      <th className="text-left p-4">Bác sĩ</th>
                      <th className="text-left p-4">Ghi chú</th>
                      <th className="text-left p-4">Thao tác</th>
                    </tr>
                  </thead>
                  <tbody>
                    {vaccinations.map((vaccination) => (
                      <tr key={vaccination.id} className="border-b hover:bg-muted/50">
                        <td className="p-4">
                          <div>
                            <div className="font-medium">{vaccination.userId.name}</div>
                            <div className="text-sm text-muted-foreground">{vaccination.userId.email}</div>
                          </div>
                        </td>
                        <td className="p-4">
                          <div>
                            <div className="font-medium">{vaccination.vaccineId.name}</div>
                            <div className="text-sm text-muted-foreground">{vaccination.vaccineId.manufacturer}</div>
                          </div>
                        </td>
                        <td className="p-4">
                          {new Date(vaccination.date).toLocaleDateString('vi-VN')}
                        </td>
                        <td className="p-4">{vaccination.location}</td>
                        <td className="p-4">
                          {vaccination.doctorId?.name || 'Chưa có'}
                        </td>
                        <td className="p-4">
                          <div className="max-w-xs truncate" title={vaccination.notes}>
                            {vaccination.notes || 'Không có'}
                          </div>
                        </td>
                        <td className="p-4">
                          <div className="flex space-x-2">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleEdit(vaccination)}
                            >
                              <Edit className="w-4 h-4" />
                            </Button>
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() => handleDelete(vaccination.id)}
                            >
                              <Trash className="w-4 h-4 text-red-600" />
                            </Button>
                          </div>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              {/* Pagination */}
              <div className="flex justify-between items-center mt-6">
                <div className="text-sm text-muted-foreground">
                  Trang {currentPage} / {totalPages}
                </div>
                <div className="flex space-x-2">
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setCurrentPage(prev => Math.max(prev - 1, 1))}
                    disabled={currentPage <= 1}
                  >
                    <ChevronLeft className="w-4 h-4" />
                  </Button>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages))}
                    disabled={currentPage >= totalPages}
                  >
                    <ChevronRight className="w-4 h-4" />
                  </Button>
                </div>
              </div>
            </>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default VaccinationHistoryPage;