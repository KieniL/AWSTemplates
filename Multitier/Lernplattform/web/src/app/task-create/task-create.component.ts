import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoursesService, CourseOverview, TasksService, Task } from '../api/courses/index';

@Component({
  selector: 'app-task-create',
  templateUrl: './task-create.component.html',
  styleUrls: ['./task-create.component.css'],
  providers: [CoursesService, TasksService]
})
export class TaskCreateComponent implements OnInit {

  newTaskForm: FormGroup;
  courses: CourseOverview[] = [];

  selectedcourse:any;
  filtered: any;

  constructor(private coursesService: CoursesService,
              private taskService: TasksService,
              private router: Router,
              private formBuilder: FormBuilder) { }

  ngOnInit() {
    this.loadCourses();

    this.newTaskForm = this.formBuilder.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      selectedcourse: ['', Validators.required],
      code: ['', Validators.required]
    });
  }

  onSubmit() {

    let task:Task = {
      name: this.f.name.value,
      description: this.f.description.value,
      code: this.f.code.value
    };

    this.taskService.postTask(this.f.selectedcourse.value, task).subscribe(
      success => {
        this.router.navigate(['/']);
      },
      error => {
        console.log(error);
      })
  }

  get f() { return this.newTaskForm.controls; }

  loadCourses(){
    this.coursesService.getCourses(localStorage.getItem('user'),false).subscribe(
      courses => {
        this.courses = courses;
      },
      error => {
        console.log(error);
      });
    }


    onOptionsSelected() {
          this.filtered = this.courses.filter(t=>t.name ==this.selectedcourse);

        }
}
