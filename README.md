Simultaneous authentication and authorization
=============================================

Simultaneous authentication and authorization for a group of spring-web-based applications thru frontend proxy

How it works
============

Lets assume we have N java web applications on the same server. In case we need shared sessions on them, we can do next steps:

- let fronted rewrite all requested URL like /requestedApp/requestedPath/... to /entryApp/requestedApp/requestedPath/...
- let entryApp create session wrapper
- let entryApp internally forward request to requestedApp
- done.
 
It doesn't matter what kind of applications we have - all of them use shared sessions (and information in them) now.
                                                    

What should be configured
=========================

- frontend (to do rewrites needed). Example configuration for nginx included here: https://github.com/Lsync/lsync-tests-spring-sharedsess-proxy/tree/master/ConfigExamples/nginx
- servlet container (to let JSESSIONID have a path like "/" and not "/appName/"). Example for Tomcat included.
