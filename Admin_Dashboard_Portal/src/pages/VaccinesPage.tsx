import React, { useState, useEffect } from 'react';
import { Plus, Search, Edit, Trash, User } from 'lucide-react';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Textarea } from '../components/ui/textarea';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Badge } from '../components/ui/badge';
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle, DialogTrigger } from '../components/ui/dialog';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from '../components/ui/alert-dialog';
import { apiService } from '../services/api';
import { Vaccine } from '../types/vaccine';
import { useToast } from '../hooks/use-toast';

const VaccinesPage = () => {
  const [vaccines, setVaccines] = useState<Vaccine[]>([]);
  const [filteredVaccines, setFilteredVaccines] = useState<Vaccine[]>([]);
  const [searchTerm, setSearchTerm] = useState('');
  const [isLoading, setIsLoading] = useState(true);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [editingVaccine, setEditingVaccine] = useState<Vaccine | null>(null);
  const { toast } = useToast();

  const [formData, setFormData] = useState({
    name: '',
    manufacturer: '',
    description: '',
    dosage: '',
    storageTemperature: '',
    shelfLife: '',
    quantity: '',
    sideEffects: '',
    contraindications: '',
  });

  useEffect(() => {
    fetchVaccines();
  }, []);

  useEffect(() => {
    filterVaccines();
  }, [vaccines, searchTerm]);

  const fetchVaccines = async () => {
    try {
      const data = await apiService.getVaccines();
      setVaccines(data.vaccines);
    } catch (error) {
      toast({
        title: "Lỗi",
        description: "Không thể tải danh sách vaccine",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const filterVaccines = () => {
    let filtered = vaccines;

    if (searchTerm) {
      filtered = filtered.filter(vaccine =>
        vaccine.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        vaccine.manufacturer.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    setFilteredVaccines(filtered);
  };

  const resetForm = () => {
    setFormData({
      name: '',
      manufacturer: '',
      description: '',
      dosage: '',
      storageTemperature: '',
      shelfLife: '',
      quantity: '',
      sideEffects: '',
      contraindications: '',
    });
    setEditingVaccine(null);
  };

  const handleOpenDialog = (vaccine?: Vaccine) => {
    if (vaccine) {
      setEditingVaccine(vaccine);
      setFormData({
        name: vaccine.name,
        manufacturer: vaccine.manufacturer,
        description: vaccine.description,
        dosage: vaccine.dosage,
        storageTemperature: vaccine.storageTemperature.toString(),
        shelfLife: vaccine.shelfLife.toString(),
        quantity: vaccine.quantity.toString(),
        sideEffects: vaccine.sideEffects.join(', '),
        contraindications: vaccine.contraindications.join(', '),
      });
    } else {
      resetForm();
    }
    setIsDialogOpen(true);
  };

  const handleCloseDialog = () => {
    setIsDialogOpen(false);
    resetForm();
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const vaccineData = {
        name: formData.name,
        manufacturer: formData.manufacturer,
        description: formData.description,
        dosage: formData.dosage,
        storageTemperature: parseFloat(formData.storageTemperature),
        shelfLife: parseInt(formData.shelfLife),
        quantity: parseInt(formData.quantity),
        sideEffects: formData.sideEffects.split(',').map(s => s.trim()).filter(s => s),
        contraindications: formData.contraindications.split(',').map(s => s.trim()).filter(s => s),
      };

      if (editingVaccine) {
        await apiService.updateVaccine(editingVaccine.id, vaccineData);
        toast({
          title: "Thành công",
          description: "Cập nhật vaccine thành công",
        });
      } else {
        await apiService.createVaccine(vaccineData);
        toast({
          title: "Thành công",
          description: "Tạo vaccine mới thành công",
        });
      }

      handleCloseDialog();
      fetchVaccines();
    } catch (error) {
      toast({
        title: "Lỗi",
        description: error instanceof Error ? error.message : "Có lỗi xảy ra",
        variant: "destructive",
      });
    }
  };

  const handleDelete = async (vaccineId: string) => {
    try {
      await apiService.deleteVaccine(vaccineId);
      toast({
        title: "Thành công",
        description: "Xóa vaccine thành công",
      });
      fetchVaccines();
    } catch (error) {
      toast({
        title: "Lỗi",
        description: "Không thể xóa vaccine",
        variant: "destructive",
      });
    }
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-800">Quản lý Vaccine</h1>
        </div>
        <Card>
          <CardContent className="p-6">
            <div className="animate-pulse space-y-4">
              {[1, 2, 3, 4, 5].map((i) => (
                <div key={i} className="h-12 bg-gray-200 rounded"></div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-800">Quản lý Vaccine</h1>
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button onClick={() => handleOpenDialog()}>
              <Plus className="w-4 h-4 mr-2" />
              Thêm Vaccine
            </Button>
          </DialogTrigger>
          <DialogContent className="max-w-2xl max-h-[90vh] overflow-y-auto">
            <DialogHeader>
              <DialogTitle>
                {editingVaccine ? 'Chỉnh sửa Vaccine' : 'Thêm Vaccine mới'}
              </DialogTitle>
              <DialogDescription>
                {editingVaccine 
                  ? 'Cập nhật thông tin vaccine' 
                  : 'Điền thông tin để tạo vaccine mới'
                }
              </DialogDescription>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Tên vaccine *</label>
                  <Input
                    value={formData.name}
                    onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Hãng sản xuất *</label>
                  <Input
                    value={formData.manufacturer}
                    onChange={(e) => setFormData({ ...formData, manufacturer: e.target.value })}
                    required
                  />
                </div>
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
                <label className="block text-sm font-medium mb-1">Liều lượng *</label>
                <Input
                  value={formData.dosage}
                  onChange={(e) => setFormData({ ...formData, dosage: e.target.value })}
                  placeholder="VD: 2 liều, cách nhau 21 ngày"
                  required
                />
              </div>
              
              <div className="grid grid-cols-3 gap-4">
                <div>
                  <label className="block text-sm font-medium mb-1">Nhiệt độ bảo quản (°C) *</label>
                  <Input
                    type="number"
                    step="0.1"
                    value={formData.storageTemperature}
                    onChange={(e) => setFormData({ ...formData, storageTemperature: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Hạn sử dụng (tháng) *</label>
                  <Input
                    type="number"
                    value={formData.shelfLife}
                    onChange={(e) => setFormData({ ...formData, shelfLife: e.target.value })}
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium mb-1">Số lượng *</label>
                  <Input
                    type="number"
                    value={formData.quantity}
                    onChange={(e) => setFormData({ ...formData, quantity: e.target.value })}
                    required
                  />
                </div>
              </div>
              
              <div>
                <label className="block text-sm font-medium mb-1">
                  Tác dụng phụ (phân cách bằng dấu phẩy)
                </label>
                <Textarea
                  value={formData.sideEffects}
                  onChange={(e) => setFormData({ ...formData, sideEffects: e.target.value })}
                  placeholder="VD: Sốt, Mệt mỏi, Đau đầu"
                  rows={2}
                />
              </div>
              
              <div>
                <label className="block text-sm font-medium mb-1">
                  Chống chỉ định (phân cách bằng dấu phẩy)
                </label>
                <Textarea
                  value={formData.contraindications}
                  onChange={(e) => setFormData({ ...formData, contraindications: e.target.value })}
                  placeholder="VD: Dị ứng nghiêm trọng, Phụ nữ mang thai"
                  rows={2}
                />
              </div>
              
              <div className="flex space-x-2 pt-4">
                <Button type="submit" className="flex-1">
                  {editingVaccine ? 'Cập nhật' : 'Tạo mới'}
                </Button>
                <Button type="button" variant="outline" onClick={handleCloseDialog}>
                  Hủy
                </Button>
              </div>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Danh sách Vaccine</CardTitle>
          <CardDescription>
            Quản lý thông tin tất cả vaccine trong hệ thống
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex space-x-4 mb-6">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-4 h-4" />
                <Input
                  placeholder="Tìm kiếm theo tên vaccine, hãng sản xuất..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  className="pl-10"
                />
              </div>
            </div>
          </div>

          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Tên Vaccine</TableHead>
                  <TableHead>Hãng sản xuất</TableHead>
                  <TableHead>Số lượng</TableHead>
                  <TableHead>Nhiệt độ bảo quản</TableHead>
                  <TableHead>Trạng thái</TableHead>
                  <TableHead className="text-right">Thao tác</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredVaccines.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={6} className="text-center py-8">
                      <div className="flex flex-col items-center space-y-2">
                        <User className="w-8 h-8 text-gray-400" />
                        <p className="text-gray-500">Không tìm thấy vaccine nào</p>
                      </div>
                    </TableCell>
                  </TableRow>
                ) : (
                  filteredVaccines.map((vaccine) => (
                    <TableRow key={vaccine.id}>
                      <TableCell className="font-medium">
                        <div>
                          <p>{vaccine.name}</p>
                          <p className="text-sm text-gray-500">{vaccine.dosage}</p>
                        </div>
                      </TableCell>
                      <TableCell>{vaccine.manufacturer}</TableCell>
                      <TableCell>
                        <span className={vaccine.quantity > 0 ? 'text-green-600' : 'text-red-600'}>
                          {vaccine.quantity.toLocaleString()}
                        </span>
                      </TableCell>
                      <TableCell>{vaccine.storageTemperature}°C</TableCell>
                      <TableCell>
                        <Badge 
                          className={vaccine.isActive 
                            ? 'bg-green-100 text-green-800' 
                            : 'bg-red-100 text-red-800'
                          }
                        >
                          {vaccine.isActive ? 'Hoạt động' : 'Ngừng hoạt động'}
                        </Badge>
                      </TableCell>
                      <TableCell className="text-right">
                        <div className="flex justify-end space-x-2">
                          <Button
                            variant="outline"
                            size="sm"
                            onClick={() => handleOpenDialog(vaccine)}
                          >
                            <Edit className="w-4 h-4" />
                          </Button>
                          <AlertDialog>
                            <AlertDialogTrigger asChild>
                              <Button variant="outline" size="sm">
                                <Trash className="w-4 h-4" />
                              </Button>
                            </AlertDialogTrigger>
                            <AlertDialogContent>
                              <AlertDialogHeader>
                                <AlertDialogTitle>Xác nhận xóa</AlertDialogTitle>
                                <AlertDialogDescription>
                                  Bạn có chắc chắn muốn xóa vaccine "{vaccine.name}"? 
                                  Hành động này không thể hoàn tác.
                                </AlertDialogDescription>
                              </AlertDialogHeader>
                              <AlertDialogFooter>
                                <AlertDialogCancel>Hủy</AlertDialogCancel>
                                <AlertDialogAction
                                  onClick={() => handleDelete(vaccine.id)}
                                  className="bg-red-600 hover:bg-red-700"
                                >
                                  Xóa
                                </AlertDialogAction>
                              </AlertDialogFooter>
                            </AlertDialogContent>
                          </AlertDialog>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default VaccinesPage;
