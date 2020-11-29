
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TasksService, Task } from '../api/courses/index'
import { DefaultService, FeedbackRequest, FeedbackResponse} from '../api/courses/index'
import { Router } from '@angular/router';

@Component({
  selector: 'app-task',
  templateUrl: './task.component.html',
  styleUrls: ['./task.component.css'],
  providers: [DefaultService ,TasksService]
})


export class TaskComponent implements OnInit {

  task: Task = {};

  tasksolvingform: FormGroup;

  command = "";

  sql = ["select", "from", "where", "group by", "order by", "insert into", "update"];



  tasklines = [];

  private taskid = "";
  private courseid = "";
  private formattedFeedback = "";

  feedback = [];
  constructor(private defaultService: DefaultService,
              private tasksService: TasksService,
              private route: ActivatedRoute,
              private formBuilder: FormBuilder,
              private router: Router,
              ) { }

  ngOnInit() {
    this.loadTask();


    this.tasksolvingform = this.formBuilder.group({
      solvinginput: ['', Validators.required],
    });

    this.tasksolvingform.controls['solvinginput'].setValue("");

    this.route.params.subscribe( params => {
      if (localStorage.getItem('taskid') == params['taskId']){
        this.tasksolvingform.controls['solvinginput'].setValue(localStorage.getItem('input'));
        localStorage.removeItem('taskid');
        localStorage.removeItem('input');
      }
    }

    );


  }

  get f() { return this.tasksolvingform.controls; }

  onSubmit() {



    let request:FeedbackRequest = {
        solved: this.f.solvinginput.value,
        taskid: this.taskid,
        courseid: this.courseid,
    };


    this.defaultService.putFeedback(this.courseid,this.taskid,request).subscribe(
      success => {

        let feedbacks = success.feedback.trim().split("\n");
        console.log(feedbacks);
        this.feedback = feedbacks;
      },
      error => {
        console.log(error);
      })
  }

  private loadTask() {
    this.route.params.subscribe( params =>
      this.tasksService.getTask(params['courseId'], params['taskId']).subscribe(
        task => {
          this.task = task;
          this.tasklines = task.description.split("\n");
          this.courseid = params['courseId'];
          this.taskid = params['taskId'];
        },
        error => {
          console.log(error);
        }));
    }




    sqlHighlighter(command){
        let myStyles = {};
        if (this.sql.includes(command.toLowerCase())){
          myStyles = {
            fontWeight: 'bold',
            color: 'blue',
            paddingRight: '5px',
            textTransform: 'uppercase'
          }
        }else
        {
          myStyles = {
            paddingRight: '5px'
          }
        }
        return myStyles;
    }


    split(taskline: string){
      return taskline.split(" ");
    }

    pauseTask(){
      this.route.params.subscribe( params =>
        localStorage.setItem('courseid', params['courseId']));
      localStorage.setItem('taskid', this.task.id);
      localStorage.setItem('input', this.tasksolvingform.controls['solvinginput'].value);
      alert("Kurs wurde pausiert");

    }

    previousTask(){
      this.feedback = [];
      this.tasksolvingform.controls['solvinginput'].setValue("");
      this.router.navigate(['/courses/'+this.courseid+'/tasks/'+this.task.previousTask]);
    }

    nextTask(){
      this.feedback = [];
      this.tasksolvingform.controls['solvinginput'].setValue("");
      this.router.navigate(['/courses/'+this.courseid+'/tasks/'+this.task.nextTask]);
    }

}
