import React, { useState, useEffect } from 'react';
import { Calendar, Clock, Plus, Eye, Filter, Trash2 } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Badge } from '../components/ui/badge';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '../components/ui/dialog';
import { Label } from '../components/ui/label';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';
import { useToast } from '../hooks/use-toast';
import { Shift } from '../types/doctor';
import { apiService } from '../services/api';
import { useAuth } from '../context/AuthContext';

const DoctorShiftsPage = () => {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [shifts, setShifts] = useState<Shift[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isRegisterDialogOpen, setIsRegisterDialogOpen] = useState(false);
  const [newShift, setNewShift] = useState({
    shiftDate: new Date().toISOString().split('T')[0],
    shiftType: 'morning' as 'morning' | 'afternoon' | 'night'
  });

  const { toast } = useToast();
  const { user } = useAuth();

  useEffect(() => {
    fetchShifts();
  }, [selectedDate]);

  const fetchShifts = async () => {
    try {
      setIsLoading(true);
      const startDate = new Date(selectedDate);
      startDate.setMonth(startDate.getMonth() - 1);
      const endDate = new Date(selectedDate);
      endDate.setMonth(endDate.getMonth() + 1);

      const response = await apiService.getDoctorShifts({
        startDate: startDate.toISOString().split('T')[0],
        endDate: endDate.toISOString().split('T')[0]
      });
      
      setShifts(response.shifts);
    } catch (error) {
      console.error('Error fetching shifts:', error);
      toast({
        title: "Lỗi",
        description: "Không thể tải danh sách ca trực",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleRegisterShift = async () => {
    try {
      setIsLoading(true);
      await apiService.registerShift(newShift);
      
      toast({
        title: "Thành công",
        description: "Đăng ký ca trực thành công",
      });
      
      setIsRegisterDialogOpen(false);
      setNewShift({
        shiftDate: new Date().toISOString().split('T')[0],
        shiftType: 'morning'
      });
      
      fetchShifts();
    } catch (error: any) {
      console.error('Error registering shift:', error);
      toast({
        title: "Lỗi",
        description: error.message || "Không thể đăng ký ca trực",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteShift = async (shiftId: string, doctorId: string) => {
    if (doctorId !== user?.id) {
      toast({
        title: "Lỗi",
        description: "Bạn chỉ có thể hủy ca trực do mình đăng ký",
        variant: "destructive",
      });
      return;
    }

    try {
      await apiService.deleteShift(shiftId);
      
      toast({
        title: "Thành công",
        description: "Hủy ca trực thành công",
      });
      
      fetchShifts();
    } catch (error: any) {
      console.error('Error deleting shift:', error);
      toast({
        title: "Lỗi",
        description: error.message || "Không thể hủy ca trực",
        variant: "destructive",
      });
    }
  };

  const getStatusBadge = (status: string) => {
    const variants: { [key: string]: "default" | "secondary" | "destructive" | "outline" } = {
      scheduled: "outline",
      active: "default",
      completed: "secondary",
      cancelled: "destructive"
    };

    const labels: { [key: string]: string } = {
      scheduled: "Đã lên lịch",
      active: "Đang hoạt động",
      completed: "Đã hoàn thành",
      cancelled: "Đã hủy"
    };

    return (
      <Badge variant={variants[status] || "outline"}>
        {labels[status] || status}
      </Badge>
    );
  };

  const getShiftTypeLabel = (type: string) => {
    const labels: { [key: string]: string } = {
      morning: "Ca sáng",
      afternoon: "Ca chiều", 
      night: "Ca tối"
    };
    return labels[type] || type;
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-800">Quản lý ca trực</h1>
        </div>
        <div className="animate-pulse space-y-4">
          <div className="h-10 bg-gray-200 rounded"></div>
          <div className="h-64 bg-gray-200 rounded"></div>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-800">Quản lý ca trực</h1>
        <div className="flex items-center gap-4">
          <Button 
            onClick={() => setIsRegisterDialogOpen(true)}
            className="bg-green-600 hover:bg-green-700"
          >
            <Plus className="w-4 h-4 mr-2" />
            Đăng ký ca trực
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Tổng ca trực</CardTitle>
            <Calendar className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{shifts.length}</div>
            <p className="text-xs text-muted-foreground">ca đã đăng ký</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Ca đang hoạt động</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {shifts.filter(s => s.status === 'active').length}
            </div>
            <p className="text-xs text-muted-foreground">đang trong ca</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Ca đã hoàn thành</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {shifts.filter(s => s.status === 'completed').length}
            </div>
            <p className="text-xs text-muted-foreground">đã hoàn thành</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Danh sách ca trực</CardTitle>
          <CardDescription>
            Quản lý các ca trực đã đăng ký
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Ngày</TableHead>
                  <TableHead>Ca trực</TableHead>
                  <TableHead>Thời gian</TableHead>
                  <TableHead>Trạng thái</TableHead>
                  <TableHead className="text-right">Thao tác</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {shifts.map((shift) => (
                  <TableRow key={shift._id}>
                    <TableCell className="font-medium">
                      {new Date(shift.shiftDate).toLocaleDateString('vi-VN')}
                    </TableCell>
                    <TableCell>{getShiftTypeLabel(shift.shiftType)}</TableCell>
                    <TableCell>
                      {new Date(shift.startTime).toLocaleTimeString('vi-VN', { 
                        hour: '2-digit', 
                        minute: '2-digit' 
                      })} - {new Date(shift.endTime).toLocaleTimeString('vi-VN', { 
                        hour: '2-digit', 
                        minute: '2-digit' 
                      })}
                    </TableCell>
                    <TableCell>{getStatusBadge(shift.status)}</TableCell>
                    <TableCell className="text-right">
                      {shift.doctorId === user?.id && shift.status === 'scheduled' && (
                        <Button
                          variant="outline"
                          size="sm"
                          onClick={() => handleDeleteShift(shift._id, shift.doctorId)}
                        >
                          <Trash2 className="w-4 h-4 mr-1" />
                          Hủy
                        </Button>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {shifts.length === 0 && (
            <div className="text-center py-8 text-gray-500">
              Chưa có ca trực nào được đăng ký
            </div>
          )}
        </CardContent>
      </Card>

      <Dialog open={isRegisterDialogOpen} onOpenChange={setIsRegisterDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Đăng ký ca trực</DialogTitle>
            <DialogDescription>
              Chọn ngày và ca trực bạn muốn đăng ký
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div>
              <Label htmlFor="shiftDate">Ngày trực</Label>
              <Input
                id="shiftDate"
                type="date"
                value={newShift.shiftDate}
                onChange={(e) => setNewShift({ ...newShift, shiftDate: e.target.value })}
              />
            </div>
            <div>
              <Label htmlFor="shiftType">Ca trực</Label>
              <Select 
                value={newShift.shiftType} 
                onValueChange={(value) => setNewShift({ ...newShift, shiftType: value as 'morning' | 'afternoon' | 'night' })}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Chọn ca trực" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="morning">Ca sáng (06:30 - 11:30)</SelectItem>
                  <SelectItem value="afternoon">Ca chiều (11:30 - 18:30)</SelectItem>
                  <SelectItem value="night">Ca tối (18:30 - 06:30)</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsRegisterDialogOpen(false)}>
              Hủy
            </Button>
            <Button onClick={handleRegisterShift} disabled={isLoading}>
              Đăng ký
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default DoctorShiftsPage;
