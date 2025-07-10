
import React, { useState } from 'react';
import { Navigate } from 'react-router-dom';
import { User, LogIn, UserCog, Stethoscope } from 'lucide-react';
import { Button } from '../components/ui/button';
import { Input } from '../components/ui/input';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '../components/ui/card';
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '../components/ui/select';
import { useAuth } from '../context/AuthContext';
import { apiService } from '../services/api';
import { useToast } from '../hooks/use-toast';

const LoginPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [selectedRole, setSelectedRole] = useState<'admin' | 'doctor'>('admin');
  const [isLoading, setIsLoading] = useState(false);
  const { login, isAuthenticated } = useAuth();
  const { toast } = useToast();

  if (isAuthenticated) {
    return <Navigate to={selectedRole === 'admin' ? "/admin" : "/doctor"} replace />;
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!email || !password) {
      toast({
        title: "Lỗi",
        description: "Vui lòng nhập đầy đủ email và mật khẩu",
        variant: "destructive",
      });
      return;
    }

    setIsLoading(true);
    
    try {
      const response = await apiService.login({ email, password });
      
      if (!['admin', 'doctor'].includes(response.user.role)) {
        toast({
          title: "Lỗi",
          description: "Bạn không có quyền truy cập hệ thống",
          variant: "destructive",
        });
        return;
      }

      if (selectedRole === 'admin' && response.user.role !== 'admin') {
        toast({
          title: "Lỗi",
          description: "Bạn không có quyền truy cập trang quản trị",
          variant: "destructive",
        });
        return;
      }

      if (selectedRole === 'doctor' && !['admin', 'doctor'].includes(response.user.role)) {
        toast({
          title: "Lỗi",
          description: "Bạn không có quyền truy cập trang bác sĩ",
          variant: "destructive",
        });
        return;
      }

      login(response.token, response.user);
      toast({
        title: "Thành công",
        description: "Đăng nhập thành công!",
      });
    } catch (error) {
      toast({
        title: "Lỗi đăng nhập",
        description: error instanceof Error ? error.message : "Sai email hoặc mật khẩu",
        variant: "destructive",
      });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-indigo-100">
      <Card className="w-full max-w-md">
        <CardHeader className="text-center">
          <div className="flex justify-center mb-4">
            <div className={`p-3 rounded-full ${selectedRole === 'admin' ? 'bg-blue-600' : 'bg-green-600'}`}>
              {selectedRole === 'admin' ? (
                <UserCog className="w-8 h-8 text-white" />
              ) : (
                <Stethoscope className="w-8 h-8 text-white" />
              )}
            </div>
          </div>
          <CardTitle className="text-2xl font-bold text-gray-800">
            Đăng nhập {selectedRole === 'admin' ? 'Admin' : 'Bác sĩ'}
          </CardTitle>
          <CardDescription>
            Nhập thông tin đăng nhập để truy cập hệ thống
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label htmlFor="role" className="block text-sm font-medium text-gray-700 mb-1">
                Vai trò
              </label>
              <Select value={selectedRole} onValueChange={(value: 'admin' | 'doctor') => setSelectedRole(value)}>
                <SelectTrigger>
                  <SelectValue placeholder="Chọn vai trò" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="admin">
                    <div className="flex items-center gap-2">
                      <UserCog className="w-4 h-4" />
                      <span>Quản trị viên</span>
                    </div>
                  </SelectItem>
                  <SelectItem value="doctor">
                    <div className="flex items-center gap-2">
                      <Stethoscope className="w-4 h-4" />
                      <span>Bác sĩ</span>
                    </div>
                  </SelectItem>
                </SelectContent>
              </Select>
            </div>

            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-1">
                Email
              </label>
              <Input
                id="email"
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                placeholder="doctor@example.com"
                className="w-full"
                required
              />
            </div>
            
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-1">
                Mật khẩu
              </label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="Nhập mật khẩu"
                className="w-full"
                required
              />
            </div>
            
            <Button
              type="submit"
              className={`w-full ${selectedRole === 'admin' ? 'bg-blue-600 hover:bg-blue-700' : 'bg-green-600 hover:bg-green-700'}`}
              disabled={isLoading}
            >
              {isLoading ? (
                "Đang đăng nhập..."
              ) : (
                <>
                  <LogIn className="w-4 h-4 mr-2" />
                  Đăng nhập
                </>
              )}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
};

export default LoginPage;
