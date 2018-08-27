# Introduction

This exercise is part of the DEA course at the HAN University of Applied Sciences. Subject to the
exercise is a partial implementation of the the HTTP-protocol in a simple Java-based server.

Web servers used on the internet are basically server software complying with the HTTP protocol. 
The HTTP protocol, like all other internet protocols, are specified in so called *Requests for Comments*
(RFC). The RFC for HTTP can be found here: https://tools.ietf.org/html/rfc7230 .

HTTP 1.1 is an ASCI-based protocol, which means that server and client communicate with each other
using readable characters. For seperating message elements, HTTP relies heavily on blank spaces and
blank lines, as you can see in the RFC. 

# Exercises
Analyze the start code in the master branch. After starting the server using the main method of the 
HttpServer-class, you can navigate your browser to [http://localhost:8383/index.html]() to see the served
content.

## Exercise 1)
Look into the HttpServer class in the startcode and try to understand at which point the program waits
for client connections. To support your analysis, use google with the keywords *java server socket*.

You will see that your server is currently only serving a single HTTP-request before terminating. Use 
your knowledge about multithreading in Java to change the HttpServer class in a way that every HTTP request
is handled in its own thread. Also change the server in a way that it keeps serving forever, thus in
an endless loop.

## Exercise 2)
Look into the ConnectionHandler class now. You will see that the server is currently neglecting the entire
HTTP-request and always serves the same content with the same status. You can verify this by simply changing
the URL in your browser. Whatever appears after the slash will be answered by the same HTML-code, for instance
[http://localhost:8383/what-a-terribly-bigoted-server.html](). You will fix this in the following steps.

### Excercise 2.1)
Revise the ConnectionHandler in a way that it reads the HTML body from a file called index.html rather than
from the constant HTTP_BODY. Make sure not to change the content of the file. You can google for the keywords
*java8 read text file* to get some support. Once you are done, access the page again using your browser to
verify that the functionality is still in place. 

### Exercise 2.2)
Now add some more content to the index.html file, restart your server and observe what happens. The content will
not be fully rendered. This is because the content-length in the HTTP-HEADER is not correct anymore. Revise the
ConnectionHandler class and make sure that the content-length is calculated automatically, depending on the content
of the served file. Look into the RFC to get hints on how the content-length is calculated. 

Once you are done, make sure to also determine the value for the Date field of the HTTP-HEADER automatically. Again,
look into the RFC to understand the expected format and then google for the format in combination with Java.

### Exercise 2.3)
Now let's add some sensibility to the actually requested resources. Remember that our server now always serves
the same file regardless of the actual request. In HTTP, the first line of a request is called start-line. It 
contains the HTTP-method, the request target (which is the requested resource), and the HTTP protocol version. 
Read up on the start-line in the RFC. 

Revise the ConnectionHandler in a way that it returns the correct status code if the HTTP method used in the
request is not *GET*. Read up on status codes and HTTP methods in the RFC. 

Now revise the ConnectionHandler in a way that it returns the correct status code if the requested resource is
not /index.html.   

Finally, make sure to return the correct status code if the protocol used is not HTTP/1.1

# Challenge
Add support for the HTTP method *HEAD*. Read up on all available methods in the RFC. You can test your implementation
using the tool Postman, for instance. 

Finally, add some dynamics regarding the requested resource, i.e. parse the name
of the requested resource from the start-line, check if a corresponding file is available and return it. Otherwise,
return the respective HTTP status code.      








 



