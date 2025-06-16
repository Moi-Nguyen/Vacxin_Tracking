
export interface Vaccine {
  id: string;
  name: string;
  manufacturer: string;
  description: string;
  dosage: string;
  storageTemperature: number;
  shelfLife: number;
  quantity: number;
  sideEffects: string[];
  contraindications: string[];
  isActive: boolean;
}

export interface VaccineRequest {
  name: string;
  manufacturer: string;
  description: string;
  dosage: string;
  storageTemperature: number;
  shelfLife: number;
  quantity: number;
  sideEffects: string[];
  contraindications: string[];
}

export interface VaccinesResponse {
  vaccines: Vaccine[];
  totalPages: number;
  currentPage: number;
}
