
import React, { useState, useEffect } from 'react';
import { Calendar, Clock, Plus, Eye, CheckCircle, User } from 'lucide-react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Badge } from '../components/ui/badge';
import { Dialog, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '../components/ui/dialog';
import { Label } from '../components/ui/label';
import { Textarea } from '../components/ui/textarea';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '../components/ui/table';
import { useToast } from '../hooks/use-toast';
import { ShiftBooking } from '../types/doctor';
import { apiService } from '../services/api';

const DoctorBookingsPage = () => {
  const [selectedDate, setSelectedDate] = useState(new Date().toISOString().split('T')[0]);
  const [shiftBookings, setShiftBookings] = useState<ShiftBooking[]>([]);
  const [selectedBookings, setSelectedBookings] = useState<Set<string>>(new Set());
  const [isLoading, setIsLoading] = useState(false);
  const [isCompleteDialogOpen, setIsCompleteDialogOpen] = useState(false);
  const [isViewDialogOpen, setIsViewDialogOpen] = useState(false);
  const [selectedBooking, setSelectedBooking] = useState<ShiftBooking | null>(null);
  const [completionData, setCompletionData] = useState({
    batchNumber: '',
    sideEffects: [] as string[],
    notes: ''
  });

  const { toast } = useToast();

  useEffect(() => {
    fetchShiftBookings();
  }, [selectedDate]);

  const fetchShiftBookings = async () => {
    try {
      setIsLoading(true);
      const response = await apiService.getDoctorShiftBookings({ date: selectedDate });
      setShiftBookings(response.bookings);
    } catch (error: any) {
      console.error('Error fetching shift bookings:', error);
      toast({
        title: "Lỗi",
        description: error.message || "Không thể tải danh sách lịch hẹn",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const toggleBookingSelection = (bookingId: string) => {
    const newSelected = new Set(selectedBookings);
    if (newSelected.has(bookingId)) {
      newSelected.delete(bookingId);
    } else {
      newSelected.add(bookingId);
    }
    setSelectedBookings(newSelected);
  };

  const selectAllBookings = () => {
    const confirmableBookings = shiftBookings.filter(b => b.status === 'confirmed');
    setSelectedBookings(new Set(confirmableBookings.map(b => b._id)));
  };

  const clearSelection = () => {
    setSelectedBookings(new Set());
  };

  const handleCompleteVaccination = async () => {
    if (selectedBookings.size === 0) {
      toast({
        title: "Lỗi",
        description: "Vui lòng chọn ít nhất một lịch hẹn để hoàn thành",
        variant: "destructive",
      });
      return;
    }

    try {
      setIsLoading(true);
      
      // Complete each selected booking
      for (const bookingId of selectedBookings) {
        await apiService.completeBooking(bookingId, completionData);
      }

      toast({
        title: "Thành công",
        description: `Đã hoàn thành tiêm chủng cho ${selectedBookings.size} lịch hẹn`,
      });
      
      setIsCompleteDialogOpen(false);
      setSelectedBookings(new Set());
      setCompletionData({
        batchNumber: '',
        sideEffects: [],
        notes: ''
      });
      
      fetchShiftBookings();
    } catch (error: any) {
      console.error('Error completing vaccination:', error);
      toast({
        title: "Lỗi",
        description: error.message || "Không thể hoàn thành tiêm chủng",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  const getStatusBadge = (status: string) => {
    const variants: { [key: string]: "default" | "secondary" | "destructive" | "outline" } = {
      pending: "outline",
      confirmed: "default",
      completed: "secondary",
      cancelled: "destructive"
    };

    const labels: { [key: string]: string } = {
      pending: "Chờ xử lý",
      confirmed: "Đã xác nhận",
      completed: "Đã hoàn thành",
      cancelled: "Đã hủy"
    };

    return (
      <Badge variant={variants[status] || "outline"}>
        {labels[status] || status}
      </Badge>
    );
  };

  const viewBookingDetails = (booking: ShiftBooking) => {
    setSelectedBooking(booking);
    setIsViewDialogOpen(true);
  };

  const addSideEffect = (effect: string) => {
    if (effect.trim() && !completionData.sideEffects.includes(effect.trim())) {
      setCompletionData({
        ...completionData,
        sideEffects: [...completionData.sideEffects, effect.trim()]
      });
    }
  };

  const removeSideEffect = (effect: string) => {
    setCompletionData({
      ...completionData,
      sideEffects: completionData.sideEffects.filter(e => e !== effect)
    });
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-800">Lịch hẹn hôm nay</h1>
        </div>
        <div className="animate-pulse space-y-4">
          <div className="h-10 bg-gray-200 rounded"></div>
          <div className="h-64 bg-gray-200 rounded"></div>
        </div>
      </div>
    );
  }

  const selectedBookingsList = shiftBookings.filter(b => selectedBookings.has(b._id));

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold text-gray-800">Lịch hẹn hôm nay</h1>
        <div className="flex items-center gap-4">
          <div className="flex items-center gap-2">
            <Calendar className="w-4 h-4" />
            <Input
              type="date"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              className="w-auto"
            />
          </div>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-4 gap-6">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Tổng lịch hẹn</CardTitle>
            <Calendar className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{shiftBookings.length}</div>
            <p className="text-xs text-muted-foreground">trong ngày {selectedDate}</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Đã xác nhận</CardTitle>
            <Clock className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {shiftBookings.filter(b => b.status === 'confirmed').length}
            </div>
            <p className="text-xs text-muted-foreground">sẵn sàng tiêm</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Đã chọn</CardTitle>
            <User className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{selectedBookings.size}</div>
            <p className="text-xs text-muted-foreground">bệnh nhân đã chọn</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Đã hoàn thành</CardTitle>
            <CheckCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {shiftBookings.filter(b => b.status === 'completed').length}
            </div>
            <p className="text-xs text-muted-foreground">đã tiêm xong</p>
          </CardContent>
        </Card>
      </div>

      {selectedBookings.size > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Bệnh nhân đã chọn ({selectedBookings.size})</CardTitle>
            <CardDescription>
              Danh sách bệnh nhân đã chọn để tiêm chủng
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="space-y-2 mb-4">
              {selectedBookingsList.map((booking) => (
                <div key={booking._id} className="flex items-center justify-between p-3 bg-blue-50 rounded-lg">
                  <div>
                    <div className="font-medium">{booking.userId.fullName}</div>
                    <div className="text-sm text-gray-600">{booking.vaccineId.name} - {booking.appointmentTime}</div>
                  </div>
                  <Button
                    variant="outline"
                    size="sm"
                    onClick={() => toggleBookingSelection(booking._id)}
                  >
                    Bỏ chọn
                  </Button>
                </div>
              ))}
            </div>
            <div className="flex gap-2">
              <Button onClick={() => setIsCompleteDialogOpen(true)} className="bg-green-600 hover:bg-green-700">
                <CheckCircle className="w-4 h-4 mr-2" />
                Hoàn thành tiêm chủng
              </Button>
              <Button variant="outline" onClick={clearSelection}>
                Bỏ chọn tất cả
              </Button>
            </div>
          </CardContent>
        </Card>
      )}

      <Card>
        <CardHeader>
          <CardTitle>Lịch hẹn trong ca trực</CardTitle>
          <CardDescription>
            Danh sách lịch hẹn ngày {new Date(selectedDate).toLocaleDateString('vi-VN')}
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex justify-between mb-4">
            <Button variant="outline" onClick={selectAllBookings}>
              Chọn tất cả lịch hẹn đã xác nhận
            </Button>
          </div>
          
          <div className="rounded-md border">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead className="w-12">Chọn</TableHead>
                  <TableHead>Thời gian</TableHead>
                  <TableHead>Bệnh nhân</TableHead>
                  <TableHead>Vaccine</TableHead>
                  <TableHead>Trạng thái</TableHead>
                  <TableHead className="text-right">Thao tác</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {shiftBookings.map((booking) => (
                  <TableRow key={booking._id}>
                    <TableCell>
                      {booking.status === 'confirmed' && (
                        <input
                          type="checkbox"
                          checked={selectedBookings.has(booking._id)}
                          onChange={() => toggleBookingSelection(booking._id)}
                          className="w-4 h-4"
                        />
                      )}
                    </TableCell>
                    <TableCell className="font-medium">{booking.appointmentTime}</TableCell>
                    <TableCell>
                      <div>
                        <div className="font-medium">{booking.userId.fullName}</div>
                        <div className="text-sm text-muted-foreground">{booking.userId.phone}</div>
                      </div>
                    </TableCell>
                    <TableCell>
                      <div>
                        <div className="font-medium">{booking.vaccineId.name}</div>
                        <div className="text-sm text-muted-foreground">{booking.vaccineId.manufacturer}</div>
                      </div>
                    </TableCell>
                    <TableCell>{getStatusBadge(booking.status)}</TableCell>
                    <TableCell className="text-right">
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => viewBookingDetails(booking)}
                      >
                        <Eye className="w-4 h-4 mr-1" />
                        Xem
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>

          {shiftBookings.length === 0 && (
            <div className="text-center py-8 text-gray-500">
              Không có lịch hẹn nào trong ngày này
            </div>
          )}
        </CardContent>
      </Card>

      <Dialog open={isCompleteDialogOpen} onOpenChange={setIsCompleteDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Hoàn thành tiêm chủng</DialogTitle>
            <DialogDescription>
              Nhập thông tin hoàn thành tiêm chủng cho {selectedBookings.size} bệnh nhân
            </DialogDescription>
          </DialogHeader>
          <div className="space-y-4">
            <div>
              <Label htmlFor="batchNumber">Số lô vaccine</Label>
              <Input
                id="batchNumber"
                value={completionData.batchNumber}
                onChange={(e) => setCompletionData({ ...completionData, batchNumber: e.target.value })}
                placeholder="Nhập số lô vaccine"
              />
            </div>
            
            <div>
              <Label>Tác dụng phụ</Label>
              <div className="space-y-2">
                <Input
                  placeholder="Nhập tác dụng phụ và nhấn Enter"
                  onKeyPress={(e) => {
                    if (e.key === 'Enter') {
                      addSideEffect(e.currentTarget.value);
                      e.currentTarget.value = '';
                    }
                  }}
                />
                <div className="flex flex-wrap gap-2">
                  {completionData.sideEffects.map((effect, index) => (
                    <Badge key={index} variant="outline" className="cursor-pointer" onClick={() => removeSideEffect(effect)}>
                      {effect} ×
                    </Badge>
                  ))}
                </div>
              </div>
            </div>
            
            <div>
              <Label htmlFor="notes">Ghi chú</Label>
              <Textarea
                id="notes"
                value={completionData.notes}
                onChange={(e) => setCompletionData({ ...completionData, notes: e.target.value })}
                placeholder="Nhập ghi chú (tùy chọn)"
              />
            </div>
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsCompleteDialogOpen(false)}>
              Hủy
            </Button>
            <Button onClick={handleCompleteVaccination} disabled={!completionData.batchNumber}>
              Hoàn thành
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <Dialog open={isViewDialogOpen} onOpenChange={setIsViewDialogOpen}>
        <DialogContent className="sm:max-w-md">
          <DialogHeader>
            <DialogTitle>Chi tiết lịch hẹn</DialogTitle>
            <DialogDescription>
              Thông tin chi tiết về lịch hẹn tiêm chủng
            </DialogDescription>
          </DialogHeader>
          {selectedBooking && (
            <div className="space-y-4">
              <div>
                <Label className="text-sm font-medium">Bệnh nhân</Label>
                <p className="text-sm">{selectedBooking.userId.fullName}</p>
                <p className="text-sm text-gray-600">{selectedBooking.userId.email}</p>
                <p className="text-sm text-gray-600">{selectedBooking.userId.phone}</p>
              </div>
              <div>
                <Label className="text-sm font-medium">Vaccine</Label>
                <p className="text-sm">{selectedBooking.vaccineId.name}</p>
                <p className="text-sm text-gray-600">{selectedBooking.vaccineId.manufacturer}</p>
              </div>
              <div>
                <Label className="text-sm font-medium">Thời gian hẹn</Label>
                <p className="text-sm">{selectedBooking.appointmentTime} - {new Date(selectedBooking.appointmentDate).toLocaleDateString('vi-VN')}</p>
              </div>
              <div>
                <Label className="text-sm font-medium">Trạng thái</Label>
                <div className="mt-1">{getStatusBadge(selectedBooking.status)}</div>
              </div>
              {selectedBooking.notes && (
                <div>
                  <Label className="text-sm font-medium">Ghi chú</Label>
                  <p className="text-sm">{selectedBooking.notes}</p>
                </div>
              )}
            </div>
          )}
          <DialogFooter>
            <Button variant="outline" onClick={() => setIsViewDialogOpen(false)}>
              Đóng
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default DoctorBookingsPage;
