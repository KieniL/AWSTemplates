import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { SchemaCreateComponent } from './schema-create/schema-create.component';
import { SchemaManageComponent } from './schema-manage/schema-manage.component';
import { CoursesComponent }   from './courses/courses.component';
import { CourseContinueComponent }   from './coursecontinue/coursecontinue.component';
import { CoursesUserComponent }   from './courses/courses-user.component';
import { CourseDetailComponent } from './course-detail/course-detail.component'
import { CourseCreateComponent } from './course-create/course-create.component'
import { CourseDeleteComponent } from './course-delete/course-delete.component'
import { CourseRestoreComponent } from './course-restore/course-restore.component'
import { TaskComponent } from './task/task.component'
import { TaskCreateComponent } from './task-create/task-create.component'
import { TaskDeleteComponent } from './task-delete/task-delete.component'
import { LoginComponent } from './login/login.component'
import { AppComponent } from './app.component';

const routes: Routes = [
  { path: 'schemacreate', component: SchemaCreateComponent },
  { path: 'schemamanage', component: SchemaManageComponent },
  { path: 'courses', component: CoursesComponent },
  { path: 'continue', component: CourseContinueComponent },
  { path: 'coursesuser', component: CoursesUserComponent },
  { path: 'coursesuser', component: CoursesComponent },
  { path: 'coursecreate', component: CourseCreateComponent },
  { path: 'coursedelete', component: CourseDeleteComponent },
  { path: 'courserestore', component: CourseRestoreComponent },
  { path: 'courses/:courseId', component: CourseDetailComponent },
  { path: 'courses/:courseId/tasks/:taskId', component: TaskComponent },
  { path: 'taskcreate', component: TaskCreateComponent },
  { path: 'taskdelete', component: TaskDeleteComponent },
  { path: 'login', component: LoginComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
