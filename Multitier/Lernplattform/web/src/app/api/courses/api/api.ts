export * from './courses.service';
import { CoursesService } from './courses.service';
export * from './default.service';
import { DefaultService } from './default.service';
export * from './tasks.service';
import { TasksService } from './tasks.service';
export const APIS = [CoursesService, DefaultService, TasksService];
