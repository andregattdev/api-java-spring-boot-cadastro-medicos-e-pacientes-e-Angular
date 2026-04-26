import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { PacientesComponent } from './components/pacientes/pacientes.component';
import { DoutoresComponent } from './components/doutores/doutores.component';
import { ConsultasComponent } from './components/consultas/consultas.component';
import { EmpresasComponent } from './components/empresas/empresas.component';
import { UsuariosComponent } from './components/usuarios/usuarios.component';
import { LayoutComponent } from './components/layout/layout.component';
import { AuthGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  {
    path: '', 
    component: LayoutComponent,
    canActivate: [AuthGuard],
    children: [
      { path: 'dashboard', component: DashboardComponent },
      { path: 'pacientes', component: PacientesComponent },
      { path: 'doutores', component: DoutoresComponent },
      { path: 'consultas', component: ConsultasComponent },
      { 
          path: 'usuarios', 
          component: UsuariosComponent,
          canActivate: [AuthGuard],
          data: { roles: ['FUNCIONARIO', 'ADMINISTRADOR'] } 
      },
      {
        path: 'empresas',
        component: EmpresasComponent,
        canActivate: [AuthGuard],
        data: { roles: ['FUNCIONARIO', 'ADMINISTRADOR'] }
      }
    ]
  },
  { path: '**', redirectTo: '/login' }
];
