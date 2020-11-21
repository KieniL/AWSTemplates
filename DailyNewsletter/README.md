# Blogpost

# In Progress since lambda can't store logs and so the function goes to error
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

For the functions python is used with a virtualenv ()
