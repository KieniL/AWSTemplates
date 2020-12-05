import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { CoursesService, CourseOverview } from '../api/courses/index'

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css'],
  providers: [CoursesService]
})
export class CoursesUserComponent implements OnInit {

  courses: CourseOverview[] = [];
  userid: string;

  constructor(private coursesService: CoursesService, private route: ActivatedRoute) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.userid = params['userid'];
    })
    this.loadCourses();
  }

  loadCourses(){
    this.coursesService.getCourses(this.userid,false).subscribe(
      courses => {
        this.courses = courses;
      },
      error => {
        console.log(error);
      });
  }

}
