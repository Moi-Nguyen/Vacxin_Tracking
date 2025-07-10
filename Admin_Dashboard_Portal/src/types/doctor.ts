
export interface Shift {
  _id: string;
  doctorId: string;
  shiftDate: string;
  shiftType: 'morning' | 'afternoon' | 'night';
  status: 'scheduled' | 'active' | 'completed' | 'cancelled';
  startTime: string;
  endTime: string;
  location?: string;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ShiftBooking {
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
  appointmentTime: string;
  status: 'pending' | 'confirmed' | 'completed' | 'cancelled';
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ShiftBookingsResponse {
  bookings: ShiftBooking[];
  shift: Shift;
  total: number;
}

export interface ShiftRegisterRequest {
  shiftDate: string;
  shiftType: 'morning' | 'afternoon' | 'night';
}

export interface ShiftDoctor {
  doctorId: {
    name: string;
    email: string;
    phone: string;
  };
  shiftType: 'morning' | 'afternoon' | 'night';
  status: string;
  startTime: string;
  endTime: string;
}

export interface ShiftDoctorsResponse {
  totalDoctors: number;
  maxDoctors: number;
  doctors: ShiftDoctor[];
}

export interface BookingCompleteRequest {
  batchNumber: string;
  sideEffects?: string[];
  notes?: string;
}
