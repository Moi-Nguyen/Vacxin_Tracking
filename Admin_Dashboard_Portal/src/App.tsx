
import { Toaster } from "@/components/ui/toaster";
import { Toaster as Sonner } from "@/components/ui/sonner";
import { TooltipProvider } from "@/components/ui/tooltip";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import AdminLayout from "./components/Layout/AdminLayout";
import DoctorLayout from "./components/Layout/DoctorLayout";
import LoginPage from "./pages/LoginPage";
import DashboardPage from "./pages/DashboardPage";
import DoctorDashboard from "./pages/DoctorDashboard";
import DoctorShiftsPage from "./pages/DoctorShiftsPage";
import DoctorBookingsPage from "./pages/DoctorBookingsPage";
import UsersPage from "./pages/UsersPage";
import VaccinesPage from "./pages/VaccinesPage";
import BookingsPage from "./pages/BookingsPage";
import FacilitiesPage from "./pages/FacilitiesPage";
import VaccinationHistoryPage from "./pages/VaccinationHistoryPage";
import VaccinationStatsPage from "./pages/VaccinationStatsPage";
import NotFound from "./pages/NotFound";

const queryClient = new QueryClient();

const App = () => (
  <QueryClientProvider client={queryClient}>
    <TooltipProvider>
      <AuthProvider>
        <Toaster />
        <Sonner />
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            
            {/* Admin Routes */}
            <Route path="/admin" element={
              <AdminLayout>
                <DashboardPage />
              </AdminLayout>
            } />
            <Route path="/admin/users" element={
              <AdminLayout>
                <UsersPage />
              </AdminLayout>
            } />
            <Route path="/admin/vaccines" element={
              <AdminLayout>
                <VaccinesPage />
              </AdminLayout>
            } />
            <Route path="/admin/bookings" element={
              <AdminLayout>
                <BookingsPage />
              </AdminLayout>
            } />
            <Route path="/admin/facilities" element={
              <AdminLayout>
                <FacilitiesPage />
              </AdminLayout>
            } />
            <Route path="/admin/vaccination-history" element={
              <AdminLayout>
                <VaccinationHistoryPage />
              </AdminLayout>
            } />
            <Route path="/admin/stats" element={
              <AdminLayout>
                <VaccinationStatsPage />
              </AdminLayout>
            } />
            
            {/* Doctor Routes */}
            <Route path="/doctor" element={
              <DoctorLayout>
                <DoctorDashboard />
              </DoctorLayout>
            } />
            <Route path="/doctor/shifts" element={
              <DoctorLayout>
                <DoctorShiftsPage />
              </DoctorLayout>
            } />
            <Route path="/doctor/bookings" element={
              <DoctorLayout>
                <DoctorBookingsPage />
              </DoctorLayout>
            } />
            
            <Route path="/" element={<LoginPage />} />
            <Route path="*" element={<NotFound />} />
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </TooltipProvider>
  </QueryClientProvider>
);

export default App;
