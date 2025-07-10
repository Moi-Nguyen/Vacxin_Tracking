import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash, Building, Eye, ToggleLeft, ToggleRight } from 'lucide-react';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Card, CardContent, CardHeader, CardTitle } from '../components/ui/card';
import { Badge } from '../components/ui/badge';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from '../components/ui/dialog';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';
import { Textarea } from '../components/ui/textarea';
import { useToast } from '../hooks/use-toast';
import { apiService } from '../services/api';
import { Facility, FacilityRequest } from '../types/facility';

const FacilitiesPage = () => {
  const [facilities, setFacilities] = useState<Facility[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingFacility, setEditingFacility] = useState<Facility | null>(null);
  const { toast } = useToast();

  const [formData, setFormData] = useState<Partial<FacilityRequest>>({
    name: '',
    address: '',
    phone: '',
    email: '',
    description: '',
    maxBookingsPerDay: 50,
    operatingHours: {
      monday: { open: '07:00', close: '17:00' },
      tuesday: { open: '07:00', close: '17:00' },
      wednesday: { open: '07:00', close: '17:00' },
      thursday: { open: '07:00', close: '17:00' },
      friday: { open: '07:00', close: '17:00' },
      saturday: { open: '07:00', close: '12:00' },
      sunday: { open: '08:00', close: '12:00' },
    },
  });

  const fetchFacilities = async () => {
    try {
      setLoading(true);
      const response = await apiService.getFacilities();
      setFacilities(response.facilities);
    } catch (error) {
      toast({
        title: "Lỗi",
        description: "Không thể tải danh sách cơ sở y tế",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchFacilities();
  }, []);

  const resetForm = () => {
    setFormData({
      name: '',
      address: '',
      phone: '',
      email: '',
      description: '',
      maxBookingsPerDay: 50,
      operatingHours: {
        monday: { open: '07:00', close: '17:00' },
        tuesday: { open: '07:00', close: '17:00' },
        wednesday: { open: '07:00', close: '17:00' },
        thursday: { open: '07:00', close: '17:00' },
        friday: { open: '07:00', close: '17:00' },
        saturday: { open: '07:00', close: '12:00' },
        sunday: { open: '08:00', close: '12:00' },
      },
    });
    setEditingFacility(null);
  };

  const handleEdit = (facility: Facility) => {
    setEditingFacility(facility);
    setFormData({
      name: facility.name,
      address: facility.address,
      phone: facility.phone,
      email: facility.email,
      description: facility.description || '',
      maxBookingsPerDay: facility.maxBookingsPerDay,
      operatingHours: facility.operatingHours || {
        monday: { open: '07:00', close: '17:00' },
        tuesday: { open: '07:00', close: '17:00' },
        wednesday: { open: '07:00', close: '17:00' },
        thursday: { open: '07:00', close: '17:00' },
        friday: { open: '07:00', close: '17:00' },
        saturday: { open: '07:00', close: '12:00' },
        sunday: { open: '08:00', close: '12:00' },
      },
    });
    setIsModalOpen(true);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      if (editingFacility) {
        await apiService.updateFacility(editingFacility.id, formData);
        toast({
          title: "Thành công",
          description: "Cập nhật cơ sở y tế thành công",
        });
      } else {
        await apiService.createFacility(formData as FacilityRequest);
        toast({
          title: "Thành công",
          description: "Tạo cơ sở y tế thành công",
        });
      }
      
      fetchFacilities();
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

  const handleDelete = async (facilityId: string) => {
    if (!confirm('Bạn có chắc chắn muốn xóa cơ sở y tế này?')) {
      return;
    }

    try {
      await apiService.deleteFacility(facilityId);
      toast({
        title: "Thành công",
        description: "Xóa cơ sở y tế thành công",
      });
      fetchFacilities();
    } catch (error: any) {
      toast({
        title: "Lỗi",
        description: error.message,
        variant: "destructive",
      });
    }
  };

  const handleToggleStatus = async (facilityId: string) => {
    try {
      await apiService.toggleFacilityStatus(facilityId);
      toast({
        title: "Thành công",
        description: "Thay đổi trạng thái cơ sở y tế thành công",
      });
      fetchFacilities();
    } catch (error: any) {
      toast({
        title: "Lỗi",
        description: error.message,
        variant: "destructive",
      });
    }
  };

  const filteredFacilities = facilities.filter(facility =>
    facility.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
    facility.address.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const daysOfWeek = [
    { key: 'monday', label: 'Thứ 2' },
    { key: 'tuesday', label: 'Thứ 3' },
    { key: 'wednesday', label: 'Thứ 4' },
    { key: 'thursday', label: 'Thứ 5' },
    { key: 'friday', label: 'Thứ 6' },
    { key: 'saturday', label: 'Thứ 7' },
    { key: 'sunday', label: 'Chủ nhật' },
  ];

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Quản lý Cơ sở Y tế</h1>
        <Dialog open={isModalOpen} onOpenChange={setIsModalOpen}>
          <DialogTrigger asChild>
            <Button onClick={resetForm} className="bg-gradient-to-r from-primary to-primary/80">
              <Plus className="w-4 h-4 mr-2" />
              Thêm Cơ sở
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-4xl max-h-[90vh] overflow-y-auto">
            <DialogHeader>
              <DialogTitle>
                {editingFacility ? 'Chỉnh sửa Cơ sở Y tế' : 'Thêm Cơ sở Y tế mới'}
              </DialogTitle>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Tên cơ sở *</label>
                  <Input
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Email *</label>
                  <Input
                    type="email"
                    value={formData.email}
                    onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                    required
                  />
                </div>
              </div>

              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Số điện thoại *</label>
                  <Input
                    value={formData.phone}
                    onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Số lịch hẹn tối đa/ngày</label>
                  <Input
                    type="number"
                    value={formData.maxBookingsPerDay}
                    onChange={(e) => setFormData({ ...formData, maxBookingsPerDay: parseInt(e.target.value) })}
                    min="1"
                  />
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium mb-1">Địa chỉ *</label>
                <Input
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-1">Mô tả</label>
                <Textarea
                  value={formData.description}
                  onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                  rows={3}
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-3">Giờ làm việc</label>
                <div className="space-y-2">
                  {daysOfWeek.map(({ key, label }) => (
                    <div key={key} className="grid grid-cols-3 gap-2 items-center">
                      <span className="text-sm">{label}</span>
                      <Input
                        type="time"
                        value={formData.operatingHours?.[key as keyof typeof formData.operatingHours]?.open || '07:00'}
                        onChange={(e) => setFormData({
                          ...formData,
                          operatingHours: {
                            ...formData.operatingHours,
                            [key]: {
                              ...formData.operatingHours?.[key as keyof typeof formData.operatingHours],
                              open: e.target.value
                            }
                          }
                        })}
                      />
                      <Input
                        type="time"
                        value={formData.operatingHours?.[key as keyof typeof formData.operatingHours]?.close || '17:00'}
                        onChange={(e) => setFormData({
                          ...formData,
                          operatingHours: {
                            ...formData.operatingHours,
                            [key]: {
                              ...formData.operatingHours?.[key as keyof typeof formData.operatingHours],
                              close: e.target.value
                            }
                          }
                        })}
                      />
                    </div>
                  ))}
                </div>
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
                  {editingFacility ? 'Cập nhật' : 'Tạo mới'}
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center space-x-2">
            <Search className="w-5 h-5" />
            <Input
              placeholder="Tìm kiếm cơ sở y tế..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="max-w-sm"
            />
          </div>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="flex justify-center py-8">
              <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-primary"></div>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
              {filteredFacilities.map((facility) => (
                <Card key={facility.id} className="hover:shadow-lg transition-shadow">
                  <CardHeader className="pb-3">
                    <div className="flex justify-between items-start">
                      <div>
                        <CardTitle className="text-lg">{facility.name}</CardTitle>
                        <Badge variant={facility.isActive ? "default" : "secondary"} className="mt-1">
                          {facility.isActive ? 'Hoạt động' : 'Vô hiệu hóa'}
                        </Badge>
                      </div>
                      <div className="flex space-x-1">
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleToggleStatus(facility.id)}
                        >
                          {facility.isActive ? (
                            <ToggleRight className="w-4 h-4 text-green-600" />
                          ) : (
                            <ToggleLeft className="w-4 h-4 text-gray-400" />
                          )}
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleEdit(facility)}
                        >
                          <Edit className="w-4 h-4" />
                        </Button>
                        <Button
                          variant="ghost"
                          size="sm"
                          onClick={() => handleDelete(facility.id)}
                        >
                          <Trash className="w-4 h-4 text-red-600" />
                        </Button>
                      </div>
                    </div>
                  </CardHeader>
                  <CardContent className="pt-0">
                    <div className="space-y-2 text-sm">
                      <div className="flex items-center space-x-2">
                        <Building className="w-4 h-4 text-muted-foreground" />
                        <span className="text-muted-foreground">{facility.address}</span>
                      </div>
                      <div className="flex justify-between">
                        <span>📞 {facility.phone}</span>
                        <span>📧 {facility.email}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="font-medium">Tối đa: {facility.maxBookingsPerDay} lịch/ngày</span>
                      </div>
                      {facility.description && (
                        <p className="text-xs text-muted-foreground mt-2 line-clamp-2">
                          {facility.description}
                        </p>
                      )}
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
};

export default FacilitiesPage;