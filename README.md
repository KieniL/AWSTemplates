# AWSTemplates

Storing of AWS Cloudformation Templates with Diagramms for different purposes

* Dockerhosting (Installs Docker on single Instance to run Containers e.g. for Environments on CD-Pipelines)
* Small MicroService Infrastructure (Installs Docker and dockerized Eureka (Port 8761) on single Instance to run few MicroServices)
* Documentation (Creates S3-Bucket and Lambda Function for storing Documentation on Confluence)
* JiraEval (Creates an Instance with Jira and Postgres for Evaluation)
* WebServer (Installs a basic configured WebServer)
* WordPress (Creates an Instance with dockerized WordPress and MySQL)
* DailyNewsletter (See Readme in the folder for more information)
* s3-angular (See Readme in the folder for more information)
* ApplicationForm (https://bitbucket.org/lukien/workspace/projects/) Contains a react frontend hosted on s3, a lambda function to process the form and then storing in dynamodb
* eks (provision an eks cluster and then deploys an example jupyterhub)