# Read Me

The original idea is from  
https://medium.com/@viraj_63415/spring-boot-async-and-security-propagation-3121ba6d9826

We know that Spring Boot sets the **Authentication** object of the user in a class called **SecurityContext** which is
stored in
a **Thread Local** object for the thread handling the request.

By default, this **SecurityContext** is not propagated to the thread which executes the Async method.

In order to propagate the security context to the thread which runs the Async method, we should use the
**DelegatingSecurityContextAsyncTaskExecutor**

**DelegatingSecurityContextAsyncTaskExecutor** sets **SecurityContext** before running the thread and then unsets it
when the task is done