
export interface Booking {
  _id: string;
  userId: {
    _id: string;
    name: string;
    email: string;
    fullName: string;
    phone: string;
  };
  vaccineId: {
    _id: string;
    name: string;
    manufacturer: string;
  };
  appointmentDate: string;
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
  batchNumber?: string;
  sideEffects?: string[];
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface BookingUpdateRequest {
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
  batchNumber?: string;
  sideEffects?: string[];
  notes?: string;
}

export interface VaccinationHistory {
  _id: string;
  userId: {
    _id: string;
    name: string;
    email: string;
    fullName: string;
    phone: string;
  };
  vaccineId: {
    _id: string;
    name: string;
    manufacturer: string;
  };
  doseNumber: number;
  vaccinationDate: string;
  batchNumber: string;
  sideEffects?: string[];
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface VaccinationHistoryRequest {
  userId: string;
  vaccineId: string;
  doseNumber: number;
  vaccinationDate: string;
  batchNumber: string;
  sideEffects?: string[];
  notes?: string;
}

export interface BookingsResponse {
  bookings: Booking[];
  total: number;
  totalPages: number;
  currentPage: number;
}

export interface VaccinationHistoryResponse {
  history: VaccinationHistory[];
  total: number;
  totalPages: number;
  currentPage: number;
}
