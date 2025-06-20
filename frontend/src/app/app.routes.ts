import {Routes} from '@angular/router';
import {LogInComponent} from './pages/log-in/log-in.component';
import {RegisterComponent} from './pages/register/register.component';
import {HomeComponent} from './pages/home/home.component';
import {DashboardComponent} from './pages/dashboard/dashboard.component';
import {MenuComponent} from './pages/menu/menu.component';
import {UpdateDiagnosisComponent} from './components/update-diagnosis/update-diagnosis.component';
import {AdminQueryComponent} from './pages/admin-query/admin-query.component';

export const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'login', component: LogInComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'menu', component: MenuComponent },
  { path: 'add-diagnosis/:id', component: UpdateDiagnosisComponent },
  { path: 'admin-query', component: AdminQueryComponent }
];
