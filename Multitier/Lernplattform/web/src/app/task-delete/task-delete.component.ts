import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoursesService, CourseOverview, TasksService, Task } from '../api/courses/index';

@Component({
  selector: 'app-task-delete',
  templateUrl: './task-delete.component.html',
  styleUrls: ['./task-delete.component.css'],
  providers: [CoursesService, TasksService]
})
export class TaskDeleteComponent implements OnInit {

  deleteTaskForm: FormGroup;
  courses: CourseOverview[] = [];
  tasks: any;

  selectedcourse:any;
  selectedtask: any;

  constructor(private coursesService: CoursesService,
              private taskService: TasksService,
              private router: Router,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loadCourses();

    this.deleteTaskForm = this.formBuilder.group({
      selectedcourse: ['', Validators.required],
      selectedtask: ['', Validators.required],
    });
  }

  onSubmit() {
    this.taskService.deleteTask(this.f.selectedcourse.value, this.f.selectedtask.value).subscribe(
      success => {
        this.router.navigate(['/']);
      },
      error => {
        console.log(error);
      })
  }

  get f() { return this.deleteTaskForm.controls; }

  loadCourses(){
    this.coursesService.getCourses(localStorage.getItem('user'),false).subscribe(
      courses => {
        this.courses = courses;
      },
      error => {
        console.log(error);
      });

    }

    changedValue(courseValue) {
      if (courseValue  == null )
      {
          this.tasks = null;
      }else
      {
        this.selectedcourse = courseValue;
        this.coursesService.getCourse(courseValue).subscribe(
          course => {
            this.tasks = course.tasks;
          },
          error => {
            console.log(error);
          });
      }
    }

    emptyCheck()
    {
      return this.tasks == null;
    }

}
