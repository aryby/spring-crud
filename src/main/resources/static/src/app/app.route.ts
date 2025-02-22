import { Routes } from '@angular/router';

// dashboard
import { IndexComponent } from './index';
import { AppLayout } from './layouts/app-layout';
import { AuthLayout } from './layouts/auth-layout';
import { UtilisateurComponent } from './mvc/components/utilisateur/utilisateur.component';

export const routes: Routes = [
    {
        path: '',
        component: AppLayout,
        children: [
            // dashboard
            { path: '', component: IndexComponent, data: { title: 'Sales Admin' } },
            { path: 'test', component: UtilisateurComponent, data: { title: 'Sales Admin' } },
        ],
    },

    {
        path: '',
        component: AuthLayout,
        children: [],
    },
];
