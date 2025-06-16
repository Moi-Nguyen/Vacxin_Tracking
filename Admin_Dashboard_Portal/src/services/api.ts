
import { User, LoginRequest, LoginResponse, RegisterRequest } from '../types/auth';
import { Vaccine, VaccineRequest, VaccinesResponse } from '../types/vaccine';

const API_BASE_URL = 'http://localhost:3000/api';

class ApiService {
  getBaseUrl() {
    return API_BASE_URL;
  }

  private getAuthHeaders() {
    const token = localStorage.getItem('admin_token');
    return {
      'Content-Type': 'application/json',
      ...(token && { Authorization: `Bearer ${token}` }),
    };
  }

  // Auth APIs
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await fetch(`${API_BASE_URL}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Đăng nhập thất bại');
    }

    return response.json();
  }

  // User APIs
  async getAllUsers(): Promise<User[]> {
    const response = await fetch(`${API_BASE_URL}/auth/users/all`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách tất cả người dùng');
    }

    return response.json();
  }

  async getUsers(params?: { search?: string; role?: string; page?: number; limit?: number }): Promise<{ users: User[]; total: number; totalPages: number; currentPage: number }> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/auth/users${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách người dùng');
    }

    return response.json();
  }

  private buildQueryString(params: Record<string, any>): string {
    const searchParams = new URLSearchParams();
    
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
        searchParams.append(key, value.toString());
      }
    });

    const queryString = searchParams.toString();
    return queryString ? `?${queryString}` : '';
  }

  async createUser(userData: RegisterRequest): Promise<{ message: string; user: User }> {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(userData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể tạo người dùng');
    }

    return response.json();
  }

  async deleteUser(userId: string): Promise<{ message: string }> {
    const response = await fetch(`${API_BASE_URL}/auth/users/${userId}`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể xóa người dùng');
    }

    return response.json();
  }

  async updateUser(userData: Partial<User>): Promise<{ message: string; user: User }> {
    const response = await fetch(`${API_BASE_URL}/auth/profile`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(userData),
    });

    if (!response.ok) {
      throw new Error('Không thể cập nhật người dùng');
    }

    return response.json();
  }

  // Vaccine APIs
  async getVaccines(): Promise<VaccinesResponse> {
    const response = await fetch(`${API_BASE_URL}/vaccines`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách vaccine');
    }

    return response.json();
  }

  async createVaccine(vaccineData: VaccineRequest): Promise<{ message: string; vaccine: Vaccine }> {
    const response = await fetch(`${API_BASE_URL}/vaccines`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(vaccineData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể tạo vaccine');
    }

    return response.json();
  }

  async deleteVaccine(vaccineId: string): Promise<{ message: string }> {
    const response = await fetch(`${API_BASE_URL}/vaccines/${vaccineId}`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể xóa vaccine');
    }

    return response.json();
  }

  async updateVaccine(vaccineId: string, vaccineData: VaccineRequest): Promise<{ message: string; vaccine: Vaccine }> {
    const response = await fetch(`${API_BASE_URL}/vaccines/${vaccineId}`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(vaccineData),
    });

    if (!response.ok) {
      throw new Error('Không thể cập nhật vaccine');
    }

    return response.json();
  }
}

export const apiService = new ApiService();
