# METRICS #
-----------

http://localhost:8080/metrics/rate
http://localhost:8080/metrics/duration
http://localhost:8080/metrics/error

# ELASTIC SEARCH #
-------------
http://localhost:8080/search/{keyword}

# CONFIGURATION #
content-web\src\main\resources\application.yml

# TASKS #
Basic requirements:
- CentOS 7 -- this can be  done  using  docker container
- Java 8
- Elasticsearch 6 -- locally tested on Elastic Search 6.8.0

Checks input folder for new files
- Done by spring integration
- partially, implemented only txt file directory, can be extended to support all 4  types very easy

Parses files content
- Done by spring batch
- Partially done, cause i didnt have a time to implement readers for all kind of files

Performance Statistics
- implemented using spring micrometer integration, could be extended to gather more information
http://localhost:8080/metrics/rate
http://localhost:8080/metrics/duration
http://localhost:8080/metrics/error

Multi-threaded (at least 1 thread per folder)
- TaskExecutorConfiguration
- configurable via application.yml

Configuration file contains database connection parameters, threads limiting, etc.
- Lots of variables set via configuration file

Stores content of processed files in Elasticsearch database
- i tested with local elastic 6.8 didnt  have time to create docker compose for elastic with application

If have any UI experience) Implement a basic UI to search files content by keywords
- provided simple endpoint to find documents via keyword
localhost:8080/search/{keyword}

Lots of work should be done but  didnt want to spend  few days on 1 task just spent few hours.
Didnt write test as u didnt ask for them but as well not to much time.
