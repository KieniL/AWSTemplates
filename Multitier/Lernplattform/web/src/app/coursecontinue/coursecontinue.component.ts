import { Component, OnInit } from '@angular/core';

import { CoursesService, Course } from '../api/courses/index'

@Component({
  selector: 'app-courses',
  templateUrl: './coursecontinue.component.html',
  styleUrls: ['./coursecontinue.component.css'],
  providers: [CoursesService]
})
export class CourseContinueComponent implements OnInit {

  course: Course = null;

  task;

  courseId;
  taskid;
  value;

  constructor(private coursesService: CoursesService) {}

  ngOnInit() {

    this.courseId = localStorage.getItem('courseid');
    this.taskid  = localStorage.getItem('taskid');
    this.value = localStorage.getItem('input');

    localStorage.removeItem('courseid');
    this.loadCourses();
  }

  loadCourses(){
    this.coursesService.getCourse(this.courseId).subscribe(
      courses => {
        this.course = courses;
        for (var index in this.course.tasks) {
          if (this.course.tasks[index].id == this.taskid){
            this.task = Number (index) + 1;
            localStorage.setItem('taskid', this.task);
          }
        }
      },
      error => {
        console.log(error);
      });


  }

}
