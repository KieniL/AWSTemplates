var bucketName = "docu-bucket";
var bucketRegion = "eu-central-1";
var IdentityPoolId = "eu-central-1:bfe5c7ee-b296-4053-83ef-b3978d1ef62a";

AWS.config.update({
  region: bucketRegion,
  credentials: new AWS.CognitoIdentityCredentials({
    IdentityPoolId: IdentityPoolId
  })
});

function uploadFileToS3 (elem) {
  var file = elem;
  var fileName = "docu_" + file.name;

  // Use S3 ManagedUpload class as it supports multipart uploads
  var upload = new AWS.S3.ManagedUpload({
    params: {
      Bucket: bucketName,
      Key: fileName,
      Body: file
    }
  });

  var promise = upload.promise();

  promise.then(
    function(data) {
      alert("Successfully uploaded file.");
    },
    function(err) {
      console.log(err);
      return alert("There was an error uploading the file: ", err.message);
    }
  );
}

function addFile() {
  var files = document.getElementById("fileupload").files;
  if (!files.length) {
    return alert("Please choose a file to upload first.");
  }
  const len = files.length;
  let i;
  for (i=0; i<len; i++) {
    uploadFileToS3(files[i]);
  }
}
