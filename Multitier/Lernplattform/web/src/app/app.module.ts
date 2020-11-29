import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CoursesComponent } from './courses/courses.component';
import { CoursesUserComponent } from './courses/courses-user.component';

import { ApiRequestInterceptor } from './api/apirequestinterceptor';
import { BASE_PATH as COURSES_BASE_PATH } from './api/courses/index';
import { BASE_PATH as USERS_BASE_PATH } from './api/users/index';
import { BASE_PATH as DATABASES_BASE_PATH } from './api/database/index';
import { environment } from '../environments/environment';
import { CourseDetailComponent } from './course-detail/course-detail.component';
import { CourseCreateComponent } from './course-create/course-create.component';
import { CourseDeleteComponent } from './course-delete/course-delete.component';
import { CourseRestoreComponent } from './course-restore/course-restore.component';
import { TaskComponent } from './task/task.component';
import { TaskCreateComponent } from './task-create/task-create.component';
import { TaskDeleteComponent } from './task-delete/task-delete.component';
import { LoginComponent } from './login/login.component';
import { CourseContinueComponent } from './coursecontinue/coursecontinue.component';
import { SchemaCreateComponent } from './schema-create/schema-create.component';
import { SchemaManageComponent } from './schema-manage/schema-manage.component';

@NgModule({
  declarations: [
    AppComponent,
    CoursesComponent,
    CoursesUserComponent,
    CourseDetailComponent,
    CourseCreateComponent,
    CourseDeleteComponent,
    CourseRestoreComponent,
    TaskComponent,
    TaskCreateComponent,
    TaskDeleteComponent,
    LoginComponent,
    CourseContinueComponent,
    SchemaCreateComponent,
    SchemaManageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule
  ],
  providers: [
    { provide: COURSES_BASE_PATH, useValue: environment.API_BASE_PATH + environment.COURSES_SERVICE_PATH },
    { provide: USERS_BASE_PATH, useValue: environment.API_BASE_PATH + environment.USERS_SERVICE_PATH },
    { provide: DATABASES_BASE_PATH, useValue: environment.API_BASE_PATH + environment.DATABASE_SERVICE_PATH },
    { provide: HTTP_INTERCEPTORS, useClass: ApiRequestInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
