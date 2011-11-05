Lighthouse Changelog
====================

1.0.6
-----
* Fixed exception when saving the username in the preferences dialog
* All lighthouse files are now being save in the workspace .metadata directory
* Improved performance in pull operations
* Showing UML interfaces in the Emerging Design diagram
* Added offline support
* Double clicking in the diagram to open a class in the editor is working again.
* Removed non-important filters and layout algorithms to cleanup the Emerging Design view.

1.0.5
-----
* Added support for multiple workspaces
* Using the MySQL server time to timestamp events

1.0.4
-----
* Improved performance of model persistence
* Fixed bug with package filters (they were not showing the package name properly)
* Hiding anonymous classes
* Fixed simple name for inner classes
* Speed it up Eclipse initialization
* Fixed bug that was making Lighthouse create events for non-shared projects
* Added filter that shows all opened files in the workspace + one hop

1.0.3
-----
* Detecting and adding merge conflicts to the model
* Added support for having classes with the same name in different projects
* Package filter now support multiple selection of packages

1.0.2
-----
* Added a button in the preferences dialog to synchronize the local model with the database

1.0.1
-----
* Fixed bug with Package filter (it was showing more packages than it should)
* Supporintg external trunks in SVN
* Fixed constructor name in UML classes (it was showing "init" before)
* Fixed showing classes that were deleted and not committed. 
* Increased primary key of Lighthouse Entity to 500 chars
