
export interface User {
  id: string;
  email: string;
  name: string;
  fullName?: string;
  age?: number;
  dob?: string;
  address?: string;
  phone?: string;
  role: 'admin' | 'doctor' | 'user';
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  message: string;
  token: string;
  user: User;
}

export interface RegisterRequest {
  name: string;
  email: string;
  password: string;
  fullName?: string;
  age?: number;
  dob?: string;
  address?: string;
  phone?: string;
  role?: 'admin' | 'doctor' | 'user';
}
