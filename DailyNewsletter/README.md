# Blogpost

Mailaddresses can be stored in dynamodb


On a trigger (not defined in this example) the dynamo-function is called which then reads all mailaddresses and adds them to the queue.<br/>

If the queue is inserted the sqs-function is called which then reads the mailaddress and tries to send the mail with SES




For deploying the infrastruktur run the terraform file by first installing terraform with install.sh.

Export the secret and the access key with this:
export AWS_ACCESS_KEY_ID=(your access key id)
export AWS_SECRET_ACCESS_KEY=(your secret access key)

Configure variables.tf file if needed

Run then make init to initalize terraform followed by <br/>
make plan for test validation and after validation <br/>
make run to apply the infrastructure<br/>
If you need to remove the infrastructure run make remove.

For the functions python is used with a virtualenv.
The file (named lambda_function.py should create a lambda_handler(event, context) function be located in site-packages folder)
Run this command in the site-packages directory to zip the code: zip -r9 ~/FILENAME.zip .


## Workspace commands to use the configuration for multiple environments
The workspace will be used for tagging the services and also add the workspace as a prepend

### make list-workspace
Show available workspaces


### make new-workspace name=YOURNAME
Creates a new workspace with the name specified


### make select-workspace name=YOURNAME
select the named workspace


### make remove-workspace name=YOURNAME
remove the named workspace



# Cost Analysis:
The storage pricing for dynamoDB is based on the amount of mailaddresses stored in db
SQS can be 0,4 $ per 1 Million Requests
Dynamo Function is one Function Call to fill in the SQS which will be less than SQS Function and can be improved by putting more MB (MB and execution Time needs to be analyzed)
SQS Funciton can be expensive since every single entry in sqs triggers one Function call