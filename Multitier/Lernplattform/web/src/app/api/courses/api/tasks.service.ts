/**
 * Course Service
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * OpenAPI spec version: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
/* tslint:disable:no-unused-variable member-ordering */

import { Inject, Injectable, Optional }                      from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams,
         HttpResponse, HttpEvent }                           from '@angular/common/http';
import { CustomHttpUrlEncodingCodec }                        from '../encoder';

import { Observable }                                        from 'rxjs/Observable';

import { Task } from '../model/task';

import { BASE_PATH, COLLECTION_FORMATS }                     from '../variables';
import { Configuration }                                     from '../configuration';


@Injectable()
export class TasksService {

    protected basePath = 'http://api/course';
    public defaultHeaders = new HttpHeaders();
    public configuration = new Configuration();

    constructor(protected httpClient: HttpClient, @Optional()@Inject(BASE_PATH) basePath: string, @Optional() configuration: Configuration) {

        if (configuration) {
            this.configuration = configuration;
            this.configuration.basePath = configuration.basePath || basePath || this.basePath;

        } else {
            this.configuration.basePath = basePath || this.basePath;
        }
    }

    /**
     * @param consumes string[] mime-types
     * @return true: consumes contains 'multipart/form-data', false: otherwise
     */
    private canConsumeForm(consumes: string[]): boolean {
        const form = 'multipart/form-data';
        for (const consume of consumes) {
            if (form === consume) {
                return true;
            }
        }
        return false;
    }


    /**
     * Delete a task
     * 
     * @param courseid Course ID
     * @param taskid Task ID
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public deleteTask(courseid: string, taskid: string, observe?: 'body', reportProgress?: boolean): Observable<any>;
    public deleteTask(courseid: string, taskid: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<any>>;
    public deleteTask(courseid: string, taskid: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<any>>;
    public deleteTask(courseid: string, taskid: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (courseid === null || courseid === undefined) {
            throw new Error('Required parameter courseid was null or undefined when calling deleteTask.');
        }
        if (taskid === null || taskid === undefined) {
            throw new Error('Required parameter taskid was null or undefined when calling deleteTask.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.delete<any>(`${this.configuration.basePath}/courses/${encodeURIComponent(String(courseid))}/tasks/${encodeURIComponent(String(taskid))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Get a task
     * 
     * @param courseid Course ID
     * @param taskid Task ID
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getTask(courseid: string, taskid: string, observe?: 'body', reportProgress?: boolean): Observable<Task>;
    public getTask(courseid: string, taskid: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Task>>;
    public getTask(courseid: string, taskid: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Task>>;
    public getTask(courseid: string, taskid: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (courseid === null || courseid === undefined) {
            throw new Error('Required parameter courseid was null or undefined when calling getTask.');
        }
        if (taskid === null || taskid === undefined) {
            throw new Error('Required parameter taskid was null or undefined when calling getTask.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<Task>(`${this.configuration.basePath}/courses/${encodeURIComponent(String(courseid))}/tasks/${encodeURIComponent(String(taskid))}`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Get the tasks of a course
     * 
     * @param courseid Course ID
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public getTasks(courseid: string, observe?: 'body', reportProgress?: boolean): Observable<Array<Task>>;
    public getTasks(courseid: string, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Array<Task>>>;
    public getTasks(courseid: string, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Array<Task>>>;
    public getTasks(courseid: string, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (courseid === null || courseid === undefined) {
            throw new Error('Required parameter courseid was null or undefined when calling getTasks.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
        ];

        return this.httpClient.get<Array<Task>>(`${this.configuration.basePath}/courses/${encodeURIComponent(String(courseid))}/tasks`,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Create a task
     * 
     * @param courseid Course ID
     * @param task 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public postTask(courseid: string, task: Task, observe?: 'body', reportProgress?: boolean): Observable<Task>;
    public postTask(courseid: string, task: Task, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Task>>;
    public postTask(courseid: string, task: Task, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Task>>;
    public postTask(courseid: string, task: Task, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (courseid === null || courseid === undefined) {
            throw new Error('Required parameter courseid was null or undefined when calling postTask.');
        }
        if (task === null || task === undefined) {
            throw new Error('Required parameter task was null or undefined when calling postTask.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.post<Task>(`${this.configuration.basePath}/courses/${encodeURIComponent(String(courseid))}/tasks`,
            task,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

    /**
     * Update a task
     * 
     * @param courseid Course ID
     * @param taskid Task ID
     * @param task 
     * @param observe set whether or not to return the data Observable as the body, response or events. defaults to returning the body.
     * @param reportProgress flag to report request and response progress.
     */
    public putTask(courseid: string, taskid: string, task: Task, observe?: 'body', reportProgress?: boolean): Observable<Task>;
    public putTask(courseid: string, taskid: string, task: Task, observe?: 'response', reportProgress?: boolean): Observable<HttpResponse<Task>>;
    public putTask(courseid: string, taskid: string, task: Task, observe?: 'events', reportProgress?: boolean): Observable<HttpEvent<Task>>;
    public putTask(courseid: string, taskid: string, task: Task, observe: any = 'body', reportProgress: boolean = false ): Observable<any> {
        if (courseid === null || courseid === undefined) {
            throw new Error('Required parameter courseid was null or undefined when calling putTask.');
        }
        if (taskid === null || taskid === undefined) {
            throw new Error('Required parameter taskid was null or undefined when calling putTask.');
        }
        if (task === null || task === undefined) {
            throw new Error('Required parameter task was null or undefined when calling putTask.');
        }

        let headers = this.defaultHeaders;

        // to determine the Accept header
        let httpHeaderAccepts: string[] = [
            'application/json'
        ];
        const httpHeaderAcceptSelected: string | undefined = this.configuration.selectHeaderAccept(httpHeaderAccepts);
        if (httpHeaderAcceptSelected !== undefined) {
            headers = headers.set('Accept', httpHeaderAcceptSelected);
        }

        // to determine the Content-Type header
        const consumes: string[] = [
            'application/json'
        ];
        const httpContentTypeSelected: string | undefined = this.configuration.selectHeaderContentType(consumes);
        if (httpContentTypeSelected !== undefined) {
            headers = headers.set('Content-Type', httpContentTypeSelected);
        }

        return this.httpClient.put<Task>(`${this.configuration.basePath}/courses/${encodeURIComponent(String(courseid))}/tasks/${encodeURIComponent(String(taskid))}`,
            task,
            {
                withCredentials: this.configuration.withCredentials,
                headers: headers,
                observe: observe,
                reportProgress: reportProgress
            }
        );
    }

}