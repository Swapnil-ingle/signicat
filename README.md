## Requirements

- [x] There are two modals Subject (User) and UserGroup (with M:M relationship).
- [x] User is able to register and login.
- [x] User registration and login endpoints are unsecured and allow requests without a JWT token.
- [x] A valid JWT token is issued when a _registered user_ logs in with valid credentials.
- [x] Only an authenticated user is able to do CRUD operations on UserGroup, the userGroup related endpoints are secured.
- [x] The issued JWT token has an encoded payload in the following format:
  * ```json {
    {
      "sub": "<user_id>",
      "groups": [
        { "id": "<group_id>", "name": "<group_name>"}
      ],
      "username": "user_username",
      "iat": "<issued_at>",
      "exp": "<expited_at>"
    }
- [x] Working Docker initialisation for MySQL DB container.
- [x] Working Spring initialisation from ```./gradlew bRu``` command.

## API Endpoints

### AuthController

| API Type | Description       | Endpoint                | Request Body                                                                        | Response Body                                                                                                                                                                                                                                                                                                                                                                                                             |
|----------|-------------------|-------------------------|-------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| POST     | Register a user   | /api/auth/register      | <pre>{"username": "samuel", <br/>"password": "root"}</pre>                          | <pre>{"id": 43, <br/>"username": "samuel", <br/>"userGroupDTOS": []}</pre>                                                                                                                                                                                                                                                                                                                                                |
| POST     | Login from a user | /api/auth/login         | <pre>{"username": "samuel", <br/>"password": "root"}</pre>                          | <pre>{"token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOjEsImdyb3VwcyI6W3siaWQiOjIsIm5hbWUiOiJTaWduaWNhdCJ9XSwidXNlcm5hbWUiOiJzd2FwIiwiaWF0IjoxNjU1MzA5MDY0LCJleHAiOjE2NTUzMTA4NjR9.WBcHDHY8nUTLlBBqfjoHjcNnYOrc8lXkDidMavhoMP8MQ_9CU9UVCIHT9lkT6LpRfHm9b9P9DrRoYPwKUlCONg", <br/>"refreshToken": "723b9d86-da03-4219-885a-6d9c4c502025", <br/>"username": "samuel", <br/>"expiresAt": "2022-06-14T19:13:07.408697600Z"}</pre>      |
| POST     | Refresh JWT Token | /api/auth/refresh/token | <pre>{"username": "swap", <br/>"refreshToken": "<REFRESH_TOKEN>}</pre>              | <pre>{"token": "<JWT_TOKEN>", <br/>"refreshToken": "<REFRESH_TOKEN>", <br/>"username": "swap", <br/>"expiresAt": "2022-06-14T19:13:07.408697600Z"}</pre>                                                                                                                                                                                                                                                                  |
| POST     | Logout a user     | /api/auth/logout        | <pre>{"username": "swap", <br/>"refreshToken": "<REFRESH_TOKEN>}</pre>              | **_void_**                                                                                                                                                                                                                                                                                                                                                                                                                |


### GroupController (Requires JWT Authentication 🔐)

| API Type | Description                                                    | Endpoint             | Request Body                                                   | Example Response                                                                                                                                          |
|----------|----------------------------------------------------------------|----------------------|----------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------|
| GET      | Get list of groups for a user                                  | /api/group           | **_void_**                                                     | <pre>[{"id": 1, "name": "<group_name_1>"}, <br/>{"id": 2, "name": "<group_name_2>"}]</pre>                                                                |
| POST     | Create a group for the logged in user and add it to that group | /api/group           | <pre>{ "groupName": "Signicat" }</pre>                         | <pre>{"id": 1, <br/>"name": "Signicat", <br/>"users": [<br/>  {"id": 1, "username": "swap"}<br/> ]}</pre>                                                 |
| POST     | Adds an existing user to an existing group by username         | /api/group/addUser   | <pre>{"username": "max", <br/>"groupId": "1"}</pre>            | <pre>{"id": 1, <br/>"name": "Signicat", <br/>"users": [<br/>  {"id": 2, "username": "max"}, <br/>  {"id": 1, "username": "swap"}<br/> ]}</pre>            |
| PUT      | Edit a group name                                              | /api/group/{groupId} | <pre>{ "groupName": "Signicat (US)" }</pre>                    | <pre>{"id": 1, <br/>"name": "Signicat (US)", <br/>"users": [<br/>  {"id": 1, "username": "swap"}<br/> ]}</pre>                                            |
| DELETE   | Delete a group                                                 | /api/group/{groupId} | **_void_**                                                     | **_void_**                                                                                                                                                |


## Additional Features Beside The Requirements

### ✔️JWT Token Expire Functionality 

* The issued JWT token has an `issued_at` and `expired_at` property attached to it, essentially a time-to-live (TTL) association for the token.
* This helps limit the session of a user to a limited time.

> **⚠️NOTE:** The addition of this functionality has made the JWT token format to deviate from requirement. I've only considered this because it is more secure and prevalent way of handling user sessions.

### ✔️JWT Token Refresh And Logout Functionality

* The login API returns a 'Refresh Token' along with the generated JWT token.
* The 'Refresh Token' services are served by an in-memory hashMap implementation.
* An `/api/auth/refresh/token` endpoint has been exposed to let users refresh the JWT token when it is expired.
* This way we have overcome the inconvenience of having the user logging in again after the JWT token has expired.
* An `/api/auth/logout` endpoint has been exposed to let users logout.
  * This invalidates the 'Refresh Token' causing the session to effectively terminate.

> **⚠️NOTE:** Users can still access secured resources until their current JWT is not expired. This is one of the hurdles when using a JWT based stateless session management.

### ✔️Code Complaint With The Latest Spring Security Module (5.7.x)

* The Spring Security library was updated recently, in which they've deprecated the `WebSecurityConfigurerAdapter` class.
* This `WebSecurityConfigurerAdapter` class, is widely used in Security Layer Configuration.
* In the recent version, it is advised to use the `SecurityFilterChain` instead. 
* This was challenging because the upgrade is very recent and there is very scarce support or reading resources available.
* So, I had to (almost) fully rely on Spring Security's internal source-code and the official documentation.

### ✔️Integration And Unit Test Suite (Using JUnit Mockito)

* I have added integration test-cases for Authentication/JWT related workflows.
* I have added Mockito based unit test-cases for CRUD operations on Subject and UserGroup modals.

### ✔️Misc

* Used a secret-key (salt) in conjunction with HS512 algorithm to generate the JWT.
* Used BCryptPasswordEncoder to encode password.
* I have used CustomRuntimeExceptions to properly validate edge cases and illegal states.
* This enables us to develop very specifically tailored Exceptions that are suitable for our use-cases.
* Retrieved and used the `userId` from SecurityContext whenever possible, as the `userId` coming from REST-APIs shouldn't always be trusted.
* Used a BaseEntity as @MappedSuperclass that contains the common attributes for all the @Entity classes.
* Used Lombok library for shorthand notation and rapid development.
* A Subject (User) is only allowed to add to, update, or delete groups for which he is already a member.

> **⚠️NOTE:** I have used Spring `application.properties` to initialize the basic DB schema, instead of using a DB versioning system like Flyway or Liquibase. 
