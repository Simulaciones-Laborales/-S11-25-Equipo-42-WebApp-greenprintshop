## end-points 


### Login
```
http://localhost:8080/api/v1/auth/login
```

Send
```json 
{
		
  "email": "user@gmail.com",
  "password":"12345678"

}
```

Response 

```json
{
  "responseCode": 200,
  "responseMessage": "SUCCESS",
  "data": {
    "nombre": "user",
    "email": "user@gmail.com",
    "roles": [
      "ROLE_USER"
    ],
    "access_token":"aiosdjfoajsdfjasdfa;lsdf;alsdfla",
    "token_type": "Bearer"
  }
}

```



### Register 
```
http://localhost:8080/api/v1/auth/register
```

Send
```json 
{
  "nombre": "user",
  "password": "12345678",
  "email":"user@gmail.com"
}
```

Response 
```json
{
  "responseCode": 200,  
  "responseMessage": "SUCCESS",
  "data": {
    "name": "user",
    "email": "user@gmail.com"
  }
}
```
