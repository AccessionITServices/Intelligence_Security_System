import boto3
import datetime
import requests
import urllib3
import chardet
import json
def lambda_handler(event, context):
    s3 = boto3.resource('s3')
    #bucket triggering
    BUCKET = event['Records'][0]['s3']['bucket']['name']
    KEY = event['Records'][0]['s3']['object']['key']
    #dynamodb connection
    dynamodb = boto3.resource('dynamodb', region_name='us-west-1')
    table = dynamodb.Table('table')
    
    BUCKET2 = 'backet1'
    datetime1=datetime.datetime.now().strftime("%A %d %B %Y %I:%M:%S%p")
    #print(datetime1)
    s3.Object(BUCKET2,KEY).copy_from(CopySource= BUCKET +'/'+ KEY)
    s3.Object(BUCKET,KEY).delete()
    object_acl = s3.ObjectAcl(BUCKET2,KEY)
    response = object_acl.put(ACL='public-read')
    s3 = boto3.client('s3')
    image=KEY
    url1 = '{}/{}/{}'.format(s3.meta.endpoint_url, BUCKET2, image)
    response3 = table.put_item(
            Item={
                'url':url1,
                'datetime':datetime1
            }
        )
    
    
    
    token="firebase token"
    url = 'https://fcm.googleapis.com/fcm/send'
    
   
    headers = {'Content-Type': 'application/json; UTF-8',
        "Authorization": 'key=firebase Authorization key'}
    body = {
        "to":token,
        "data":{  
           "title":"hello",
           "body":datetime1,
           "imageurl":url1
        },
        "notification":{  
           "title":"HomeSecuritySystem",
           "body":"hi, person is detected",
           "content_available": "true"
        }
    
    }
     
    res=requests.post(url, data=json.dumps(body), headers=headers)
