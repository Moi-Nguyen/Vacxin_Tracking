export interface Facility {
  id: string;
  name: string;
  address: string;
  phone: string;
  email: string;
  description?: string;
  operatingHours?: {
    monday?: { open: string; close: string };
    tuesday?: { open: string; close: string };
    wednesday?: { open: string; close: string };
    thursday?: { open: string; close: string };
    friday?: { open: string; close: string };
    saturday?: { open: string; close: string };
    sunday?: { open: string; close: string };
  };
  location?: {
    latitude: number;
    longitude: number;
  };
  isActive: boolean;
  maxBookingsPerDay: number;
  createdBy?: string;
}

export interface FacilityRequest {
  name: string;
  address: string;
  phone: string;
  email: string;
  description?: string;
  operatingHours?: {
    monday?: { open: string; close: string };
    tuesday?: { open: string; close: string };
    wednesday?: { open: string; close: string };
    thursday?: { open: string; close: string };
    friday?: { open: string; close: string };
    saturday?: { open: string; close: string };
    sunday?: { open: string; close: string };
  };
  location?: {
    latitude: number;
    longitude: number;
  };
  maxBookingsPerDay: number;
}

export interface FacilitiesResponse {
  facilities: Facility[];
  total: number;
  totalPages: number;
  currentPage: number;
}