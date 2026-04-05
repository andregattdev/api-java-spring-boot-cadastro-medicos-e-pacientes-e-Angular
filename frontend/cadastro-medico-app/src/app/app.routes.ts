import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PacientesComponent } from './components/pacientes/pacientes.component';
import { DoutoresComponent } from './components/doutores/doutores.component';
import { ConsultasComponent } from './components/consultas/consultas.component';
import { EmpresasComponent } from './components/empresas/empresas.component';
import { UsuariosComponent } from './components/usuarios/usuarios.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  {
    path: 'dashboard',
    component: DashboardComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'pacientes',
    component: PacientesComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'doutores',
    component: DoutoresComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'consultas',
    component: ConsultasComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'usuarios',
    component: UsuariosComponent,
    canActivate: [AuthGuard]
  },
  {
    path: 'empresas',
    component: EmpresasComponent,
    canActivate: [AuthGuard]
  },
  { path: '**', redirectTo: '/login' }
];
