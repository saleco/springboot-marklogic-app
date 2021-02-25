Spring Boot with Marklogic DB

### Pre requisites

####Marklogic Docker image download 

`docker pull store/ma%rklogicdb/marklogic-server:10.0-6.1-dev-centos`

####Run Marklogic container 
`docker run -d -it -p 8000:8000 -p 8001:8001 -p 8002:8002 -e MARKLOGIC_INIT=true -e MARKLOGIC_ADMIN_USERNAME=admin -e MARKLOGIC_ADMIN_PASSWORD=admin  store/marklogicdb/marklogic-server:10.0-6.1-dev-centos`

###Configure user rights for admin account
- Go to http://localhost:8001/user-admin.xqy and login with admin/admin credentials
- On the roles list select: rest-reader, rest-reader-internal, rest-writer rest-writer-internal
- Click on Ok to save
