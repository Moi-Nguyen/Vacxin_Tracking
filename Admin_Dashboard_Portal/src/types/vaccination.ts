export interface Vaccination {
  id: string;
  userId: {
    id: string;
    name: string;
    email: string;
  };
  vaccineId: {
    id: string;
    name: string;
    manufacturer: string;
  };
  date: string;
  location: string;
  doctorId?: {
    id: string;
    name: string;
  };
  notes?: string;
}

export interface VaccinationRequest {
  userId: string;
  vaccineId: string;
  date: string;
  location: string;
  doctorId?: string;
  notes?: string;
}

export interface VaccinationResponse {
  vaccinations: Vaccination[];
  total: number;
  totalPages: number;
  currentPage: number;
}

export interface VaccinationStats {
  vaccineStats: {
    vaccineName: string;
    count: number;
  }[];
  monthlyStats: {
    month: number;
    year: number;
    count: number;
  }[];
}

export interface DashboardStats {
  totalUsers: number;
  totalVaccines: number;
  totalBookings: number;
  totalFacilities: number;
  recentBookings: any[];
  vaccineUsageStats: {
    vaccineName: string;
    count: number;
  }[];
  monthlyVaccinationStats: {
    month: string;
    count: number;
  }[];
}