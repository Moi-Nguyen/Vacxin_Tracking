import { User, LoginRequest, LoginResponse, RegisterRequest } from '../types/auth';
import { Vaccine, VaccineRequest, VaccinesResponse } from '../types/vaccine';
import { 
  Booking, 
  BookingUpdateRequest, 
  BookingsResponse, 
  VaccinationHistory, 
  VaccinationHistoryRequest, 
  VaccinationHistoryResponse 
} from '../types/booking';
import { Shift, ShiftRegisterRequest, ShiftBookingsResponse } from '../types/doctor';
import { Facility, FacilityRequest, FacilitiesResponse } from '../types/facility';
import { Vaccination, VaccinationRequest, VaccinationResponse, VaccinationStats, DashboardStats } from '../types/vaccination';

const API_BASE_URL = 'http://localhost:3000/api';

class ApiService {
  getBaseUrl() {
    return API_BASE_URL;
  }

  private getAuthHeaders() {
    const adminToken = localStorage.getItem('admin_token');
    const doctorToken = localStorage.getItem('doctor_token');
    const token = adminToken || doctorToken;
    
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

  // New admin method for updating any user
  async updateUserAsAdmin(userId: string, userData: Partial<User>): Promise<{ message: string; user: User }> {
    const response = await fetch(`${API_BASE_URL}/auth/users/${userId}`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(userData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể cập nhật người dùng');
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

  // Booking APIs
  async getBookings(params?: { 
    status?: string; 
    search?: string; 
    page?: number; 
    limit?: number; 
  }): Promise<BookingsResponse> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/vaccination/admin/bookings${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách lịch hẹn');
    }

    return response.json();
  }

  async updateBookingStatus(bookingId: string, data: BookingUpdateRequest): Promise<{ message: string; booking: Booking }> {
    const response = await fetch(`${API_BASE_URL}/vaccination/admin/bookings/${bookingId}/status`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể cập nhật trạng thái lịch hẹn');
    }

    return response.json();
  }

  // Vaccination History APIs
  async getVaccinationHistory(params?: { 
    search?: string; 
    vaccineId?: string; 
    page?: number; 
    limit?: number; 
  }): Promise<VaccinationHistoryResponse> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/vaccination/admin/history${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy lịch sử tiêm chủng');
    }

    return response.json();
  }

  async createVaccinationHistory(data: VaccinationHistoryRequest): Promise<{ message: string; history: VaccinationHistory }> {
    const response = await fetch(`${API_BASE_URL}/vaccination/admin/history`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể tạo lịch sử tiêm chủng');
    }

    return response.json();
  }

  // Doctor APIs
  async getDoctorShiftBookings(params?: { date?: string }): Promise<ShiftBookingsResponse> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/doctor/shifts/bookings${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách lịch hẹn ca trực');
    }

    return response.json();
  }

  async registerShift(shiftData: { shiftDate: string; shiftType: 'morning' | 'afternoon' | 'night' }): Promise<{ message: string; shift: Shift }> {
    const response = await fetch(`${API_BASE_URL}/doctor/shifts`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(shiftData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể đăng ký ca trực');
    }

    return response.json();
  }

  async getDoctorShifts(params?: { startDate?: string; endDate?: string; page?: number; limit?: number }): Promise<{ shifts: Shift[]; total: number; totalPages: number; currentPage: number }> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/doctor/shifts${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách ca trực');
    }

    return response.json();
  }

  async deleteShift(shiftId: string): Promise<{ message: string }> {
    const response = await fetch(`${API_BASE_URL}/doctor/shifts/${shiftId}`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể hủy ca trực');
    }

    return response.json();
  }

  async completeBooking(bookingId: string, data: { batchNumber: string; sideEffects?: string[]; notes?: string }): Promise<{ message: string; booking: any }> {
    const response = await fetch(`${API_BASE_URL}/doctor/bookings/${bookingId}/complete`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể hoàn thành tiêm chủng');
    }

    return response.json();
  }

  async getShiftDoctors(): Promise<{ totalDoctors: number; maxDoctors: number; doctors: any[] }> {
    const response = await fetch(`${API_BASE_URL}/doctor/shifts/doctors`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách bác sĩ trong ca');
    }

    return response.json();
  }

  // New Admin Booking APIs
  async getAllBookings(params?: { 
    status?: string; 
    facilityId?: string; 
    startDate?: string; 
    endDate?: string; 
    page?: number; 
    limit?: number; 
  }): Promise<{ success: boolean; bookings: any[]; total: number; totalPages: number; currentPage: number }> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/booking/admin/all${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách lịch hẹn');
    }

    return response.json();
  }

