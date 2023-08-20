Employee Overlap Application
This application determines the overlapping working days for employees on projects. Users can upload a CSV file containing employee details, project IDs, and their work periods, and the system will calculate overlapping days and display them.

Features
File upload functionality to provide employee data.
Calculation of overlapping days for employees working on the same project.
Intuitive display of overlapping periods for easy interpretation.

Running the Project:
You can use (Intellij)
Run project
Open a web browser and go to http://localhost:8080/.
You should see the file upload form.

Usage
Navigate to the main page.
Use the file upload button to select and upload your CSV file containing the employee data.
View the results in the table below.

File Format
The expected CSV file format is:

Copy code
EmployeeID,ProjectID,StartDate,EndDate

Example:

Copy code
143,10,2013-11-01,2014-01-05
218,10,2012-05-16,NULL
143,10,2009-01-01,2011-04-27
Note: Use "NULL" for ongoing projects.

Troubleshooting
If you face issues while running the application:

Ensure Java 11 or later is installed.
Check the error logs in the console for specific issues and resolutions.