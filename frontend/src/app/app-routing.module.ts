import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';

const routes: Routes = [
  { path: 'customer', loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule) }, 
  { path: 'admin', loadChildren: () => import('./admin/admin.module').then(m => m.AdminModule) } , 
  { path : 'login' , component : LoginComponent } , 
  { path : 'register' , component : RegisterComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