  async updateBookingStatusAdmin(bookingId: string, data: { status: string; notes?: string }): Promise<{ success: boolean; message: string; booking: any }> {
    const response = await fetch(`${API_BASE_URL}/booking/${bookingId}/status`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể cập nhật trạng thái lịch hẹn');
    }

    return response.json();
  }

  // Facility Management APIs
  async getFacilities(params?: { page?: number; limit?: number }): Promise<FacilitiesResponse> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/facilities${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách cơ sở y tế');
    }

    return response.json();
  }

  async createFacility(facilityData: FacilityRequest): Promise<{ success: boolean; message: string; facility: Facility }> {
    const response = await fetch(`${API_BASE_URL}/facilities`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(facilityData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể tạo cơ sở y tế');
    }

    return response.json();
  }

  async updateFacility(facilityId: string, facilityData: Partial<FacilityRequest>): Promise<{ success: boolean; message: string; facility: Facility }> {
    const response = await fetch(`${API_BASE_URL}/facilities/${facilityId}`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(facilityData),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể cập nhật cơ sở y tế');
    }

    return response.json();
  }

  async deleteFacility(facilityId: string): Promise<{ success: boolean; message: string }> {
    const response = await fetch(`${API_BASE_URL}/facilities/${facilityId}`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể xóa cơ sở y tế');
    }

    return response.json();
  }

  async toggleFacilityStatus(facilityId: string): Promise<{ success: boolean; message: string; facility: { id: string; isActive: boolean } }> {
    const response = await fetch(`${API_BASE_URL}/facilities/${facilityId}/toggle`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể thay đổi trạng thái cơ sở y tế');
    }

    return response.json();
  }

  // Vaccination Management APIs
  async getAllVaccinations(params?: { 
    search?: string; 
    startDate?: string; 
    endDate?: string; 
    page?: number; 
    limit?: number; 
  }): Promise<VaccinationResponse> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/vaccination/admin/all${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy danh sách lịch sử tiêm chủng');
    }

    return response.json();
  }

  async getVaccinationStats(params?: { startDate?: string; endDate?: string }): Promise<VaccinationStats> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/vaccination/admin/stats${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy thống kê tiêm chủng');
    }

    return response.json();
  }

  async getVaccinationsByVaccine(vaccineId: string, params?: { page?: number; limit?: number }): Promise<VaccinationResponse> {
    const queryString = params ? this.buildQueryString(params) : '';
    const response = await fetch(`${API_BASE_URL}/vaccination/admin/vaccine/${vaccineId}${queryString}`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy lịch sử tiêm theo vaccine');
    }

    return response.json();
  }

  async createVaccination(data: VaccinationRequest): Promise<{ message: string; vaccination: Vaccination }> {
    const response = await fetch(`${API_BASE_URL}/vaccination`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể tạo lịch sử tiêm chủng');
    }

    return response.json();
  }

  async updateVaccination(vaccinationId: string, data: Partial<VaccinationRequest>): Promise<{ message: string; vaccination: Vaccination }> {
    const response = await fetch(`${API_BASE_URL}/vaccination/${vaccinationId}`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể cập nhật lịch sử tiêm chủng');
    }

    return response.json();
  }

  async deleteVaccination(vaccinationId: string): Promise<{ message: string }> {
    const response = await fetch(`${API_BASE_URL}/vaccination/${vaccinationId}`, {
      method: 'DELETE',
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể xóa lịch sử tiêm chủng');
    }

    return response.json();
  }

  // Toggle vaccine status
  async toggleVaccineStatus(vaccineId: string): Promise<{ message: string; vaccine: { id: string; isActive: boolean } }> {
    const response = await fetch(`${API_BASE_URL}/vaccines/${vaccineId}/toggle`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.message || 'Không thể thay đổi trạng thái vaccine');
    }

    return response.json();
  }

  // Dashboard Stats API
  async getDashboardStats(): Promise<DashboardStats> {
    const response = await fetch(`${API_BASE_URL}/admin/dashboard/stats`, {
      headers: this.getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error('Không thể lấy thống kê dashboard');
    }

    return response.json();
  }
}

export const apiService = new ApiService();
